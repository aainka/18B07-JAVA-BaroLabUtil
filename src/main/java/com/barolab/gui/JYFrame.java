package com.barolab.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class JYFrame {

	JFrame frame = new JFrame();

	public JYFrame(String title) {
		frame.setTitle(title);
		frame.setPreferredSize(new Dimension(1000, 600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.getContentPane().setLayout(new BorderLayout());
//		frame.getContentPane().add(leftPanel, BorderLayout.WEST);
//		frame.getContentPane().add(rightTab, BorderLayout.CENTER);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getPreferredSize();
//		if (frameSize.height > screenSize.height) {
//			frameSize.height = screenSize.height;
//		}
//		if (frameSize.width > screenSize.width) {
//			frameSize.width = screenSize.width;
//		}
		frame.setLocation(screenSize.width / 2 - (int)frameSize.getWidth() / 2, screenSize.height / 2 - (int)frameSize.getHeight() / 2);
		frame.setResizable(true);
		frame.setVisible(true);
	}

	public JYPanel addPane(String name, String position) {
		JYPanel join = new JYPanel();
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(join.panel, position);
		join.init(name);
		frame.pack();
		return join;
	}

}
