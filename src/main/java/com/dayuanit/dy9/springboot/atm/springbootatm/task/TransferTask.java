package com.dayuanit.dy9.springboot.atm.springbootatm.task;

import com.dayuanit.dy9.springboot.atm.springbootatm.entity.Transfer;
import com.dayuanit.dy9.springboot.atm.springbootatm.enums.TransferEnum;
import com.dayuanit.dy9.springboot.atm.springbootatm.mapper.TransferMapper;
import com.dayuanit.dy9.springboot.atm.springbootatm.service.BankCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@Component
public class TransferTask {

    @Autowired
    private TransferMapper transferMapper;

    @Autowired
    private BankCardService bankCardService;

    @Scheduled(cron = "0/5 * * * * ?")
//    @Async 支持并发操作
    public void process() {
        final Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, -30);


        int offset = 0;
        int prePageNum = 2;
        int currentPage = 1;

        List<Transfer> transfers = Collections.EMPTY_LIST;

        do {
            transfers = transferMapper.listTransfer(TransferEnum.待处理.getK(), instance.getTime(), 0, prePageNum);
            //处理转账订单
            doTransfer(transfers);
            currentPage ++;
//            offset = (currentPage - 1) * prePageNum;
        } while(transfers.size() > 0);

    }

    private void doTransfer(List<Transfer> transfers) {
        for (Transfer transfer : transfers) {
            try {
                bankCardService.processTransferOrder(transfer);
            } catch (Exception e) {
                e.printStackTrace();
                //TODO 修改转账订单为失败
                bankCardService.processTransferFaild(transfer);
            }

        }
    }
}
