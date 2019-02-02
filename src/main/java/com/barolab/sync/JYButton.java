package com.barolab.sync;

import java.awt.Component;

import javax.swing.JButton;

import com.barolab.gui.Widget;

public class JYButton extends Widget {

	JButton button = new JButton();

	public JYButton(String name) {
		super(name);
		button.setText(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Component getContent() {
		// TODO Auto-generated method stub
		return button;
	}

	@Override
	public Widget add(Widget child, String constraints) {
		// TODO Auto-generated method stub
		return null;
	}

}
