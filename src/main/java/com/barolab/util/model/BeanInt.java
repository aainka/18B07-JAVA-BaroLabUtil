package com.barolab.util.model;

import java.lang.reflect.InvocationTargetException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

public class BeanInt extends BeanType {

	Class type = int.class;
	static Object value = null;

	public BeanInt(BeanAttribute bAttr) {
		this.bAttr = bAttr;
	}

	public static Integer getValue(BeanAttribute atr, Object target) {
		try {
			value = atr.getter.invoke(target);
			return (Integer) atr.getter.invoke(target);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			if (!atr.beanClass.getClazz().equals(target.getClass())) {
				System.out.println("Target Class is not matched. input class = " + target.getClass());
			}
			e.printStackTrace();
		}
		return (int) 0;
	}

	public int compareTargetAtr(BeanAttribute atr, Object target0, Object target1) {
		int s0 = getValue(atr, target0);
		int s1 = getValue(atr, target1);
		return s0 - s1;
	}

	@Override
	public int compareValue(BeanAttribute atr, Object value0, Object value1) {
		int s0 = (int) value0;
		int s1 = (int) value1;
		return s0 - s1;
	}

	@Override
	public void writeXlsValue(  Object recObject, Cell cell) {
		try {
			int value = (int) bAttr.getGetter().invoke(recObject, null);
			cell.setCellValue(value);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void readXlsValue(Object targetObject, Cell cell) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		CellType type = cell.getCellTypeEnum();
		switch (type.getCode()) {
		case 0: // numeric and date
			int v0 = (int) cell.getNumericCellValue();
			bAttr.getSetter().invoke(targetObject, v0);
			break;
//		case 1: // String
//			String value = cell.getStringCellValue();
//			bAttr.getSetter().invoke(targetObject, value);
//			break;
		case 3: // BLANK
			break;
		default:
			System.out.println("[ERROR] type = " + type + " name=" + bAttr.getName());
		}
	}

}
