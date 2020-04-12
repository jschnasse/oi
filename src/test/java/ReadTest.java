
/* Copyright 2020 Jan Schnasse. Licensed under the EPL 2.0 */
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.junit.Test;
import org.schnasse.cjxy.main.Main;
import org.schnasse.cjxy.reader.JsonReader;
import org.schnasse.cjxy.reader.YamlReader;
import org.schnasse.cjxy.writer.ContextWriter;
import org.schnasse.cjxy.writer.CsvWriter;
import org.schnasse.cjxy.writer.JsonWriter;
import org.schnasse.cjxy.writer.XmlWriter;
import org.schnasse.cjxy.writer.YamlWriter;

public class ReadTest {
	@Test
	public void readJson() throws Exception {
		Map<String, Object> map = JsonReader
				.getMap(Thread.currentThread().getContextClassLoader().getResourceAsStream("json/HT015847062.json"));
		JsonWriter.gprint(map);
		YamlWriter.gprint(map);
		XmlWriter.gprint(map);
	}

	@Test
	public void readYaml() throws Exception {
		Map<String, Object> map = YamlReader
				.getMap(Thread.currentThread().getContextClassLoader().getResourceAsStream("json/HT015847062.yml"));
		JsonWriter.gprint(map);
		YamlWriter.gprint(map);
		XmlWriter.gprint(map);
	}

	@Test
	public void readXml() throws Exception {
		Map<String, Object> map = org.schnasse.cjxy.reader.XmlReader
				.getMap(Thread.currentThread().getContextClassLoader().getResourceAsStream("json/HT015847062.xml"));
		JsonWriter.gprint(map);
		YamlWriter.gprint(map);
		XmlWriter.gprint(map);
	}

	@Test
	public void readCsv() throws Exception {
		Map<String, Object> map = org.schnasse.cjxy.reader.CsvReader
				.getMap(Thread.currentThread().getContextClassLoader().getResourceAsStream("json/HT015847062.csv"),null,",");
		JsonWriter.gprint(map);
		YamlWriter.gprint(map);
		XmlWriter.gprint(map);
	//	CsvWriter.gprint(map);
	}
	
	@Test
	public void readCsv2() throws Exception {
		Map<String, Object> map = org.schnasse.cjxy.reader.CsvReader
				.getMap(Thread.currentThread().getContextClassLoader().getResourceAsStream("json/HT015847062_v2.csv"),
						new String[] {"OclcNumber","URI","Title","Author","Date"},",");
		JsonWriter.gprint(map);
		YamlWriter.gprint(map);
		XmlWriter.gprint(map);
		//CsvWriter.gprint(map);
	}
	
	@Test
	public void readRdf() throws Exception {
		Map<String, Object> map = org.schnasse.cjxy.reader.RdfReader.getMap(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("json/HT015847062.json"),
				RDFFormat.JSONLD, JsonReader
						.getMap(Thread.currentThread().getContextClassLoader().getResourceAsStream("json/frame.json")));
		JsonWriter.gprint(map);
		YamlWriter.gprint(map);
		XmlWriter.gprint(map);
	}

	@Test
	public void writeContext() throws Exception {
		Map<String, Object> map = org.schnasse.cjxy.reader.JsonReader
				.getMap(Thread.currentThread().getContextClassLoader().getResourceAsStream("json/49726990.json"));
		ContextWriter.gprint(map);

		map = org.schnasse.cjxy.reader.JsonReader
				.getMap(Thread.currentThread().getContextClassLoader().getResourceAsStream("json/HT015847062.json"));
		ContextWriter.gprint(map);
	}

	@Test
	public void mainTest() throws Exception {
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
//		Main.main(s + "/src/test/resources/json/HT015847062.rdf", "-f" + s + "/src/test/resources/json/frame.json",
//				"-tjson");
//		Main.main(s + "/src/test/resources/json/HT015847062.rdf", "-f" + s + "/src/test/resources/json/frame.json",
//				"-tyaml");
//		Main.main(s + "/src/test/resources/json/HT015847062.rdf", "-f" + s + "/src/test/resources/json/frame.json",
//				"-txml");
//		Main.main(s + "/src/test/resources/json/HT015847062.rdf", "-f" + s + "/src/test/resources/json/frame.json",
//				"-trdf");
//		Main.main(s + "/src/test/resources/json/HT015847062.csv");
//		Main.main(s + "/src/test/resources/json/HT015847062_v2.csv","--header=DATE,OCLC,TITLE,AUTHOR,URI");

//		Main.main(s + "/src/test/resources/json/HT015847062_v2.csv","--header=DATE,OCLC,TITLE,AUTHOR,URI","-tjson");

		Main.main(s + "/src/test/resources/json/HT015847062_v2.csv","--header=DATE,OCLC,TITLE,AUTHOR,URI","-tcontext");
	//	Main.main(s + "/src/test/resources/json/HT015847062_v2.csv","--header=DATE,OCLC,TITLE,AUTHOR,URI","-trdf");
	}
}
