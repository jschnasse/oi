package org.schnasse.oi.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public class UrlUtilTest {
	@Test
	public void testEncode() {
		try (InputStream in = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("url/url-succeding-tests.json")) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode testdata = mapper.readValue(in, JsonNode.class).at("/tests");
			for (JsonNode test : testdata) {
				String url = test.at("/in").asText();
				String expected = test.at("/out").asText();
				String encodedUrl = URLUtil.saveEncode(url);
				System.out.println(url+" , '"+expected+"' , '"+encodedUrl+"'");
				org.junit.Assert.assertEquals(expected,encodedUrl);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
	public void testDecode() {
		try (InputStream in = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("url/url-succeding-tests.json")) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode testdata = mapper.readValue(in, JsonNode.class).at("/tests");
			for (JsonNode test : testdata) {
				String url = test.at("/in").asText();
				String expected = test.at("/out").asText();
				String decodedUrl = URLUtil.decode(expected);
			  if(!expected.equals(decodedUrl)){
				System.out.println("In:\t"+url+"\nDec:\t"+decodedUrl + "\nExp:\t"+expected+"\n");
			  }
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testDecodeWithErrorMessages() {
		try (InputStream in = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("url/url-succeding-tests.json")) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode testdata = mapper.readValue(in, JsonNode.class).at("/tests");
			for (JsonNode test : testdata) {
				String url = test.at("/in").asText();
				String expected = test.at("/out").asText();

				String decodedUrl = "ERROR";
				try {
					decodedUrl = URLUtil.decode(expected);
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
				if (!url.equals(decodedUrl)) {
					System.out.println("In:\t" + expected);
					System.out.println("Expect:\t" + url);
					System.out.println("Actual:\t" + decodedUrl);
					System.out.println("");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testEncodeWithErrorMessages() {
		try (InputStream in = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("url/url-succeding-tests.json")) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode testdata = mapper.readValue(in, JsonNode.class).at("/tests");
			for (JsonNode test : testdata) {
				String url = test.at("/in").asText();
				String expected = test.at("/out").asText();

				String encodedUrl = "ERROR";
				try {
					encodedUrl = URLUtil.saveEncode(url);
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
				if (!expected.equals(encodedUrl)) {
					System.out.println("In:\t" + url);
					System.out.println("Expect:\t" + expected);
					System.out.println("Actual:\t" + encodedUrl);
					System.out.println("");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testfailingEncodeWithErrorMessages() {
		try (InputStream in = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("url/url-failing-tests.json")) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode testdata = mapper.readValue(in, JsonNode.class).at("/tests");
			for (JsonNode test : testdata) {
				String url = test.at("/in").asText();
				String expected = test.at("/out").asText();
				String encodedUrl = "ERROR";
				try {
					encodedUrl = URLUtil.saveEncode(url);
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
				System.out.println("In:\t" + url);
				System.out.println("Expect:\t" + expected);
				System.out.println("Actual:\t" + encodedUrl);
				System.out.println("");
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String asJson(Object obj) throws Exception {
		StringWriter w = new StringWriter();
		new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true).writeValue(w, obj);
		String result = w.toString();
		return result;
	}

	public void generateTestData() {
		generateTestData("urls-local.json");
		generateTestData("urls.json");
	}

	public void generateTestData(String name) {
		List<Map<String, Object>> success = new ArrayList<>(200);

		List<Map<String, Object>> failing = new ArrayList<>(200);
		try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(name)) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode urlData = mapper.readValue(in, JsonNode.class);
			JsonNode groups = urlData.at("/tests/group");
			for (JsonNode group : groups) {
				JsonNode tests = group.at("/test");
				for (JsonNode test : tests) {

					Map<String, Object> testData = new TreeMap<>();

					String expectedUrl = test.at("/encoded").asText();
					String unencodedUrl = test.at("/unencoded").asText();
					try {
						String encodedUrl = URLUtil.encode(unencodedUrl);
						if (!expectedUrl.equals(encodedUrl))
							throw new RuntimeException("Unexpected comparison!");
						testData.put("in", unencodedUrl);
						testData.put("out", expectedUrl);
						success.add(testData);
					} catch (Exception e) {
						testData.put("in", unencodedUrl);
						testData.put("out", expectedUrl);
						failing.add(testData);
					}
				}
			}
			System.out.println("Working URLs --------------");
			System.out.println(asJson(success));
			System.out.println("Not Working URLs-----------");
			System.out.println(asJson(failing));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	@Test
	public void readFromUrl() {
		try (InputStream in = getInputStreamFromUrl("http://jira.atlassian.com/rest/api/2/issue/JSWCLOUD-11658")) {
			System.out.println(convertInputStreamToString(in));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test(expected = RuntimeException.class)
	public void readFromUrlWithBasicAuth() {
		String user = "aUser";
		String passwd = "aPasswd";
		try (InputStream in = getInputStreamFromUrl(
						new URL("https://jira.atlassian.com/rest/api/2/issue/JSWCLOUD-11658"), user, passwd)) {
			System.out.println(convertInputStreamToString(in));
		} catch (Exception e) {
			System.out.println("If basic auth is provided, it should be correct: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Test
	public void readFromUrlWithNoHeaders() throws IOException {
		try (InputStream in = URLUtil.urlToInputStream(new URL("http://google.de"), null)) {
			System.out.println(convertInputStreamToString(in));
		}
	}

	private InputStream getInputStreamFromUrl(URL url, String user, String passwd) throws IOException {
		String encoded = Base64.getEncoder().encodeToString((user + ":" + passwd).getBytes(StandardCharsets.UTF_8));
		return URLUtil.urlToInputStream(url, mapOf("Accept", "application/json", "Authorization", "Basic " + encoded,
						"User-Agent", "myApplication"));
	}

	private InputStream getInputStreamFromUrl(String url) throws IOException {
		return URLUtil.urlToInputStream(new URL(url), mapOf("Accept", "application/json", "User-Agent", "myApplication"));
	}
	private String convertInputStreamToString(InputStream inputStream) throws IOException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) != -1) {
			result.write(buffer, 0, length);
		}
		return result.toString("UTF-8");
	}

	@SuppressWarnings("unchecked")
	private <K, V> Map<K, V> mapOf(Object... keyValues) {
		Map<K, V> map = new HashMap<>();
		K key = null;
		for (int index = 0; index < keyValues.length; index++) {
			if (index % 2 == 0) {
				key = (K) keyValues[index];
			} else {
				map.put(key, (V) keyValues[index]);
			}
		}
		return map;
	}
}
