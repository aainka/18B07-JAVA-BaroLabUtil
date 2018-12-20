package com.barolab.util.model;

import java.lang.reflect.InvocationTargetException;

import org.apache.poi.ss.usermodel.Cell;

import lombok.extern.java.Log;

public class BeanInt extends BeanType {
	
	Class type = int.class;
	static Object value = null;
	
	public static Integer getValue(BeanAttribute atr, Object target) {
		try {
			  value = atr.getter.invoke(target);
			return (Integer) atr.getter.invoke(target);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			if ( !atr.beanClass.getClazz().equals(target.getClass()) ) {
				System.out.println("Target Class is not matched. input class = "+target.getClass());
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
	protected void writeExcelCell(BeanAttribute atr, Object recObject, Cell cell) {
		try {
			int value = (int) atr.getGetter().invoke(recObject, null);
			cell.setCellValue(value);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	 
}
