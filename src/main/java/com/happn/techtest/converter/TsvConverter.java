package com.happn.techtest.converter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.happn.techtest.model.POI;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

/**
 * Convertit un fichier TSV en Liste de {@link POI}.
 * 
 * @author genkl
 *
 */
public class TsvConverter {
	private static final String ROW_PATTERN = "^(?<id>.*)\\s+(?<latitude>[-+]?[0-9]*\\.?[0-9]+)\\s+(?<longitude>[-+]?[0-9]*\\.?[0-9]+)";
	private static final Pattern pattern = Pattern.compile(ROW_PATTERN);

	public static List<POI> tsvToPois(File tsvFile) throws FileNotFoundException {
		TsvParserSettings settings = new TsvParserSettings();
		TsvParser parser = new TsvParser(settings);

		List<String[]> rows = parser.parseAll(new FileReader(tsvFile));

		return convertToPois(rows);
	}

	private static List<POI> convertToPois(List<String[]> rows) {
		List<POI> convertedPois = new ArrayList<>();
		for (String[] row : rows) {
			POI poi = stringTableToPoi(row);
			if (poi != null) {
				convertedPois.add(stringTableToPoi(row));
			}
		}
		return convertedPois;
	}

	private static POI stringTableToPoi(String[] row) {
		if (row.length == 1) {
			Matcher matcher = pattern.matcher(row[0].trim());
			if (matcher.find()) {
				if (matcher.groupCount() == 3) {
					return new POI(matcher.group("id"), 
							Float.valueOf(matcher.group("latitude")),
							Float.valueOf(matcher.group("longitude")));
				}
			}
		}
		return null;
	}
}