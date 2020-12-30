/* Copyright 2020 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.oi.main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.schnasse.oi.helper.TestHelper;
import org.schnasse.oi.reader.JsonReader;
import org.schnasse.oi.reader.RdfReader;
import org.schnasse.oi.reader.YamlReader;

import picocli.CommandLine;

public class MainTest {
	private static final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory
			.getLogger(MainTest.class);
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	private final PrintStream originalErr = System.err;

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@After
	public void restoreStreams() {
		System.setOut(originalOut);
		System.setErr(originalErr);
	}

	@Test
	public void csv_to_json() throws Exception {
		final Path currentRelativePath = Paths.get("");
		final String s = currentRelativePath.toAbsolutePath().toString();
		new CommandLine(new Main()).setCaseInsensitiveEnumValuesAllowed(true)
				.execute(s + "/src/test/resources/csv/in/Kampfmittelfunde_2019.csv", "-d;", "-tjson");
		final Map<String, Object> expected = JsonReader.getMap(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("csv/out/Kampfmittelfunde_2019.csv.json"));
		final Map<String, Object> actual = JsonReader.getMap(new ByteArrayInputStream(outContent.toByteArray()));
		TestHelper.mapCompare(expected, actual);
	}

	@Test
	public void json_to_yml() throws Exception {
		final Path currentRelativePath = Paths.get("");
		final String s = currentRelativePath.toAbsolutePath().toString();
		new CommandLine(new Main()).setCaseInsensitiveEnumValuesAllowed(true)
				.execute(s + "/src/test/resources/json/in/rosenmontag.json");

		final Map<String, Object> expected = YamlReader.getMap(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("json/out/rosenmontag.json.yml"));
		final Map<String, Object> actual = YamlReader.getMap(new ByteArrayInputStream(outContent.toByteArray()));
		// TestHelper.mapCompare(expected, actual);
	}

	@Test
	public void json_to_rdf() throws Exception {
		final Path currentRelativePath = Paths.get("");
		final String s = currentRelativePath.toAbsolutePath().toString();
		new CommandLine(new Main()).setCaseInsensitiveEnumValuesAllowed(true)
				.execute(s + "/src/test/resources/json/in/rosenmontag.json", "-tjsonld");

		// Read expected
		final Map<String, Object> frame = JsonReader.getMap(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("json/context/rosenmontag.json.context"));
		final Map<String, Object> expected = RdfReader.getMap(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("json/out/rosenmontag.json.jsonld"),
				RDFFormat.JSONLD, frame);
		// Get actual from stream
		final Map<String, Object> actual = RdfReader.getMap(new ByteArrayInputStream(outContent.toByteArray()),
				RDFFormat.JSONLD, frame);
		TestHelper.mapCompare(expected, actual);
	}

//	@Test
//	public void json_to_rdf2() throws Exception {
//		final Path currentRelativePath = Paths.get("");
//		final String s = currentRelativePath.toAbsolutePath().toString();
//		new CommandLine(new Main()).setCaseInsensitiveEnumValuesAllowed(true).execute(
//				s + "/src/test/resources/json/in/rosenmontag.json",
//				"-f" + s + "/src/test/resources/json/context/rosenmontag.json.context", "-tjson");
//
//		// Read expected
//		final Map<String, Object> frame = JsonReader.getMap(Thread.currentThread().getContextClassLoader()
//				.getResourceAsStream("json/context/rosenmontag.json.context"));
//		final Map<String, Object> expected = RdfReader.getMap(
//				Thread.currentThread().getContextClassLoader().getResourceAsStream("json/out/rosenmontag.json.jsonld"),
//				RDFFormat.JSONLD, frame);
//		// Get actual from stream
//		final Map<String, Object> actual = RdfReader.getMap(new ByteArrayInputStream(outContent.toByteArray()),
//				RDFFormat.JSONLD, null);
//		TestHelper.mapCompare(expected, actual);
//	}
}
