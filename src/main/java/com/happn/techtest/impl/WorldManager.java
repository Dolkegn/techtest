package com.happn.techtest.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.happn.techtest.model.POI;
import com.happn.techtest.model.World;
import com.happn.techtest.model.Zone;

/**
 * Manipule les données d'un {@link World} pour extraire et formater un certain
 * nombre de paramètres.
 * 
 * @author genkl
 *
 */
public class WorldManager {
	private World world;

	public WorldManager(World world) {
		this.world = world;
	}

	/**
	 * Retourne le nombre de PoI présents dans une zone donnée
	 * 
	 * @param zone
	 * @return
	 */
	public int getNumberOfPoiInZone(Zone zone) {
		List<POI> poisInZone = world.getZonesWithInterest().get(zone);
		return poisInZone == null ? 0 : poisInZone.size();
	}

	/**
	 * Retourne les n (n = numberOfZones) zones contenant le + de {@link POI}.
	 * 
	 * @param numberOfZones le nombre de zones à retourner
	 * @return
	 */
	public List<Zone> getnDensestZone(int numberOfZones) {
		Comparator<Map.Entry<Zone, List<POI>>> compareByNumberOfPoI = (entry1, entry2) -> entry2.getValue().size()
				- entry1.getValue().size();
		return world.getZonesWithInterest().entrySet().stream()
				.sorted(compareByNumberOfPoI)
				.map(Map.Entry::getKey)
				.limit(numberOfZones)
				.collect(Collectors.toList());
	}
}