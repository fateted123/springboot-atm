package com.dayuanit.dy9.springboot.atm.springbootatm.controller;

import com.dayuanit.dy9.springboot.atm.springbootatm.dto.CardDTO;
import com.dayuanit.dy9.springboot.atm.springbootatm.dto.FlowDTO;
import com.dayuanit.dy9.springboot.atm.springbootatm.dto.ResponseDTO;
import com.dayuanit.dy9.springboot.atm.springbootatm.entity.Card;
import com.dayuanit.dy9.springboot.atm.springbootatm.holder.PageHolder;
import com.dayuanit.dy9.springboot.atm.springbootatm.service.BankCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class BankCardController extends BaseController {

    @Autowired
    private BankCardService bankCardService;

    @RequestMapping("/card/openAccount")
    public ResponseDTO openAccount(String pwd, String confimPwd, HttpSession session) {
        System.out.println(pwd + "," + confimPwd);
        bankCardService.openAccount(pwd, confimPwd, getUserId(session));
        return ResponseDTO.sucess();
    }

    @RequestMapping("/card/listCard")
    public ResponseDTO listCard(HttpSession session) {
        return ResponseDTO.sucess(bankCardService.listEnableCards(getUserId(session)));
    }

    @RequestMapping("/card/deposit")
    public ResponseDTO deposit(int cardId, String pwd, String amount, HttpSession session) {
        System.out.println(cardId + "," + pwd);
        bankCardService.deposit(getUserId(session), cardId, pwd, amount);
        return ResponseDTO.sucess();
    }

    @RequestMapping(value = "/card/listFlow", method = RequestMethod.POST)
    public ResponseDTO listFlow(int cardId, String pwd, int cuurentPage) {
        return ResponseDTO.sucess(bankCardService.loadFlows(cardId, pwd, cuurentPage));
    }

    @RequestMapping(value = "/card/transfer", method = RequestMethod.POST)
    public ResponseDTO transfer(int cardId, String inCardNum, String pwd, String amount) {
//        bankCardService.tranfer(cardId, inCardNum, amount, pwd);
        bankCardService.transferDelay(cardId, inCardNum, amount, pwd);
        return ResponseDTO.sucess();
    }

    @RequestMapping("/card/loadTop10Flow")
    public ResponseDTO loadTop10Flow(HttpSession session) {
        return ResponseDTO.sucess(bankCardService.listTop10Flows(getUserId(session)));
    }

    @RequestMapping("/card/downLoadFlows")
    public void downLoadFlows(int cardId, String pwd, HttpServletResponse response, HttpSession session) {

        final Card card = bankCardService.loadCard(cardId);

        response.setContentType("application/octet-stream;charset=utf-8");
        response.setHeader("content-disposition", "attachment;filename=" + card.getCardNum() + ".csv");

        //T查询流水
        int currentPage = 1;

        PageHolder pageHolder = null;
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "GBK"))) {
            do {
                pageHolder = bankCardService.loadFlows(cardId, pwd, currentPage);
                currentPage ++;

                for (FlowDTO dto : (List<FlowDTO>)pageHolder.getData()) {
                    bw.write(dto.getCardNum() + "," + dto.getAmount() + "," + dto.getFlowDesc() + "," + dto.getCreateTime());
                    bw.newLine();
                }
                bw.flush();

            } while (null != pageHolder && pageHolder.getTotalPage() != pageHolder.getCurrnetPage());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
