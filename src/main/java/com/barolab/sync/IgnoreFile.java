package com.barolab.sync;

public class IgnoreFile {
	
	static String[] igset = {"/target",".class","/.","/__"};

	public static boolean ignore(String name) {
		// TODO Auto-generated method stub
		for (String match : igset) {
			if ( name.indexOf(match)>=0) {
				return true;
			}
		}
		return false;
	}

}
