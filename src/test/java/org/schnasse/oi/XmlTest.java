package org.schnasse.oi;
import java.util.Map;

import org.junit.Test;
import org.locationtech.jts.util.Assert;

public class XmlTest {
	@Test
	public void yml_to_json() throws Exception {
		Map<String, Object> yml = org.schnasse.oi.reader.XmlReader.getMap(Thread.currentThread()
				.getContextClassLoader().getResourceAsStream("xml/in/HT015847062.xml"));
		Map<String, Object> json = org.schnasse.oi.reader.JsonReader.getMap(Thread.currentThread()
				.getContextClassLoader().getResourceAsStream("xml/out/HT015847062.xml.json"));
		Assert.equals(yml, json);
	}
}
