package com.barolab.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 

public class TableMap {

	private Map<String, Map<String, List<Object>>> map = new HashMap<String, Map<String, List<Object>>>();
	private List<String> columnList = new ArrayList<String>();

	Map<String, List<Object>> rowMap = null;
	List<Object> valueList = null;

	public void put(String key1, String key2, Object value) {
		if (!columnList.contains(key2)) {
			columnList.add(key2);
		}
		rowMap = map.get(key1);
		if (rowMap == null) {
			rowMap = new HashMap<String, List<Object>>();
			map.put(key1, rowMap);
		}

		valueList = rowMap.get(key2);
		if (valueList == null) {
			valueList = new ArrayList<Object>();
			rowMap.put(key2, valueList);
		}
		valueList.add(value);
	}

	public List<?> get(String key1, String key2) {
		rowMap = map.get(key1);
		if (rowMap != null) {
			valueList = rowMap.get(key2);
			if (valueList != null) {
				return valueList;
			}
		}
	
		return null;
	}

	public List<String> getRowKeys() {
		Object[] array = map.keySet().toArray();
		List<String> a = new ArrayList();
		for (int i = 0; i < array.length; i++) {
			a.add((String) array[i]);
		}
		Collections.sort(a, new Ascending());
		return a;
	}

	public List<String> getColumnKeys() {
		Collections.sort(columnList, new Ascending());
		return columnList;
	}

 

	public void build(String string, String string2) {
		// TODO Auto-generated method stub
		
	}
	
	private class Ascending implements Comparator<String> {

		@Override
		public int compare(String arg0, String arg1) {
			return arg0.compareTo(arg1);
		}

	}

}
