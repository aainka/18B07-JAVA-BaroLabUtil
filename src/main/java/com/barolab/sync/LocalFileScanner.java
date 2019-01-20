package com.barolab.sync;

import java.io.File;
import java.util.List;

public class LocalFileScanner implements FileScanner {

	public OV_FileInfo scanAll(OV_FileInfo parent, String path) {
		/*
		 * make node and scan
		 */
		System.out.println("path=" + path);
		OV_FileInfo myfi = new OV_FileInfo(path, parent);
		File myfp = new File(path);
		if (myfp.isDirectory()) {
			myfi.set_dir(true);
			for (File fp : myfp.listFiles()) {
				scanAll(myfi, path + "/" + fp.getName());
			}
		}
		return myfi;
	}

	@Override
	public List<OV_FileInfo> getDir(String dir) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String arg[]) {
		OV_FileInfo root = new LocalFileScanner().scanAll(null, "C:/tmp");
		OV_FileInfo.dumpTree(root);
	}

}
