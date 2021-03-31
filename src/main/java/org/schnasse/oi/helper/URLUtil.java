/* Copyright 2021 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.oi.helper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.IDN;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.CharMatcher;

/**
 * 
 * @author Jan Schnasse
 *
 */
public class URLUtil {
	/*
	 * This method will only encode an URL if it is not encoded already. It will
	 * also replace '+'-encoded spaces with percent encoding.
	 */
	public static String saveEncode(String url) {
		try {
			String passedUrl = url.replaceAll("\\+", "%20");
			if (!isAlreadyEncoded(passedUrl)) {
				return encode(passedUrl);
			}
			if (!CharMatcher.ascii().matchesAllOf(passedUrl)) {
				return encode(passedUrl);
			}
			if (passedUrl.endsWith(":/")) {
				return encode(passedUrl);
			}
			if (Character.isUpperCase(passedUrl.charAt(0))) {
				return encode(passedUrl);
			}
			return passedUrl;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static boolean isAlreadyEncoded(String passedUrl) {
		boolean isEncoded = true;
		if (passedUrl.matches(".*[\\ \"\\<\\>\\{\\}|\\\\^~\\[\\]].*")) {
			isEncoded = false;
		}
		return isEncoded;
	}

	public static String encode(String url) {
		try {
			System.out.println(url);
			URL u = new URL(url);
			URI uri = new URI(u.getProtocol(), u.getUserInfo(), IDN.toASCII(u.getHost()), u.getPort(), u.getPath(),
					u.getQuery(), u.getRef());
			String correctEncodedURL = uri.toASCIIString();
			return correctEncodedURL;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String decode(String url) {
		try {
			URL u = new URL(url);
			String protocol = u.getProtocol();
			String userInfo = u.getUserInfo();
			String host = u.getHost() != null ? IDN.toUnicode(u.getHost()) : null;
			int port = u.getPort();
			String path = u.getPath() != null ? URLDecoder.decode(u.getPath(), StandardCharsets.UTF_8.name()) : null;
			String ref = u.getRef();
			String query = u.getQuery() != null ? URLDecoder.decode(u.getQuery(), StandardCharsets.UTF_8.name()) : null;

			protocol = protocol != null ? protocol + "://" : "";
			userInfo = userInfo != null ? userInfo : "";
			host = host != null ? host : "";
			String portStr = port != -1 ? ":" + port : "";
			path = path != null ? path : "";
			query = query != null ? "?" + query : "";
			ref = ref != null ? "#" + ref : "";

			return String.format("%s%s%s%s%s%s%s", protocol, userInfo, host, portStr, path, ref, query);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static InputStream urlToInputStream(URL url, Map<String, String> args) {
		HttpURLConnection con = null;
		InputStream inputStream = null;
		try {
			con = (HttpURLConnection) url.openConnection();
			con.setInstanceFollowRedirects(false);
			con.setConnectTimeout(15000);
			con.setReadTimeout(15000);
			if (args != null) {
				for (Entry<String, String> e : args.entrySet()) {
					con.setRequestProperty(e.getKey(), e.getValue());
				}
			}
			con.connect();
			int responseCode = con.getResponseCode();
			/*
			 * By default the connection will follow redirects. The following block is only
			 * entered if the implementation of HttpURLConnection does not perform the
			 * redirect. The exact behavior depends to the actual implementation (e.g.
			 * sun.net). !!! Attention: This block allows the connection to switch protocols
			 * (e.g. HTTP to HTTPS), which is <b>not</b> default behavior. See:
			 * https://stackoverflow.com/questions/1884230 for more info!!!
			 */
			if (responseCode < 400 && responseCode > 299) {
				String redirectUrl = con.getHeaderField("Location");
				try {
					URL newUrl = new URL(redirectUrl);
					return urlToInputStream(newUrl, args);
				} catch (MalformedURLException e) {
					URL newUrl = new URL(url.getProtocol() + "://" + url.getHost() + redirectUrl);
					return urlToInputStream(newUrl, args);
				}
			}
			/* !!!!! */
			throwExceptionIfServerAnswersInWrongFormat(args, con);
			inputStream = con.getInputStream();
			return inputStream;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void throwExceptionIfServerAnswersInWrongFormat(Map<String, String> args, HttpURLConnection con) {
		if (args != null) {
			String accept = args.get("accept");
			if (accept != null && !accept.isEmpty()) {
				String contentType = con.getHeaderField("Content-Type");
				if (contentType != null && !contentType.isEmpty()) {
					contentType = contentType.trim().toLowerCase();
					accept = accept.trim().toLowerCase();
					if (!contentType.startsWith(accept)) {
						throw new RuntimeException("Website does not answer in correct format! Asked for accept:"
								+ accept + " but got content-type:" + contentType);
					}
				}
			}
		}
	}

	public static boolean isValidUrl(String url) {
		try {
			new URL(url);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
