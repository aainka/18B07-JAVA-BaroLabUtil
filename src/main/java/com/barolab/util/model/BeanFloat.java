package com.barolab.util.model;

import java.lang.reflect.InvocationTargetException;

import org.apache.poi.ss.usermodel.Cell;

public class BeanFloat extends BeanType {

	Class type = Float.class;

	public static Float getValue(BeanAttribute atr, Object target) {
		try {
			return (Float) atr.getter.invoke(target);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return (float) 0;
	}

	public int compare(BeanAttribute atr, Object target0, Object target1) {
		Float s0 = getValue(atr, target0);
		Float s1 = getValue(atr, target1);
		return s0.compareTo(s1);
	}

	@Override
	protected void writeExcelCell(BeanAttribute atr, Object recObject, Cell cell) {
		try {
			float value = (float) atr.getGetter().invoke(recObject, null);
			cell.setCellValue(value);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
