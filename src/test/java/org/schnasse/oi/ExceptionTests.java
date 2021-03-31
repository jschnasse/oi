/* Copyright 2021 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.oi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;
import org.schnasse.oi.reader.CsvReader;
import org.schnasse.oi.reader.JsonReader;
import org.schnasse.oi.reader.XmlReader;
import org.schnasse.oi.reader.YamlReader;

public class ExceptionTests {

	@Test
	public void empty_yml_read_fail() {
		String str = "";
		testme(str, in -> {
			return YamlReader.getMap(in);
		});
	}

	@Test
	public void empty_xml_read_fail() {
		String str = "";
		testme(str, in -> {
			return XmlReader.getMap(in);
		});
	}

	@Test
	public void empty_json_read_fail() {
		String str = "";
		testme(str, in -> {
			return JsonReader.getMap(in);
		});
	}

	// @Test
	public void empty_csv_read_fail() {
		String str = "";
		testme(str, in -> {
			return CsvReader.getMap(in, new String[] { "", "" }, ",", "\"");
		});
	}

	public void testme(String wrongFormatString, IGetMap mapper) {
		try (InputStream in = new ByteArrayInputStream(wrongFormatString.getBytes())) {
			mapper.getMap(in);
			fail("Must throw an exception!");
		} catch (Exception e) {
			String expected = "No content or wrong format!";
			assertEquals(expected, e.getMessage());
		}
	}

}
