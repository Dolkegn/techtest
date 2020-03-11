package com.happn.techtest;

import static org.junit.jupiter.api.Assertions.fail;

import org.apache.commons.cli.ParseException;

/**
 * N'est pas une vraie classe de test, me sert à explorer et tester les sorties
 * consoles via l'IDE
 * 
 * @author genkl
 *
 */
class MainTest {

	private String poisFilepath = Thread.currentThread().getContextClassLoader().getResource("jUnitMainPoisFile.tsv")
			.getPath();

//	@Test
	void test() {
		String[] jUnitArgs = { "--nbpoi", "{\"min_lat\": 6.5,\"min_lon\": -7}" };

		try {
			Main.main(jUnitArgs);
		} catch (ParseException e) {
			fail("Shouldn't get ParseException : " + e);
		}
	}
	
//	@Test
	void testDensest() {
		String[] jUnitArgs = { "--densest", "3" };

		try {
			Main.main(jUnitArgs);
		} catch (ParseException e) {
			fail("Shouldn't get ParseException : " + e);
		}
	}

//	@Test
	void testDensestInFile() {
		String[] jUnitArgs = { "--file", poisFilepath, "--densest", "3" };

		try {
			Main.main(jUnitArgs);
		} catch (ParseException e) {
			fail("Shouldn't get ParseException : " + e);
		}
	}

//	@Test
	void testPoisInZoneFromFile() {
		String[] jUnitArgs = { "--file", poisFilepath, "--nbpoi", "{\"min_lat\": 1.0,\"min_lon\": 1.0}" };

		try {
			Main.main(jUnitArgs);
		} catch (ParseException e) {
			fail("Shouldn't get ParseException : " + e);
		}
	}
}
