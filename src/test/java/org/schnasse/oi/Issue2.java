/* Copyright 2021 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.oi;

import java.util.Map;

import org.junit.Test;
import org.schnasse.oi.writer.JsonWriter;

/* The provided data [1] was Latin-1 encoded. I converted the example file to UTF-8 upfront [2].
 * 
 * [1] https://www.dimdi.de/dynamic/.downloads/sonstige/mesh/mesh-2019-datenbankfassung.zip
 * [2] iconv -f LATIN1 -t UTF-8 ANN.TXT
 */
public class Issue2 {
	@Test
	public void mesh_csv_runs_through() throws Exception {
		Map<String, Object> map = org.schnasse.oi.reader.CsvReader.getMap(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("issue-2/ANN.TXT"),
				new String[] { "a", "b" }, ";", "|");
		JsonWriter.gprint(map);
	}
}
