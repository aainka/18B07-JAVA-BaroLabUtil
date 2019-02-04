package com.barolab.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class JYPanel extends Widget {

	JPanel panel = new JPanel();

	public JYPanel(String name) {
		super(name);
		panel.setLayout(new BorderLayout());
		Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		TitledBorder title = BorderFactory.createTitledBorder(raisedetched, name);
		panel.setBorder(title);

	}

	@Override
	public Container getComponent() {
		return panel;
	}

	@Override
	public Widget setUpper(Widget upper, String constraints) {
		Component parent = upper.getComponent();
		if (parent instanceof JComponent) {
			// System.out.println("parent is " + parent.getClass() + " from " + getName());
			((JComponent) parent).add(panel, constraints);
		}
		if (parent instanceof JTabbedPane) {
			((JTabbedPane) parent).addTab(this.getName(), panel);
		}
		return this;
	}

	public Widget setSize(int width, int height) {
		panel.setPreferredSize(new Dimension(width, height));
		return this;
	}

}
