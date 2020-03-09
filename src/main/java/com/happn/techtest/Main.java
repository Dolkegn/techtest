package com.happn.techtest;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.happn.techtest.converter.JsonConverter;
import com.happn.techtest.converter.TsvConverter;
import com.happn.techtest.impl.WorldManager;
import com.happn.techtest.model.POI;
import com.happn.techtest.model.World;
import com.happn.techtest.model.Zone;

/**
 * 
 * @author genkl
 *
 */
public class Main {
	private static World world;
	private static List<POI> pois;
	private static float zoneIncrement = 0.5f;

	public static void main(String[] args) throws ParseException {
		for (String arg : args) {
			System.out.println("Argument = " + arg);
		}

		// Instanciation du monde
		world = new World(-90, 90, -180, 180, zoneIncrement);
		
		final Options options = configParameters();
		final CommandLineParser parser = new DefaultParser();
		final CommandLine line = parser.parse(options, args);

		// Fichier de PoIs
		URL url = Thread.currentThread().getContextClassLoader().getResource("META-INF/defaultPOIS.tsv");
		String filename = line.getOptionValue("file", url.getPath());
		pois = new ArrayList<>();
		try {
			pois = TsvConverter.tsvToPois(new File(filename));
		} catch (FileNotFoundException e) {
			System.err.println(
					"Bad parameter for filename. Please enter a valid path." + new File(filename).getAbsolutePath());
			System.exit(3);
		}

		// Compter les POI d'une zone.
		String jsonZone = line.getOptionValue("nbpoi", "");
		if (!jsonZone.isEmpty()) {
			countPoiByZone(jsonZone);
			System.exit(0);
		}

		// Récupérer les n zones les plus denses.
		String numberOfDensestZones = line.getOptionValue("densest", "");
		if (!numberOfDensestZones.isEmpty()) {
			world.addPointsOfInterest(pois);
			showNDensestZones(numberOfDensestZones);
			System.exit(0);
		}

		final HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("Main", options, true);
		System.exit(0);
	}
	
	private static void countPoiByZone(String jsonZone) {
		Zone zone;
		try {
			zone = JsonConverter.jsonToZone(jsonZone);

			// Seuls les POI de la zone nous intéressent au final. il est inutile de générer
			// le monde complet. on génère un monde de la taille de la zone pour calculer
			// ses POI
			World zoneWorld = new World(zone, zoneIncrement);
			zoneWorld.addPointsOfInterest(pois);
			WorldManager worldManager = new WorldManager(zoneWorld);

			System.out.println(JsonConverter.intToJson(worldManager.getNumberOfPoiInZone(zone)));
		} catch (JsonProcessingException e) {
			System.err.println("Bad parameter for nbpoi. Pleaser enter a valid JSON.");
			System.exit(3);
		}
	}

	private static void showNDensestZones(String numberOfDensestZones) {
		int n = 0;
		try {
			n = Integer.valueOf(numberOfDensestZones);
			WorldManager worldManager = new WorldManager(world);

			for (Zone densestZone : worldManager.getnDensestZone(n)) {
				System.out.println(JsonConverter.toJson(densestZone));
			}

		} catch (NumberFormatException e) {
			System.err.println("Bad parameter for densest. Please enter an integer.");
			System.exit(3);
		}
	}

	private static Options configParameters() {
		final Option poisFileOption = Option.builder("f") 
	            .longOpt("file")
	            .desc("Path du fichier TSV contenant les points d'intérêt")
	            .hasArg(true) 
	            .argName("file")
	            .required(false) 
	            .build();
	
		final Option nbPoiOption = Option.builder("n") 
	            .longOpt("nbpoi") //
	            .desc("Retourne le nombre de PoI pour une zone donnée. ")
	            .hasArg(true) 
	            .argName("nbpoi")
	            .required(false) 
	            .build();
		
		final Option densestZonesOption = Option.builder("d") 
	            .longOpt("densest") //
	            .desc("Retourne les n zones les plus denses.")
	            .hasArg(true) 
	            .argName("densest")
	            .required(false) 
	            .build();
		
		final Options options = new Options();
		options.addOption(poisFileOption);
		options.addOption(nbPoiOption);
		options.addOption(densestZonesOption);
		
		return options;
	}
}
