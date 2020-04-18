/* Copyright 2020 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.oi.writer;

import org.schnasse.oi.writer.base.Writer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

public class XmlWriter {
	public static ObjectMapper createMapper() {
		XmlMapper mapper = new XmlMapper();
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
		mapper.configure(ToXmlGenerator.Feature.WRITE_XML_1_1, true);
		return mapper;
	}

	public static void gprint(Object map) {
		Writer.gprint(createMapper(), map);
	}
}
