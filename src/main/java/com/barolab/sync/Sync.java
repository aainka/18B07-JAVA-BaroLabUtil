package com.barolab.sync;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class Sync {
	
	String remote_host = "211.239.124.246:19801";
	String remote_homeDir = "/proj7java-workspace/18B07-BaroLabUtil/";
	TestFileRest remote = new TestFileRest();
	LocalFileScanner local = new LocalFileScanner();

	public void sync() {
//		remote.setHost(remote_host);
//		remote.setHomeDir(remote_homeDir);
//		try {
//			List<OV_FileInfo> remote_files = remote.getDir("");
//			List<OV_FileInfo> local_files = local.getDir("");
//		} catch (IOException | URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	//	remote.setH
		String local_path = "C:/@SWDevelopment/workspace-java/18B07-BaroLabUtil/";
		local.scanAll(null, local_path);
	}
	 

	public static void main(String[] args) {
		new Sync().sync();

	}

}
