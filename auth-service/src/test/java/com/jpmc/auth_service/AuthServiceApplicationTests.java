package com.jpmc.auth_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceApplicationTests {

	@Autowired
	private AuthService authService;

	@Autowired
	private JwtUtil jwtUtil;

	@Test
	void contextLoads() {
	}

	@Test
	void testRegisterNewUser() {
		String result = authService.register("testuser_junit", "password123");
		assertEquals("User registered successfully", result);
	}

	@Test
	void testRegisterDuplicateUser() {
		authService.register("duplicate_user", "password123");
		String result = authService.register("duplicate_user", "password123");
		assertEquals("Username already exists", result);
	}

	@Test
	void testLoginSuccess() {
		authService.register("login_test_user", "password123");
		String result = authService.login("login_test_user", "password123");
		assertNotNull(result);
		assertTrue(result.startsWith("eyJ"));
	}

	@Test
	void testLoginWrongPassword() {
		authService.register("wrong_pass_user", "password123");
		String result = authService.login("wrong_pass_user", "wrongpassword");
		assertEquals("Invalid password", result);
	}

	@Test
	void testLoginUserNotFound() {
		String result = authService.login("nonexistent_user", "password123");
		assertEquals("User not found", result);
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
}