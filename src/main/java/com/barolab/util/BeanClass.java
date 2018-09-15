package com.barolab.util;

import java.lang.reflect.Field;
import java.util.LinkedList;

public class BeanClass {
	LinkedList<BeanAttribute> attrs = new LinkedList<BeanAttribute>();
	private Class clazz;

	public void init(Class clazz) {
		this.clazz = clazz;
		int count = 0;
		for (Field f : clazz.getDeclaredFields()) { // attribute name
			BeanAttribute attr = new BeanAttribute();
			BeanAttribute attr2 = new BeanAttribute();
			attr.setClazz(clazz);
			if (attr.init(f, count) != null) {
				attrs.add(attr);
				System.out.println( "init.attr = " + f.getName()+ " "+count);
				count++;
			} else {
				System.out.println("init.attr.nok = " + f.getName()+ " "+count);
			}
			
		}
	}

	public BeanAttribute findAttribute(String name) {
		for (BeanAttribute attr : attrs) {
			if ( attr.getName().equals(name)) {
				System.out.println("Found attribute name "+name);
				return attr;
			}
		}
		return null;
	}
}
