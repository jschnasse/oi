/* Copyright 2021 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.oi.writer;

import org.schnasse.oi.writer.base.Writer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class YamlWriter {

	public static ObjectMapper createMapper() {
		return new ObjectMapper(new YAMLFactory()).configure(SerializationFeature.INDENT_OUTPUT, true);
	}

	public static void gprint(Object map) {
		Writer.gprint(createMapper(), map);
	}
}
