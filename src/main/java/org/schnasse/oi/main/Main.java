/* Copyright 2020 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.oi.main;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.schnasse.oi.helper.Helper;
import org.schnasse.oi.reader.CsvReader;
import org.schnasse.oi.reader.JsonReader;
import org.schnasse.oi.reader.RdfReader;
import org.schnasse.oi.reader.XmlReader;
import org.schnasse.oi.reader.YamlReader;
import org.schnasse.oi.writer.ContextWriter;
import org.schnasse.oi.writer.JsonWriter;
import org.schnasse.oi.writer.XmlWriter;
import org.schnasse.oi.writer.YamlWriter;

import com.google.common.io.Files;

import ch.qos.logback.classic.Level;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "oi", mixinStandardHelpOptions = true, version = "oi 0.3.0", description = "Converts yaml,json,xml,rdf to each other.")
public class Main implements Callable<Integer> {
	private static final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory
			.getLogger(Main.class);
	@Parameters(index = "0", arity = "0..1", description = "Input file.")
	private String inputFile;

	@Option(names = { "-t", "--type" }, description = "yaml,json,xml,rdf,context,csv")
	private String type = "yaml";

	@Option(names = { "-i", "--inputType" }, description = "yml,json,xml,rdf,context,csv,nt,turtle")
	String inputType;

	@Option(names = { "-f", "--frame" }, paramLabel = "JsonLdFrame", description = "A json-ld Frame")
	String frame;

	@Option(names = { "--header" }, paramLabel = "HeaderFields", description = "a comma separated list of headers")
	String header;

	@Option(names = { "-d", "--delimiter" }, paramLabel = "Delimiter", description = "delimiter for csv")
	String delimiter = ",";

	@Option(names = { "-q", "--quoteChar" }, paramLabel = "QuoteChar", description = "quote char for csv")
	String quoteChar = "\"";

	@Option(names = { "-v", "--verbose" }, paramLabel = "Verbosity", description = "Increase Verbosity to Warn")
	boolean levelWarn = false;

	@Option(names = { "-vv" }, paramLabel = "High Verbosity", description = "Increase Verbosity to Debug")
	boolean levelDebug = false;

	public static void main(String... args) {
		try {
			int exitCode = new CommandLine(new Main()).execute(args);
			System.exit(exitCode);
		} catch (Exception e) {
			logger.debug("", e);
			System.exit(1);
		}
	}

	public static void setLoggingLevel(Level level) {
		ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory
				.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
		root.setLevel(level);
	}

	@Override
	public Integer call() throws Exception {
		if (levelWarn) {
			setLoggingLevel(Level.WARN);
		} else if (levelDebug) {
			setLoggingLevel(Level.DEBUG);
		} else {
			setLoggingLevel(Level.OFF);
		}
		convert();
		return 0;
	}

	private void convert() {
		try (InputStream in = getInput(inputFile)) {
			inputType = findInputType();
			Map<String, Object> content = new HashMap<>();
			Map<String, Object> frameMap = new HashMap<>();
			if (frame != null) {
				frameMap = JsonReader.getMap(Helper.getInputStream(frame));
			}
			if ("json".equals(inputType) && frame == null) {
				content = JsonReader.getMap(in);
			} else if ("yml".equals(inputType) || "yaml".equals(inputType)) {
				content = YamlReader.getMap(in);
			} else if ("xml".equals(inputType)) {
				content = XmlReader.getMap(Helper.getInputStream(inputFile));
			} else if ("csv".equals(inputType)) {
				if (header != null) {
					content = CsvReader.getMap(in, header.split(","), delimiter, quoteChar);
				} else {
					content = CsvReader.getMap(in, null, delimiter, quoteChar);
				}
			} else if ("rdf".equals(type) || "rdf".equals(inputType) || "nt".equals(inputType)
					|| "json".equals(inputType) || "turtle".equals(inputType)) {
				if ("rdf".equals(inputType) && frame != null) {
					content = RdfReader.getMap(in, RDFFormat.RDFXML, frameMap);
				} else if ("rdf".equals(inputType) && frame == null) {
					content = RdfReader.getMap(in, RDFFormat.RDFXML, null);
				} else if ("nt".equals(inputType) && frame != null) {
					content = RdfReader.getMap(in, RDFFormat.NTRIPLES, frameMap);
				} else if ("turtle".equals(inputType) && frame != null) {
					content = RdfReader.getMap(in, RDFFormat.TURTLE, frameMap);
				} else if ("json".equals(inputType) && frame != null) {
					content = RdfReader.getMap(JsonReader.getMap(in), RDFFormat.JSONLD, frameMap);
				} else if (frame == null) {
					throw new RuntimeException("Please provide a Frame!");
				}
			}
			if (frame != null) {
				content.put("@context", frameMap.get("@context"));
			}

			if ("xml".equals(type)) {
				XmlWriter.gprint(content);
			} else if ("json".equals(type)) {
				JsonWriter.gprint(content);
			} else if ("yaml".equals(type)) {
				YamlWriter.gprint(content);
			} else if ("rdf".equals(type)) {
				JsonWriter.gprint(content);
			} else if ("context".equals(type)) {
				ContextWriter.gprint(content);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String findInputType() {
		if (inputType == null) {
			if (inputFile != null) {
				inputType = Files.getFileExtension(inputFile);
			} else {
				throw new RuntimeException("Please provide an input type via -i,--inputType");
			}
		}
		return inputType;
	}

	private InputStream getInput(String inputFile) {
		if (inputFile != null) {
			return Helper.getInputStream(inputFile);
		}
		return System.in;
	}

}
