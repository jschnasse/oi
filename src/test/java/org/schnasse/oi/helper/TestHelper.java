package org.schnasse.oi.helper;

import java.util.Map;

import org.junit.Assert;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

public class TestHelper {

	public static void mapCompare(Map m1, Map m2) {
		MapDifference<String, Object> diff = Maps.difference(m1, m2);
		if (!diff.areEqual()) {
			System.out.println(diff.entriesDiffering());
			System.out.println(diff.entriesOnlyOnLeft());
			System.out.println(diff.entriesOnlyOnRight());
		}
		Assert.assertTrue(diff.areEqual());
	}
}
