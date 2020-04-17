import java.util.Map;

import org.junit.Test;
import org.locationtech.jts.util.Assert;

public class JsonTest {
	@Test
	public void json_to_yml() throws Exception {
		Map<String, Object> json = org.schnasse.cjxy.reader.JsonReader.getMap(Thread.currentThread()
				.getContextClassLoader().getResourceAsStream("json/in/rosenmontag.json"));
		Map<String, Object> yml = org.schnasse.cjxy.reader.YamlReader.getMap(Thread.currentThread()
				.getContextClassLoader().getResourceAsStream("json/out/rosenmontag.json.yml"));
		Assert.equals(yml, json);
	}
}
