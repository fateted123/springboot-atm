package com.dayuanit.dy9.springboot.atm.springbootatm;

import com.dayuanit.dy9.springboot.atm.springbootatm.service.BankCardService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootAtmApplicationTests {

	public static final int max_thread = 200;

	public static CountDownLatch countDownLatch = new CountDownLatch(max_thread);
	public static CyclicBarrier cyclicBarrier = new CyclicBarrier(max_thread);

	@Autowired
	private BankCardService bankCardService;

	@Test
	@Commit
	public void testDepositCurrent() {
		MyRunable myRunable = new MyRunable(bankCardService);

		for (int i = 0; i < max_thread; i++) {
			new Thread(myRunable).start();
		}

		try {
			SpringbootAtmApplicationTests.countDownLatch.await();
			System.out.println("-------- test over ---------");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


	}

}

class MyRunable implements Runnable {

	private BankCardService bankCardService;

	public MyRunable(BankCardService bankCardService) {
		this.bankCardService = bankCardService;
	}

	@Override
	public void run() {
		try {
			SpringbootAtmApplicationTests.cyclicBarrier.await();
		} catch (Exception e) {
			e.printStackTrace();
		}

		bankCardService.deposit(1, 1, "111", "1");
		SpringbootAtmApplicationTests.countDownLatch.countDown();
	}
}
