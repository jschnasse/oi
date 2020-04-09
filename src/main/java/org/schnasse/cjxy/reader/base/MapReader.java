/* Copyright 2019 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.cjxy.reader.base;

/* Copyright 2019 Jan Schnasse. Licensed under the EPL 2.0 */

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;

public class MapReader {
	public static Map<String, Object> getMap(InputStream inputStream) {
		try {
			CsvMapper mapper = new CsvMapper();
			mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
			MappingIterator<String[]> it = mapper.readerFor(String[].class).readValues(inputStream);
			List<Map<String, Object>> all = new ArrayList<>();
			while (it.hasNext()) {
				String[] row = it.next();
				Map<String, Object> map = new TreeMap<>();
				for (int j = 0; j < row.length; j++) {
					String col = row[j];
					map.put(j + "", col);
				}
				all.add(map);
			}
			Map<String, Object> result = new HashMap<>();
			result.put("map", all);
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
