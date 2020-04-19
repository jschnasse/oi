package org.schnasse.oi.main;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.schnasse.oi.reader.JsonReader;
import org.schnasse.oi.reader.YamlReader;

import picocli.CommandLine;

public class MainTest {
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
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		new CommandLine(new Main()).execute(s + "/src/test/resources/csv/in/Kampfmittelfunde_2019.csv","-d;","-tjson");
		Map<String,Object> expected = JsonReader.getMap(Thread.currentThread()
				.getContextClassLoader().getResourceAsStream("csv/out/Kampfmittelfunde_2019.csv.json"));
		Map<String,Object> actual = JsonReader.getMap(new ByteArrayInputStream(outContent.toByteArray()));
		assertEquals(expected,actual);
	}
	
	@Test
	public void json_to_yml() throws Exception {
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		new CommandLine(new Main()).execute(s + "/src/test/resources/json/in/rosenmontag.json");
		Map<String,Object> expected = YamlReader.getMap(Thread.currentThread()
				.getContextClassLoader().getResourceAsStream("json/out/rosenmontag.json.yml"));
		Map<String,Object> actual = YamlReader.getMap(new ByteArrayInputStream(outContent.toByteArray()));
		assertEquals(expected,actual);
	}
}
