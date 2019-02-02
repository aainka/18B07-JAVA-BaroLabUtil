package com.barolab.gui;

import java.awt.Component;

import lombok.Data;

@Data
public abstract class Widget {
	
	private final String name;
	
// 	public void init() ;
//	public void build() ;
//	public void add(String name, Widget widget, Object ... arg);

//	void init(String name);

	abstract public Component getContent();
	abstract public Widget add( Widget child, String constraints);
	
	public Widget(String name) {
		this.name = name;
	}

}
