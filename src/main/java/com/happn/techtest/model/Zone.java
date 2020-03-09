package com.happn.techtest.model;

import java.util.Objects;

/**
 * Représente une Zone définie par 4 coordonnées. Les coordonnées d'une
 * {@link Zone} peuvent être instanciées mais non modifiées.
 * 
 * @author genkl
 *
 */
public class Zone {
	private Float min_lat;
	private Float max_lat;
	private Float min_lon;
	private Float max_lon;
	
	public Zone() {
	}

	public Zone(float minLatitude, float maxLatitude, float minLongitude, float maxLongitude) {
		this.min_lat = minLatitude;
		this.min_lon = minLongitude;
		this.max_lat = maxLatitude;
		this.max_lon = maxLongitude;
	}
	
	public Float getMin_lat() {
		return min_lat;
	}

	public Float getMax_lat() {
		return max_lat;
	}

	public Float getMin_lon() {
		return min_lon;
	}

	public Float getMax_lon() {
		return max_lon;
	}
	
	public void setMin_lat(float min_lat) {
		if (this.min_lat == null)
			this.min_lat = min_lat;
	}

	public void setMax_lat(float max_lat) {
		if (this.max_lat == null)
			this.max_lat = max_lat;
	}

	public void setMin_long(float min_long) {
		if (this.min_lon == null)
			this.min_lon = min_long;
	}

	public void setMax_long(float max_long) {
		if (this.max_lon == null)
			this.max_lon = max_long;
	}

	public Zone fillCoordinate(float increment) {
		if ((min_lat != null || max_lat != null) && (min_lon != null || max_lon != null)) {
			if (min_lat != null)
				setMax_lat(min_lat + increment);
			if (max_lat != null)
				setMin_lat(max_lat - increment);
			if (min_lon != null)
				setMax_long(min_lon + increment);
			if (max_lon != null)
				setMin_long(max_lon - increment);
			return this;
		} else {
			throw new UnsupportedOperationException("Unable to fill coordinate for the Zone : " + this.toString());
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (this.getClass() != obj.getClass())
			return false;

		Zone other = (Zone) obj;
		return this.min_lat.equals(other.getMin_lat()) && this.max_lat.equals(other.getMax_lat())
				&& this.min_lon.equals(other.getMin_lon()) && this.max_lon.equals(other.getMax_lon());
	}

	@Override
	public int hashCode() {
		return Objects.hash(min_lat, min_lon, max_lat, max_lon);
	}
}