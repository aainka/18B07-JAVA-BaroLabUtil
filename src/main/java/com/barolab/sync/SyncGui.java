package com.barolab.sync;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;

public class SyncGui {

	public void build() {
		JFrame frame = new JFrame("2019 SynGui / SmartBuilder");
		{
			JPanel leftPanel = new JPanel();
			{
				leftPanel.setPreferredSize(new Dimension(300, 500));
				JTable jt = new JTable();
				JScrollPane js = new JScrollPane(jt);
				leftPanel.setLayout(new BorderLayout());
				leftPanel.add(js);
			}
			JTabbedPane rightTab = null;
			{
				rightTab = new JTabbedPane();
				JTextPane b = new JTextPane();
				b.setPreferredSize(new Dimension(200, 200));
				b.setText("bbbbb");
				rightTab.addTab("aaa", b);
				rightTab.addTab("bb", b);
				rightTab.addTab("cc", b);
				rightTab.addTab("dd", b);
			}
			frame.setSize(600, 600);
			frame.getContentPane().setLayout(new BorderLayout());
			frame.getContentPane().add(BorderLayout.WEST, leftPanel);
			frame.getContentPane().add(BorderLayout.CENTER, rightTab);
			frame.show(true);
		}
		
		
		JPanel x = new JPanel();
		x.setLayout(new BorderLayout());
		

		JTextPane a = new JTextPane();

		a.setText("aaaaa");
	

		// Frame().add(Panel("ComparePanel").add(TextEditor("LEFT"),
		// TextEditor("RIGHT"));
	}

	public static void main(String[] args) {
		new SyncGui().build();

	}

}
