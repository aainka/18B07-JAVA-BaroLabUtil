package com.barolab.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class JYPanel extends Widget {

	JPanel panel = new JPanel();
	// String name = null;

	public JYPanel(String name) {
		super(name);
		Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		TitledBorder title = BorderFactory.createTitledBorder(raisedetched, name);
		panel.setBorder(title);

		panel.setLayout(new BorderLayout());
	}
	
	@Override
	public Component getContent() {
		return panel;
	}

	public Widget add(Widget child, String position) {
		panel.add(child.getContent(),position);
		return child;
	}

	public void addTableView(String string) {
		JYTable table = new JYTable("xx");
		table.build(null);
		panel.add(table.jscrollPane);

	}

	
	public void init(String name) {
		Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		TitledBorder title = BorderFactory.createTitledBorder(raisedetched, name);
		panel.setBorder(title);
		// panel.setPreferredSize(new Dimension(300, 100));
	}

	public Widget setSize(int i, int j) {
		panel.setPreferredSize(new Dimension(100,100));
		return this;
	}



}
