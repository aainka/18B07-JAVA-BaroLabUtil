package com.barolab.util;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class TypeUtil<T> {

	public Class test(List<T> k) {

		Method method;
		try {
			for (Method m : this.getClass().getMethods()) {
				if (m.getName().equals("test")) {
					System.out.println("class = " + m);
					Type listType = m.getGenericParameterTypes()[0];
					if (listType instanceof ParameterizedType) {
						Type elementType = ((ParameterizedType) (listType)).getActualTypeArguments()[0];
						System.out.println("Type = " + elementType);
						return (Class)elementType;
					}
				}
			}

		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	//	k.add("jeon");

//		ParameterizedType superclass = (ParameterizedType) clazz.getGenericSuperclass();
//		Type[] types = superclass.getActualTypeArguments();
//		Class actualdataType = null;
//		System.out.println(" T1 = "+types.length);
//		 
//		if (types != null && types.length > 0 && (types[0] instanceof Class<?>)) {
//			actualdataType = (Class<?>) types[0];
//		}
//		System.out.println("actualdataType = "+actualdataType);
		return null;
	}

	static public void main(String[] args) {
		List<String> k = new LinkedList<String>();
		new TypeUtil<String>().test(k);
	}
}
