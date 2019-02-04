package com.barolab.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;

public class JYButton extends Widget {

	JButton button = new JButton();
	Object source = this;

	public JYButton(String name) {
		super(name);
		button.setText(name);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new JYParam(source).add("source", source).add("action", getName()).send();
			}
		});
		// TODO Auto-generated constructor stub
	}

	@Override
	public Container getComponent() {
		// TODO Auto-generated method stub
		return button;
	}

	@Override
	public Widget setUpper(Widget upper, String constraints) {
		Component parent = upper.getComponent();
		if (parent instanceof JComponent) {
			JComponent parent2 = (JComponent) parent;
			if (!(parent2.getLayout() instanceof FlowLayout)) {
				parent2.setLayout(new FlowLayout());
			}
			parent2.add(button);
		}
		return this;
	}





}
