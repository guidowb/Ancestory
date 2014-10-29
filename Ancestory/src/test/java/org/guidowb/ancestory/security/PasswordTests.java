package org.guidowb.ancestory.security;

import static org.junit.Assert.*;

import org.junit.Test;

public class PasswordTests {

	@Test
	public void toHexWorksForZeroes() {
		byte[] bytes = { 0, 0, 0, 0 };
		String hex = Password.toHex(bytes);
		assertEquals("00000000", hex);
	}

	@Test
	public void toHexWorksForPositiveValues() {
		byte[] bytes = { 64, 48, 32, 16 };
		String hex = Password.toHex(bytes);
		assertEquals("40302010", hex);
	}

	@Test
	public void toHexWorksForNegativeValues() {
		byte[] bytes = { -64, -48, -32, -16 };
		String hex = Password.toHex(bytes);
		assertEquals("c0d0e0f0", hex);
	}

	@Test
	public void passwordRecognized() {
		String password = "S3cret";
		String hash = Password.hashPassword(password);
		assertTrue(Password.isPassword(password, hash));
	}
}
