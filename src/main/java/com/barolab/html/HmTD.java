package com.barolab.html;

import java.util.Date;

public class HmTD extends HmObject {

	private int width = 0;
	private String align = null;

	public void toHTML(HttpPrintStream h) {
		String stag = new String("<td");
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
		comp.add(sValue);

	}

	public void add(Date spentOn) {
		comp.add(spentOn.toLocaleString());

	}

	public HmTD setWidth(int width) {
		this.width = width;
		return this;
	}

	public HmTD setAligh(String align) {
		this.align = align;
		return this;
	}

}
