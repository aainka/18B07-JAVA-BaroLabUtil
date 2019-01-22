package com.barolab.sync;

import java.io.IOException;
import java.util.List;

import lombok.Data;

@Data
public abstract class FileScanner {
	protected String homeDir;

	abstract public OV_FileInfo scanAll(OV_FileInfo parent, OV_FileInfo myfi) throws IOException;

	abstract public List<OV_FileInfo> getDir(String dir) throws Exception;

	abstract public void read(OV_FileInfo fi);

	abstract public OV_FileInfo write(OV_FileInfo fi);
}
