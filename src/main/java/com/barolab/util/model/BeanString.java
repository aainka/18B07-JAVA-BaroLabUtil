package com.barolab.util.model;

import java.lang.reflect.InvocationTargetException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

public class BeanString extends BeanType {

	public BeanString(BeanAttribute bAttr) {
		this.bAttr = bAttr;
	}

	public static String getValue(BeanAttribute atr, Object target) {
		try {
			return (String) atr.getter.invoke(target);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int compareTargetAtr(BeanAttribute atr, Object target0, Object target1) {
		String s0 = getValue(atr, target0);
		String s1 = getValue(atr, target1);

		if (s0 == null && s1 == null) {
			return 0;
		}
		if (s0 == null) {
			return 1;
		}
		if (s1 == null) {
			return -1;
		}
		return s0.compareTo(s1);
	}

	@Override
	public int compareValue(BeanAttribute atr, Object value0, Object value1) {
		String s0 = value0.toString();
		String s1 = value1.toString();
		return s0.compareTo(s1);
	}

	@Override
	public void writeXlsValue(Object targetObject, Cell cell) {
		try {
			String value = (String) bAttr.getGetter().invoke(targetObject, null);
			cell.setCellValue(value);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void readXlsValue(Object targetObject, Cell cell)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		CellType type = cell.getCellTypeEnum();
		switch (type.getCode()) {
		case 0: // numeric and date
			int v0 = (int) cell.getNumericCellValue();
			String s = "" + v0;
			bAttr.getSetter().invoke(targetObject, s);
			break;
		case 1: // String
			String value = cell.getStringCellValue();
			bAttr.getSetter().invoke(targetObject, value);
			break;
		case 3: // BLANK
			break;
		default:
			System.out.println("[ERROR] type = " + type + " name=" + bAttr.getName());
		}

	}

}
