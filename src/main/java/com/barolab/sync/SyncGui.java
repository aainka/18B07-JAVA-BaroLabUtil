package com.barolab.sync;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;

import com.barolab.gui.JYFrame;
import com.barolab.gui.JYPanel;

public class SyncGui {

	public void build() {
		JYFrame frame = new JYFrame("2019 SynGui / SmartBuilder");
		JYPanel pan = frame.addPane("left", BorderLayout.CENTER);
		 frame.addPane("right", BorderLayout.EAST);
		{
			pan.addTableView("syncTable");
		}
//		{
//			JPanel leftPanel = new JPanel();
//			leftPanel.setPreferredSize(new Dimension(700, 500));
//			leftPanel.setLayout(new BorderLayout());
//			{
//				leftPanel.add(new JLabel("sync list"), BorderLayout.NORTH);
//
//			
//				leftPanel.add(JYTable.create("syncTable"), BorderLayout.CENTER);
//
//				JPanel pan = new JPanel();
//				pan.setLayout(new FlowLayout());
//				pan.add(new JButton("set"));
//
//				leftPanel.add(pan, BorderLayout.SOUTH);
//			}
//			JTabbedPane rightTab = null;
//			{
//				rightTab = new JTabbedPane();
//				addTabPanel("aaa", rightTab);
//				addTabPanel("bbb", rightTab);
//				addTabPanel("ccc", rightTab);
//			}

//			
//			
//		}
	}

	public void addTabPanel(String name, JTabbedPane tabbedPane) {
		JTextPane b = new JTextPane();
		b.setPreferredSize(new Dimension(200, 200));
		b.setText(name + "..." + name);
		tabbedPane.addTab(name, b);
	}

	public void tableSelected() {
		// tableModel에서 데이타를 축출하여
		// tabPane의 TextEditor로 전달한다.
	}

	// DATA Area

	public DefaultTableModel getTableModel() {
		Sync sn = new Sync();
		return sn.getTableModel();
//		String[] title = { "s1", "s2", "s3" };
//		Object[][] contents = { { "a", "b", "c" } };
//		return new DefaultTableModel(contents, title);
	}

	public static void main(String[] args) {
		new SyncGui().build();

	}

}
