package com.barolab.html;

import java.util.Date;

public class HmTD extends HmObject {

	private int width = 0;
	private String align = null;
	private String stagParam = new String();

	public void toHTML(HttpBuilder h) {
		String stag = new String("<td" + stagParam);
		if (width > 0) {
			stag += " width=" + width;
		}
		if (align != null) {
			stag += " align=\"" + align + "\"";
		}
		h.println(stag + " >");
		for (Object anObject : comp) {
			h.println(anObject);
		}
		h.println("</td>");

	}

	public void add(int value) {
		comp.add("" + value);
	}

	public void add(float value) {
		comp.add("" + value);

	}

	public void add(String sValue) {
		if (sValue == null) {
			comp.add(" ");
		} else {
			comp.add(sValue);
		}

	}

	public void add(Date date) {
		if (date != null) {
			comp.add(date.toLocaleString());
		}

	}

	public HmTD setWidth(int width) {
		this.width = width;
		return this;
	}

	public HmTD setAligh(String align) {
		this.align = align;
		return this;
	}

	public HmTD setAttribute(String param, int value) {
		stagParam += " " + param + "='" + value + "' ";
		return this;
	}

}
