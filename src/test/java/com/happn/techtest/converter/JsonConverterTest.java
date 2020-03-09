package com.happn.techtest.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.happn.techtest.model.Zone;

class JsonConverterTest {

	@Test
	void jsonToZone() {
		Zone expectedZone = new Zone(0, 0.5f, -1, -0.5f);
		String json = "{\"min_lat\":0.0,\"max_lat\":0.5,\"min_lon\":-1.0,\"max_lon\":-0.5}";

		try {
			assertEquals(expectedZone, JsonConverter.jsonToZone(json));
		} catch (JsonMappingException e) {
			fail("Shouldn't get a JsonMappingException : " + e.getMessage());
		} catch (JsonProcessingException e) {
			fail("Shouldn't get a JsonProcessingException : " + e.getMessage());
		}
	}

	@Test
	void jsonWithMinLatLongToZone() {
		Zone expectedZone = new Zone(0, 0.5f, -1, -0.5f);
		String json = "{\"min_lat\":0.0,\"min_lon\":-1.0}";

		try {
			assertEquals(expectedZone, JsonConverter.jsonToZone(json));
		} catch (JsonMappingException e) {
			fail("Shouldn't get a JsonMappingException : " + e.getMessage());
		} catch (JsonProcessingException e) {
			fail("Shouldn't get a JsonProcessingException : " + e.getMessage());
		}
	}

	@Test
	void jsonWithMinLatMaxLongToZone() {
		Zone expectedZone = new Zone(0, 0.5f, -1, -0.5f);
		String json = "{\"min_lat\":0.0,\"max_lon\":-0.5}";

		try {
			assertEquals(expectedZone, JsonConverter.jsonToZone(json));
		} catch (JsonMappingException e) {
			fail("Shouldn't get a JsonMappingException : " + e.getMessage());
		} catch (JsonProcessingException e) {
			fail("Shouldn't get a JsonProcessingException : " + e.getMessage());
		}
	}

	@Test
	void jsonWithMaxLatMaxLongToZone() {
		Zone expectedZone = new Zone(0, 0.5f, -1, -0.5f);
		String json = "{\"max_lat\":0.5,\"max_lon\":-0.5}";

		try {
			assertEquals(expectedZone, JsonConverter.jsonToZone(json));
		} catch (JsonMappingException e) {
			fail("Shouldn't get a JsonMappingException : " + e.getMessage());
		} catch (JsonProcessingException e) {
			fail("Shouldn't get a JsonProcessingException : " + e.getMessage());
		}
	}

	@Test
	void jsonWithMaxLatMinLongToZone() {
		Zone expectedZone = new Zone(0, 0.5f, -1, -0.5f);
		String json = "{\"max_lat\":0.5,\"min_long\":-1.0}";

		try {
			assertEquals(expectedZone, JsonConverter.jsonToZone(json));
		} catch (JsonMappingException e) {
			fail("Shouldn't get a JsonMappingException : " + e.getMessage());
		} catch (JsonProcessingException e) {
			fail("Shouldn't get a JsonProcessingException : " + e.getMessage());
		}
	}
}
