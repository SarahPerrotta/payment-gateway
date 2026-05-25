package com.jpmc.transaction_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionServiceApplicationTests {

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private JwtUtil jwtUtil;

	@Test
	void contextLoads() {
	}

	@Test
	void testJwtTokenGeneration() {
		String token = jwtUtil.generateToken("testuser");
		assertNotNull(token);
		assertTrue(token.startsWith("eyJ"));
	}

	@Test
	void testJwtTokenValidation() {
		String token = jwtUtil.generateToken("testuser");
		assertTrue(jwtUtil.validateToken(token));
	}

	@Test
	void testJwtExtractUsername() {
		String token = jwtUtil.generateToken("testuser");
		String username = jwtUtil.extractUsername(token);
		assertEquals("testuser", username);
	}

	@Test
	void testJwtInvalidToken() {
		assertFalse(jwtUtil.validateToken("invalid.token.here"));
	}

	@Test
	void testCreateTransaction() {
		Transaction transaction = new Transaction();
		transaction.setFromAccount("ACC001");
		transaction.setToAccount("ACC002");
		transaction.setAmount(100.00);
		transaction.setCurrency("GBP");

		Transaction saved = transactionService.createTransaction(transaction);

		assertNotNull(saved.getId());
		assertEquals("ACC001", saved.getFromAccount());
		assertEquals("ACC002", saved.getToAccount());
		assertNotNull(saved.getStatus());
		assertNotNull(saved.getTimestamp());
	}

	@Test
	void testGetTransaction() {
		Transaction transaction = new Transaction();
		transaction.setFromAccount("ACC003");
		transaction.setToAccount("ACC004");
		transaction.setAmount(200.00);
		transaction.setCurrency("GBP");

		Transaction saved = transactionService.createTransaction(transaction);
		Transaction found = transactionService.getTransaction(saved.getId());

		assertNotNull(found);
		assertEquals(saved.getId(), found.getId());
	}
}