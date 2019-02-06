package com.barolab.sync;

import java.awt.BorderLayout;

import javax.swing.table.DefaultTableModel;

import com.barolab.gui.JYButton;
import com.barolab.gui.JYController;
import com.barolab.gui.JYFrame;
import com.barolab.gui.JYPanel;
import com.barolab.gui.JYParam;
import com.barolab.gui.JYTabbedPane;
import com.barolab.gui.JYTable;
import com.barolab.gui.JYTextPane;
import com.barolab.gui.Widget;

public class SyncGui implements JYController {

	JYFrame frame;

	public void build() {
		frame = new JYFrame("2019 SynGui / SmartBuilder");
		frame.setController(this);
		frame.size(1500, 900);
		Widget pan = frame.add(new JYPanel("PathView"), BorderLayout.WEST);
		{
			pan.add(new JYTable("table").columnWidths( new int[] {300,100,50}).size(700, 100), BorderLayout.CENTER);
			Widget box = pan.add(new JYPanel("ControlButton").size(100, 100), BorderLayout.SOUTH);
			{
				box.add(new JYButton("scan"), null);
				box.add(new JYButton("원격전송"), null);
				box.add(new JYButton("action"), null);
			}

		}
		Widget pan2 = frame.add(new JYPanel("EditView"), BorderLayout.CENTER);
		{
			Widget tpan = pan2.add(new JYTabbedPane("aa"), BorderLayout.CENTER);
			tpan.add(new JYPanel("aaa"), null).add(new JYTextPane("Editor"), BorderLayout.CENTER);
			tpan.add(new JYPanel("aa1"), null);
			tpan.add(new JYPanel("aa2"), null);
			tpan.add(new JYPanel("aa3"), null);
		}
	}

	// DATA Area

	public DefaultTableModel getTableModel() {
		Sync sn = new Sync();
		return sn.getTableModel();
	}

	Sync sync = new Sync();

	@Override
	public void event(JYParam param) {
		System.out.println("Receive Event from " + param);
		if (param.match("event", "tableSelected")) {
			int rowNum = (int) param.get("rowNum");
			JYTextPane textPane = (JYTextPane) Widget.findWidget(frame, "Editor");
			if ( rowNum >=0 ) {
			textPane.textPane.setText(sync.scanList.get(rowNum).getSrc().getText_in_file());
			}

		}
		if (param.match("action", "scan")) {
			JYTable wtable = (JYTable) Widget.findWidget(frame, "table");
			wtable.jtable.setModel(sync.getTableModel());
			wtable.jtable.updateUI();
		//	new JYConfirm(frame).setMessage("Scan OK").show();
		}
		if (param.match("action", "action")) {
			JYTable wtable = (JYTable) Widget.findWidget(frame, "table");
			int[] rownum = wtable.jtable.getSelectedRows();
			// System.out.println("rownum is "+rownum[0]);

			JYTextPane textPane = (JYTextPane) Widget.findWidget(frame, "Editor");
			textPane.textPane.setText("xxx");
		}
	
		if (param.match("action", "원격전송")) {
			JYTable wtable = (JYTable) Widget.findWidget(frame, "table");
			int[] rownum = wtable.jtable.getSelectedRows();
			sync.updateSelectedSync(rownum);
		}
	}

	public static void main(String[] args) {
		new SyncGui().build();
	}

}
