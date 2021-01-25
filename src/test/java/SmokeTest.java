
/* Copyright 2020 Jan Schnasse. Licensed under the EPL 2.0 */

import java.io.InputStream;
import java.util.Map;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.junit.Test;
import org.schnasse.oi.reader.JsonReader;
import org.schnasse.oi.reader.YamlReader;
import org.schnasse.oi.writer.ContextWriter;
import org.schnasse.oi.writer.JsonWriter;
import org.schnasse.oi.writer.RdfWriter;
import org.schnasse.oi.writer.XmlWriter;
import org.schnasse.oi.writer.YamlWriter;

public class SmokeTest {
	@Test
	public void readJson() throws Exception {
		Map<String, Object> map = JsonReader.getMap(getInputStream("old.tests/HT015847062.json"));
		JsonWriter.gprint(map);
		YamlWriter.gprint(map);
		XmlWriter.gprint(map);
	}

	@Test
	public void readYaml() throws Exception {
		Map<String, Object> map = YamlReader.getMap(getInputStream("old.tests/HT015847062.yml"));
		JsonWriter.gprint(map);
		YamlWriter.gprint(map);
		XmlWriter.gprint(map);
	}

	@Test
	public void readXml() throws Exception {
		Map<String, Object> map = org.schnasse.oi.reader.XmlReader.getMap(getInputStream("old.tests/HT015847062.xml"));
		JsonWriter.gprint(map);
		YamlWriter.gprint(map);
		XmlWriter.gprint(map);
	}

	@Test
	public void readCsv() throws Exception {
		Map<String, Object> map = org.schnasse.oi.reader.CsvReader
				.getMap(getInputStream("csv/in/BesucherzahlenMuseen2019.csv"), null, ";", null);
		JsonWriter.gprint(map);
		YamlWriter.gprint(map);
		XmlWriter.gprint(map);
	}

	@Test
	public void readCsv2() throws Exception {
		Map<String, Object> map = org.schnasse.oi.reader.CsvReader.getMap(
				getInputStream("old.tests/HT015847062_v2.csv"),
				new String[] { "OclcNumber", "URI", "Title", "Author", "Date" }, ",", null);
		JsonWriter.gprint(map);
		YamlWriter.gprint(map);
		XmlWriter.gprint(map);
	}

	@Test
	public void readRdf() throws Exception {
		Map<String, Object> map = org.schnasse.oi.reader.RdfReader.getMapWithHandcraftedFrame(
				JsonReader.getMap(getInputStream("old.tests/HT015847062.json")), RDFFormat.JSONLD,
				JsonReader.getMap(getInputStream("old.tests/context.jsonld")));
		JsonWriter.gprint(map);
		YamlWriter.gprint(map);
		XmlWriter.gprint(map);
	}

	@Test
	public void readTurtleRdf() throws Exception {
		final Map<String, Object> map = org.schnasse.oi.reader.RdfReader.getMap(
				getInputStream("rdf/in/stack43638342.rdf"), RDFFormat.TURTLE,
				JsonReader.getMap(getInputStream("rdf/context/stack43638342.rdf.context")));
		JsonWriter.gprint(map);
		YamlWriter.gprint(map);
		XmlWriter.gprint(map);
	}

	@Test
	public void readJson_createRdf() throws Exception {
		final Map<String, Object> map = org.schnasse.oi.reader.JsonReader
				.getMap(getInputStream("json/in/rosenmontag.json"));
		JsonWriter.gprint(map);
		RdfWriter.gprint(map,RDFFormat.RDFXML);

	}

	private InputStream getInputStream(String path) {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
	}

	@Test
	public void readJson_2() throws Exception {
		Map<String, Object> map = org.schnasse.oi.reader.RdfReader.getMapWithHandcraftedFrame(
				JsonReader.getMap(getInputStream("json/in/arbeitsmarktKoeln.json")), RDFFormat.JSONLD,
				JsonReader.getMap(getInputStream("json/context/arbeitsmarktKoeln.json.context")));
		JsonWriter.gprint(map);
	}

	@Test
	public void writeContext() throws Exception {
		Map<String, Object> map = org.schnasse.oi.reader.JsonReader.getMap(getInputStream("old.tests/49726990.json"));
		ContextWriter.gprint(map);

		map = org.schnasse.oi.reader.JsonReader.getMap(getInputStream("old.tests/HT015847062.json"));
		ContextWriter.gprint(map);
	}

	@Test
	public void writeContext2() throws Exception {
		Map<String, Object> map = org.schnasse.oi.reader.RdfReader.getMap(
				getInputStream("rdf/in/entwicklung-der-kriminalitat-nach-fallen-und-aufklarungen-seit-1990.rdf"),
				RDFFormat.RDFXML, null);
		ContextWriter.gprint(map);

		map = org.schnasse.oi.reader.RdfReader.getMap(
				getInputStream("rdf/in/entwicklung-der-kriminalitat-nach-fallen-und-aufklarungen-seit-1990.rdf"),
				RDFFormat.RDFXML, JsonReader.getMap(Thread.currentThread().getContextClassLoader().getResourceAsStream(
						"rdf/context/entwicklung-der-kriminalitat-nach-fallen-und-aufklarungen-seit-1990.rdf.context")));
		JsonWriter.gprint(map);
	}
}
