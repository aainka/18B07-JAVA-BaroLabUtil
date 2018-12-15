package com.barolab.html;

import java.util.LinkedList;
import java.util.List;

public abstract class HmObject {

	protected List comp = new LinkedList();
	
	public abstract void toHTML(HttpPrintStream h) ;
}
