/* Copyright 2020 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.cjxy.writer;

import org.schnasse.cjxy.writer.base.Writer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlWriter {
	public static ObjectMapper createMapper() {
		return new XmlMapper().configure(SerializationFeature.INDENT_OUTPUT, true);
	}

	public static void gprint(Object map) {
		Writer.gprint(createMapper(), map);
	}
}
