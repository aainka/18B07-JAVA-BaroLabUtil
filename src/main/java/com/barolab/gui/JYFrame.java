package com.barolab.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class JYFrame extends Widget {

	JFrame frame = new JFrame();

	public JYFrame(String name) {
		super(name);
		frame.setTitle(name);
		frame.setPreferredSize(new Dimension(1500, 900));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.getContentPane().setLayout(new BorderLayout());

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getPreferredSize();
//		if (frameSize.height > screenSize.height) {
//			frameSize.height = screenSize.height;
//		}
//		if (frameSize.width > screenSize.width) {
//			frameSize.width = screenSize.width;
//		}
		frame.setLocation(screenSize.width / 2 - (int) frameSize.getWidth() / 2,
				screenSize.height / 2 - (int) frameSize.getHeight() / 2);
		frame.setResizable(true);
		frame.pack();
		frame.setVisible(true);
	}
	@Override
	public Container getComponent() {
		return frame.getContentPane();
	}
	
	public Widget size(int width, int height) {
		frame.setPreferredSize(new Dimension(width, height));
		frame.pack();
		return this;
	}
	
	@Override
	public Widget setUpper(Widget upper, String constraints) {
		// TODO Auto-generated method stub
		return null;
	}



}
