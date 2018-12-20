package com.barolab.util.model;

import org.apache.poi.ss.usermodel.Cell;

public abstract class BeanType {
	
	public abstract int compareTargetAtr(BeanAttribute atr, Object target0, Object target1) ;
 	public abstract int compareValue(BeanAttribute atr, Object value0, Object value1) ;

	protected abstract void writeExcelCell(BeanAttribute atr, Object recObject, Cell cell);
}
