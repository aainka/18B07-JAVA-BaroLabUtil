package com.barolab.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class JYPanel implements Widget {
	
	JPanel panel = new JPanel();

	public void addTableView(String string) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(String name) {
		panel.setPreferredSize(new Dimension(100,100));
		JTextArea textArea = new JTextArea(name);
		panel.add(textArea);
	 	panel.setBackground(Color.BLUE);
	}

}
