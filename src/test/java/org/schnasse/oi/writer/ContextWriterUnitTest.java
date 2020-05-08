package org.schnasse.oi.writer;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.Test;
import org.schnasse.oi.reader.JsonReader;

public class ContextWriterUnitTest extends WriterUnitTest {

	@Test
	public void testContextWriter() throws IOException {
		try (InputStream input = new ByteArrayInputStream(new String("{\"root\": \"test\"}").getBytes())) {
			Map<String, Object> map = JsonReader.getMap(input);
			ContextWriter.gprint(map);
			assertEquals("{\"@context\":{\"root\":{\"@id\":\"info:oi/root\"}}}",
					outContent.toString().replaceAll("\\s+", ""));
		}
	}
}
