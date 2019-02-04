package com.barolab.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

public class JYTabbedPane extends Widget {

	private JTabbedPane tpan = new JTabbedPane();

	public JYTabbedPane(String name) {
		super(name);
	}

	@Override
	public Container getComponent() {
		return tpan;
	}

	public void addTabPanel(String name, JTabbedPane tabbedPane) {
		JTextPane b = new JTextPane();
		b.setPreferredSize(new Dimension(200, 200));
		b.setText(name + "..." + name);
		tabbedPane.addTab(name, b);
	}

	@Override
	public Widget setUpper(Widget upper, String constraints) {
		Component parent = upper.getComponent();
		if (parent instanceof JPanel) {
			((JPanel) parent).add(tpan, constraints);
		}
		return this;
	}





}
