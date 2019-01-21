package com.barolab.sync;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;

import com.barolab.log.LogConfig;

import lombok.extern.java.Log;

@Log
public class Sync {

//	String remote_host = "211.239.124.246:19808"; // f25
//	String remote_host2 = "110.13.71.93:9292"; // H1

//	String remote_homeDir = "/proj7/GITHUB/18B07-BaroLabUtil/";
//	String local_path = "C:/@SWDevelopment/workspace-java/18B07-BaroLabUtil/";

	String remote_homeDir = "/proj7/GITHUB/18004-DashConsole/";
	String local_path = "C:/@SWDevelopment/workspace-java/18004-DashConsole/";

//	String remote_homeDir2 = "/proj7java-workspace/18B07-BaroLabUtil/";
	// RemoteFileScanner remote = new RemoteFileScanner(remote_host, "/proj7");
//	LocalFileScanner local = new LocalFileScanner("xxx");

	boolean lock = true;

	public void sync() throws IOException {
		LogConfig.setLevel("com.barolab.sync.*", Level.ALL);

		LocalFileScanner local = new LocalFileScanner("S:/tmp/18B07-BaroLabUtil");
		OV_FileInfo a = local.scanAll();
		// RemoteFileScanner remote = new RemoteFileScanner("100.99.14.164:9292",
		// "/root/project");
//		RemoteFileScanner remote = new RemoteFileScanner("211.239.124.246:19808", "/proj7/GITHUB/18B07-BaroLabUtil/");
		RemoteFileScanner remote = new RemoteFileScanner("192.168.25.50:9292", "/root/AAA/18B07-BaroLabUtil");
		OV_FileInfo b = remote.scanAll();
		compareFile(a, b);

//		  syncProject("18004-DashConsole", "211.239.124.246:19808", "/proj7/GITHUB",
//		  "C:/@SWDevelopment/workspace-java");
		// syncProject("18B07-BaroLabUtil", "211.239.124.246:19808", "/proj7/GITHUB",
		// "C:/@SWDevelopment/workspace-java");
		// syncProject("18B07-BaroLabUtil", "211.239.124.246:19808", "/proj7/GITHUB",
		// "S:/sw-dev/eclipse-workspace-18b");
	}

	public void syncProject(String projName, String host, String remoteDir, String localDir) throws IOException {

//		remote_homeDir = remoteDir + "/" + projName + "/";
//		local_path = localDir + "/" + projName + "/";
		// local.homeDir = localDir + "/" + projName;

		// OV_FileInfo fs0 = local.scanAll(null, null);
		// OV_FileInfo fs1 = remote.scanAll(null, new OV_FileInfo(remote_homeDir, null,
		// remote));
		// OV_FileInfo.dumpTree(fs0);
		// compare_to(fs0, fs1);
	}

	private void compareFile(OV_FileInfo fs0, OV_FileInfo fs1) {
	
		if (fs0.children == null) {
			return;
		}
		for (OV_FileInfo src : fs0.children) {
			OV_FileInfo dst = find(src, fs1.children);
			if (dst == null) {
				log.info("--> not_exist = " + src.getFullPath());
				if (remoteWrite(src, fs1.getScanner())) {
				compareFile(src, dst); // recursive
				}
			} else {
				compareTime(src, dst);
				compareFile(src, dst); // recursive
			}
		}
	}

	private void compareTime(OV_FileInfo src, OV_FileInfo dst) {
		long s0 = src.getUpdated().getTime();
		long d0 = dst.getUpdated().getTime();
		long diff = d0 - s0;
		if (diff != 0) {
			if (diff < 0) {
				log.info("--> " +src.getFullPath()+", t="+diff);
				if (!lock)
					remoteWrite(src, dst.getScanner());
			} else {
				log.info("<-- " +src.getFullPath()+", t="+diff);
				if (!lock)
					remoteGet(dst, src.getScanner());
			}
		}
	}

	private void remoteGet(OV_FileInfo dst, FileScanner scanner) {
		log.info("<< " + dst.getFullPath());
		dst.read();
		scanner.write(dst);
	}

	private boolean remoteWrite(OV_FileInfo src, FileScanner scanner) {
		log.info(" -> " + src.getFullPath());
//		String homeDir = src.getHomeDir();
//		String path = src.getPath();
//		String name = remote.getHomeDir() + src.getPath();
//		System.out.println("homeDir=" + src.getHomeDir());
//		System.out.println("Path=" + src.getPath());
//		System.out.println("New=" + name);
		// src.setName(name);
		if (src.is_dir()) {
return scanner.write(src);
		} else {
//			name = local.getHomeDir() + src.getPath();
//			src.setName(name);
			src.read();
			System.out.println("SRC.READ=" + src.json());
//			name = remote.getHomeDir() + src.getPath();
//			src.setName(name);
			scanner.write(src); // @Todo cron to dst
		}
		// if (true) return;
		return false;

	}

	private OV_FileInfo find(OV_FileInfo t, LinkedList<OV_FileInfo> children) {
		if (children != null) {
			String name = t.getShortName();
			for (OV_FileInfo c : children) {
				if (name.equals(c.getShortName())) {
					// System.out.println("find " + name + " c1=" + c.getShortName());
					return c;
				}
			}
		}
		return null;
	}

	public static void main(String[] args) {
		try {
			new Sync().sync();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
