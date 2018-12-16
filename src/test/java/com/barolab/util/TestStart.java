package com.barolab.util;

import java.util.LinkedList;

import org.apache.poi.ss.usermodel.Cell;
import org.junit.Test;

import com.barolab.util.model.BeanAttribute;

public class TestStart {
	private OV_Item item;

	@Test
	public void testAC() {
		System.out.println("test-3");
		testObjectDump();
		testBB();
		System.out.println("all test completed...");
	}

	public void testObjectDump() {
		System.out.println("*** TEST (1) ");
		item = new OV_Item();
		item.setName("Kim");
		item.setX(45);
		System.out.println(LogUtil.dump(item));
	}

	public void testBB() {
		System.out.println("*** TEST (2) ");
		LinkedList<OV_Item> list = new LinkedList();
		list.add(item);
		ExcelObjectWriter w = new ExcelObjectWriter() {

			@Override
			public int format(Cell cell, BeanAttribute attr, Object value) {
				// TODO Auto-generated method stub
				return 0;
			}

		};
		w.write(list, null, "C:/tmp/aa.xlsx");
	}

}
