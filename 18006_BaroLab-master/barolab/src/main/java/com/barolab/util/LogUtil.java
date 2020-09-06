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
		return anObject.getClass().getSimpleName()+" "+s ;
	}

}
