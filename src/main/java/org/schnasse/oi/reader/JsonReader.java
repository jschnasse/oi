/* Copyright 2021 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.oi.reader;

import java.io.InputStream;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonReader {
	static public Map<String, Object> getMap(InputStream in) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readValue(in, JsonNode.class);
			Map<String, Object> map = null;
			if (node instanceof ObjectNode) {
				map = mapper.convertValue(node, new TypeReference<Map<String, Object>>() {
				});
			} else {
				ObjectNode root = mapper.createObjectNode();
				root.set("@graph", node);
				map = mapper.convertValue(root, new TypeReference<Map<String, Object>>() {
				});
			}
			return map;
		} catch (MismatchedInputException e) {
			throw new RuntimeException(Messages.NO_CONTENT_OR_WRONG_FORMAT);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
