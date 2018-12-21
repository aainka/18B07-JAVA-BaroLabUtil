package com.barolab.html;

public class HmTable extends HmObject {

	@Override
	public void toHTML(HttpPrintStream h) {
		h.println("<table class='mytable' >");
		for ( Object anObject : comp) {
			HmObject hObject = (HmObject) anObject;
			hObject.toHTML(h);
			
		}
		h.println("</table>");
	}

	public HmTR addTR() {
		HmTR tr = new HmTR();
		comp.add(tr);
		return tr;
	}

}
