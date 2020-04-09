package org.schnasse.cjxy.settings;

import java.util.HashMap;
import java.util.Map;

public class Frame {

	public static Map<String, Object> getFrame() {
		Map<String, Object> context = new HashMap<>();
		context.put("@context", "http://schema.org/");
		return context;
	}
}
