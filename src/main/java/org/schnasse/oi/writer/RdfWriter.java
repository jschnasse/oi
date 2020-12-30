package org.schnasse.oi.writer;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.Map;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.schnasse.oi.reader.RdfReader;
import org.schnasse.oi.writer.base.Writer;

public class RdfWriter {
	public static void gprint(final Map<String, Object> json, final RDFFormat format) {
		final Map<String, Object> context = ContextWriter.findContext(json);
		final Map<String, Object> rdf = org.schnasse.oi.reader.RdfReader.getFramedJson(json, context);
		final Collection<Statement> myGraph = RdfReader.readRdfToGraph(
				new ByteArrayInputStream(Writer.write(JsonWriter.createMapper(), rdf).getBytes()), RDFFormat.JSONLD,
				"");
		System.out.println(RdfReader.graphToString(myGraph, format));
	}
}
