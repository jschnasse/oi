/* Copyright 2019 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.cjxy.main;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.schnasse.cjxy.helper.Helper;
import org.schnasse.cjxy.reader.CsvReader;
import org.schnasse.cjxy.reader.JsonReader;
import org.schnasse.cjxy.reader.XmlReader;
import org.schnasse.cjxy.reader.YamlReader;
import org.schnasse.cjxy.writer.JsonWriter;
import org.schnasse.cjxy.writer.XmlWriter;
import org.schnasse.cjxy.writer.YamlWriter;

import com.google.common.io.Files;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "cjxy", mixinStandardHelpOptions = true, version = "checksum 0.1.0", description = "Converts yaml,json,xml to each other.")
public class Main implements Callable<Integer> {

	@Parameters(index = "0", arity = "1", description = "Input file.")
	private String inputFile;

	@Option(names = { "-t", "--type" }, description = "yaml,json,xml")
	private String type = "yaml";

	@Option(names = { "-f", "--fields" }, description = "Provide a comma separated list of files.")
	private String fields = "";

	public static void main(String... args) {
		int exitCode = new CommandLine(new Main()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public Integer call() throws Exception { // your business logic goes here...
		convert(inputFile, type);
		return 0;
	}

	private void convert(String inF, String outT) {
		String suffix = Files.getFileExtension(inF);
		Map<String, Object> content = new HashMap<>();

		if ("json".equals(suffix)) {
			content = JsonReader.getMap(Helper.getInputStream(inF));
		} else if ("yml".equals(suffix) || "yaml".equals(suffix)) {
			content = YamlReader.getMap(Helper.getInputStream(inF));
		} else if ("xml".equals(suffix)) {
			content = XmlReader.getMap(Helper.getInputStream(inF));
		} else if ("csv".equals(suffix)) {
			content = CsvReader.getMap(Helper.getInputStream(inF));
		}

		if ("xml".equals(type)) {
			XmlWriter.gprint(content);
		} else if ("json".equals(type)) {
			JsonWriter.gprint(content);
		} else if ("yaml".equals(type)) {
			YamlWriter.gprint(content);
		}
	}

}
