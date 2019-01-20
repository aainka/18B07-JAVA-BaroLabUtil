package com.barolab.sync;

import java.util.List;

import lombok.Data;

@Data
public abstract class FileScanner {
	protected String homeDir;

	abstract public OV_FileInfo scanAll(OV_FileInfo parent, OV_FileInfo myfi);

	abstract public List<OV_FileInfo> getDir(String dir);

	abstract public void read(OV_FileInfo fi);
}
