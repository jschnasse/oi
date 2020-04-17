/* Copyright 2020 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.cjxy.reader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlReader {
	static public Map<String, Object> getMap(InputStream in) {
		try {
			ObjectMapper mapper = new XmlMapper();
			@SuppressWarnings("unchecked")
			Map<String, Object> map = mapper.readValue(in, HashMap.class);
			return map;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
