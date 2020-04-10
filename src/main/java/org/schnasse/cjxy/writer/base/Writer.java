/* Copyright 2020 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.cjxy.writer.base;

import java.io.File;
import java.io.StringWriter;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class Writer {

	@SafeVarargs
	static public void print(ObjectMapper mapper, Map<String, Object>... maps) {
		for (Map<String, Object> m : maps) {
			System.out.println(write(mapper, m) + "\n");
		}
	}

	static public String write(ObjectMapper mapper, Object obj) {
		try {
			StringWriter w = new StringWriter();
			mapper.writeValue(w, obj);
			return w.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void createFile(ObjectMapper mapper, String path, Map<String, Object> content) {
		try {
			Files.asCharSink(new File(path), Charsets.UTF_8).write(write(mapper, content));
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}

	public static void gprint(ObjectMapper mapper, Map<String, Object> json) {
		print(mapper, json);
	}
}
