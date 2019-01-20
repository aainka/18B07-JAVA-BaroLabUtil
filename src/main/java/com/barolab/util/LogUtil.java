package com.barolab.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LogUtil {

	static public Date getTime() {
		long time = System.currentTimeMillis();
		return new Date(time);
	}

	static public String getToday() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh_mm_ss");
		return simpleDateFormat.format(getTime());
	}

	static public String dump(Object anObject) {
		GsonBuilder builder = new GsonBuilder();
		builder.setDateFormat("yyyy-MM-dd hh:mm:ss");
		Gson gson = builder.setPrettyPrinting().create();
		String s = gson.toJson(anObject);
		return anObject.getClass().getSimpleName() + " " + s;
	}

	static public void getEnv() {
		System.out.println("운영체제 종류: " + System.getProperty("os.name"));
		System.out.println("자바 가상머신 버전: " + System.getProperty("java.vm.version"));
		System.out.println("클래스 버전: " + System.getProperty("java.class.version"));
		System.out.println("사용자 로그인ID: " + System.getProperty("user.name"));
	}

	public static String getOsName() {
		return System.getProperty("os.name");
	}

}
