package com.happn.techtest.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

class POITest {

	private POI refPOI = new POI("poiId", 10, -12.2f);
	private POI poiCopy = new POI("poiId", 10, -12.2f);

	@Test
	void testEqualsSameReferences() {
		assertTrue(refPOI.equals(refPOI));
	}

	@Test
	void testEqualsDifferentReferences() {
		assertTrue(refPOI.equals(poiCopy));
	}

	@Test
	public void testEqualsDifferentClasses() {
		final class JunitPoi extends POI {
			public JunitPoi(String id, float latitude, float longitude) {
				super(id, latitude, longitude);
			}
		}

		assertFalse(refPOI.equals(new JunitPoi("poiId", 10, -12.2f)));
	}

	@Test
	public void testContainsInSet() {
		Set<POI> pois = new HashSet<>();
		pois.add(refPOI);
		assertTrue(pois.contains(refPOI));
		assertTrue(pois.contains(poiCopy));
	}

	@Test
	public void testGetInMap() {
		Map<POI, String> zones = new HashMap<>();
		final String OK = "OK";
		zones.put(refPOI, OK);

		assertEquals(OK, zones.get(refPOI));
		assertEquals(OK, zones.get(poiCopy));
	}

}
