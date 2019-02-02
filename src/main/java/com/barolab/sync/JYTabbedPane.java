package com.barolab.sync;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

import com.barolab.gui.Widget;

public class JYTabbedPane extends Widget {

	private JTabbedPane tpan = new JTabbedPane();

	public JYTabbedPane(String name) {
		super(name);
	}
	
	@Override
	public Component getContent() {
		return tpan;
	}

	@Override
	public Widget add(Widget child, String constraints) {
		tpan.addTab(child.getName(),child.getContent());
		return null;
	}

	
	public void init(String name) {
		
//		JTabbedPane rightTab = null;
//		{
//			rightTab = new JTabbedPane();
//			addTabPanel("aaa", rightTab);
//			addTabPanel("bbb", rightTab);
//			addTabPanel("ccc", rightTab);
//		}
	}

	public void addTabPanel(String name, JTabbedPane tabbedPane) {
		JTextPane b = new JTextPane();
		b.setPreferredSize(new Dimension(200, 200));
		b.setText(name + "..." + name);
		tabbedPane.addTab(name, b);
	}


}
