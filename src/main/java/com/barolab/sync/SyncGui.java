package com.barolab.sync;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;

import com.barolab.gui.JYFrame;
import com.barolab.gui.JYPanel;
import com.barolab.gui.JYTable;
import com.barolab.gui.Widget;

public class SyncGui {

	public void build() {
		JYFrame frame = new JYFrame("2019 SynGui / SmartBuilder");
		JYPanel pan = frame.addPane("SyncTablePanel", BorderLayout.WEST);
		{
			pan.add(new JYTable("table"), BorderLayout.CENTER);
			Widget box = pan.add(new JYPanel("ControlButton").setSize(100, 100), BorderLayout.SOUTH);
			box.add(new JYButton("xxx"),null);
		}
		JYPanel pan2 = frame.addPane("EditorTabbed", BorderLayout.CENTER);
		{
			Widget tpan = pan2.add(new JYTabbedPane("aa"), BorderLayout.CENTER);
			tpan.add(new JYPanel("aaa"),null);
			tpan.add(new JYPanel("aa1"),null);
			tpan.add(new JYPanel("aa2"),null);
			tpan.add(new JYPanel("aa3"),null);
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
