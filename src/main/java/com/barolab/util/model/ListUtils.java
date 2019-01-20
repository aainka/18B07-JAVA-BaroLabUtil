package com.barolab.util.model;

import java.util.List;

import com.barolab.util.TableMap;

public abstract class ListUtils<T> {

	protected BeanClass bClass;

	public abstract List<T> getList();

	public abstract void setList(List<T> list);
	public abstract List<T> load(boolean cacheMode);
	public abstract String pivot(String opname, List<T> list) ;

	public void sort(String... keys) {
		bClass.sort(getList(), keys);
	}

	public void subsort(List<?> list, String... keys) {
		bClass.sort(list, keys);

	}

	public T find(String atrName, Object value) {
		Object element = bClass.matchAtrValue(getList(), atrName, value);
		return (T) element;
	}

	public TableMap toTableMap(String rowKeyName, String colKeyName) {
		return bClass.toTableMap(getList(), rowKeyName, colKeyName);
	}

	public void removeElements(String atrName, String... args) {
		bClass.removeElement(getList(), atrName, args);
	}

	// *******************************
	// * Excel Support
	// *******************************

	public List<?> readExcel(String filename, String sheetName) {
		if ( sheetName == null) {
			sheetName = "Sheet1";
		}
		return bClass.readExcel(filename, sheetName);
	}
	
	public void writeExcel(String filename) {
		bClass.writeExcel(filename, "Sheet1", getList());
	}
	
	public void writeExcel(String filename, List<T> plist) {
		bClass.writeExcel(filename, "Sheet1", plist);
	}

	public static void copyTo(String cmValues, Object obj1, Object obj2) {
		String[] atrNames = cmValues.split(",");
		BeanClass bClass1 = BeanClass.getInstance(obj1.getClass());
		BeanClass bClass2 = BeanClass.getInstance(obj1.getClass());
		for (String atrName : atrNames) {
			Object value = bClass1.getAttribute(atrName).getValue(obj1);
			bClass2.getAttribute(atrName).setValue(obj2, value);
		}
	}

 

}
