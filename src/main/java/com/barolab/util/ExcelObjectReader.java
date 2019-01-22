package com.barolab.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.barolab.util.model.BeanAttribute;
import com.barolab.util.model.BeanClass;

import lombok.extern.java.Log;

@Log
public class ExcelObjectReader<T> extends ExcelObjectDefault {

	private DataFormatter dataFormatter = new DataFormatter();
	private List<BeanAttribute> columnAttributeList = new LinkedList<BeanAttribute>();
	Workbook workbook = null;

	public List<T> read(BeanClass beanClass, String sheetname, String filename)
			throws EncryptedDocumentException, InvalidFormatException, IOException {

		Sheet sheet = null;

		FileInputStream inputStream = new FileInputStream(filename);
		workbook = WorkbookFactory.create(inputStream);
		sheet = null;
		if (sheetname == null) {
			sheet = workbook.getSheetAt(0);
		} else {
			sheet = workbook.getSheet(sheetname);
		}

		/**
		 * Read ColumnAttribute
		 */
		Row row0 = sheet.getRow(0);
		for (Cell cell : row0) {
			String name = cell.getStringCellValue();
			BeanAttribute attr = beanClass.getAttribute(name);
			if (attr != null) {
				columnAttributeList.add(attr);
				attr.setXlsColIndex(cell.getColumnIndex());
			}
		}

		/**
		 * make value Object list
		 */
		List targetList = new LinkedList();
		for (Row row : sheet) { // Each Row
			if (row.getRowNum() == 0)
				continue;
			Object targetObject = null;
			try {
				targetObject = beanClass.getClazz().newInstance();
			} catch (InstantiationException | IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (BeanAttribute attr : columnAttributeList) { // Each Activated Attribute
				Cell cell = row.getCell(attr.getXlsColIndex());
				if (cell == null) {
				} else {
					try {
						attr.getBeanType().readXlsValue(targetObject, cell);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 
//					CellType type = cell.getCellTypeEnum();
//					try {
//						switch (type.getCode()) {
//						case 0: // numeric and date
//							if (attr.getType() == float.class) {
//								float v0 = (float) cell.getNumericCellValue();
//								attr.getSetter().invoke(targetObject, v0);
//							}
//							if (attr.getType() == Date.class) {
//								Date v0 = cell.getDateCellValue();
//								attr.getSetter().invoke(targetObject, v0);
//							}
//							break;
//						case 1: // String
//							String value = cell.getStringCellValue();
//							attr.getSetter().invoke(targetObject, value);
//							// log.warning("value.string=" + value + sPos);
//							// + ", sheet=" + sheet.getSheetName());
//							break;
//						case 3: // BLANK
//							break;
//						default:
//							System.out.println("[ERROR] type = " + type + " name=" + attr.getName());
//						}
//					} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				}
			}
			targetList.add(targetObject);
		}
		log.info("Read class(" + beanClass.getClazz().getSimpleName() + ") count=" + targetList.size() + ".");
		inputStream.close();
		return targetList;
	}
}
