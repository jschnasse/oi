/* Copyright 2020 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.cjxy.main;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.schnasse.cjxy.helper.Helper;
import org.schnasse.cjxy.reader.CsvReader;
import org.schnasse.cjxy.reader.JsonReader;
import org.schnasse.cjxy.reader.RdfReader;
import org.schnasse.cjxy.reader.XmlReader;
import org.schnasse.cjxy.reader.YamlReader;
import org.schnasse.cjxy.writer.ContextWriter;
import org.schnasse.cjxy.writer.JsonWriter;
import org.schnasse.cjxy.writer.XmlWriter;
import org.schnasse.cjxy.writer.YamlWriter;

import com.google.common.io.Files;

import ch.qos.logback.classic.Level;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "cjxy", mixinStandardHelpOptions = true, version = "checksum 0.1.0", description = "Converts yaml,json,xml,rdf to each other.")
public class Main implements Callable<Integer> {

	@Parameters(index = "0", arity = "1", description = "Input file.")
	private String inputFile;

	@Option(names = { "-t", "--type" }, description = "yaml,json,xml,rdf,context")
	private String type = "yaml";

	@Option(names = { "-f", "--frame" }, paramLabel = "JsonLdFrame", description = "A json-ld Frame")
	String frame;

	public static void main(String... args) {
		setLoggingLevel(Level.OFF);
		int exitCode = new CommandLine(new Main()).execute(args);
		System.exit(exitCode);
	}
	public static void setLoggingLevel(Level level) {
	    ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
	    root.setLevel(level);
	}
	@Override
	public Integer call() throws Exception { // your business logic goes here...
		convert(inputFile, type);
		return 0;
	}

	private void convert(String inF, String outT) {
		String suffix = Files.getFileExtension(inF);
		Map<String, Object> content = new HashMap<>();

		if ("json".equals(suffix) && frame == null) {
			content = JsonReader.getMap(Helper.getInputStream(inF));
		} else if ("yml".equals(suffix) || "yaml".equals(suffix)) {
			content = YamlReader.getMap(Helper.getInputStream(inF));
		} else if ("xml".equals(suffix)) {
			content = XmlReader.getMap(Helper.getInputStream(inF));
		} else if ("csv".equals(suffix)) {
			content = CsvReader.getMap(Helper.getInputStream(inF));
		}

		if (frame != null && ("rdf".equals(type)||"rdf".equals(suffix) || "nt".equals(suffix) || "json".equals(suffix))) {
			if ("rdf".equals(suffix) && frame != null) {
				content = RdfReader.getMap(Helper.getInputStream(inF), RDFFormat.RDFXML,
						JsonReader.getMap(Helper.getInputStream(frame)));
			} else if ("nt".equals(suffix) && frame != null) {
				content = RdfReader.getMap(Helper.getInputStream(inF), RDFFormat.NTRIPLES,
						JsonReader.getMap(Helper.getInputStream(frame)));
			} else if ("json".equals(suffix) && frame != null) {
				content = RdfReader.getMap(Helper.getInputStream(inF), RDFFormat.JSONLD,
						JsonReader.getMap(Helper.getInputStream(frame)));	}
		}
		else {
			throw new RuntimeException("Please provide a Frame!");
		}

		if ("xml".equals(type)) {
			XmlWriter.gprint(content);
		} else if ("json".equals(type)) {
			JsonWriter.gprint(content);
		} else if ("yaml".equals(type)) {
			YamlWriter.gprint(content);
		} else if ("rdf".equals(type)) {
			JsonWriter.gprint(content);
		}else if("context".equals(type)) {
			ContextWriter.gprint(content);
		}
	}

}
