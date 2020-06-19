/* Copyright 2020 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.oi.writer;

import org.schnasse.oi.writer.base.Writer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonWriter {

	public static ObjectMapper createMapper() {
		return new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);// .configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED,
																						// true);
	}

	public static void gprint(Object map) {
		Writer.gprint(createMapper(), map);
	}
}
