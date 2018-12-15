package com.barolab.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableMap<T> {

	private Map<String, Map<String, List<T>>> map = new HashMap<String, Map<String, List<T>>>();
	private List<String> columnList = new ArrayList<String>();

	Map<String, List<T>> rowMap = null;
	List<T> valueList = null;

	public void put(String key1, String key2, T value) {
		if (!columnList.contains(key2)) {
			columnList.add(key2);
		}
		rowMap = map.get(key1);
		if (rowMap == null) {
			rowMap = new HashMap<String, List<T>>();
			map.put(key1, rowMap);
		}

		valueList = rowMap.get(key2);
		if (valueList == null) {
			valueList = new ArrayList<T>();
			rowMap.put(key2, valueList);
		}
		valueList.add(value);
	}

	public List<T> get(String key1, String key2) {
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
		return a;
	}

	public List<String> getColumnKeys() {
		return columnList;
	}

	public void build(String string, String string2) {
		// TODO Auto-generated method stub

	}

}
