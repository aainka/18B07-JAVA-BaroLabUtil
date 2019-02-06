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
	private String path;
	private Date updated;
	private Date created;
	private boolean is_dir;
	private String text_in_file;
	transient LinkedList<OV_FileInfo> children= new LinkedList<OV_FileInfo>();
	transient private OV_FileInfo parent;
	transient private FileScanner scanner;
	transient private boolean childChanged = false;

	public OV_FileInfo(String path, OV_FileInfo parent) {
		this.path = path;
		if (parent != null) {
			this.parent = parent;
			this.parent.children.add(this);
			this.scanner = this.parent.getScanner();
			File fp = new File(this.getFullPath());
			if (fp.isDirectory()) {
				is_dir = true;
			}
		}
	}

	// #################################################################
	// ## Naming
	// #################################################################

	public String getShortName() {
		return path.substring(path.lastIndexOf("/") + 1, path.length());
	}

	public String getFullPath() {
		if (path == null || path.length() <= 0 ) {
			return scanner.homeDir;
		} else {
			return scanner.homeDir + "/" + path;
		}
	}

	public String getFullPath(RemoteFileApi scanner0) {
		if (path == null || path.length() <= 0) {
			return scanner0.homeDir;
		} else {
			return scanner0.homeDir + "/" + path;
		}
	}

	public String json() {
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		String json_string = gson.toJson(this);
		return json_string;
	}

	public void setCreatedStr(String msg) throws ParseException {
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		created = transFormat.parse(msg);
		updated = created;
	}

	public static void dumpTree(OV_FileInfo root) {
		System.out.println("dumpTr: " + root.getFullPath());
		if (root.children != null) {
			for (OV_FileInfo fi : root.children) {
				dumpTree(fi);
			}
		}
	}

	public void add(OV_FileInfo child) {
		if (children == null) {
			children = new LinkedList<OV_FileInfo>();
		}
		children.add(child);
		child.setScanner(scanner);
	}

	public void copyFrom(OV_FileInfo a) {
		this.path = a.path;
		this.created = a.created;
		this.updated = a.updated;
		this.text_in_file = a.text_in_file;
	}

	// ############################################################
	// ## scanner related
	// ############################################################

	public void read() {
		scanner.read(this);
	}

	public void write() {
		scanner.write(this);
	}

}
