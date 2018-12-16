package com.barolab.util.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.barolab.util.ExcelObjectReader;
import com.barolab.util.TableMap;

import lombok.Data;
import lombok.extern.java.Log;

@Log
@Data
public class BeanClass<T> {

	LinkedList<BeanAttribute> attrs = new LinkedList<BeanAttribute>();
	private Class clazz;
	private String name;

	public BeanClass(Class clazz) {
		init(clazz);
	}

	public void init(Class clazz) {
		this.clazz = clazz;
		name = clazz.getSimpleName();
		int count = 0;
		for (Field f : clazz.getDeclaredFields()) { // attribute name
			BeanAttribute attr = new BeanAttribute();
			attr.setClazz(clazz);
			if (attr.init(f, count) != null) {
				attrs.add(attr);
				// System.out.println( "init.attr = " + f.getName()+ " "+count);
				count++;
			} else {
				// System.out.println("init.attr.nok = " + f.getName()+ " "+count);
			}

		}
	}

	public BeanAttribute findAttribute(String name) {
		for (BeanAttribute attr : attrs) {
			if (attr.getName().equals(name)) {
				log.finer("Found attribute name " + name);
				return attr;
			}
		}
		return null;
	}

	// ****************************************************************************
	// ** SORT UTIL
	// ****************************************************************************

	public void sort(List<T> list, String... atr) {
		AtrComparator comparator = new AtrComparator();
		comparator.setAtr(atr);
		Collections.sort(list, comparator);
	}

	private class AtrComparator implements Comparator<T> {

		String[] atr = null;

		public void setAtr(String[] atr) {
			this.atr = atr;
		}

		@Override
		public int compare(T arg0, T arg1) {
			int result = 0;
			for (int clevel = 0; clevel < atr.length; clevel++) {
				String atrName = atr[clevel];

				BeanAttribute bAtr = findAttribute(atr[clevel]);
				// System.out.println("atr.compare = "+atrName+", bean = "+bAtr);
				if (bAtr == null) {
					log.severe("Can't found attribute = " + atr[clevel]);
					return 0;
				} else {
					// BeanType beanType = TypeFactory.find(bAtr.getType().getName());
					result = bAtr.getBeanType().compare(bAtr, arg0, arg1);
				}
				if (result != 0)
					break;
			}
			return result;
		}
	}

	// *****************************************************************************
	// ** EXCEL SUPPORT
	// *****************************************************************************

	public List<T> readExcel(String filename, String sheetname) {
		return new ExcelObjectReader<T>().read(this, sheetname, filename);
	}

	private transient XSSFWorkbook workbook = new XSSFWorkbook();
	private transient Sheet sheet;
	private CellStyle cellStyleDate;
	private transient CellStyle cellStyleHref;
	private transient CellStyle cellStyleHeader;

	public int writeExcel(String filename, String sheetname, List<T> list) {

		cellStyleDate = workbook.createCellStyle();
		CreationHelper createHelper = workbook.getCreationHelper();
		short dateFormat = createHelper.createDataFormat().getFormat("yyyy-MM-dd");
		cellStyleDate.setDataFormat(dateFormat);

		cellStyleHeader = workbook.createCellStyle();
		XSSFColor color = new XSSFColor(new java.awt.Color(128, 200, 200)); // accepts a short value
		cellStyleHeader.setFillForegroundColor((short) 5);
		cellStyleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		/**
		 * Class Validation Check.
		 */
		if (list.size() > 0) {
			Class a = list.get(0).getClass();
			if (!a.equals(clazz)) {
				System.out.println("ERROR element.class = " + a.getName());
				return -1;
			}
		}

		if (sheetname == null) {
			sheetname = "Sheet1";
		}

		sheet = workbook.createSheet(sheetname);
		// init_w();

		////////////////////////////////////////////
		int rowIndex = 0;

		/*
		 * Header Writing
		 */
		{
			Row row = sheet.createRow(rowIndex++);

			for (BeanAttribute attr : attrs) { // attribute name
				Cell cell = row.createCell(attr.getIndex());
				cell.setCellValue(attr.getName());
				cell.setCellStyle(cellStyleHeader);
			}
		}
		/*
		 * Body Writing
		 */
		for (Object recObject : list) {
			Row row = sheet.createRow(rowIndex++);
			for (BeanAttribute attr : attrs) { // attribute name
				Cell cell = row.createCell(attr.getIndex());
				try {
					Object value = attr.getGetter().invoke(recObject, null);
					if (value == null)
						continue;
					if (value.getClass() == float.class || value.getClass() == Float.class) {
						attr.getBeanType().writeExcelCell(attr, recObject, cell);
						// cell.setCellValue((Float) value);
					}
					if (value.getClass() == int.class || value.getClass() == Integer.class) {
						cell.setCellValue((Integer) value);
					}
					if (value.getClass() == String.class) {
						attr.getBeanType().writeExcelCell(attr, recObject, cell);
						// cell.setCellValue((String) value);
					}
					if (value.getClass() == java.util.Date.class) {
						cell.setCellStyle(cellStyleDate);
						attr.getBeanType().writeExcelCell(attr, recObject, cell);
//						java.util.Date vDate = (Date) value;
//						if (vDate != null) {
//							cell.setCellValue(vDate);
//						
//						}
					}
				} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		log.info("write body ok");

		// colIndex = 0;
		for (BeanAttribute attr : attrs) { // attribute name
			sheet.autoSizeColumn(attr.getIndex());
		}
		log.info("write body ok2");
		try {
			FileOutputStream out = new FileOutputStream(new File(filename));
			workbook.write(out);
			out.close();
			log.info("write body ok3");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return 0;
	}

	public TableMap<T> toTableMap(List<T> list, String rowKey, String colKey) {
		log.info("toTableMap row=" + rowKey + " col=" + colKey);
		TableMap<T> tbl = new TableMap<T>();
		BeanAttribute rAtr = findAttribute(rowKey);
		BeanAttribute cAtr = findAttribute(colKey);

		for (T element : list) {
			Object rKey = rAtr.getValue(element);
			Object cKey = cAtr.getValue(element);
			tbl.put("" + rKey, "" + cKey, element);
		}
		System.out.println(tbl.getRowKeys());
		System.out.println(tbl.getColumnKeys());
		return tbl;

	}

	public boolean match(String key, String... args) {
		for (String arg : args) {
			if (key.equals(arg)) {
				return true;
			}
		}
		return false;
	}

	public boolean contains(String key, String... args) {
		for (String arg : args) {
			if (key.indexOf(arg) >= 0) {
				return true;
			}
		}
		return false;
	}

	public void removeElement(List<T> list, String atrName, String... args) {
		List<T> tmp = new ArrayList<T>();
		BeanAttribute atr = findAttribute(atrName);
		for (T element : list) {
			Object value = atr.getValue(element);
			if (value != null) {
				if (contains((String) value, args)) {
					tmp.add(element);
				}
			}
		}
		list.removeAll(tmp);

	}

}
