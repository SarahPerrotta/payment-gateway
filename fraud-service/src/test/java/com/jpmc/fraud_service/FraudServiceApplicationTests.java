package com.jpmc.fraud_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FraudServiceApplicationTests {

	@Autowired
	private FraudRuleEngine fraudRuleEngine;

	@Test
	void contextLoads() {
	}

	@Test
	void testSmallAmountApproved() {
		FraudCheckRequest request = new FraudCheckRequest();
		request.setFromAccount("ACC001");
		request.setToAccount("ACC002");
		request.setAmount(500.00);
		request.setCurrency("GBP");

		FraudCheckResponse response = fraudRuleEngine.evaluate(request);
		assertEquals("APPROVED", response.getResult());
	}

	@Test
	void testLargeAmountFlagged() {
		FraudCheckRequest request = new FraudCheckRequest();
		request.setFromAccount("ACC001");
		request.setToAccount("ACC002");
		request.setAmount(5000.00);
		request.setCurrency("GBP");

		FraudCheckResponse response = fraudRuleEngine.evaluate(request);
		assertEquals("FLAGGED", response.getResult());
	}

	@Test
	void testSameAccountFlagged() {
		FraudCheckRequest request = new FraudCheckRequest();
		request.setFromAccount("ACC001");
		request.setToAccount("ACC001");
		request.setAmount(100.00);
		request.setCurrency("GBP");

		FraudCheckResponse response = fraudRuleEngine.evaluate(request);
		assertEquals("FLAGGED", response.getResult());
	}

	@Test
	void testZeroAmountFlagged() {
		FraudCheckRequest request = new FraudCheckRequest();
		request.setFromAccount("ACC001");
		request.setToAccount("ACC002");
		request.setAmount(0.00);
		request.setCurrency("GBP");

		FraudCheckResponse response = fraudRuleEngine.evaluate(request);
		assertEquals("FLAGGED", response.getResult());
	}

	@Test
	void testNegativeAmountFlagged() {
		FraudCheckRequest request = new FraudCheckRequest();
		request.setFromAccount("ACC001");
		request.setToAccount("ACC002");
		request.setAmount(-100.00);
		request.setCurrency("GBP");

		FraudCheckResponse response = fraudRuleEngine.evaluate(request);
		assertEquals("FLAGGED", response.getResult());
	}

	@Test
	void testExactThresholdApproved() {
		FraudCheckRequest request = new FraudCheckRequest();
		request.setFromAccount("ACC001");
		request.setToAccount("ACC002");
		request.setAmount(1000.00);
		request.setCurrency("GBP");

		FraudCheckResponse response = fraudRuleEngine.evaluate(request);
		assertEquals("APPROVED", response.getResult());
	}

	@Test
	void testAboveThresholdFlagged() {
		FraudCheckRequest request = new FraudCheckRequest();
		request.setFromAccount("ACC001");
		request.setToAccount("ACC002");
		request.setAmount(1000.01);
		request.setCurrency("GBP");

		FraudCheckResponse response = fraudRuleEngine.evaluate(request);
		assertEquals("FLAGGED", response.getResult());
	}
}