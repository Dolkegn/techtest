package com.happn.techtest.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World extends Zone {
	private final float increment;
	private Map<Zone, List<POI>> zonesWithInterest = new HashMap<>();

	public World(float minLatitude, float maxLatitude, float minLongitude, float maxLongitude, float increment) {
		super(minLatitude, maxLatitude, minLongitude, maxLongitude);
		this.increment = increment;
	}

	public World(Zone zone, float increment) {
		this(zone.getMin_lat(), zone.getMax_lat(), zone.getMin_lon(), zone.getMax_lon(), increment);
	}

	public float getIncrement() {
		return increment;
	}

	/**
	 * Retourne l'ensemble des {@link Zone} contenant des {@link POI}.</br>
	 * Cette Map est initialement vide et se remplit lorsqu'on ajoute des points
	 * d'intérêt au {@link World} via {@link World#addPointsOfInterest(List)}.
	 * 
	 * @return Map mappant une {@link Zone} avec les {@link POI} qui la composent
	 */
	public Map<Zone, List<POI>> getZonesWithInterest() {
		return zonesWithInterest;
	}

	/**
	 * Fournit une liste de {@link POI} au monde en cours afin de les associer au
	 * zones du monde.
	 * 
	 * @param pointsOfInterest liste des points d'intérêt à renseigner
	 */
	public void addPointsOfInterest(List<POI> pointsOfInterest) {
		pointsOfInterest.stream().filter(this::isInWorld).forEach(this::associatePoIAndZone);
	}
	
	private boolean isInWorld(POI pointOfInterest) {
		float poiLat = pointOfInterest.getLatitude();
		float poiLong = pointOfInterest.getLongitude();
		return areCoordinateInWorld(poiLat, poiLong);
	}

	private boolean areCoordinateInWorld(float latitude, float longitude) {
		return areCoordinateGretterEqualsThanMinWorldBorder(latitude, longitude) 
				&& latitude <= this.getMax_lat()
				&& longitude <= this.getMax_lon();
	}

	private boolean areCoordinateGretterEqualsThanMinWorldBorder(float latitude, float longitude) {
		return latitude >= this.getMin_lat() && longitude >= this.getMin_lon();
	}

	protected void associatePoIAndZone(POI pointOfInterest) {
		List<Zone> poiZones = createZoneFromPoI(pointOfInterest);
		poiZones.forEach(zone -> addPoIToZone(zone, pointOfInterest));
	}
	
	private List<Zone> createZoneFromPoI(POI pointOfInterest) {
		float minBorderZoneLatitude = getZoneMinBorderValue(pointOfInterest.getLatitude());
		float minBorderZoneLongitude = getZoneMinBorderValue(pointOfInterest.getLongitude());
		
		return createZones(pointOfInterest, minBorderZoneLatitude, minBorderZoneLongitude);
	}
	
	private float getZoneMinBorderValue(float poiCoordinate) {
		float absolutePoiCoordinate = Math.abs(poiCoordinate);
		float distToZoneCoordinate = absolutePoiCoordinate % increment;
		if (distToZoneCoordinate == 0) {
			return poiCoordinate;
		} else {
			float borderValue = absolutePoiCoordinate - distToZoneCoordinate;
			return poiCoordinate < 0 ? -(borderValue + increment) : borderValue;
		}
	}

	private List<Zone> createZones(POI pointOfInterest, float zoneMinLatitude, float zoneMinLongitude) {
		List<Zone> zonesForPoi = new ArrayList<>();
		boolean isPoiLatitudeOnManyZone = zoneMinLatitude == pointOfInterest.getLatitude()
				&& zoneMinLatitude > this.getMin_lat();
		boolean isPoiLongitudeOnManyZone = zoneMinLongitude == pointOfInterest.getLongitude()
				&& zoneMinLongitude > this.getMin_lon();

		if (isPoiLatitudeOnManyZone)
			zonesForPoi.add(createZone(zoneMinLatitude - increment, zoneMinLongitude));

		if (isPoiLongitudeOnManyZone)
			zonesForPoi.add(createZone(zoneMinLatitude, zoneMinLongitude - increment));

		if (isPoiLatitudeOnManyZone && isPoiLongitudeOnManyZone)
			zonesForPoi.add(createZone(zoneMinLatitude - increment, zoneMinLongitude - increment));

		if (areCoordinateGretterEqualsThanMinWorldBorder(zoneMinLatitude, zoneMinLongitude)
				&& areCoordinateLessThanMaxWorldBorder(zoneMinLatitude, zoneMinLongitude))
			zonesForPoi.add(createZone(zoneMinLatitude, zoneMinLongitude));

		return zonesForPoi;
	}

	private Zone createZone(float minLatitude, float minLongitude) {
		return new Zone(minLatitude, minLatitude + increment, minLongitude, minLongitude + increment);
	}

	private boolean areCoordinateLessThanMaxWorldBorder(float latitude, float longitude) {
		return latitude < this.getMax_lat() && longitude < this.getMax_lon();
	}

	private void addPoIToZone(Zone zone, POI poi) {
		List<POI> poisForZone = zonesWithInterest.get(zone);
		if (poisForZone == null) {
			zonesWithInterest.put(zone, new ArrayList<>(Arrays.asList(poi)));
		} else {
			poisForZone.add(poi);
		}
	}

}
