package com.barolab.util.model;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;

public class BeanDate extends BeanType {

	public static java.util.Date getValue(BeanAttribute atr, Object target) {
		try {
			return (Date) atr.getter.invoke(target);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int compare(BeanAttribute atr, Object target0, Object target1) {
		java.util.Date s0 = getValue(atr, target0);
		java.util.Date s1 = getValue(atr, target1);
		return s0.compareTo(s1);
	}

	@Override
	protected void writeExcelCell(BeanAttribute atr, Object recObject, Cell cell) {
		try {
			java.util.Date value = (Date) atr.getGetter().invoke(recObject, null);
			cell.setCellValue(value);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

 
}
