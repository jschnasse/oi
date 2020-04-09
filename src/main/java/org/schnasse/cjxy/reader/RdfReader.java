package org.schnasse.cjxy.reader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;

import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;
import com.google.common.base.Charsets;

public class RdfReader {
	static public Map<String, Object> getMap(InputStream in, RDFFormat format, Map<String, Object> frame) {
		return getMap(readRdfToString(in, format, RDFFormat.JSONLD, ""), frame);
	}

	private static Map<String, Object> getMap(String rdfGraphAsJson, Map<String, Object> frame) {
		try {
			Map<String, Object> result = removeGraphArray(getFramedJson(createJsonObject(rdfGraphAsJson), frame));
			result.put("@context", frame.get("@context"));
			return result;
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
