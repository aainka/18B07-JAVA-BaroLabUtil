package com.barolab.sync;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;

import com.barolab.log.LogConfig;

import lombok.extern.java.Log;

@Log
public class Sync {

//	String remote_homeDir = "/proj7/GITHUB/18B07-BaroLabUtil/";
//	String local_path = "C:/@SWDevelopment/workspace-java/18B07-BaroLabUtil/";

//	String remote_homeDir = "/proj7/GITHUB/18004-DashConsole/";
//	String local_path = "C:/@SWDevelopment/workspace-java/18004-DashConsole/";

//	String remote_homeDir2 = "/proj7java-workspace/18B07-BaroLabUtil/";
	// RemoteFileScanner remote = new RemoteFileScanner(remote_host, "/proj7");
//	LocalFileScanner local = new LocalFileScanner("xxx");
	// RemoteFileScanner remote = new RemoteFileScanner(host13F,
	// "/root/project");
//	RemoteFileScanner remote = new RemoteFileScanner(hostFun25, "/proj7/GITHUB/18B07-BaroLabUtil/");
//	  syncProject("18004-DashConsole", hostFun25, "/proj7/GITHUB",
//	  "C:/@SWDevelopment/workspace-java");
	// syncProject("18B07-BaroLabUtil", hostFun25, "/proj7/GITHUB",
	// "C:/@SWDevelopment/workspace-java");
	// syncProject("18B07-BaroLabUtil", hostFun25, "/proj7/GITHUB",
	// "S:/sw-dev/eclipse-workspace-18b");
	// LocalFileScanner local = new LocalFileScanner("S:/tmp/18B07-BaroLabUtil");
//	
//
//	RemoteFileScanner remote = new RemoteFileScanner("192.168.25.50:9292", "/root/AAA/18B07-BaroLabUtil");

	boolean lock = true;
	LocalFileScanner local;
	RemoteFileScanner remote;

	String hostHomeOne = "110.13.71.93:9292";
	String host13F = "100.99.14.164:9292";
	String hostFun25 = "211.239.124.246:19808";
	String hostLocal = "192.168.25.50:9292";
	
	StringBuilder xx = new StringBuilder();

	private void config_one() {
		// local = new LocalFileScanner("C:/tmp/project");
		// S:\sw-dev\eclipse-workspace-18b
		// local = new
		// LocalFileScanner("C:/@SWDevelopment/workspace-java/18B07-BaroLabUtil/");
		local = new LocalFileScanner("C:/@SWDevelopment/workspace-java/18B07-BaroLabUtil/");
		remote = new RemoteFileScanner(hostHomeOne, "/root/project/18B07-BaroLabUtil");
//		local = new LocalFileScanner("C:/@SWDevelopment/workspace-java/18004-DashConsole");
//		remote = new RemoteFileScanner(hostHomeOne, "/root/project/18004-DashConsole");
	}

	public void test() {
		// syncProject("18B07-BaroLabUtil", hostLocal, "/root/project",
		// "C:/@SWDevelopment/workspace-java");
		syncProject("18B07-BaroLabUtil", hostLocal, "/root/project", "S:/sw-dev/eclipse-workspace-18b");
	}

	public void syncProject(String projName, String host, String remoteDir, String localDir) {
		System.out.println("Project=" + projName);
		LogConfig.setLevel("com.barolab.sync.*", Level.ALL);
		local = new LocalFileScanner(localDir + "/" + projName);
		remote = new RemoteFileScanner(host, remoteDir + "/" + projName);
		// config_one();
		OV_FileInfo a = local.scanAll();
		OV_FileInfo b = remote.scanAll();
		compareFile(a, b);
		System.out.println("Completed");
		System.out.println(xx.toString());
	}

	public void sync() throws IOException {
		LogConfig.setLevel("com.barolab.sync.*", Level.ALL);
		config_one();
		OV_FileInfo a = local.scanAll();
		OV_FileInfo b = remote.scanAll();
		compareFile(a, b);
		System.out.println("Completed");
	}

	/**
	 * Directory time must be changed after all node changed under own directory.
	 * 
	 * @param srcParent
	 * @param dstParent
	 */
	private void compareFile(OV_FileInfo srcParent, OV_FileInfo dstParent) {
		if (srcParent.children != null) {
			for (OV_FileInfo src : srcParent.children) {
				OV_FileInfo dst = find(src, dstParent.children);
				if (dst == null) {
					report("--> not_exist = " + src.getFullPath());
					log.info("--> not_exist = " + src.getFullPath());
					dst = remoteWrite(src, dstParent.getScanner()); // create node
					if (dst != null) {
						srcParent.setChildChanaged(true);
						compareFile(src, dst);
					} // recursive
				} else {
					compareTime(src, dst);
					compareFile(src, dst); // recursive
				}
			}
			if (srcParent.is_dir() && srcParent.isChildChanaged()) {
				remoteWrite(srcParent, dstParent.getScanner()); // overwrite time
			}
		}
	}

	private void compareTime(OV_FileInfo src, OV_FileInfo dst) {
		long s0 = src.getUpdated().getTime();
		long d0 = dst.getUpdated().getTime();
		long diff = d0 - s0;
		if (diff != 0) {
			if (diff < 0) {
				report("--> " + src.getFullPath() + ", t=" + diff);
				log.info("--> " + src.getFullPath() + ", t=" + diff);
				if (!lock  ) {
					remoteWrite(src, dst.getScanner());
					src.getParent().setChildChanaged(true);
				}
			} else {
				report("<-- " + src.getFullPath() + ", t=" + diff);
				if (!lock) {
					// remoteGet(dst, src.getScanner()); // @ 로컬 타입도 업데이트 해야 하나?
				}
			}
		}
	}

	private void remoteGet(OV_FileInfo dst, FileScanner scanner) {
		log.info("<< " + dst.getFullPath());
		dst.read();
		scanner.write(dst);
	}

	private OV_FileInfo remoteWrite(OV_FileInfo src, FileScanner scanner) {
		log.info(" -> " + src.getFullPath());
		OV_FileInfo np = null;
		if (src.is_dir()) {
			src.read();
			np = scanner.write(src);
		} else {
			src.read();
			// System.out.println("SRC.READ=" + src.json());
			np = scanner.write(src); // @Todo cron to dst
		}
		if (np != null) {
			np.setScanner(scanner);
		}
		return np;

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
	
	private void report(String msg) {
		xx.append(msg+"\n");
	}

	public static void main(String[] args) {
		new Sync().test();

	}

}
