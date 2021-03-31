/* Copyright 2021 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.oi;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class Issue4 {
	@SuppressWarnings("unchecked")
	@Test
	public void read_icd10() {
		try (InputStream in = new ByteArrayInputStream("<array><item>1</item><item>2</item></array>".getBytes())) {
			Map<String, Object> map = org.schnasse.oi.reader.XmlReader.getMap(in);
			Assert.assertTrue(((List<String>) map.get("item")).contains("1"));
			Assert.assertTrue(((List<String>) map.get("item")).contains("2"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void read_icd10_complex() {
		String xml = "<array><subject>\n" + "    <type>ComplexSubject</type>\n" + "    <componentList>\n"
				+ "      <id>https://d-nb.info/gnd/4260601-9</id>\n" + "      <type>SubjectHeading</type>\n"
				+ "      <source>\n" + "        <id>https://d-nb.info/gnd/7749153-1</id>\n"
				+ "        <label>Gemeinsame Normdatei (GND)</label>\n" + "      </source>\n"
				+ "      <label>Vorhersagbarkeit</label>\n" + "      <gndIdentifier>4260601-9</gndIdentifier>\n"
				+ "    </componentList>\n" + "    <label>Vorhersagbarkeit</label>\n" + "  </subject>\n"
				+ "  <subject>\n" + "    <type>ComplexSubject</type>\n" + "    <componentList>\n"
				+ "      <id>https://d-nb.info/gnd/4047390-9</id>\n" + "      <type>SubjectHeading</type>\n"
				+ "      <source>\n" + "        <id>https://d-nb.info/gnd/7749153-1</id>\n"
				+ "        <label>Gemeinsame Normdatei (GND)</label>\n" + "      </source>\n"
				+ "      <label>Prognose</label>\n" + "      <gndIdentifier>4047390-9</gndIdentifier>\n"
				+ "    </componentList>\n" + "    <label>Prognose</label>\n" + "  </subject></array>";
		try (InputStream in = new ByteArrayInputStream(xml.getBytes())) {
			Map<String, Object> map = org.schnasse.oi.reader.XmlReader.getMap(in);
			Assert.assertTrue(((List<String>) map.get("subject")).size() == 2);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
