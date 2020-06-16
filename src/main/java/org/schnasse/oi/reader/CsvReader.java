/* Copyright 2020 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.oi.reader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder;

public class CsvReader {
	public static Map<String, Object> getMap(InputStream inputStream, String[] header, String delimiter,
			String quoteChar) {
		try {
			if (delimiter == null) {
				delimiter = ",";
			}
			if (quoteChar == null) {
				quoteChar = "\"";
			}
			Builder sb = CsvSchema.builder();
			sb.setColumnSeparator(delimiter.charAt(0));
			sb.setQuoteChar(quoteChar.charAt(0));

			CsvSchema schema = sb.build();
			CsvMapper mapper = new CsvMapper();
			mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);

			MappingIterator<String[]> it = mapper.readerFor(String[].class).with(schema).readValues(inputStream);
			List<Map<String, Object>> all = new ArrayList<>();
			if (header == null) {
				header = it.next();
			}
			while (it.hasNext()) {
				String[] row = it.next();
				Map<String, Object> map = new TreeMap<>();
				if (row.length != header.length) {
					System.out.println(Arrays.toString(row) + " " + row.length + "\n" + Arrays.toString(header) + " "
							+ header.length);
				}
				for (int j = 0; j < row.length; j++) {
					String col = row[j];
					map.put(header[j].trim(), col);
				}
				all.add(map);
			}
			Map<String, Object> result = new HashMap<>();
			result.put("data", all);
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
