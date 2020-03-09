package com.happn.techtest.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorldTest {
	private World jUnitWorld;
	
	@BeforeEach
	public void initWorld() {
		jUnitWorld = new World(-90, 90, -180, 180, 0.5f);
	}
	
	@Test
	public void testWorldWith2PoI() {
		POI poi1 = new POI("id1", 0.2f, 0.3f);
		POI poi2 = new POI("id2", 1.6f, 1.7f);
		List<POI> jUnitPois = Arrays.asList(poi1, poi2);
		
		jUnitWorld.addPointsOfInterest(jUnitPois);

		assertNumberOfZoneIs(2);
		assertZoneExistsAndContains(new Zone(0, 0.5f, 0, 0.5f), poi1);
		assertZoneExistsAndContains(new Zone(1.5f, 2, 1.5f, 2), poi2);
	}
	
	@Test
	public void testDontAddPOIIfOutWorld() {
		POI offWorldPoi = new POI("id_offworld", -100.2f, -200.2f);
		List<POI> jUnitPois = Arrays.asList(offWorldPoi);
		
		jUnitWorld.addPointsOfInterest(jUnitPois);
		
		assertNumberOfZoneIs(0);
	}

	@Test
	public void testOnePoiIn4Zones() {
		POI poiIn4Zones = new POI("idMultipleZones", 1f, 1f);
		List<POI> jUnitPois = Arrays.asList(poiIn4Zones);

		jUnitWorld.addPointsOfInterest(jUnitPois);

		assertNumberOfZoneIs(4);
		assertZoneExistsAndContains(new Zone(1, 1.5f, 1, 1.5f), poiIn4Zones);
		assertZoneExistsAndContains(new Zone(0.5f, 1, 1, 1.5f), poiIn4Zones);
		assertZoneExistsAndContains(new Zone(1, 1.5f, 0.5f, 1), poiIn4Zones);
		assertZoneExistsAndContains(new Zone(0.5f, 1, 0.5f, 1), poiIn4Zones);
	}

	@Test
	public void testPoiInWorldLimit() {
		POI poiWorldLimit = new POI("idWorldLimit", -90f, 180f);
		List<POI> jUnitPois = Arrays.asList(poiWorldLimit);

		jUnitWorld.addPointsOfInterest(jUnitPois);

		assertNumberOfZoneIs(1);
		assertZoneExistsAndContains(new Zone(-90, -89.5f, 179.5f, 180), poiWorldLimit);
	}

	@Test
	public void test3PoiInSameZone() {
		POI poiSameZone1 = new POI("id1", 10.1f, -10.1f);
		POI poiSameZone2 = new POI("id2", 10.2f, -10.2f);
		POI poiSameZone3 = new POI("id3", 10.3f, -10.3f);
		List<POI> jUnitPois = Arrays.asList(poiSameZone1, poiSameZone2, poiSameZone3);

		jUnitWorld.addPointsOfInterest(jUnitPois);

		assertNumberOfZoneIs(1);
		assertZoneExistsAndContains(new Zone(10, 10.5f, -10.5f, -10), poiSameZone1, poiSameZone2, poiSameZone3);
	}

	private void assertNumberOfZoneIs(int expectedNumberOfZones) {
		Map<Zone, List<POI>> poisByZone = jUnitWorld.getZonesWithInterest();
		assertTrue(poisByZone.size() == expectedNumberOfZones);
	}

	private void assertZoneExistsAndContains(Zone zoneToCheck, POI... pois) {
		List<POI> poisInZone = jUnitWorld.getZonesWithInterest().get(zoneToCheck);
		assertTrue(poisInZone.size() == pois.length);
		Arrays.asList(pois).forEach(poi -> assertTrue(poisInZone.contains(poi)));
	}
}
