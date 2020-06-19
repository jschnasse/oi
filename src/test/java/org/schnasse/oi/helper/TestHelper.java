package org.schnasse.oi.helper;

import java.util.Map;

import org.jline.utils.Log;
import org.junit.Assert;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

public class TestHelper {

	public static void mapCompare(Map m1, Map m2) {
		MapDifference<String, Object> diff = Maps.difference(m1, m2);
		if (!diff.areEqual()) {
			Log.error(diff.entriesDiffering());
			Log.warn(diff.entriesOnlyOnLeft());
			Log.warn(diff.entriesOnlyOnRight());
		}
		Assert.assertTrue(diff.areEqual());
	}
}
