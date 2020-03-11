package com.happn.techtest.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.happn.techtest.model.Zone;

/**
 * Conversion des objets du modèle en objet JSON et inversement.
 * 
 * @author genkl
 *
 */
public class JsonConverter {

	/**
	 * Convertit un Objet en JSON
	 * 
	 * @param object l'Objet à convertir
	 * @return la représentation JSON de l'object
	 */
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

	/**
	 * Transforme un String JSON en objet Zone
	 * 
	 * @param json JSON pouvant être convertit en {@link Zone}
	 * @return {@link Zone}
	 * @throws JsonProcessingException
	 */
	public static Zone jsonToZone(String json) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		Zone tZone = mapper.readValue(json, Zone.class);
		return tZone.fillCoordinate(0.5f);
	}

	/**
	 * Retourne un objet JSON contenant la valeur intValue
	 * 
	 * <pre>
	 * Exemple de retour :
	 * 
	 * }
	 *  "value" : 2
	 * }
	 * </pre>
	 * 
	 * @param intValue entier à retourner
	 * @return une String JSON contenant intValue.
	 */
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
