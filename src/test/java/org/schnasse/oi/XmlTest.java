package org.schnasse.oi;

import java.util.Map;

import org.junit.Test;
import org.schnasse.oi.helper.TestHelper;

public class XmlTest {
	@Test
	public void yml_to_json() throws Exception {
		Map<String, Object> xml = org.schnasse.oi.reader.XmlReader
				.getMap(Thread.currentThread().getContextClassLoader().getResourceAsStream("xml/in/HT015847062.xml"));
		Map<String, Object> json = org.schnasse.oi.reader.JsonReader.getMap(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("xml/out/HT015847062.xml.json"));
		TestHelper.mapCompare(xml, json);
	}
}
