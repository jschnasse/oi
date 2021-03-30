package org.schnasse.oi.writer;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;

public class JsonSchemaWriter {

	public static void gprint(Map<String, Object> json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(mapper);
			JsonSchema schema = schemaGen.generateSchema(Map.class);
			JsonWriter.gprint(schema);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
