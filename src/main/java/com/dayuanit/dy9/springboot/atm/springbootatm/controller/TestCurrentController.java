package com.dayuanit.dy9.springboot.atm.springbootatm.controller;

import com.dayuanit.dy9.springboot.atm.springbootatm.dto.ResponseDTO;
import com.dayuanit.dy9.springboot.atm.springbootatm.service.BankCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class TestCurrentController extends BaseController {

    @Autowired
    private BankCardService bankCardService;

    public static int success = 0;
    public static int error = 0;

    public static AtomicInteger atomicSuccess = new AtomicInteger(0);
    public static AtomicInteger atomicError = new AtomicInteger(0);

    @RequestMapping("/test/testDeposit")
    public ResponseDTO testDeposit(int maxThread) {
        success = 0;
        error = 0;

        atomicError = new AtomicInteger(0);
        atomicSuccess = new AtomicInteger(0);

        CountDownLatch countDownLatch = new CountDownLatch(maxThread);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(maxThread);

        for (int i = 0; i < maxThread; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        cyclicBarrier.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    bankCardService.deposit(1, 1, "111", "1");
                    try {
                        bankCardService.deposit2(1, 1, "111", "1");
                        TestCurrentController.success ++;
                        atomicSuccess.getAndAdd(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        TestCurrentController.error ++;
                        atomicError.getAndAdd(1);
                    }

                    countDownLatch.countDown();
                }
            }).start();
        }

        try {
            countDownLatch.await();
            System.out.println("-------- test over ---------");
            System.out.println("success=" + success + ", error=" + error);
            System.out.println("success=" + atomicSuccess.get() + ", error=" + atomicError.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ResponseDTO.sucess();
    }
}
