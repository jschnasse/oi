import java.util.Map;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.junit.Test;
import org.schnasse.cjxy.reader.JsonReader;
import org.schnasse.cjxy.reader.YamlReader;
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
				.getMap(Thread.currentThread().getContextClassLoader().getResourceAsStream("json/HT015847062.csv"));
		JsonWriter.gprint(map);
		YamlWriter.gprint(map);
		XmlWriter.gprint(map);
	}

	@Test
	public void readRdf() throws Exception {
		// HTTP
		System.setProperty("http.proxyHost", "http://192.168.10.66");
		System.setProperty("http.proxyPort", "3128");
		System.setProperty("http.nonProxyHosts", "localhost|127.0.0.1");

		// HTTPS
		System.setProperty("https.proxyHost", "http://192.168.10.66");
		System.setProperty("https.proxyPort", "3128");

		Map<String, Object> map = org.schnasse.cjxy.reader.RdfReader.getMap(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("json/HT015847062.json"),
				RDFFormat.JSONLD);
		JsonWriter.gprint(map);
		YamlWriter.gprint(map);
		XmlWriter.gprint(map);
	}
}
