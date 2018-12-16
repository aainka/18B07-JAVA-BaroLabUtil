package com.barolab.util.model;

import java.lang.reflect.InvocationTargetException;

import org.apache.poi.ss.usermodel.Cell;

public class BeanString extends BeanType   {

	public static String getValue(BeanAttribute atr, Object target) {
		try {
			return (String) atr.getter.invoke(target);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int compare(BeanAttribute atr, Object target0, Object target1) {
		String s0 = getValue(atr, target0);
		String s1 = getValue(atr, target1);
		return s0.compareTo(s1);
	}

	@Override
	protected void writeExcelCell(BeanAttribute atr, Object recObject, Cell cell) {
		try {
			String value = (String) atr.getGetter().invoke(recObject, null);
			cell.setCellValue(value);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

 
}
