package com.barolab.sync;

import java.util.List;

public interface FileScanner {
	
	public OV_FileInfo scanAll(OV_FileInfo parent, String path);

	public List<OV_FileInfo> getDir(String dir);
}
