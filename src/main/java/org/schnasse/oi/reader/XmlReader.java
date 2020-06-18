/* Copyright 2020 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.oi.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class XmlReader {

	@SuppressWarnings("deprecation")
	static public Map<String, Object> getMap(InputStream in) {
		try {
			ObjectMapper mapper = new XmlMapper();
			mapper.registerModule(new SimpleModule().addDeserializer(Object.class, new UntypedObjectDeserializer() {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				@Override
				protected Map<String, Object> mapObject(JsonParser jp, DeserializationContext ctxt) throws IOException {
					JsonToken t = jp.getCurrentToken();
					Multimap<String, Object> result = ArrayListMultimap.create();
					if (t == JsonToken.START_OBJECT) {
						t = jp.nextToken();
					}
					if (t == JsonToken.END_OBJECT) {
						return (Map) result.asMap();
					}
					do {
						String fieldName = jp.getCurrentName();
						jp.nextToken();
						result.put(fieldName, deserialize(jp, ctxt));
					} while (jp.nextToken() != JsonToken.END_OBJECT);
					return (Map) result.asMap();
				}
			}));

			@SuppressWarnings({ "unchecked", "rawtypes" })
			Map<String, Object> map = (Map) mapper.readValue(in, Object.class);
			return map;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
