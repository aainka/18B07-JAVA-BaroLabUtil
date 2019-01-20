package com.barolab.sync;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class RemoteFileScanner extends FileScanner {


	
	TestFileRest remote = new TestFileRest();
	private String host;

	public RemoteFileScanner(String  host) {
		this.host = host;
	}

	@Override
	public List<OV_FileInfo> getDir(String dir) {
		return null;
	}

	@Override
	public OV_FileInfo scanAll(OV_FileInfo parent, OV_FileInfo myfi) {
		
	 	String path = myfi.getName();
		if ( homeDir == null ) {
			homeDir = path;
		}
		remote.setHost(host);
		/*
		 * make node and scan
		 */
		System.out.println("host="+host+" path=" + path);
	//	OV_FileInfo myfi = new OV_FileInfo(path, parent, this);
		try {
			for (OV_FileInfo cfi : remote.getDir(path)) {
				if ( IgnoreFile.ignore(cfi.getName()) ) {
					continue;
				}
				myfi.add(cfi);
				if (!cfi.is_dir()) {
				//	myfi.add(cfi);
				}else  {
					scanAll(myfi, cfi);
				}

			}
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myfi;
	}

	public static void main(String[] args) {
//		OV_FileInfo root = new RemoteFileScanner("").scanAll(null, "/root");
//		OV_FileInfo.dumpTree(root);

	}

	/*
	 * Remote에 화일생성
	 */
	public void write(OV_FileInfo finfo ) {
		try {
			remote.writeFileDir(finfo);
		} catch (Exception   e) {
			e.printStackTrace();
		}
	}

	@Override
	public void read(OV_FileInfo fi) {
		// TODO Auto-generated method stub
		
	}
}
