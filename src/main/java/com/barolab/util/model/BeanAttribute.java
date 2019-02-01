package com.barolab.util.model;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lombok.Data;
import lombok.extern.java.Log;

@Data
@Log
public class BeanAttribute {
	private int index;
	private String name;
	public Method getter;
	private Method setter;
	private int xlsColIndex;
	private BeanType beanType;
	public BeanClass beanClass;

	public BeanAttribute init(BeanClass beanClass, Field f, int index) {
		this.beanClass = beanClass;
		this.index = index;
		name = f.getName();
		// type = f.getType();
		try {
			String funcName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
			getter = beanClass.getClazz().getMethod(funcName, null);
			funcName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
			Class[] argTypes = new Class[] { f.getType() };
			setter = beanClass.getClazz().getMethod(funcName, argTypes);
			beanType = BeanClass.createBeanType(f.getType().getName(), this);
		} catch (NoSuchMethodException | SecurityException e) {
			// e.printStackTrace();
			log.finer("BeanAttribute skip attribute = " + f.getName());
			return null;
		}
		return this;
	}

	public String toString() {
		return "bAtr[" + name + ", type=" + beanType.getClass() + "] ";
	}

	public Object getValue(Object recObject) {
		Object value = null;
		try {
			value = getter.invoke(recObject);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	public void setValue(Object target, Object value) {
		try {
			setter.invoke(target, value);
			// System.out.println("setter = "+setter+"value= %d "+value);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.info("Failed bAttr("+name+") <-"+value +" ("+value.getClass().getName()+")");
			e.printStackTrace();
		}
	}
}
