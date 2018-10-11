package com.dayuanit.dy9.springboot.atm.springbootatm.service;

import com.dayuanit.dy9.springboot.atm.springbootatm.dto.CardDTO;
import com.dayuanit.dy9.springboot.atm.springbootatm.dto.FlowDTO;
import com.dayuanit.dy9.springboot.atm.springbootatm.entity.Card;
import com.dayuanit.dy9.springboot.atm.springbootatm.entity.Flow;
import com.dayuanit.dy9.springboot.atm.springbootatm.entity.Transfer;
import com.dayuanit.dy9.springboot.atm.springbootatm.enums.CardStatusEnum;
import com.dayuanit.dy9.springboot.atm.springbootatm.enums.FlowTypeEnum;
import com.dayuanit.dy9.springboot.atm.springbootatm.enums.TransferEnum;
import com.dayuanit.dy9.springboot.atm.springbootatm.exception.BizException;
import com.dayuanit.dy9.springboot.atm.springbootatm.holder.PageHolder;
import com.dayuanit.dy9.springboot.atm.springbootatm.mapper.CardMapper;
import com.dayuanit.dy9.springboot.atm.springbootatm.mapper.FlowMapper;
import com.dayuanit.dy9.springboot.atm.springbootatm.mapper.TransferMapper;
import com.dayuanit.dy9.springboot.atm.springbootatm.util.CardUtils;
import com.dayuanit.dy9.springboot.atm.springbootatm.util.MoneyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BankCardService {

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private FlowMapper flowMapper;

    @Autowired
    private TransferMapper transferMapper;

    public void openAccount(String pwd, String confirmPwd, int userId) {
        if (StringUtils.isBlank(pwd) || StringUtils.isBlank(confirmPwd)) {
            throw new BizException("有必填参数没有填写");
        }

        if (!pwd.equals(confirmPwd)) {
           throw new BizException("两次密码不一致");
        }

        final Card card = new Card();
        card.setBalance("0");
        card.setCardNum(CardUtils.createCardNum());
        card.setCreateTime(new Date());
        card.setModifyTime(new Date());
        card.setPwd(pwd);
        card.setStatus(CardStatusEnum.enable.getK());
        card.setUserId(userId);

        int rows = cardMapper.insert(card);
        if (1 != rows) {
            throw new BizException("开户失败");
        }

    }

    public List<CardDTO> listEnableCards(int userId) {
        final List<Card> cards = cardMapper.listCard(userId, CardStatusEnum.enable.getK());
        List<CardDTO> dtoList = new ArrayList<>(cards.size());//推荐此种写法 防止数组扩容问题 提高性能
//        List<CardDTO> dtoList = new ArrayList<>();

        for (Card card : cards) {
            final CardDTO cardDTO = new CardDTO();
            dtoList.add(cardDTO);

            cardDTO.setId(card.getId());
            cardDTO.setCardNum(CardUtils.formatCard(card.getCardNum()));
            cardDTO.setBalance(card.getBalance());
        }

        return dtoList;
    }


    //乐观锁
    @Transactional(rollbackFor = Exception.class)
    public void deposit2(int userId, int cardId, String pwd, String amount){
        Card card = cardMapper.selectByPrimaryKey(cardId);

        if (StringUtils.isBlank(pwd)) {
            throw new BizException("银行卡密码不能为空");
        }


        if (null == card) {
            throw new BizException("银行卡不存在");
        }

        if (!pwd.equals(card.getPwd())) {
            throw new BizException("银行卡不存在或者密码错误");
        }

        if (userId != card.getUserId()) {
            throw new BizException("银行卡不属于你");
        }

        if (card.getStatus() != CardStatusEnum.enable.getK()) {
            throw new BizException("银行卡不可用");
        }

        if (StringUtils.isBlank(amount)) {
            throw new BizException("请输入存款金额");
        }

        int amountNum = Integer.valueOf(amount);
        if (amountNum <= 0) {
            throw new BizException("请输入合法存款金额");
        }

        // 修改银行卡余额
        card.setBalance(MoneyUtils.plus(card.getBalance(), amount));
        card.setModifyTime(new Date());

        //乐观锁 通过版本号的对比 达到一致性
        int rows = cardMapper.modifyBalance2(card.getCardNum(), card.getBalance(), card.getModifyTime(), card.getCardVersion(), card.getCardVersion() + 1);
        if (1 != rows) {
            throw new BizException("存款失败");
        }

        // 增加流水
        final Flow flow = new Flow();
        flow.setAmount(amount);
        flow.setCardNum(card.getCardNum());
        flow.setCreateTime(new Date());
        flow.setFlowDesc(FlowTypeEnum.deposit.getV());
        flow.setFlowType(FlowTypeEnum.deposit.getK());
        flow.setUserId(userId);

        rows = flowMapper.insert(flow);
        if (1 != rows) {
            throw new BizException("存款失败");
        }

    }

    //悲观锁
    @Transactional(rollbackFor = Exception.class)
    public void deposit(int userId, int cardId, String pwd, String amount){
        Card card = cardMapper.selectByPrimaryKey(cardId);

        //使用悲观锁 是由数据库维护的
        card = cardMapper.getCard4Lock(card.getCardNum());//行锁 本质是锁索引 开启事务

        if (StringUtils.isBlank(pwd)) {
            throw new BizException("银行卡密码不能为空");
        }


        if (null == card) {
            throw new BizException("银行卡不存在");
        }

        if (!pwd.equals(card.getPwd())) {
            throw new BizException("银行卡不存在或者密码错误");
        }

        if (userId != card.getUserId()) {
            throw new BizException("银行卡不属于你");
        }

        if (card.getStatus() != CardStatusEnum.enable.getK()) {
            throw new BizException("银行卡不可用");
        }

        if (StringUtils.isBlank(amount)) {
            throw new BizException("请输入存款金额");
        }

        int amountNum = Integer.valueOf(amount);
        if (amountNum <= 0) {
            throw new BizException("请输入合法存款金额");
        }



        // 修改银行卡余额
        card.setBalance(MoneyUtils.plus(card.getBalance(), amount));
        card.setModifyTime(new Date());

        int rows = cardMapper.modifyBalance(card.getCardNum(), card.getBalance(), card.getModifyTime());
        if (1 != rows) {
            throw new BizException("存款失败");
        }

        // 增加流水
        final Flow flow = new Flow();
        flow.setAmount(amount);
        flow.setCardNum(card.getCardNum());
        flow.setCreateTime(new Date());
        flow.setFlowDesc(FlowTypeEnum.deposit.getV());
        flow.setFlowType(FlowTypeEnum.deposit.getK());
        flow.setUserId(userId);

        rows = flowMapper.insert(flow);
        if (1 != rows) {
            throw new BizException("存款失败");
        }

    }

    public PageHolder loadFlows(int cardId, String pwd, int cuurentPage) {

        if (StringUtils.isBlank(pwd)) {
            throw new BizException("密码不能为空");
        }

        final Card card = cardMapper.selectByPrimaryKey(cardId);
        if (null == card) {
            throw new BizException("银行卡不存在");
        }

        if (!pwd.equals(card.getPwd())) {
            throw new BizException("密码不正确");
        }


        PageHolder pageHolder = PageHolder.builder(cuurentPage, flowMapper.countFlow(card.getCardNum()));

        final List<Flow> flows = flowMapper.listFlow(card.getCardNum(), pageHolder.getOffset(), PageHolder.prePageNum);
        List<FlowDTO> dtoList = new ArrayList<>(flows.size());

        for (Flow flow : flows) {
            final FlowDTO flowDTO = new FlowDTO();
            dtoList.add(flowDTO);

            flowDTO.setFlowDesc(flow.getFlowDesc());
            flowDTO.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(flow.getCreateTime()));
            flowDTO.setCardNum(CardUtils.formatCard(flow.getCardNum()));
            flowDTO.setAmount(flow.getAmount());
        }

        pageHolder.setData(dtoList);

        return pageHolder;
    }

    private void checkTransfer(int cardId, String inCardNum, String amount, String pwd) {
        //各种参数的校验
        if (StringUtils.isBlank(pwd)) {
            throw new BizException("密码不能为空");
        }

        if (StringUtils.isBlank(inCardNum)) {
            throw new BizException("转入卡号不能为空");
        }

        if (StringUtils.isBlank(amount)) {
            throw new BizException("金额不能为空");
        }

        Card outCard = cardMapper.selectByPrimaryKey(cardId);
        if (null == outCard) {
            throw new BizException("卡号不存在");
        }

        Card inCard = cardMapper.getCard(inCardNum);
        if (null == inCard) {
            throw new BizException("转入银行卡不存在");
        }

        if (!pwd.equals(outCard.getPwd())) {
            throw new BizException("密码不正确");
        }

        //余额是否足够
        if (!MoneyUtils.checkBalance(outCard.getBalance(), amount)) {
            throw new BizException("余额不足");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void tranfer(int cardId, String inCardNum, String amount, String pwd) {

        checkTransfer(cardId, inCardNum, amount, pwd);

        Card outCard = cardMapper.selectByPrimaryKey(cardId);

        Card inCard = cardMapper.getCard(inCardNum);

        // 减去转账用户的银行卡余额
        outCard.setBalance(MoneyUtils.sub(outCard.getBalance(), amount));
        int rows = cardMapper.modifyBalance(outCard.getCardNum(), outCard.getBalance(), new Date());
        if (1 != rows) {
            throw new BizException("转账失败");
        }

        // 增加流水
        Flow flow = new Flow();
        flow.setUserId(outCard.getUserId());
        flow.setFlowType(FlowTypeEnum.transferOut.getK());
        flow.setFlowDesc(FlowTypeEnum.transferOut.getV());
        flow.setCreateTime(new Date());
        flow.setCardNum(outCard.getCardNum());
        flow.setAmount(amount);
        rows = flowMapper.insert(flow);
        if (1 != rows) {
            throw new BizException("转账失败");
        }

        // 增加转入账户银行卡余额

        inCard.setBalance(MoneyUtils.plus(inCard.getBalance(), amount));
        rows = cardMapper.modifyBalance(inCard.getCardNum(), inCard.getBalance(), new Date());
        if (1 != rows) {
            throw new BizException("转账失败");
        }

        //增加流水
        flow = new Flow();
        flow.setUserId(inCard.getUserId());
        flow.setFlowType(FlowTypeEnum.transferIn.getK());
        flow.setFlowDesc(FlowTypeEnum.transferIn.getV());
        flow.setCreateTime(new Date());
        flow.setCardNum(inCard.getCardNum());
        flow.setAmount(amount);
        rows = flowMapper.insert(flow);
        if (1 != rows) {
            throw new BizException("转账失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void transferDelay(int cardId, String inCardNum, String amount, String pwd) {
        checkTransfer(cardId, inCardNum, amount, pwd);

        Card outCard = cardMapper.selectByPrimaryKey(cardId);
        Card inCard = cardMapper.getCard(inCardNum);

        // 减去转账用户的银行卡余额
        outCard.setBalance(MoneyUtils.sub(outCard.getBalance(), amount));
        int rows = cardMapper.modifyBalance(outCard.getCardNum(), outCard.getBalance(), new Date());
        if (1 != rows) {
            throw new BizException("转账失败");
        }

        // 增加流水
        Flow flow = new Flow();
        flow.setUserId(outCard.getUserId());
        flow.setFlowType(FlowTypeEnum.transferOut.getK());
        flow.setFlowDesc(FlowTypeEnum.transferOut.getV());
        flow.setCreateTime(new Date());
        flow.setCardNum(outCard.getCardNum());
        flow.setAmount(amount);
        rows = flowMapper.insert(flow);
        if (1 != rows) {
            throw new BizException("转账失败");
        }

        // 生成转账订单
        final Transfer transfer = new Transfer();
        transfer.setAmount(amount);
        transfer.setCreateTime(new Date());
        transfer.setInCardNum(inCard.getCardNum());
        transfer.setModifyTime(new Date());
        transfer.setOutCardNum(outCard.getCardNum());
        transfer.setStatus(TransferEnum.待处理.getK());
        transfer.setUserId(outCard.getUserId());
        rows = transferMapper.insert(transfer);

        if (1 != rows) {
            throw new BizException("转账失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void processTransferOrder(Transfer transfer) {
        //  先给对方加钱
        Card inCard = cardMapper.getCard(transfer.getInCardNum());
        Card outCard = cardMapper.getCard(transfer.getOutCardNum());

        inCard.setBalance(MoneyUtils.plus(inCard.getBalance(), transfer.getAmount()));

        int rows = cardMapper.modifyBalance(inCard.getCardNum(), inCard.getBalance(), new Date());
        if (1 != rows) {
            throw new BizException("转账失败");
        }

        //增加流水
        Flow flow = new Flow();
        flow.setUserId(inCard.getUserId());
        flow.setFlowType(FlowTypeEnum.transferIn.getK());
        flow.setFlowDesc(FlowTypeEnum.transferIn.getV());
        flow.setCreateTime(new Date());
        flow.setCardNum(inCard.getCardNum());
        flow.setAmount(transfer.getAmount());
        rows = flowMapper.insert(flow);
        if (1 != rows) {
            throw new BizException("转账失败");
        }

        // 修改转账订单状态为成功
        rows = transferMapper.modifyTransferStatus(transfer.getId(), TransferEnum.转账成功.getK());
        if (1 != rows) {
            throw new BizException("转账失败");
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void processTransferFaild(Transfer transfer) {
        // 修改转账订单为失败
        int rows = transferMapper.modifyTransferStatus(transfer.getId(), TransferEnum.转账失败.getK());
        if (1 != rows) {
            throw new BizException("返钱失败");
        }

        //TODO 将钱返回转出账户
        final Card outCard = cardMapper.getCard(transfer.getOutCardNum());
        outCard.setBalance(MoneyUtils.plus(outCard.getBalance(), transfer.getAmount()));

        rows = cardMapper.modifyBalance(outCard.getCardNum(), outCard.getBalance(), new Date());
        if (1 != rows) {
            throw new BizException("返钱失败");
        }

    }


    public List<FlowDTO> listTop10Flows(int userId) {

        final List<Flow> flows = flowMapper.listTop10(userId);
        List<FlowDTO> dtos = new ArrayList<>(flows.size());

        for (Flow flow : flows) {
            FlowDTO dto = new FlowDTO();
            dto.setAmount(flow.getAmount());
            dto.setCardNum(CardUtils.formatCard(flow.getCardNum()));
            dto.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(flow.getCreateTime()));
            dto.setFlowDesc(flow.getFlowDesc());

            dtos.add(dto);
        }

        return dtos;
    }

    public Card loadCard(int cardId) {
        return cardMapper.selectByPrimaryKey(cardId);
    }
}
