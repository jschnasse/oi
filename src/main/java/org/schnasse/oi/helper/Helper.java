/* Copyright 2020 Jan Schnasse. Licensed under the EPL 2.0 */
package org.schnasse.oi.helper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Helper {

	public static InputStream getInputStream(String pathtofile) {
		try {
			if (pathtofile == null || pathtofile.isEmpty())
				return null;
			FileInputStream in = new FileInputStream(pathtofile);
			return in;
		} catch (FileNotFoundException e) {
			System.out.println(e.getLocalizedMessage());
			return null;
		} catch (Exception e) {

			throw new RuntimeException(e);
		}
	}

}
