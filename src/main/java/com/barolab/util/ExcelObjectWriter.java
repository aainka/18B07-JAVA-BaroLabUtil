package com.barolab.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.barolab.util.model.BeanAttribute;

public abstract class ExcelObjectWriter extends ExcelObjectDefault {

	public abstract int format(Cell cell, BeanAttribute attr, Object value);

	protected static final int FORMAT_YET = 0;
	protected static final int FORMAT_OK = 1;
	private int count;
	private transient CellStyle cellStyleDate;
	private transient CellStyle cellStyleHref;
	private transient CellStyle cellStyleHeader;
	private transient XSSFWorkbook workbook = new XSSFWorkbook();
	private transient Sheet sheet;
//	BeanClass beanClass = new BeanClass();

	private void initStyle() {
		{
			cellStyleDate = workbook.createCellStyle();
			CreationHelper createHelper = workbook.getCreationHelper();
			short dateFormat = createHelper.createDataFormat().getFormat("yyyy-MM-dd");
			cellStyleDate.setDataFormat(dateFormat);
		}
		{
			cellStyleHeader = workbook.createCellStyle();
			// byte[] rgb = new byte[] { (byte) 200, (byte) 200, (byte) 200 };
			XSSFColor color = new XSSFColor(new java.awt.Color(128, 200, 200)); // accepts a short value
			cellStyleHeader.setFillForegroundColor((short) 5);
			cellStyleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// XSSFFont hlink_font = workbook.createFont();
			// hlink_font.setUnderline(XSSFFont.U_SINGLE);
			// hlink_font.setColor(IndexedColors.BLUE.getIndex());
			// cellStyleHref.setFont(hlink_font);
		}
		{
			cellStyleHref = workbook.createCellStyle();
			XSSFFont hlink_font = workbook.createFont();
			hlink_font.setUnderline(XSSFFont.U_SINGLE);
			hlink_font.setColor(IndexedColors.BLUE.getIndex());
			cellStyleHref.setFont(hlink_font);
		}
	}

	public void setHyperlink(Cell cell, String address, int value) {
		org.apache.poi.ss.usermodel.Hyperlink href = workbook.getCreationHelper().createHyperlink(HyperlinkType.URL);
		href.setAddress(address);
		cell.setHyperlink(href);
		cell.setCellValue(value);
		cell.setCellStyle(cellStyleHref);
	}

	public void write(List<?> list, Class elementClass, String sheetname, String filename) {

		this.filename = filename;
		this.count = list.size();
		beanClass.init(elementClass);

		/*
		 * verify data
		 */
		if (list.size() > 0) {
			Class a = list.get(0).getClass();
			if (!a.equals(elementClass)) {
				System.out.println("ERROR element.class = " + a.getName());
				return;
			}
		}

		if (sheetname == null) {
			sheetname = "Sheet1";
		}
		sheet = workbook.createSheet(sheetname);
		initStyle();

		////////////////////////////////////////////
		int rowIndex = 0;

		/*
		 * Header Writing
		 */
		{
			Row row = sheet.createRow(rowIndex++);

			for (BeanAttribute attr : beanClass.attrs) { // attribute name
				Cell cell = row.createCell(attr.getIndex());
				cell.setCellValue(attr.getName());
				cell.setCellStyle(cellStyleHeader);
			}
		}
		/*
		 * Body Writing
		 */
		for (Object anObject : list) {
			Row row = sheet.createRow(rowIndex++);
			for (BeanAttribute attr : beanClass.attrs) { // attribute name
				Cell cell = row.createCell(attr.getIndex());
				try {
					Object value = attr.getGetter().invoke(anObject, null);
					if (value == null)
						continue;
					if (format(cell, attr, value) != 0) {
						continue;
					}
					if (value.getClass() == float.class || value.getClass() == Float.class) {
						cell.setCellValue((Float) value);
					}
					if (value.getClass() == int.class || value.getClass() == Integer.class) {
						cell.setCellValue((Integer) value);
					}
					if (value.getClass() == String.class) {
						cell.setCellValue((String) value);
					}
					if (value.getClass() == java.util.Date.class) {
						java.util.Date vDate = (Date) value;
						if (vDate != null) {
							cell.setCellValue(vDate);
							cell.setCellStyle(cellStyleDate);
						}
					}
				} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		// colIndex = 0;
		for (BeanAttribute attr : beanClass.attrs) { // attribute name
			sheet.autoSizeColumn(attr.getIndex());
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
}
