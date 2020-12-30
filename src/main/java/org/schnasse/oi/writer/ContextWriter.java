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

	public static void gprint(final Map<String, Object> map) {
		final Map<String, Object> result = findContext(map);
		Writer.gprint(createMapper(), result);
	}

	public static Map<String, Object> findContext(final Map<String, Object> map) {
		Map<String, Object> result = useExistingContext(map);
		if (result == null) {
			result = createContext(map);
		}
		return result;
	}

	private static Map<String, Object> createContext(final Map<String, Object> map) {
		final ObjectMapper mapper = new ObjectMapper();
		final List<String> keys = findKeys(mapper.convertValue(map, ObjectNode.class));
		final ObjectNode data = mapper.createObjectNode();
		final ObjectNode context = mapper.createObjectNode();
		data.set("@context", context);
		final String computername = getComputername();
		for (final String key : keys) {
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
			final Object o = map.get(key);
			final ObjectNode entry = mapper.createObjectNode();
			final String shortKey = generateReadableKey(key);
			entry.put("@id", createId(key.toString(), computername));
			if (o instanceof Set) {
				entry.put("@container", "@set");
			} else if (o instanceof List) {
				entry.put("@container", "@list");
			}
			context.set(shortKey, entry);
		}
		final Map<String, Object> result = mapper.convertValue(data, new TypeReference<Map<String, Object>>() {
		});
		return result;
	}

	private static String generateReadableKey(final String key) {
		try {
			if (URLUtil.isValidUrl(key)) {
				final String result = URLUtil.saveEncode(key);

				int i = result.lastIndexOf("#");
				if (i < 0) {
					i = result.lastIndexOf("/");
				}
				if (i < 0 || i + 1 == result.length()) {
					i = 0;
				}
				return result.substring(i + 1);
			}
		} catch (final Exception e) {
			// if saveEncode does not work, leave it as is.
		}
		return key;
	}

	private static String getComputername() {
		return "oi";
	}

	private static String createId(final String key, final String computername) {
		if (URLUtil.isValidUrl(key))
			return key;
		return "info:" + computername + "/" + key;
	}

	public static List<String> findKeys(final ObjectNode map) {
		final List<String> result = new ArrayList<>();
		final Iterator<String> it = map.fieldNames();
		while (it.hasNext()) {
			final String field = it.next();
			result.add(field);
			final JsonNode value = map.get(field);
			if (value.isArray()) {
				result.addAll(findKeys((ArrayNode) value));
			} else if (value.isObject()) {
				result.addAll(findKeys((ObjectNode) value));
			}
		}
		return result;
	}

	public static List<String> findKeys(final ArrayNode map) {
		final List<String> result = new ArrayList<>();
		final Iterator<JsonNode> it = map.iterator();
		while (it.hasNext()) {
			final JsonNode value = it.next();
			if (value.isArray()) {
				result.addAll(findKeys((ArrayNode) value));
			} else if (value.isObject()) {
				result.addAll(findKeys((ObjectNode) value));
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Object> useExistingContext(final Map<String, Object> map) {
		final Object context = map.get("@context");
		if (context != null) {
			if (context instanceof String) {
				final Map<String, Object> result = new HashMap<>();
				result.put("@context", context);
				return result;
			} else if (context instanceof Map) {
				final Map<String, Object> result = new HashMap<>();
				result.put("@context", map.get("@context"));
				return result;
			}
		}
		return null;
	}
}
