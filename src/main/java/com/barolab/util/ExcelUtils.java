package com.barolab.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFFont;

import com.barolab.util.model.BeanAttribute;
import com.barolab.util.model.BeanClass;

import lombok.extern.java.Log;

/**
 * 추가 : Featue 기존 액셀의 필드를 수정하기는 기능 각 데이타는 Row 번호를 기억한다. 수정 API를 제공한다.
 * 
 * @author ejaejeo
 *
 * @param <T>
 */
@Log
public class ExcelUtils<T> extends ExcelObjectDefault {

 	private DataFormatter dataFormatter = new DataFormatter();
	private List<BeanAttribute> columnAttributeList = new LinkedList<BeanAttribute>();
	private BeanClass bClass = null;
	private Workbook workbook = null;
	private Sheet sheet = null;
	private String filename = null;
	private Map<Object, Integer> map = new HashMap<Object, Integer>();
	

	public List<T> read(BeanClass beanClass, String sheetname, String filename)
			throws EncryptedDocumentException, InvalidFormatException, IOException {

		this.bClass = beanClass;
		this.filename = filename;

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
			if (row.getRowNum() == 0) { // first line skip (headline)
				continue;
			}
			Object targetObject = null;
			try {
				targetObject = beanClass.getClazz().newInstance();
				targetList.add(targetObject);
				map.put(targetObject, row.getRowNum());
			} catch (InstantiationException | IllegalAccessException e1) {
				e1.printStackTrace();
			}
			for (BeanAttribute attr : columnAttributeList) { // Each Activated Attribute
				Cell cell = row.getCell(attr.getXlsColIndex());
				if (cell != null) {
					try {
						attr.getBeanType().readXlsValue(targetObject, cell);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
		log.info("Read class(" + beanClass.getClazz().getSimpleName() + ") count=" + targetList.size() + ".");
		inputStream.close();
		return targetList;
	}
	
	public void append(List<T> list) throws EncryptedDocumentException, InvalidFormatException, IOException {
		if (list.size() > 0) {
			int rowCount = sheet.getLastRowNum();
			for (T anObject : list) {
				Row row = sheet.createRow(++rowCount);
				for (BeanAttribute bAttr : columnAttributeList) {
					Cell cell = row.createCell(bAttr.getXlsColIndex());
					try {
						bAttr.getBeanType().writeXlsValue(anObject, cell);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		try {
			FileOutputStream outputStream = new FileOutputStream(filename);
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void writeValue(Object targetObject, String atrName, Object value) {
		int rownum = map.get(targetObject);
		// log.info("rownum = "+rownum+", atrName= "+atrName+", value="+value);
		int cellnum = -1;
		for ( BeanAttribute bAttr : this.columnAttributeList) {
			if ( atrName.equals( bAttr.getName() )){
				Cell cell = sheet.getRow(rownum).getCell(bAttr.getXlsColIndex());
				if ( cell == null) {
					cell = sheet.getRow(rownum).createCell(bAttr.getXlsColIndex());
				}
				try {
					bAttr.setValue(targetObject, value);
					bAttr.getBeanType().writeXlsValue(targetObject, cell);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void setHyperlink(Cell cell, String address, int value) {
		org.apache.poi.ss.usermodel.Hyperlink href = workbook.getCreationHelper().createHyperlink(HyperlinkType.URL);
		href.setAddress(address);
		cell.setHyperlink(href);
		cell.setCellValue(value);
		cell.setCellStyle(cellStyleHref);
	}

	CellStyle cellStyleHref;

	public void addRedmineNumberLinke(int cellnum) {

		XSSFFont hlink_font = (XSSFFont) workbook.createFont();
		hlink_font.setUnderline(XSSFFont.U_SINGLE);
		hlink_font.setColor(IndexedColors.BLUE.getIndex());
		cellStyleHref = workbook.createCellStyle();
		cellStyleHref.setFont(hlink_font);

		for (Row row : sheet) {
			Cell cell = row.getCell(cellnum);
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				int value = (int) cell.getNumericCellValue();
				String url = "http://redmine.ericssonlg.com/redmine/issues/" + value;
				setHyperlink(cell, url, value);
				break;
			case Cell.CELL_TYPE_STRING:
				System.out.println("Value.s = " + cell.getRowIndex());
				break;
			}
		}
	}
}
