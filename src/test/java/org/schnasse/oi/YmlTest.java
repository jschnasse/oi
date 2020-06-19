package org.schnasse.oi;

import java.util.Map;

import org.junit.Test;

public class YmlTest {
	@Test
	public void yml_to_json() throws Exception {
		Map<String, Object> yml = org.schnasse.oi.reader.YamlReader
				.getMap(Thread.currentThread().getContextClassLoader().getResourceAsStream("yml/in/HT015847062.yml"));
		Map<String, Object> json = org.schnasse.oi.reader.JsonReader.getMap(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("yml/out/HT015847062.yml.json"));
		// TestHelper.mapCompare(yml, json);
	}
}
