package com.happn.techtest.model;

import java.util.Objects;

/**
 * Point d'intéret.
 * 
 * @author genkl
 *
 */
public class POI {
	private String id;
	private float latitude;
	private float longitude;
	
	public POI(String id, float latitude, float longitude) {
		super();
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public String getId() {
		return id;
	}

	public float getLatitude() {
		return latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (this.getClass() != obj.getClass())
			return false;

		POI other = (POI) obj;
		return this.id.equals(other.getId()) && this.latitude == other.getLatitude()
				&& this.longitude == other.getLongitude();
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, latitude, longitude);
	}
}
