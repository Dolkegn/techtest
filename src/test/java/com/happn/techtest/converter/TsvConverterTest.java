package com.happn.techtest.converter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.happn.techtest.model.POI;

class TsvConverterTest {

	@Test
	void testConvertTSVToPoIs() {
		try {
			URL url = Thread.currentThread().getContextClassLoader().getResource("jUnitPois.tsv");
			List<POI> convertedPois = TsvConverter.tsvToPois(new File(url.getPath()));

			assertTrue(convertedPois.size() == 8);
			assertTrue(convertedPois.contains(new POI("id1", -48.6f, -37.7f)));
			assertTrue(convertedPois.contains(new POI("id2", -27.1f, 8.4f)));
			assertTrue(convertedPois.contains(new POI("id3", 6.6f, -6.9f)));
			assertTrue(convertedPois.contains(new POI("id4", -2.3f, 38.3f)));
			assertTrue(convertedPois.contains(new POI("id5", 6.8f, -6.9f)));
			assertTrue(convertedPois.contains(new POI("id6", -2.5f, 38.3f)));
			assertTrue(convertedPois.contains(new POI("id7", 0.1f, -0.1f)));
			assertTrue(convertedPois.contains(new POI("id8", -2.1f, 38.1f)));

		} catch (FileNotFoundException e) {
			fail("Should find the file");
		}
	}

	@Test
	void testNoTSVFile() {
		try {
			TsvConverter.tsvToPois(new File("noFile.tsv"));
			fail("Found a file who doesn't exists");
		} catch (FileNotFoundException e) {
		}
	}

	@Test
	void testWrongTSVFormat() {
		try {
			URL url = Thread.currentThread().getContextClassLoader().getResource("jUnitWrongPattern.tsv");
			List<POI> convertedPois = TsvConverter.tsvToPois(new File(url.getPath()));
			assertTrue(convertedPois.size() == 0);
		} catch (FileNotFoundException e) {
			fail("Should find the file");
		}
	}
}
