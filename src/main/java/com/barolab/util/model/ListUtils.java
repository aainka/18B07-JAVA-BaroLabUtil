package com.barolab.util.model;

import java.util.List;

import com.barolab.util.TableMap;

import Platform.DashConsole.OV_Issue;
import Platform.DashConsole.OV_LCM;
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
		return bClass.readExcel(filename, "Sheet1");
	}
	
	public void writeExcel(String filename) {
		bClass.writeExcel(filename, "Sheet1", getList());
	}

	public static void copyTo(String cmValues, Object obj1, Object obj2) {
		String[] atrNames = cmValues.split(",");
		BeanClass bClass1 = BeanClass.getInstance(obj1.getClass());
		BeanClass bClass2 = BeanClass.getInstance(obj1.getClass());
		for ( String atrName : atrNames) {
			Object value = bClass1.getAttribute(atrName).getValue(obj1);
			bClass2.getAttribute(atrName).setValue(obj2, value);
		}
	}

}
