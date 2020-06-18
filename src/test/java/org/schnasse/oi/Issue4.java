package org.schnasse.oi;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class Issue4 {
	@SuppressWarnings("unchecked")
	@Test
	public void read_icd10() {
		try (InputStream in = new ByteArrayInputStream("<array><item>1</item><item>2</item></array>".getBytes())) {
			Map<String, Object> map = org.schnasse.oi.reader.XmlReader.getMap(in);
			Assert.assertTrue(((List<String>) map.get("item")).contains("1"));
			Assert.assertTrue(((List<String>) map.get("item")).contains("2"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
