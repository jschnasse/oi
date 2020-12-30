/* Copyright 2020 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.oi.helper;

import java.util.Map;

import org.junit.Assert;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

import ch.qos.logback.classic.Level;

public class TestHelper {
	private static final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory
			.getLogger(TestHelper.class);

	public static void mapCompare(final Map m1, final Map m2) {

		logger.setLevel(Level.DEBUG);
		final MapDifference<String, Object> diff = Maps.difference(m1, m2);
		if (!diff.areEqual()) {
			logger.debug("", diff.entriesDiffering());
			logger.debug("", diff.entriesOnlyOnLeft());
			logger.debug("", diff.entriesOnlyOnRight());
		}
		Assert.assertTrue(diff.areEqual());
	}
}
