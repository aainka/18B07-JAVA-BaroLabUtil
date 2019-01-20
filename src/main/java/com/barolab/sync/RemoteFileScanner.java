package com.barolab.sync;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class RemoteFileScanner implements FileScanner {

	TestFileRest remote = new TestFileRest();

	@Override
	public List<OV_FileInfo> getDir(String dir) {
		return null;
	}

	@Override
	public OV_FileInfo scanAll(OV_FileInfo parent, String path) {
		remote.setHost("100.99.14.164:9292");
		/*
		 * make node and scan
		 */
		System.out.println("path=" + path);
		OV_FileInfo myfi = new OV_FileInfo(path, parent);
		try {
			for (OV_FileInfo cfi : remote.getDir(path)) {
				if (cfi.getName().indexOf("/.") >= 0)
					continue;
				if (cfi.getName().indexOf("/__") >= 0)
					continue;
				if (!cfi.is_dir()) {
					myfi.add(cfi);
				}else  {
					scanAll(myfi, cfi.getName());
				}

			}
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myfi;
	}

	public static void main(String[] args) {
		OV_FileInfo root = new RemoteFileScanner().scanAll(null, "/root");
		OV_FileInfo.dumpTree(root);

	}
}
