package com.barolab.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
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



public abstract class ExcelObjectWriter extends ExcelObjectDefault {

	protected static final int FORMAT_YET = 0;
	protected static final int FORMAT_OK = 1;
	private int count;
	private transient CellStyle cellStyleDate;
	private transient CellStyle cellStyleHref;
	private transient CellStyle cellStyleHeader;
	private transient XSSFWorkbook workbook = new XSSFWorkbook();
	private transient Sheet sheet;

	public abstract int format(Cell cell, Field f, Object anObject);

	private void init_w() {
		{
			cellStyleDate = workbook.createCellStyle();
			CreationHelper createHelper = workbook.getCreationHelper();
			short dateFormat = createHelper.createDataFormat().getFormat("yyyy-MM-dd");
			cellStyleDate.setDataFormat(dateFormat);
		}
		{
			cellStyleHeader = workbook.createCellStyle();
			//byte[] rgb = new byte[] { (byte) 200, (byte) 200, (byte) 200 };
			XSSFColor color = new XSSFColor(new java.awt.Color(128,200,200)); // accepts a short value
			cellStyleHeader.setFillForegroundColor((short)5) ;
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

	public void write(List<?> list, String sheetname, String filename) {
		this.filename = filename;
		this.clazz = list.get(0).getClass();
		this.classname = clazz.getName();
		this.count = list.size();
		sheet = workbook.createSheet(sheetname);
		init_w();

		////////////////////////////////////////////
		System.out.println(LogUtil.dump(this));
		System.out.println("End");
		// Class clazz = OV_Issue.class;
		int colIndex = 0;
		int rowIndex = 0;

		/*
		 * Header Writing
		 */
		{
			Row row = sheet.createRow(rowIndex++);
			
			for (Field f : clazz.getDeclaredFields()) { // attribute name
				Cell cell = row.createCell(colIndex++);
				cell.setCellValue(f.getName());
				cell.setCellStyle(cellStyleHeader);
			}
		}

		/*
		 * Body Writing
		 */
		for (Object anObject : list) {
			Row row = sheet.createRow(rowIndex++);
			colIndex = 0;
			for (Field f : clazz.getDeclaredFields()) { // attribute name
				Cell cell = row.createCell(colIndex++);
				try {
					if (format(cell, f, anObject) == FORMAT_OK) {
						continue;
					}
					if (f.getType() == int.class || f.getType() == Integer.class) {
						cell.setCellValue(f.getInt(anObject));
					}
					if (f.getType() == String.class) {
						cell.setCellValue((String) f.get(anObject));
					}
					if (f.getType() == java.util.Date.class) {
						java.util.Date vDate = (Date) f.get(anObject);
						if (vDate != null) {
							cell.setCellValue(vDate);
							cell.setCellStyle(cellStyleDate);
						}
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sheet.autoSizeColumn(colIndex-1);
			}
		}
		try {
			FileOutputStream out = new FileOutputStream(new File(filename));
			workbook.write(out);
			out.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
