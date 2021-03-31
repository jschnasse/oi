/* Copyright 2021 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.oi.writer;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.Test;
import org.schnasse.oi.reader.JsonReader;
import org.schnasse.oi.reader.XmlReader;

public class JsonSchemaWriterUnitTest extends WriterUnitTest {
	
	@Test
	public void testJsonSchemaWriter() throws IOException {
		try (InputStream input = new ByteArrayInputStream(
				new String("<?xml version='1.1' encoding='UTF-8'?><LinkedHashMap><root>test</root></LinkedHashMap>")
						.getBytes())) {
			Map<String, Object> map = XmlReader.getMap(input);
			JsonSchemaWriter.gprint(map);
			String expected="{"+
					"\"title\":\"OiGeneratedSchema\","+
					"\"description\":\"https://github.com/jschnasse/oi\","+
					"\"type\":\"object\","+
					"\"properties\":{"+
					"\"root\":{"+
					"\"type\":\"array\","+
					"\"items\":{"+
					"\"type\":\"string\""+
					"}"+
					"}"+
					"}"+
					"}";
			assertEquals(expected.replaceAll("\\s+", ""),outContent.toString().replaceAll("\\s+", ""));

		}
	}

	@Test
	public void testJsonSchemaWriter2() throws IOException {
		try (InputStream input = new ByteArrayInputStream(
				new String("{\n" + "  \"productId\": 1,\n" + "  \"productName\": \"A green door\",\n"
						+ "  \"price\": 12.50,\n" + "  \"tags\": [ \"home\", \"green\" ]\n" + "}").getBytes())) {
			Map<String, Object> map = JsonReader.getMap(input);
			JsonSchemaWriter.gprint(map);
			String expected = "{" + "\"title\":\"OiGeneratedSchema\","
					+ "\"description\":\"https://github.com/jschnasse/oi\"," + "\"type\":\"object\","
					+ "\"properties\":{" + "\"productId\":{" + "\"type\":\"number\"" + "}," + "\"productName\":{"
					+ "\"type\":\"string\"" + "}," + "\"price\":{" + "\"type\":\"number\"" + "}," + "\"tags\":{"
					+ "\"type\":\"array\"," + "\"items\":{" + "\"type\":\"string\"" + "}" + "}" + "}" + "}";
			assertEquals(expected.replaceAll("\\s+", ""), outContent.toString().replaceAll("\\s+", ""));

		}
	}

	@Test
	public void testJsonSchemaWriter3() throws IOException {
		try (InputStream input = new ByteArrayInputStream(
				new String("{\n" + 
						"    \"productId\": 1,\n" + 
						"    \"productName\": \"An ice sculpture\",\n" + 
						"    \"price\": 12.50,\n" + 
						"    \"tags\": [ \"cold\", \"ice\" ],\n" + 
						"    \"dimensions\": {\n" + 
						"      \"length\": 7.0,\n" + 
						"      \"width\": 12.0,\n" + 
						"      \"height\": 9.5\n" + 
						"    },\n" + 
						"    \"warehouseLocation\": {\n" + 
						"      \"latitude\": -78.75,\n" + 
						"      \"longitude\": 20.4\n" + 
						"    }\n" + 
						"  }")
						.getBytes())) {
			Map<String, Object> map = JsonReader.getMap(input);
			JsonSchemaWriter.gprint(map);
			String expected="{"+
					"\"title\":\"OiGeneratedSchema\","+
					"\"description\":\"https://github.com/jschnasse/oi\","+
					"\"type\":\"object\","+
					"\"properties\":{"+
					"\"productId\":{"+
					"\"type\":\"number\""+
					"},"+
					"\"productName\":{"+
					"\"type\":\"string\""+
					"},"+
					"\"price\":{"+
					"\"type\":\"number\""+
					"},"+
					"\"tags\":{"+
					"\"type\":\"array\","+
					"\"items\":{"+
					"\"type\":\"string\""+
					"}"+
					"},"+
					"\"dimensions\":{"+
					"\"type\":\"object\","+
					"\"properties\":{"+
					"\"length\":{"+
					"\"type\":\"number\""+
					"},"+
					"\"width\":{"+
					"\"type\":\"number\""+
					"},"+
					"\"height\":{"+
					"\"type\":\"number\""+
					"}"+
					"}"+
					"},"+
					"\"warehouseLocation\":{"+
					"\"type\":\"object\","+
					"\"properties\":{"+
					"\"latitude\":{"+
					"\"type\":\"number\""+
					"},"+
					"\"longitude\":{"+
					"\"type\":\"number\""+
					"}"+
					"}"+
					"}"+
					"}"+
					"}";
				assertEquals(expected.replaceAll("\\s+", ""),outContent.toString().replaceAll("\\s+", ""));

		}
	}
	
	@Test
	public void testJsonSchemaWriter_complexArray() throws IOException {
		try (InputStream input = new ByteArrayInputStream(
				new String("{\"complexArray\":[{\"id\":[\"id1\",\"id2\"],\"name\":{\"vorname\":\"Max\",\"nachname\":\"mustermann\"}}]}")
						.getBytes())) {
			Map<String, Object> map = JsonReader.getMap(input);
			JsonSchemaWriter.gprint(map);
			}
	}
	
	@Test
	public void testJsonSchemaWriter_arrayInArray() throws IOException {
		try (InputStream input = new ByteArrayInputStream(
				new String("{\"matrix\":[[\"1\",\"2\",\"3\"],[\"4\",\"5\",\"6\"]]}")
						.getBytes())) {
			Map<String, Object> map = JsonReader.getMap(input);
			JsonSchemaWriter.gprint(map);
			String expected="{"+
					"\"title\":\"OiGeneratedSchema\","+
					"\"description\":\"https://github.com/jschnasse/oi\","+
					"\"type\":\"object\","+
					"\"properties\":{"+
					"\"matrix\":{"+
					"\"type\":\"array\","+
					"\"items\":{"+
					"\"type\":\"array\","+
					"\"items\":{"+
					"\"type\":\"string\""+
					"}"+
					"}"+
					"}"+
					"}"+
					"}";
			assertEquals(expected.replaceAll("\\s+", ""),outContent.toString().replaceAll("\\s+", ""));

			}
	}
	
	@Test
	public void testJsonSchemaWriter_issue15() throws IOException {
		try (InputStream input = new ByteArrayInputStream(
				new String("{\"emptyArray\":[]}") 
						.getBytes())) {
			Map<String, Object> map = JsonReader.getMap(input);
			JsonSchemaWriter.gprint(map);
		String expected="{"+
				"\"title\":\"OiGeneratedSchema\","+
				"\"description\":\"https://github.com/jschnasse/oi\","+
				"\"type\":\"object\","+
				"\"properties\":{"+
				"\"emptyArray\":{"+
				"\"type\":\"array\""+
				"}"+
				"}"+
				"}";
		assertEquals(expected.replaceAll("\\s+",""),outContent.toString().replaceAll("\\s+",""));
			}
	}
}
