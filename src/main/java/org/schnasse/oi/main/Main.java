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
import org.schnasse.oi.writer.RdfWriter;
import org.schnasse.oi.writer.XmlWriter;
import org.schnasse.oi.writer.YamlWriter;

import com.google.common.io.Files;

import ch.qos.logback.classic.Level;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "oi", mixinStandardHelpOptions = true, version = "oi 0.5.1", description = "Converts yaml,json,xml,rdf to each other.")
public class Main implements Callable<Integer> {
	private static final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory
			.getLogger(Main.class);

	@Parameters(index = "0", arity = "0..1", description = "Input file.")
	private String inputFile;

	@Option(names = { "-t", "--type" }, description = "yml,json,xml,rdf,context,csv,nt,turtle,ntriples,jsonld")
	Type type = Type.YML;

	@Option(names = { "-i", "--inputType" }, description = "yml,json,xml,rdf,context,csv,nt,turtle,ntriples,jsonld")
	Type inputType;

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

	enum Type {
		XML, JSON, JSONLD, CSV, YML, RDF, RDFXML, NTRIPLES, TURTLE, CONTEXT
	}

	public static void main(String... args) {
		try {
			int exitCode = new CommandLine(new Main()).setCaseInsensitiveEnumValuesAllowed(true).execute(args);
			System.exit(exitCode);
		} catch (Throwable e) {
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
			Map<String, Object> content = readDataToMap(in);
			print(content);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Map<String, Object> readDataToMap(InputStream in) {
		Map<String, Object> frameMap = readFrameToMap();
		Map<String, Object> content = new HashMap<>();
		if (frameMap == null) {
			switch (inputType) {
			case XML:
				content = XmlReader.getMap(Helper.getInputStream(inputFile));
				break;
			case JSON:
				content = JsonReader.getMap(in);
				break;
			case YML:
				content = YamlReader.getMap(in);
				break;
			case CSV: {
				if (header != null) {
					content = CsvReader.getMap(in, header.split(","), delimiter, quoteChar);
				} else {
					content = CsvReader.getMap(in, null, delimiter, quoteChar);
				}
				break;
			}
			case CONTEXT:
				break;
			case JSONLD:
				content = RdfReader.getMap(in, RDFFormat.JSONLD, null);
				break;
			case NTRIPLES:
				content = RdfReader.getMap(in, RDFFormat.NTRIPLES, null);
				break;
			case RDF:
				content = RdfReader.getMap(in, RDFFormat.JSONLD, null);
				break;
			case RDFXML:
				content = RdfReader.getMap(in, RDFFormat.RDFXML, null);
				break;
			case TURTLE:
				content = RdfReader.getMap(in, RDFFormat.TURTLE, null);
				break;
			default:
				break;
			}
		} else {
			switch (inputType) {
			case RDFXML:
				content = RdfReader.getMap(in, RDFFormat.RDFXML, frameMap);
				break;
			case NTRIPLES:
				content = RdfReader.getMap(in, RDFFormat.NTRIPLES, frameMap);
				break;
			case TURTLE:
				content = RdfReader.getMap(in, RDFFormat.TURTLE, frameMap);
				break;
			case JSON:
				content = RdfReader.getMap(JsonReader.getMap(in), RDFFormat.JSONLD, frameMap);
				break;
			case RDF:
			case JSONLD:
				content = RdfReader.getMap(JsonReader.getMap(in), RDFFormat.JSONLD, frameMap);
				break;
			case CONTEXT:
				break;
			case CSV:
				break;
			case XML:
				break;
			case YML:
				break;
			default:
				break;
			}
			content.put("@context", frameMap.get("@context"));
		}
		return content;
	}

	private void print(Map<String, Object> content) {
		switch (type) {
		case XML:
			XmlWriter.gprint(content);
			break;
		case JSON:
			JsonWriter.gprint(content);
			break;
		case YML:
			YamlWriter.gprint(content);
			break;
		case RDFXML:
			RdfWriter.gprint(content, RDFFormat.RDFXML);
			break;
		case TURTLE:
			RdfWriter.gprint(content, RDFFormat.TURTLE);
			break;
		case NTRIPLES:
			RdfWriter.gprint(content, RDFFormat.NTRIPLES);
			break;
		case RDF:
		case JSONLD:
			RdfWriter.gprint(content, RDFFormat.JSONLD);
			break;
		case CONTEXT:
			ContextWriter.gprint(content);
			break;
		default:
			throw new RuntimeException("You have specified an unknown"
					+ " --type. \n Please lookup the list of valid types with 'oi --help'.");
		}
	}

	private Map<String, Object> readFrameToMap() {
		if (frame != null) {
			Map<String, Object> frameMap = new HashMap<>();
			frameMap = JsonReader.getMap(Helper.getInputStream(frame));
			return frameMap;
		}
		return null;
	}

	private Type findInputType() {
		if (inputType == null) {
			if (inputFile != null) {
				inputType = Type.valueOf(Files.getFileExtension(inputFile).toUpperCase());
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
