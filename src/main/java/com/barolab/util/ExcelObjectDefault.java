package com.barolab.util;

import java.lang.reflect.Field;
import java.util.LinkedList;

public class ExcelObjectDefault {
	protected String filename;
	protected String classname;
	protected transient Class clazz;
	LinkedList<BeanAttribute> attrs = new LinkedList<BeanAttribute>();

	public void init() {
		int count = 0;
		for (Field f : clazz.getDeclaredFields()) { // attribute name
			BeanAttribute attr = new BeanAttribute();
			attr.setClazz(clazz);
			attr.init(f);
			attrs.add(attr);
		}
	}

	public void debugf(String s) {
		System.out.println(s);
	}
}
