package com.barolab.sync;

import java.util.List;

public interface FileScanner {
	public List<OV_FileInfo> getDir(String dir);
}
