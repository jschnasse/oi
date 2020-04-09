import java.util.Map;

import org.junit.Test;
import org.schnasse.cjxy.reader.base.JsonReader;
import org.schnasse.cjxy.reader.base.YamlReader;
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
		Map<String, Object> map = org.schnasse.cjxy.reader.base.XmlReader
				.getMap(Thread.currentThread().getContextClassLoader().getResourceAsStream("json/HT015847062.xml"));
		JsonWriter.gprint(map);
		YamlWriter.gprint(map);
		XmlWriter.gprint(map);
	}

	@Test
	public void readCsv() throws Exception {
		Map<String, Object> json = org.schnasse.cjxy.reader.base.MapReader
				.getMap(Thread.currentThread().getContextClassLoader().getResourceAsStream("json/HT015847062.csv"));
		JsonWriter.gprint(json);
		YamlWriter.gprint(json);
		XmlWriter.gprint(json);
	}
}
