/* Copyright 2019 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.cjxy.writer;

import java.util.Map;

import org.schnasse.cjxy.writer.base.Writer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvFactory;

public class CsvWriter {

	public static ObjectMapper createMapper() {
		return new ObjectMapper(new CsvFactory()).configure(SerializationFeature.INDENT_OUTPUT, true);
	}

	public static void gprint(Map<String, Object> map) {
		Writer.gprint(createMapper(), map);
	}
}
