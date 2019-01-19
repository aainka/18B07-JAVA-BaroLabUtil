package com.barolab.sync;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.Data;

@Data
public class OV_FileInfo {
	private String name;
	private Date updated;
	private Date created;
	private boolean is_dir;
	private String text_in_file;
	transient File fp;
	transient private List<OV_FileInfo> children;
	
	public OV_FileInfo(File newfp, OV_FileInfo parent) {
		this.fp = newfp;
		this.name = newfp.getName();
		this.
	}

	public String json() {
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		String json_string = gson.toJson(this);
		return json_string;
	}

	public void setCreatedStr(String msg) throws ParseException {
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		created = transFormat.parse(msg);
	}


}
