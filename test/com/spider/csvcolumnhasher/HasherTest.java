package com.spider.csvcolumnhasher;

import static org.junit.Assert.*;

import org.junit.Test;

public class HasherTest {

	@Test
	public void test() throws Exception {
		String input = "mysensitivedata";
		String salt = "blah";
		String expectedOutput = "a27a312ea98e5eb5537e847c980c40f5bd7a07f6";
		String output = CSVColumnHasher.getSaltedHash(input, salt.getBytes(), 10);
		assertEquals(expectedOutput, output);
	}

}
