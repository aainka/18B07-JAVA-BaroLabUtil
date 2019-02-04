package com.barolab.gui;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import lombok.Data;

@Data
public class JYTextPane extends Widget {

	public JTextPane textPane = new JTextPane();
	JScrollPane jscrollPane = new JScrollPane(textPane);

	public JYTextPane(String name) {
		super(name);
	}

	@Override
	public Container getComponent() {
		return jscrollPane;
	}

	@Override
	public Widget setUpper(Widget upper, String constraints) {
		Component parent = upper.getComponent();
		if (parent instanceof JPanel) {
			((JPanel) parent).add(getComponent(), constraints);
		}
		return this;
	}
}
