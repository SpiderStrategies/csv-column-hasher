package com.spider.csvcolumnhasher;

import static org.junit.Assert.*;

import org.junit.Test;

public class HasherTest {

	@Test
	public void test() throws Exception {
		String input = "mysensitivedata";
		String salt = "blah";
		String expectedOutput = "12e8d8f3e1bbfb791b45db99748eb252b6bcae98";
		String output = CSVColumnHasher.getSaltedHash(input, salt.getBytes());
		assertEquals(expectedOutput, output);
	}

}
