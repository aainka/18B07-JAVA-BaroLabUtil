package com.barolab.sync;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

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
	transient LinkedList<OV_FileInfo> children;
	transient private OV_FileInfo parent;
	transient private FileScanner fileScanner;

	public OV_FileInfo(String name, OV_FileInfo parent, FileScanner fileScanner) {
		this.name = name;
		this.fileScanner = fileScanner;
		if (parent != null) {
			this.parent = parent;
			if (parent.children == null) {
				parent.children = new LinkedList<OV_FileInfo>();
			}
			parent.children.add(this);
		}
		File fp = new File(name);
		if (fp.isDirectory()) {
			is_dir = true;
		}
	}

	public OV_FileInfo() {
		// TODO Auto-generated constructor stub
	}
	
	// #################################################################
	// ## Naming
	// #################################################################

	public String getShortName() {
		return name.substring(name.lastIndexOf("/") + 1, name.length());
	}
	
	public String getHomeDir() {
		return fileScanner.getHomeDir();
	}
	
	public String getPath() {
		int size = fileScanner.getHomeDir().length();
		return name.substring(size+1);
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

	public static void dumpTree(OV_FileInfo root) {
		System.out.println("dumpTr: " + root.name);
		if (root.children != null) {
			for (OV_FileInfo fi : root.children) {
				dumpTree(fi);
			}
		}
	}

	public void add(OV_FileInfo cfi) {
		if (children == null) {
			children = new LinkedList<OV_FileInfo>();
		}
		children.add(cfi);
	}

	public void read() {
		fileScanner.read(this);
	}



}
