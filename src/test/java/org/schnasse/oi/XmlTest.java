package org.schnasse.oi;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
		// TestHelper.mapCompare(xml, json);
	}

	/*
	 * <array><item>one</item><item>two</item></array>
	 * 
	 * "array":{ "item": ["one", "two"] }
	 * 
	 * <noArray><aTag>oneItem</aTag></noArray>
	 * 
	 * "noArray":{ "aTag":"oneItem" }
	 */
	@Test
	public void yml_to_json_array() throws Exception {
		try (InputStream input1 = new ByteArrayInputStream(
				new String("<array><item>one</item><item>two</item></array>").getBytes());
				InputStream input2 = new ByteArrayInputStream(
						new String("{\"item\": [\"one\", \"two\"]}").getBytes())) {
			Map<String, Object> xml = org.schnasse.oi.reader.XmlReader.getMap(input1);
			Map<String, Object> json = org.schnasse.oi.reader.JsonReader.getMap(input2);
			TestHelper.mapCompare(xml, json);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void yml_to_json_noArray() throws Exception {
		try (InputStream input1 = new ByteArrayInputStream(
				new String("<noArray><aTag>oneItem</aTag></noArray>").getBytes());
				InputStream input2 = new ByteArrayInputStream(new String("{\"aTag\":[\"oneItem\"]}").getBytes())) {
			Map<String, Object> xml = org.schnasse.oi.reader.XmlReader.getMap(input1);
			Map<String, Object> json = org.schnasse.oi.reader.JsonReader.getMap(input2);
			TestHelper.mapCompare(xml, json);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
