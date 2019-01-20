package com.barolab.sync;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;



import com.barolab.log.LogConfig;

import lombok.extern.java.Log;

@Log
public class Sync {

	String remote_host = "211.239.124.246:19808"; // f25
//	String remote_host2 = "110.13.71.93:9292"; // H1

//	String remote_homeDir = "/proj7/GITHUB/18B07-BaroLabUtil/";
//	String local_path = "C:/@SWDevelopment/workspace-java/18B07-BaroLabUtil/";

	String remote_homeDir = "/proj7/GITHUB/18004-DashConsole/";
	String local_path = "C:/@SWDevelopment/workspace-java/18004-DashConsole/";

//	String remote_homeDir2 = "/proj7java-workspace/18B07-BaroLabUtil/";
	RemoteFileScanner remote = new RemoteFileScanner(remote_host);
	LocalFileScanner local = new LocalFileScanner();

	public void sync() throws IOException {
		LogConfig.setLevel("com.barolab.sync", Level.ALL);
//		  syncProject("18004-DashConsole", "211.239.124.246:19808", "/proj7/GITHUB",
//		  "C:/@SWDevelopment/workspace-java");
		syncProject("18B07-BaroLabUtil", "211.239.124.246:19808", "/proj7/GITHUB", "C:/@SWDevelopment/workspace-java");
	}

	public void syncProject(String projName, String host, String remoteDir, String localDir) throws IOException {
		remote_host = host;
		remote_homeDir = remoteDir + "/" + projName + "/";
		local_path = localDir + "/" + projName + "/";
		OV_FileInfo fs0 = local.scanAll(null, new OV_FileInfo(local_path, null, local));
		OV_FileInfo fs1 = remote.scanAll(null, new OV_FileInfo(remote_homeDir, null, remote));
	//	OV_FileInfo.dumpTree(fs0);
		compare_to(fs0, fs1);
	}

	private void compare_to(OV_FileInfo fs0, OV_FileInfo fs1) {
		if (fs0.children == null) {
			return;
		}
		for (OV_FileInfo src : fs0.children) {
			OV_FileInfo dst = find(src, fs1.children);
			if (dst == null) {
				System.out.println(">>>>> not_exist = " + src.getName());
				remoteWrite(src);
			} else {
				compareTime(src, dst);
				compare_to(src, dst); // recursive
			}
		}
	}

	private void compareTime(OV_FileInfo src, OV_FileInfo dst) {
		long s0 = src.getUpdated().getTime();
		long d0 = dst.getUpdated().getTime();
		long diff = d0 - s0;
		if (diff != 0) {
			if (diff < 0) {
				System.out.println("--> time =" + (d0 - s0) + " " + src.getPath());
				remoteWrite(src);
			} else {
				System.out.println("<-- time =" + (d0 - s0) + " " + src.getPath());
				remoteGet(dst);
			}
		}
	}

	private void remoteGet(OV_FileInfo dst) {
		log.info("<< "+dst.getPath());
		dst.read();
		String homeDir = dst.getHomeDir();
		String path = dst.getPath();
		String name = local.getHomeDir() + dst.getPath();
		dst.setName(name);
		local.write(dst);
	}

	private void remoteWrite(OV_FileInfo src) {
		log.info(">> "+src.getPath());
		// if (true) return;
		src.read();
		String homeDir = src.getHomeDir();
		String path = src.getPath();
		String name = remote.getHomeDir() + src.getPath();
//		System.out.println("homeDir=" + src.getHomeDir());
//		System.out.println("Path=" + src.getPath());
//		System.out.println("New=" + name);
		src.setName(name);
		remote.write(src); // @Todo cron to dst
	}

	private OV_FileInfo find(OV_FileInfo t, LinkedList<OV_FileInfo> children) {
		String name = t.getShortName();
		for (OV_FileInfo c : children) {
			if (name.equals(c.getShortName())) {
				// System.out.println("find " + name + " c1=" + c.getShortName());
				return c;
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
