package com.barolab.util.model;

import java.lang.reflect.InvocationTargetException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

public class BeanFloat extends BeanType {

	Class type = Float.class;

	public BeanFloat(BeanAttribute bAttr) {
		this.bAttr = bAttr;
	}

	public static Float getValue(BeanAttribute atr, Object target) {
		try {
			return (Float) atr.getter.invoke(target);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return (float) 0;
	}

	public int compareTargetAtr(BeanAttribute atr, Object target0, Object target1) {
		Float s0 = getValue(atr, target0);
		Float s1 = getValue(atr, target1);
		return s0.compareTo(s1);
	}

	@Override
	public int compareValue(BeanAttribute atr, Object value0, Object value1) {
		float a = (float) value0;
		float b = (float) value1;
		return (int) ((int) a - b);
	}

	@Override
	public void writeXlsValue(Object targetObject, Cell cell)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		float value = (float) bAttr.getGetter().invoke(targetObject, null);
		cell.setCellValue(value);
	}

	@Override
	public void readXlsValue(Object targetObject, Cell cell)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		CellType type = cell.getCellTypeEnum();
		switch (type.getCode()) {
		case 0: // numeric and date
			float v0 = (float) cell.getNumericCellValue();
			bAttr.getSetter().invoke(targetObject, v0);
		case 1: // String
			String value = cell.getStringCellValue();
			bAttr.getSetter().invoke(targetObject, value);
			// log.warning("value.string=" + value + sPos);
			// + ", sheet=" + sheet.getSheetName());
			break;
		case 3: // BLANK
			break;
		default:
			System.out.println("[ERROR] type = " + type + " name=" + bAttr.getName());
		}

	}
}
