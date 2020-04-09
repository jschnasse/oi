/* Copyright 2020 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.cjxy.writer;

import java.util.Map;

import org.schnasse.cjxy.writer.base.Writer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonWriter {

	public static ObjectMapper createMapper() {
		return new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);
	}

	public static void gprint(Map<String, Object> map) {
		Writer.gprint(createMapper(), map);
	}
}
