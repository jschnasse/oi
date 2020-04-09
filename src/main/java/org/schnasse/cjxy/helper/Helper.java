/* Copyright 2019 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.cjxy.helper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Helper {

	public static InputStream getInputStream(String pathToPasswd) {
		try {
			if (pathToPasswd == null || pathToPasswd.isEmpty())
				return null;
			FileInputStream in = new FileInputStream(pathToPasswd);
			return in;
		} catch (FileNotFoundException e) {
			System.out.println(e.getLocalizedMessage());
			return null;
		} catch (Exception e) {

			throw new RuntimeException(e);
		}
	}

}
