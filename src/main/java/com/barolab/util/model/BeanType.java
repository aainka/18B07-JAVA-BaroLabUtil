package com.barolab.util.model;

import org.apache.poi.ss.usermodel.Cell;

public abstract class BeanType {
	public abstract int compare(BeanAttribute atr, Object target0, Object target1) ;

	protected abstract void writeExcelCell(BeanAttribute atr, Object recObject, Cell cell);
}
