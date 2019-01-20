package com.barolab.html;

import java.util.LinkedList;
import java.util.List;

public class HmTR extends HmObject {

	public HmTD addTD() {
		HmTD tr = new HmTD();
		comp.add(tr);
		return tr;
	}

	@Override
	public void toHTML(HttpBuilder h) {
		h.println("<tr>");
		for ( Object anObject : comp) {
		
			HmObject hObject = (HmObject) anObject;
			hObject.toHTML(h);
			
		}
		h.println("</tr>");
	}

}
