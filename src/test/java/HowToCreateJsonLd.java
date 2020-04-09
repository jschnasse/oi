/* Copyright 2020 Jan Schnasse. Licensed under the EPL 2.0 */
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Collection;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;

public class HowToCreateJsonLd {
	@org.junit.Test
	public void testForYourCode() {
		String data = "{\"name\" : \"Andreas\",\"age\" : 20,\"profession\" : \"student\", \"personalWebsite\" : \"example.com\",\"@context\": \"http://schema.org/\"}";
		try (InputStream in = new ByteArrayInputStream(data.getBytes("utf-8"))) {
			String dataAsRdf = readRdfToString(in, RDFFormat.JSONLD, RDFFormat.NTRIPLES, "");
			System.out.println(dataAsRdf);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@org.junit.Test
	public void jsonFileToRDF() {

		try (InputStream in = Thread.currentThread().getContextClassLoader()
				.getSystemResourceAsStream("43219064.json")) {
			String dataAsRdf = readRdfToString(in, RDFFormat.JSONLD, RDFFormat.NTRIPLES, "");
			System.out.println(dataAsRdf);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param in      a rdf input stream
	 * @param inf     the rdf format of the input stream
	 * @param outf    the output format
	 * @param baseUrl usually the url of the resource
	 * @return a string representation
	 */
	public static String readRdfToString(InputStream in, RDFFormat inf, RDFFormat outf, String baseUrl) {
		Collection<Statement> myGraph = null;
		myGraph = readRdfToGraph(in, inf, baseUrl);
		return graphToString(myGraph, outf);
	}

	/**
	 * @param inputStream an Input stream containing rdf data
	 * @param inf         the rdf format
	 * @param baseUrl     see sesame docu
	 * @return a Graph representing the rdf in the input stream
	 */
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

	/**
	 * Transforms a graph to a string.
	 * 
	 * @param myGraph a sesame rdf graph
	 * @param outf    the expected output format
	 * @return a rdf string
	 */
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
