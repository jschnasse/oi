/* Copyright 2021 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.oi;

import java.io.InputStream;
import java.util.Map;

public interface IGetMap {
	public Map<String, Object> getMap(InputStream in);
}
