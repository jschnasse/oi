/* Copyright 2021 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.oi;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
		// TestHelper.mapCompare(json, yml);
	}

	@Test
	public void json_to_yml_array_to_array() throws Exception {
		try (InputStream input1 = new ByteArrayInputStream(
				new String("\n" + "	{\"features\":[\"f1\",\"f2\"]}").getBytes());
				InputStream input2 = new ByteArrayInputStream(new String("features:\n  - f1\n  - f2").getBytes())) {
			Map<String, Object> json = org.schnasse.oi.reader.JsonReader.getMap(input1);
			Map<String, Object> yml = org.schnasse.oi.reader.YamlReader.getMap(input2);
			TestHelper.mapCompare(json, yml);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@Test
	public void json_to_yml_single_element() throws Exception {
		try (InputStream input1 = new ByteArrayInputStream(new String("\n" + "	{\"features\":\"f1\"}").getBytes());
				InputStream input2 = new ByteArrayInputStream(new String("features:\n f1\n").getBytes())) {
			Map<String, Object> json = org.schnasse.oi.reader.JsonReader.getMap(input1);
			Map<String, Object> yml = org.schnasse.oi.reader.YamlReader.getMap(input2);
			TestHelper.mapCompare(json, yml);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
