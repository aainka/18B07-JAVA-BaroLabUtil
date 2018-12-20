package com.barolab.util.model;

import java.util.List;

import org.apache.poi.ss.formula.functions.T;

import com.barolab.util.TableMap;

import Platform.DashConsole.OV_Issue;
import Platform.DashConsole.OV_TimeEntry;

public class ListUtils {

	public static void sort(List<?> list, String... keys) {
		Object anObject = list.get(0);
		BeanClass bClass = BeanClass.getInstance(anObject.getClass());
		bClass.sort(list, keys);
	}

	public static TableMap toTableMap(List<?> list, String rowKeyName, String colKeyName) {

		/**
		 * find BeanClass
		 */
		Object anObject = list.get(0);
		BeanClass bClass = BeanClass.getInstance(anObject.getClass());

		/**
		 * make TableMap
		 */
		return bClass.toTableMap(list, rowKeyName, colKeyName);
	}

	public static Object find(List<?> list, String atrName, Object value) {
		/**
		 * find BeanClass
		 */
		Object anObject = list.get(0);
		BeanClass bClass = BeanClass.getInstance(anObject.getClass());
		Object element = bClass.matchAtrValue(list, atrName, value);
		return element;
	}

	public static void removeElements(List<?> list, String atrName, String ... args) {
		/**
		 * find BeanClass
		 */
		Object anObject = list.get(0);
		BeanClass bClass = BeanClass.getInstance(anObject.getClass());
	 bClass.removeElement(list, atrName, args);
		
	}

	public static void writeExcel(List<?> list, String filename) {
		/**
		 * find BeanClass
		 */
		Object anObject = list.get(0);
		BeanClass bClass = BeanClass.getInstance(anObject.getClass());
		bClass.writeExcel(filename, "sheet0", list);
	}

}
