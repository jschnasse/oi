package org.schnasse.oi;

import java.util.Map;

import org.junit.Test;
import org.schnasse.oi.helper.TestHelper;

public class CsvTest {
	@Test
	public void csv_to_yml() throws Exception {
		Map<String, Object> csv = org.schnasse.oi.reader.CsvReader.getMap(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("csv/in/BesucherzahlenMuseen2019.csv"), null, ";", null);
		Map<String, Object> yml = org.schnasse.oi.reader.YamlReader.getMap(Thread.currentThread()
				.getContextClassLoader().getResourceAsStream("csv/out/BesucherzahlenMuseen2019.csv.yml"));
		TestHelper.mapCompare(yml, csv);
	}

	@Test
	public void csv_to_json() throws Exception {
		Map<String, Object> csv = org.schnasse.oi.reader.CsvReader.getMap(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("csv/in/BesucherzahlenMuseen2019.csv"), null, ";", "\"");
		Map<String, Object> json = org.schnasse.oi.reader.JsonReader.getMap(Thread.currentThread()
				.getContextClassLoader().getResourceAsStream("csv/out/BesucherzahlenMuseen2019.csv.json"));
		TestHelper.mapCompare(json, csv);
	}
}
