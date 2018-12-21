package com.barolab.util.model;

import java.util.List;

import com.barolab.util.TableMap;

import Platform.DashConsole.OV_TimeEntry;

public abstract class ListUtils {
	
	protected BeanClass bClass;


	public abstract List<?> getList();

	public void sort(String... keys) {
		bClass.sort(getList(), keys);
	}
	
	public void subsort(List<?> list , String... keys) {
		bClass.sort(list, keys);
		
	}

	public Object find(String atrName, Object value) {
		Object element = bClass.matchAtrValue(getList(), atrName, value);
		return element;
	}

	public TableMap toTableMap(String rowKeyName, String colKeyName) {
		return bClass.toTableMap(getList(), rowKeyName, colKeyName);
	}

	public void removeElements(  String atrName, String... args) {
		bClass.removeElement(getList(), atrName, args);
	}
	
	//*******************************
	//* Excel Support
	//*******************************
	
	public List<?> readExcel(String filename) {
		return bClass.readExcel(filename, "sheet0");
	}
	
	public void writeExcel(String filename) {
		bClass.writeExcel(filename, "sheet0", getList());
	}

}
