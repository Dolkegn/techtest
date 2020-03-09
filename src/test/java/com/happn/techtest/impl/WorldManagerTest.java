package com.happn.techtest.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.happn.techtest.model.POI;
import com.happn.techtest.model.World;
import com.happn.techtest.model.Zone;

class WorldManagerTest {

	private WorldManager worldManager;
	private World jUnitWorld;

	private Zone zoneWith4PoI = new Zone(-5, -4, -5, -4);
	private Zone zoneWith2PoILat = new Zone(-6, -5, -5, -4);
	private Zone zoneWith2PoILong = new Zone(-5, -4, -6, -5);
	private Zone zoneWith1PoI = new Zone(-6, -5, -6, -5);
	@BeforeEach
	public void init() {
		jUnitWorld = new World(-10, 10, -10, 10, 1);
		jUnitWorld.addPointsOfInterest(createTestPoIs("jUnitPoI", -5, -5));

		this.worldManager = new WorldManager(jUnitWorld);
	}

	/**
	 * Connaissant les coordonnées d'une zone, créé une liste de 4 {@link POI} tels
	 * que :<br>
	 * - un appartient à 1 zone<br>
	 * - un appartient à 2 zones partageant la même latitude<br>
	 * - un appartient à 2 zones partageant la même longitude<br>
	 * - un appartient à 4 zones partageant latitude et/ou longitude
	 * 
	 * <pre>
	 *  ___ ___   
	 * |   . . |
	 * |___._._|
	 * |   |   |
	 * |___|___|
	 * 
	 * (Ci-dessus, les points représentent les positions des {@link POI}. 
	 * Les coordonnées fournies sont au centre du cube.
	 * Le cube représente 4 zones)
	 * </pre>
	 * 
	 * @param baseId         le préfixe de l'ID des POI
	 * @param latBorderZone  la latitude correspondant à un bord de zone
	 * @param longBorderZone la longitude correspondant à un bord de zone
	 * @return la liste de {@link POI} générés
	 */
	private List<POI> createTestPoIs(String baseId, float latBorderZone, float longBorderZone) {
		float poiLat = latBorderZone + 0.1f;
		float poiLong = longBorderZone + 0.1f;
		return new ArrayList<>(Arrays.asList(new POI(baseId + "_in1Zone", poiLat, poiLong),
				new POI(baseId + "_in2ZonesLat", latBorderZone, poiLong),
				new POI(baseId + "_in2ZonesLong", poiLat, longBorderZone),
				new POI(baseId + "_in4Zones", latBorderZone, longBorderZone)));
	}

	@Test
	void testGet4PoisInZone() {
		assertNumberOfPoIInZoneIs(zoneWith4PoI, 4);
	}

	@Test
	void testGet2PoiInZone() {
		assertNumberOfPoIInZoneIs(zoneWith2PoILat, 2);
		assertNumberOfPoIInZoneIs(zoneWith2PoILong, 2);
	}

	@Test
	void testGet1PoiInZone() {
		assertNumberOfPoIInZoneIs(zoneWith1PoI, 1);
	}

	@Test
	void testZoneOutOfWorld() {
		jUnitWorld.addPointsOfInterest(new ArrayList<>(Arrays.asList(new POI("PoIOutWorld", 15.1f, 15.1f))));
		assertNumberOfPoIInZoneIs(new Zone(15, 16, 15, 16), 0);
	}

	@Test
	void testZoneBordersAreWorldBorders() {
		jUnitWorld = new World(0, 1, 0, 1, 1);
		jUnitWorld.addPointsOfInterest(createTestPoIs("poiWorld", 0, 0));
		worldManager = new WorldManager(jUnitWorld);

		assertNumberOfPoIInZoneIs(new Zone(0, 1, 0, 1), 4);
		assertNumberOfPoIInZoneIs(new Zone(-1, 0, 0, 1), 0);
		assertNumberOfPoIInZoneIs(new Zone(0, 1, -1, 0), 0);
		assertNumberOfPoIInZoneIs(new Zone(-1, 0, -1, 0), 0);
	}

	private void assertNumberOfPoIInZoneIs(Zone zone, int expectedNumberOfPoI) {
		int numberOfPoI = worldManager.getNumberOfPoiInZone(zone);
		assertTrue(numberOfPoI == expectedNumberOfPoI);
	}

	@Test
	void testGetDensestZone() {
		List<Zone> densestZones = worldManager.getnDensestZone(1);
		assertTrue(densestZones.size() == 1);
		assertTrue(densestZones.get(0).equals(zoneWith4PoI));
	}

	@Test
	void testGet3DensestZones() {
		List<Zone> densestZones = worldManager.getnDensestZone(3);
		assertTrue(densestZones.size() == 3);
		assertTrue(densestZones.get(0).equals(zoneWith4PoI));
		assertTrue(densestZones.contains(zoneWith2PoILat));
		assertTrue(densestZones.contains(zoneWith2PoILong));
	}

}
