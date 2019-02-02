package com.barolab.sync;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.table.DefaultTableModel;

import com.barolab.log.LogConfig;

import lombok.extern.java.Log;

@Log
public class Sync {

	// updated1234

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

	boolean syncGetLock = false;
	boolean syncPutLock = true;
	LocalFileScanner local;
	RemoteFileScanner remote;

	String hostHomeOne = "110.13.71.93:9292";
	String host13F = "100.99.14.164:9292";
	String hostFun25 = "211.239.124.246:19808";
	String hostLocal = "192.168.25.50:9292";

	StringBuilder xx = new StringBuilder();
	List<OV_ScanRpt> scanList = null;

	private void config_one() {
		// local = new LocalFileScanner("C:/tmp/project");
		// S:\sw-dev\eclipse-workspace-18b
		// local = new
		// LocalFileScanner("C:/@SWDevelopment/workspace-java/18B07-BaroLabUtil/");
		local = new LocalFileScanner("C:/@SWDevelopment/workspace-java/18B07-BaroLabUtil/");
		remote = new RemoteFileScanner(hostHomeOne, "/root/SynHub/18B07-BaroLabUtil");
//		local = new LocalFileScanner("C:/@SWDevelopment/workspace-java/18004-DashConsole");
//		remote = new RemoteFileScanner(hostHomeOne, "/root/project/18004-DashConsole");
	}

	public DefaultTableModel getTableModel() {
		System.out.println("testGui.... Called");
		scanList = new LinkedList<OV_ScanRpt>();
		String host;
		String localDir;
		if (false) {
			syncGetLock = true;
			syncPutLock = true;
			host = hostLocal;
			localDir = "S:/sw-dev/eclipse-workspace-18b";
		} else {
			syncGetLock = true;
			syncPutLock = true;
			host = hostHomeOne;
			localDir = "C:/@SWDevelopment/workspace-java";
		}
		LogConfig.setLevel("com.barolab.sync", Level.INFO);
		// LogConfig.setLevel("com.barolab.sync.*", Level.ALL);
		syncProject("18B07-BaroLabUtil", host, "/root/SynHub", localDir);
//		syncProject("19A01-PyRestfulApi", host, "/root/SynHub", localDir);
//		syncProject("18004-DashConsole", host, "/root/SynHub", localDir);

		String[] columnName = { "File", "Mode", "Date" };
		DefaultTableModel model = new DefaultTableModel(columnName, 0);

		for (OV_ScanRpt trace : scanList) {
			Object[] objList = new Object[] { trace.getSrc().getPath(), trace.getOp(), trace.getSrc().getUpdated() };
			model.addRow(objList);
		}
		JYWidget.find("syncTalbe").setData(model);
		return model;
	}

	public void test() {
		String host;
		String localDir;
		if (false) {
			syncGetLock = true;
			syncPutLock = true;
			host = hostLocal;
			localDir = "S:/sw-dev/eclipse-workspace-18b";
		} else {
			syncGetLock = true;
			syncPutLock = true;
			host = hostHomeOne;
			localDir = "C:/@SWDevelopment/workspace-java";
		}
		LogConfig.setLevel("com.barolab.sync", Level.INFO);
		// LogConfig.setLevel("com.barolab.sync.*", Level.ALL);
		syncProject("18B07-BaroLabUtil", host, "/root/SynHub", localDir);
		syncProject("19A01-PyRestfulApi", host, "/root/SynHub", localDir);
		syncProject("18004-DashConsole", host, "/root/SynHub", localDir);

	}

	public void syncProject(String projName, String host, String remoteDir, String localDir) {
		System.out.println("Project=" + projName);
		xx = new StringBuilder();
		local = new LocalFileScanner(localDir + "/" + projName);
		remote = new RemoteFileScanner(host, remoteDir + "/" + projName);
		// config_one();
		OV_FileInfo a = local.scanAll();
		OV_FileInfo b = remote.scanAll();
		compareFile(a, b);
		System.out.println("Completed");
		System.out.println(xx.toString());
	}

	public void sync99() throws IOException {

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
		if (dstParent == null) {
			return; // when lock
		}
		log.config(srcParent.getFullPath());
		if (srcParent.children != null) {
			for (OV_FileInfo src : srcParent.children) {
				OV_FileInfo dst = find(src, dstParent.children);
				if (dst == null) {
					report("--> X " + src.getFullPath());
					{
						  OV_ScanRpt.create("remoteCreate",scanList,src,dst);
					}
					dst = remoteWrite(src, dstParent.getScanner()); // create node
					if (dst != null) {
						srcParent.setChildChanged(true);
					} // recursive
				} else {
					if (!isContextSame(src, dst)) {
						compareTime(src, dst);
					}
				}
				// isContextSame(src, dst);
				compareFile(src, dst); // recursive
			}
			if (srcParent.is_dir() && srcParent.isChildChanged()) {
				remoteWrite(srcParent, dstParent.getScanner()); // overwrite time
			}
			if (dstParent.is_dir() && dstParent.isChildChanged()) {
				// localUpdateTime(dstParent, srcParent.getScanner());
			}
		}
	}

	private boolean isContextSame(OV_FileInfo src, OV_FileInfo dst) {
		if (dst == null) {
			return false; // remotewrite lock 시 null.
		}
		if (src.is_dir()) {
			return true;
		}
		src.read();
		dst.read();
		String src_context = src.getText_in_file();
		String dst_context = dst.getText_in_file();
		int diff = src_context.length() - dst_context.length();
		if (src_context.equals(dst_context)) {
			// log.severe("*************** Context is same " + src.getFullPath());
			return true;
		} else {
			log.severe("*************** Context is different " + src.getFullPath());
			return false;
		}

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

	private void compareTime(OV_FileInfo src, OV_FileInfo dst) {
		long s0 = src.getUpdated().getTime();
		long d0 = dst.getUpdated().getTime();
		long diff = d0 - s0;
		if (diff != 0) {
			if (diff < 0) {
				report("--> " + src.getFullPath() + ", t=" + src.getUpdated());
				{
					  OV_ScanRpt.create("put",scanList,src,dst);
				}
				log.info("--> " + src.getFullPath() + ", t=" + diff);
				if (!syncPutLock) {
					OV_FileInfo a = remoteWrite(src, dst.getScanner());
					if (a != null) {
						src.getParent().setChildChanged(true);
					}
				}
			} else {
				report("<-- " + src.getFullPath() + ", s=" + src.getUpdated() + " d=" + dst.getUpdated());
				{
					  OV_ScanRpt.create("Get",scanList,src,dst);
				}
				remoteGet(dst, src.getScanner()); // @ 로컬 타입도 업데이트 해야 하나?
			}
		}
	}

	// ##################################################################
	// ## Remote Sync
	// ##################################################################

	private void remoteGet(OV_FileInfo dst, FileScanner scanner) {
		if (syncGetLock) {
			return;
		}
		log.info("<< " + dst.getFullPath() + " dir?" + dst.is_dir());
		if (!dst.is_dir()) {
			dst.read();
		}
		scanner.write(dst);
//		dst.getParent().setChildChanged(true);
	}

	private OV_FileInfo remoteWrite(OV_FileInfo src, FileScanner scanner) {
		if (syncPutLock) {
			return null;
		}
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

	private void report(String msg) {
		// log.info(msg);
		xx.append(msg + "\n");
//		OV_ScanRpt nr = new OV_ScanRpt();
//		nr.setOp(msg);
//		scanList.add(nr);
	}

	public static void main9(String[] args) {
		new Sync().test();

	}

}
