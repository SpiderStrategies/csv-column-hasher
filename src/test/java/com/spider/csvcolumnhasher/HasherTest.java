package com.spider.csvcolumnhasher;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HasherTest {

	@Test
	public void test() throws Exception {
		String input = "mysensitivedata";
		String salt = "blah";
		String expectedOutput = "a27a312ea98e5eb5537e847c980c40f5bd7a07f6";
		CSVColumnHasher hasher = new CSVColumnHasher();
		String output = hasher.getSaltedHash(input, salt.getBytes(), 10);
		assertEquals(expectedOutput, output);
	}

}
