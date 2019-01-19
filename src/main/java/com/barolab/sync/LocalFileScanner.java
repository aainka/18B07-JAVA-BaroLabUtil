package com.barolab.sync;

import java.io.File;
import java.util.List;

public class LocalFileScanner implements FileScanner {

	public OV_FileInfo scanAll(OV_FileInfo parent,String path) {
		String fpath = null;
		for( File fp : parent.fp.listFiles()) {
			OV_FileInfo fi = new OV_FileInfo(fp, parent);
			if ( fi.is_dir() ) {
				scanAll(fi, fp.getName()); //@Todo
			}
		}
		return null;
	}

	@Override
	public List<OV_FileInfo> getDir(String dir) {
		// TODO Auto-generated method stub
		return null;
	}

}
