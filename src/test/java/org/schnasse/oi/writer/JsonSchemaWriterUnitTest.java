package org.schnasse.oi.writer;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.Test;
import org.schnasse.oi.reader.XmlReader;

public class JsonSchemaWriterUnitTest extends WriterUnitTest  {
	@Test
	public void testJsonSchemaWriter() throws IOException {
		try (InputStream input = new ByteArrayInputStream(
				new String("<?xml version='1.1' encoding='UTF-8'?><LinkedHashMap><root>test</root></LinkedHashMap>")
						.getBytes())) {
			Map<String, Object> map = XmlReader.getMap(input);
			JsonSchemaWriter.gprint(map);
			assertEquals(
					"{\"type\":\"object\",\"additionalProperties\":{\"type\":\"any\"}}",
					outContent.toString().replaceAll("\\s+", ""));

		}
	}
}
