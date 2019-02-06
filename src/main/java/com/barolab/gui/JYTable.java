package com.barolab.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class JYTable extends Widget {

	public JTable jtable = new JTable();
	JScrollPane jscrollPane = new JScrollPane(jtable);
	DefaultTableModel model = new DefaultTableModel();

	public JYTable(String name) {
		super(name);
		Vector<String> bb = new Vector<String>();
		bb.add("aa");
		bb.add("bb");
		bb.add("cc");
		model.setColumnIdentifiers(bb);
		jtable.setModel(model);
	//	jtable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	//	jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jtable.setAutoCreateColumnsFromModel(false);

		jtable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (arg0.getValueIsAdjusting()) {
					return;
				}
				System.out.println("valueChanged."+arg0.getFirstIndex());
				new JYParam(source) //
						.add("event", "tableSelected") //
						.add("rowNum", jtable.getSelectedRow()) //
						.add("colNum", jtable.getSelectedColumn()) //
						.send();
				// new JYParam(source).add("source", source).add("action", getName()).send();
			}
		});
	}

	@Override
	public Container getComponent() {
		return jscrollPane;
	}

	@Override
	public Widget setUpper(Widget upper, String constraints) {
		Component parent = upper.getComponent();
		if (parent instanceof JPanel) {
			((JPanel) parent).add(jscrollPane, constraints);
		}
		return this;
	}



	public void build(final JYController controller) {

	}

	public Widget columnWidths(int[] widths) {
		int index =0;
		for ( int width : widths) {
			jtable.getColumnModel().getColumn(index++).setPreferredWidth(width);
		}
		// TODO Auto-generated method stub
		return this;
	}

}
