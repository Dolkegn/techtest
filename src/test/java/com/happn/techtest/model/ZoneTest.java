package com.happn.techtest.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

class ZoneTest {

	private Zone refZone = new Zone(-12f, 45f, 7.5f, 9f);
	private Zone copiedZone = new Zone(-12f, 45f, 7.5f, 9f);

	@Test
	void testEqualsSameReferences() {
		assertTrue(refZone.equals(refZone));
	}

	@Test
	void testEqualsDifferentReferences() {
		assertTrue(refZone.equals(copiedZone));
	}

	@Test
	public void testEqualsDifferentClasses() {
		final class JunitZone extends Zone {
			public JunitZone(float minLatitude, float maxLatitude, float minLongitude, float maxLongitude) {
				super(minLatitude, maxLatitude, minLongitude, maxLongitude);
			}
		}

		assertFalse(refZone.equals(new JunitZone(12f, 45f, 7.5f, 9f)));
	}

	@Test
	public void testContainsInSet() {
		Set<Zone> zones = new HashSet<>();
		zones.add(refZone);
		assertTrue(zones.contains(refZone));
		assertTrue(zones.contains(copiedZone));
	}

	@Test
	public void testGetInMap() {
		Map<Zone, String> zones = new HashMap<>();
		final String OK = "OK";
		zones.put(refZone, OK);

		assertEquals(OK, zones.get(refZone));
		assertEquals(OK, zones.get(copiedZone));
	}
}
