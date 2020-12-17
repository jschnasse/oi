/* Copyright 2020 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.oi.writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.schnasse.oi.helper.URLUtil;
import org.schnasse.oi.writer.base.Writer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ContextWriter {

	public static ObjectMapper createMapper() {
		return new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);
	}

	public static void gprint(Map<String, Object> map) {
		Map<String, Object> result = findContext(map);
		Writer.gprint(createMapper(), result);
	}

	public static Map<String, Object> findContext(Map<String, Object> map) {
		Map<String, Object> result = useExistingContext(map);
		if (result == null) {
			result = createContext(map);
		}
		return result;
	}

	private static Map<String, Object> createContext(Map<String, Object> map) {
		ObjectMapper mapper = new ObjectMapper();
		List<String> keys = findKeys(mapper.convertValue(map, ObjectNode.class));
		ObjectNode data = mapper.createObjectNode();
		ObjectNode context = mapper.createObjectNode();
		data.set("@context", context);
		String computername = getComputername();
		for (String key : keys) {
			if ("@id".equals(key))
				continue;
			if ("@type".equals(key))
				continue;
			if ("@value".equals(key))
				continue;
			if ("@graph".equals(key))
				continue;
			if ("@list".equals(key))
				continue;
			Object o = map.get(key);
			ObjectNode entry = mapper.createObjectNode();
			String shortKey = generateReadableKey(key);
			entry.put("@id", createId(key.toString(), computername));
			if (o instanceof Set) {
				entry.put("@container", "@set");
			} else if (o instanceof List) {
				entry.put("@container", "@list");
			}
			context.set(shortKey, entry);
		}
		Map<String, Object> result = mapper.convertValue(data, new TypeReference<Map<String, Object>>() {
		});
		return result;
	}

	private static String generateReadableKey(String key) {
		try {
			if (URLUtil.isValidUrl(key)) {
				String result = URLUtil.saveEncode(key);

				int i = result.lastIndexOf("#");
				if (i < 0) {
					i = result.lastIndexOf("/");
				}
				if (i < 0 || i + 1 == result.length()) {
					i = 0;
				}
				return result.substring(i + 1);
			}
		} catch (Exception e) {
			// if saveEncode does not work, leave it as is.
		}
		return key;
	}

	private static String getComputername() {
		return "oi";
	}

	private static String createId(String key, String computername) {
		if (URLUtil.isValidUrl(key))
			return key;
		return "info:" + computername + "/" + key;
	}

	public static List<String> findKeys(ObjectNode map) {
		List<String> result = new ArrayList<>();
		Iterator<String> it = map.fieldNames();
		while (it.hasNext()) {
			String field = it.next();
			result.add(field);
			JsonNode value = map.get(field);
			if (value.isArray()) {
				result.addAll(findKeys((ArrayNode) value));
			} else if (value.isObject()) {
				result.addAll(findKeys((ObjectNode) value));
			}
		}
		return result;
	}

	public static List<String> findKeys(ArrayNode map) {
		List<String> result = new ArrayList<>();
		Iterator<JsonNode> it = map.iterator();
		while (it.hasNext()) {
			JsonNode value = it.next();
			if (value.isArray()) {
				result.addAll(findKeys((ArrayNode) value));
			} else if (value.isObject()) {
				result.addAll(findKeys((ObjectNode) value));
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Object> useExistingContext(Map<String, Object> map) {
		Object context = map.get("@context");
		if (context != null) {
			if (context instanceof String) {
				Map<String, Object> result = new HashMap<>();
				result.put("@context", context);
				return result;
			} else if (context instanceof Map) {
				return (Map<String, Object>) map.get("@context");
			}
		}
		return null;
	}
}
