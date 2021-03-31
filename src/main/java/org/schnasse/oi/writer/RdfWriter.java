/* Copyright 2021 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.oi.writer;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.schnasse.oi.reader.RdfReader;
import org.schnasse.oi.writer.base.Writer;

public class RdfWriter {
	public static void gprint(Map<String, Object> json, RDFFormat format) {
//		final Map<String, Object> context = new LinkedHashMap<>();
//		context.put("@context",);
		final Map<String, Object> rdf = org.schnasse.oi.reader.RdfReader.getFramedJson(json, ContextWriter.findContext(json));
		Collection<Statement> myGraph = RdfReader.readRdfToGraph(
				new ByteArrayInputStream(Writer.write(JsonWriter.createMapper(), rdf).getBytes()), RDFFormat.JSONLD,
				"");
		System.out.println(RdfReader.graphToString(myGraph, format));
	}
}
