
/*******************************************************************************
 * Copyright 2017 Jan Schnasse
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;
import org.junit.Test;

import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;
import com.google.common.base.Charsets;

public class HowToConvertRdfToJson {

	@Test
	public void convertRdfToPrettyJson() {
		try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("book.ttl")) {
			System.out.println(getPrettyJsonLdString(in, RDFFormat.TURTLE, getFrame()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void convertRdfToPrettyJson2() {
		try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("50588066.json")) {
			System.out.println(getPrettyJsonLdString(in, RDFFormat.JSONLD, getFrame2()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param in     Input stream with rdf data
	 * @param format format of the rdf data
	 * @return a pretty JSON document as String
	 */
	public static String getPrettyJsonLdString(InputStream in, RDFFormat format, Map<String, Object> frame) {
		return getPrettyJsonLdString(readRdfToString(in, format, RDFFormat.JSONLD, ""), frame);
	}

	/**
	 * @param statements rdf statements collected
	 * @return a pretty JSON document as String
	 */
	public static String getPrettyJsonLdString(Collection<Statement> statements, Map<String, Object> frame) {
		return getPrettyJsonLdString(graphToString(statements, RDFFormat.JSONLD), frame);
	}

	public static Map<String, Object> getPrettyJsonLdMap(InputStream in, RDFFormat format, Map<String, Object> frame) {
		return getMap(readRdfToString(in, format, RDFFormat.JSONLD, ""), frame);
	}

	private static Map<String, Object> getMap(String rdfGraphAsJson, Map<String, Object> frame) {
		try {
			// @formatter:off
			return removeGraphArray(getFramedJson(createJsonObject(rdfGraphAsJson), frame));
			// @formatter:on
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String getPrettyJsonLdString(String rdfGraphAsJson, Map<String, Object> frame) {
		try {
			// @formatter:off
			return JsonUtils.toPrettyString(getMap(rdfGraphAsJson, frame));
			// @formatter:on
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Map<String, Object> removeGraphArray(Map<String, Object> framedJson) {
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> graph = (List<Map<String, Object>>) framedJson.get("@graph");
		return graph.get(0);
	}

	private static Map<String, Object> getFramedJson(Object json, Map<String, Object> frame) {
		try {
			return JsonLdProcessor.frame(json, frame, new JsonLdOptions());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Map<String, Object> getFrame() {
		Map<String, Object> context = new HashMap<>();
		context.put("@context", "http://schema.org/");
		return context;
	}

	private static Map<String, Object> getFrame2() {
		Map<String, Object> frame = new HashMap<>();
		frame.put("@type", "dcat:Distribution");
		Map<String, Object> context = new HashMap<>();
		context.put("dct", "http://purl.org/dc/terms/");
		context.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		context.put("dcat", "http://www.w3.org/ns/dcat#");
		context.put("example", "http://example.com/vocabulary/");
		frame.put("@context", context);
		return frame;
	}

	private static Object createJsonObject(String ld) {
		try (InputStream inputStream = new ByteArrayInputStream(ld.getBytes(Charsets.UTF_8))) {
			Object jsonObject = JsonUtils.fromInputStream(inputStream);
			return jsonObject;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Collection<Statement> readRdfToGraph(final InputStream inputStream, final RDFFormat inf,
			final String baseUrl) {
		try {
			final RDFParser rdfParser = Rio.createParser(inf);
			final StatementCollector collector = new StatementCollector();
			rdfParser.setRDFHandler(collector);
			rdfParser.parse(inputStream, baseUrl);
			return collector.getStatements();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String readRdfToString(InputStream in, RDFFormat inf, RDFFormat outf, String baseUrl) {
		Collection<Statement> myGraph = null;
		myGraph = readRdfToGraph(in, inf, baseUrl);
		return graphToString(myGraph, outf);
	}

	public static String graphToString(Collection<Statement> myGraph, RDFFormat outf) {
		StringWriter out = new StringWriter();
		RDFWriter writer = Rio.createWriter(outf, out);
		try {
			writer.startRDF();
			for (Statement st : myGraph) {
				writer.handleStatement(st);
			}
			writer.endRDF();
		} catch (RDFHandlerException e) {
			throw new RuntimeException(e);
		}
		return out.getBuffer().toString();
	}
}
