package org.schnasse.oi;

import java.util.Map;

import org.junit.Test;
import org.schnasse.oi.helper.TestHelper;

public class JsonTest {
	@Test
	public void json_to_yml() throws Exception {
		Map<String, Object> json = org.schnasse.oi.reader.JsonReader
				.getMap(Thread.currentThread().getContextClassLoader().getResourceAsStream("json/in/rosenmontag.json"));
		Map<String, Object> yml = org.schnasse.oi.reader.YamlReader.getMap(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("json/out/rosenmontag.json.yml"));
		TestHelper.mapCompare(json, yml);
	}
}
