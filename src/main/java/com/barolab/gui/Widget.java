package com.barolab.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;

import lombok.Data;

@Data
public abstract class Widget {

	private String name = null;
	private JYController controller = null;
	private Widget parent;
//	protected Container component;
	Widget source;
	private List<Widget> list = new LinkedList<Widget>();

	abstract public Widget setUpper(Widget upper, String constraints);

	public Widget(String name ) {
		this.name = name;
		this.source = this;
	}
	
	public String toString() {
		return this.getClass().getSimpleName() +"(name:"+name+")";
	}
	
	public abstract Container getComponent();
	
	public Widget size(int width, int height) {
		getComponent().setPreferredSize(new Dimension(width, height));
		return this;
	}
	
	// ##############################################
	// ## Widget Binding
	// ##############################################
	
	public static Widget findWidget(Widget root, String name) {
	//	System.out.println("findWidet() :: child.name = "+ root.name);
		if ( root.name.equals(name)) {
			return root;
		} else {
			for( Widget child : root.list) {
				Widget wp = findWidget(child,name);
				if ( wp != null) {
					return wp;
				}
			}
		}
		return null;
	}
	
	final public Widget add(Widget child, String constraints) {
		child.setUpper(this, constraints);
		child.parent = this;
		list.add(child);
		return child;
	}

	// ##############################################
	// ## Widget Binding
	// ##############################################
	
	public void fireEvent(JYParam param) {
		JYController cp = this.controller;
		Widget wp = this;
		while (wp.controller == null && wp.parent != null) {
			wp = wp.parent;
		}
		if (wp != null) {
			wp.controller.event(param);
		} else {
			System.out.println("CANT FOUND CONTROLLER!!!");
		}
	}
	
	



}
