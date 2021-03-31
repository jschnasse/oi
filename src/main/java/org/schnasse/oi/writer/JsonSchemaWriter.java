package org.schnasse.oi.writer;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonSchemaWriter {

	private static ObjectMapper m = new ObjectMapper();

	public static void gprint(Map<String, Object> json) {
		try {
			ObjectNode schema = m.createObjectNode();
			String title = "Oi Generated Schema";
			String description = "https://github.com/jschnasse/oi";
			schema.put("title", title);
			schema.put("description", description);
			schema.put("type", "object");
			JsonNode properties = createProperty(m.convertValue(json, JsonNode.class), null);
			schema.set("properties", properties);
			JsonWriter.gprint(schema);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static ObjectNode createProperty(JsonNode jsonData, JsonNodeType type) throws IOException {
		ObjectNode propObject = m.createObjectNode();
		for (Iterator<Entry<String,JsonNode>> iterator = jsonData.fields(); iterator.hasNext();) {
			Entry<String,JsonNode> field=iterator.next();
			String fieldName = field.getKey();
			JsonNode curNode = field.getValue();
			JsonNodeType curNodeType = curNode.getNodeType();
			ObjectNode property = processJsonField(curNode, curNodeType);
			if (!property.isEmpty()) {
				propObject.set(fieldName, property);
			}
		}
		return propObject;
	}

	private static ObjectNode processJsonField(JsonNode curNode, JsonNodeType curNodeType)
			throws IOException {
		ObjectNode property = m.createObjectNode();
		switch (curNodeType) {
		case ARRAY:
			property.put("type", "array");
			if(!curNode.isEmpty()) {
				JsonNodeType typeOfArrayElements=curNode.get(0).getNodeType();
				if(typeOfArrayElements.equals(JsonNodeType.OBJECT)) {
					property.set("items", createProperty(curNode.get(0), typeOfArrayElements));
				}else {
					property.set("items", processJsonField(curNode.get(0), typeOfArrayElements));
				}
			}
			break;
		case BOOLEAN:
			property.put("type", "boolean");
			break;
		case NUMBER:
			property.put("type", "number");
			break;
		case OBJECT:
			property.put("type", "object");
			property.set("properties", createProperty(curNode, curNodeType));
			break;
		case STRING:
			property.put("type", "string");
			break;
		default:
			break;
		}
		return property;
	}

}
