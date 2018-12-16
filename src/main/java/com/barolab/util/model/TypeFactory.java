package com.barolab.util.model;

import java.util.HashMap;
import java.util.Map;

public class TypeFactory {

	static TypeFactory instance = null;

	private Map<String, Object> map = new HashMap<String, Object>();

	public static TypeFactory getInstance() {
		if (instance == null) {
			instance = new TypeFactory();
			instance.init();
		}
		return instance;
	}

	private void init() {
		map.put("int", new BeanInt());
		map.put("float", new BeanFloat());
		map.put("java.lang.String", new BeanString());
		map.put("java.util.Date", new BeanDate());
	}

	public static BeanType find(String name) {
		BeanType beanType = (BeanType) getInstance().map.get(name);
		if (beanType == null) {
			System.out.println("Can't found beanType = " + name);
		}
		return (BeanType) getInstance().map.get(name);
	}

}
