/* Copyright 2021 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.oi.writer;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.Test;
import org.schnasse.oi.reader.JsonReader;

public class XmlWriterUnitTest extends WriterUnitTest {

	@Test
	public void testXmlWriter() throws IOException {
		try (InputStream input = new ByteArrayInputStream(new String("{\"root\": \"test\"}").getBytes())) {
			Map<String, Object> map = JsonReader.getMap(input);
			XmlWriter.gprint(map);
			assertEquals("<?xml version='1.1' encoding='UTF-8'?><LinkedHashMap><root>test</root></LinkedHashMap>"
					.replaceAll("\\s+", ""), outContent.toString().replaceAll("\\s+", ""));
		}
	}
}
