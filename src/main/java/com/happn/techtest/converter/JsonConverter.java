package com.happn.techtest.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.happn.techtest.model.Zone;

public class JsonConverter {

	public static String toJson(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			System.out.println("Conversion de l'objet en JSON impossible.");
			e.printStackTrace();
		}
		return "";
	}

	public static Zone jsonToZone(String json) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		Zone tZone = mapper.readValue(json, Zone.class);
		return tZone.fillCoordinate(0.5f);
	}

	public static String intToJson(int intValue) {
		return toJson(new Value(intValue));
	}

	private static class Value {
		private int value;

		public Value(int value) {
			super();
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
}
