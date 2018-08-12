package com.barolab.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
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

public class ExcelObjectReader extends ExcelObjectDefault {

	private Field[] vfields = new Field[100];
	private DataFormatter dataFormatter = new DataFormatter();

	public void init() {

	}
	
	public List<?> read(Class clazz, String sheetname, String filename){
		try {
			return read0(clazz,sheetname, filename);
		} catch (EncryptedDocumentException | InvalidFormatException | InstantiationException | IllegalAccessException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<?> read0(Class clazz, String sheetname, String filename) throws EncryptedDocumentException,
			InvalidFormatException, IOException, InstantiationException, IllegalAccessException {

		this.clazz = clazz;
		Sheet sheet = null;
		FileInputStream fileInputStream = new FileInputStream(filename);
		Workbook workbook = WorkbookFactory.create(fileInputStream);
		if (sheetname == null) {
			sheet = workbook.getSheetAt(0);
		} else {
			sheet = workbook.getSheet(sheetname);
		}

		/**
		 * make attribute list
		 */

		Row row0 = sheet.getRow(0);
		for (Cell cell : row0) {
			String name = cell.getStringCellValue();
			try {
				Field f = clazz.getField(name);
				vfields[cell.getColumnIndex()] = f;
			} catch (NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
		}

		/**
		 * make value Object list
		 */
		List list = new LinkedList();
		for (Row row : sheet) {
			if (row.getRowNum() == 0)
				continue;

			// OV_Issue x = new OV_Issue();
			Object valueObject = clazz.newInstance();
			for (Cell cell : row) {
				CellType type = cell.getCellTypeEnum();
				Field f = vfields[cell.getColumnIndex()];
				try {
					switch (type.getCode()) {
					case 0: // numeric and date
						if (f.getType() == int.class) {
							int v0 = (int) cell.getNumericCellValue();
							f.set(valueObject, v0);
						}
						if (f.getType() == Date.class) {
							Date v0 = cell.getDateCellValue();
							f.set(valueObject, v0);
						}
						break;
					case 1: // String
						String value = cell.getStringCellValue();
						f.set(valueObject, value);
						break;
					case 3: // BLANK
						break;
					default:
						System.out.println("[ERROR] type = " + type + " name=" + f.getName());
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// vfields[cell.getColumnIndex()].set(x, value);
				// String cellValue = dataFormatter.formatCellValue(cell);
				// System.out.print(cellValue + "\t");
			}
			list.add(valueObject);
		}
		System.out.println(" read.count = " + list.size());
		return list;
	}
}
