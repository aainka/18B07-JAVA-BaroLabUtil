package com.barolab.util.model;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

public class BeanDate extends BeanType {

	public BeanDate(BeanAttribute bAttr) {
		this.bAttr = bAttr;
	}

	public static java.util.Date getValue(BeanAttribute atr, Object target) {
		try {
			return (Date) atr.getter.invoke(target);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int compareTargetAtr(BeanAttribute atr, Object target0, Object target1) {
		java.util.Date s0 = getValue(atr, target0);
		java.util.Date s1 = getValue(atr, target1);
		return s0.compareTo(s1);
	}

	@Override
	public int compareValue(BeanAttribute atr, Object value0, Object value1) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	// ########################################################
	// ## XLS
	// ########################################################

	@Override
	public void writeXlsValue(Object targetObject, Cell cell)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		java.util.Date value = (Date) bAttr.getGetter().invoke(targetObject, null);
		cell.setCellValue(value);
	}

	@Override
	public void readXlsValue(Object targetObject, Cell cell)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		CellType type = cell.getCellTypeEnum();
		switch (type.getCode()) {
		case 0: // numeric and date
			Date v0 = cell.getDateCellValue();
			bAttr.getSetter().invoke(targetObject, v0);
			break;
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
