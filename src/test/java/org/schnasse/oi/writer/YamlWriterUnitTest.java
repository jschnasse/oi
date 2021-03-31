/* Copyright 2021 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.oi.writer;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.Test;
import org.schnasse.oi.reader.XmlReader;

public class YamlWriterUnitTest extends WriterUnitTest {

	@Test
	public void testYamlWriter() throws IOException {
		try (InputStream input = new ByteArrayInputStream(
				new String("<?xml version='1.1' encoding='UTF-8'?><LinkedHashMap><root>test</root></LinkedHashMap>")
						.getBytes())) {
			Map<String, Object> map = XmlReader.getMap(input);
			YamlWriter.gprint(map);
			assertEquals("---root:-\"test\"", outContent.toString().replaceAll("\\s+", ""));
		}
	}
}
