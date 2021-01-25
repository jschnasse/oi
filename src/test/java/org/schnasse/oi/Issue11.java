package org.schnasse.oi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.schnasse.oi.helper.TestHelper;
import org.schnasse.oi.main.Main;
import org.schnasse.oi.reader.YamlReader;

import picocli.CommandLine;

public class Issue11 {
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
	
	@SuppressWarnings("unchecked")
	@Test
	public void read_schema_org_integration_test() {
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		new CommandLine(new Main()).setCaseInsensitiveEnumValuesAllowed(true).execute(s + "/src/test/resources/issue-11/in/books.ttl","-i","turtle","-f"+s + "/src/test/resources/issue-11/in/books.frame");		
		Map<String, Object> expected = YamlReader.getMap(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("issue-11/out/books.yml"));
		Map<String, Object> actual = YamlReader.getMap(new ByteArrayInputStream(outContent.toByteArray()));
		TestHelper.mapCompare(expected, actual);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void read_schema_org_integration_test2() {
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		new CommandLine(new Main()).setCaseInsensitiveEnumValuesAllowed(true).execute(s + "/src/test/resources/issue-11/in/books.ttl","-i","turtle");		
		Map<String, Object> expected = YamlReader.getMap(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("issue-11/out/book-gen.yml"));
		Map<String, Object> actual = YamlReader.getMap(new ByteArrayInputStream(outContent.toByteArray()));
		TestHelper.mapCompare(expected, actual);
	}
}
