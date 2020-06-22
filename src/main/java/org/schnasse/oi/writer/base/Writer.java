/* Copyright 2020 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.oi.writer.base;

import java.io.StringWriter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Writer {

	@SafeVarargs
	static public void print(ObjectMapper mapper, Object... maps) {
		for (Object m : maps) {
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

	public static void gprint(ObjectMapper mapper, Object json) {
		print(mapper, json);
	}
}
