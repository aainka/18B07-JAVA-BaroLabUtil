package com.barolab.gui;

import java.util.HashMap;

public class JYParam extends HashMap<String, Object> {

	private Object source;

	public JYParam(Object source) {
		this.put("source", source);
		this.source = source;
	}

	public JYParam add(String name, Object value) {
		this.put(name, value);
		return this;
	}

	public void send() {
		((Widget) source).fireEvent(this);
	}

	public boolean match(String name, String targetValue) {
		Object value = this.get(name);
		if (targetValue.equals(value)) {
			return true;
		}
		return false;
	}
}
