package org.schnasse.oi.writer;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.Test;
import org.schnasse.oi.reader.XmlReader;

public class JsonWriterUnitTest extends WriterUnitTest {

	@Test
	public void testJsonWriter() throws IOException {
		try (InputStream input = new ByteArrayInputStream(
				new String("<?xml version='1.1' encoding='UTF-8'?><LinkedHashMap><root>test</root></LinkedHashMap>")
						.getBytes())) {
			Map<String, Object> map = XmlReader.getMap(input);
			JsonWriter.gprint(map);
			assertEquals("{\"root\":[\"test\"]}", outContent.toString().replaceAll("\\s+", ""));
		}
	}
}
