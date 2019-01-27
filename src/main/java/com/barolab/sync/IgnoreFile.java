package com.barolab.sync;

public class IgnoreFile {

	static String[] igset = { "/target", ".class", "/.", "/__", ".out", "*.log", "*.zip", "*.ppt", "*.xls", ".pptx",
			".xlsx" };

	public static boolean ignore(String name) {
		for (String match : igset) {
			if (name.indexOf(match) >= 0) {
				return true;
			}
		}
		return false;
	}

}
