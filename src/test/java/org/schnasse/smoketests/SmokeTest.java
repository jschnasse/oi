package org.schnasse.smoketests;

/* Copyright 2020 Jan Schnasse. Licensed under the EPL 2.0 */

import java.util.Map;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.junit.Test;
import org.schnasse.oi.reader.JsonReader;
import org.schnasse.oi.reader.YamlReader;
import org.schnasse.oi.writer.ContextWriter;
import org.schnasse.oi.writer.JsonWriter;
import org.schnasse.oi.writer.XmlWriter;
import org.schnasse.oi.writer.YamlWriter;

public class SmokeTest {
	@Test
	public void readJson() throws Exception {
		Map<String, Object> map = JsonReader.getMap(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("old.tests/HT015847062.json"));
		JsonWriter.gprint(map);
		YamlWriter.gprint(map);
		XmlWriter.gprint(map);
	}

	@Test
	public void readYaml() throws Exception {
		Map<String, Object> map = YamlReader.getMap(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("old.tests/HT015847062.yml"));
		JsonWriter.gprint(map);
		YamlWriter.gprint(map);
		XmlWriter.gprint(map);
	}

	@Test
	public void readXml() throws Exception {
		Map<String, Object> map = org.schnasse.oi.reader.XmlReader.getMap(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("old.tests/HT015847062.xml"));
		JsonWriter.gprint(map);
		YamlWriter.gprint(map);
		XmlWriter.gprint(map);
	}

	@Test
	public void readCsv() throws Exception {
		Map<String, Object> map = org.schnasse.oi.reader.CsvReader.getMap(Thread.currentThread()
				.getContextClassLoader().getResourceAsStream("csv/in/BesucherzahlenMuseen2019.csv"), null, ";");
		JsonWriter.gprint(map);
		YamlWriter.gprint(map);
		XmlWriter.gprint(map);
	}

	@Test
	public void readCsv2() throws Exception {
		Map<String, Object> map = org.schnasse.oi.reader.CsvReader.getMap(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("old.tests/HT015847062_v2.csv"),
				new String[] { "OclcNumber", "URI", "Title", "Author", "Date" }, ",");
		JsonWriter.gprint(map);
		YamlWriter.gprint(map);
		XmlWriter.gprint(map);
	}

	@Test
	public void readRdf() throws Exception {
		Map<String, Object> map = org.schnasse.oi.reader.RdfReader.getMap(
				JsonReader.getMap(Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("old.tests/HT015847062.json")),
				RDFFormat.JSONLD, JsonReader.getMap(Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("old.tests/context.jsonld")));
		JsonWriter.gprint(map);
		YamlWriter.gprint(map);
		XmlWriter.gprint(map);
	}

	@Test
	public void readJson_2() throws Exception {
		Map<String, Object> map = org.schnasse.oi.reader.RdfReader.getMap(
				JsonReader.getMap(Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("json/in/arbeitsmarktKoeln.json")),
				RDFFormat.JSONLD, JsonReader.getMap(Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("json/context/arbeitsmarktKoeln.json.context")));
		JsonWriter.gprint(map);
	}

	@Test
	public void writeContext() throws Exception {
		Map<String, Object> map = org.schnasse.oi.reader.JsonReader
				.getMap(Thread.currentThread().getContextClassLoader().getResourceAsStream("old.tests/49726990.json"));
		ContextWriter.gprint(map);

		map = org.schnasse.oi.reader.JsonReader.getMap(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("old.tests/HT015847062.json"));
		ContextWriter.gprint(map);
	}

	@Test
	public void writeContext2() throws Exception {
		Map<String, Object> map = org.schnasse.oi.reader.RdfReader.getMap(
				Thread.currentThread().getContextClassLoader().getResourceAsStream(
						"rdf/in/entwicklung-der-kriminalitat-nach-fallen-und-aufklarungen-seit-1990.rdf"),
				RDFFormat.RDFXML, null);
		ContextWriter.gprint(map);
		
		map = org.schnasse.oi.reader.RdfReader.getMap(
				Thread.currentThread().getContextClassLoader().getResourceAsStream(
						"rdf/in/entwicklung-der-kriminalitat-nach-fallen-und-aufklarungen-seit-1990.rdf"),
				RDFFormat.RDFXML, JsonReader.getMap(Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("rdf/context/entwicklung-der-kriminalitat-nach-fallen-und-aufklarungen-seit-1990.rdf.context")));
		JsonWriter.gprint(map);
	}

}
