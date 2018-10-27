package com.barolab.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import lombok.Data;
import lombok.extern.java.Log;

@Data
@Log
public class BeanAttribute {
	private int index;
	private String name;
	private Class Type;
	private Method getter, setter;
	private Class clazz;
	private int xlsColIndex;

	public BeanAttribute init(Field f, int index) {
		this.index = index;
		name = f.getName();
		Type = f.getType();
		try {
			String funcName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
			getter = clazz.getMethod(funcName, null);
			funcName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
			Class[] argTypes = new Class[] { f.getType() };
			setter = clazz.getMethod(funcName, argTypes);
		} catch (NoSuchMethodException | SecurityException e) {
			// e.printStackTrace();
			log.finer("BeanAttribute skip attribute = " + f.getName());
			return null;
		}
		return this;
	}
}
