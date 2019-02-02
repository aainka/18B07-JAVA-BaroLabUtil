package com.barolab.gui;

import java.awt.Component;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class JYTable extends Widget {

	private JTable table = new JTable();
	JScrollPane jscrollPane = new JScrollPane(table);
	DefaultTableModel model = new DefaultTableModel();

	public JYTable(String name) {
		super(name);
		Vector<String> bb = new Vector<String>();
		bb.add("aa");
		bb.add("bb");
		bb.add("cc");
	 	model.setColumnIdentifiers(bb);
	 	table.setModel(model);
	}
	
	@Override
	public Component getContent() {
		// TODO Auto-generated method stub
		return jscrollPane;
	}
	
	@Override
	public Widget add(Widget child, String constraints) {
		// TODO Auto-generated method stub
		return null;
	}


	
	public void build(final JYController controller) {
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				JYParam param = new JYParam();
				param.put("source",this);
				param.put("event","tableSelected");
				param.put("rowNum",table.getSelectedRow());
				param.put("colNum",table.getSelectedColumn());
				controller.event(param);
			} 
		});
	
	}

 
	public void init(String name) {
		// TODO Auto-generated method stub
		
	}







}
