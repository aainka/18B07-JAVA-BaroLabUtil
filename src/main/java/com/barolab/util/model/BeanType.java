package com.barolab.util.model;

import java.lang.reflect.InvocationTargetException;

import org.apache.poi.ss.usermodel.Cell;

public abstract class BeanType {

	public BeanAttribute bAttr;

	public abstract int compareTargetAtr(BeanAttribute atr, Object target0, Object target1);

	public abstract int compareValue(BeanAttribute atr, Object value0, Object value1);

	public abstract void writeXlsValue(Object targetObject, Cell cell)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException;

	public abstract void readXlsValue(Object targetObject, Cell cell)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException;


}
