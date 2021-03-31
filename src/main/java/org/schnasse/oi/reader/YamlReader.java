/* Copyright 2021 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.oi.reader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class YamlReader {
	static public Map<String, Object> getMap(InputStream in) {
		try {
			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
			@SuppressWarnings("unchecked")
			Map<String, Object> map = mapper.readValue(in, HashMap.class);
			return map;
		} catch (MismatchedInputException e) {
			throw new RuntimeException(Messages.NO_CONTENT_OR_WRONG_FORMAT);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
