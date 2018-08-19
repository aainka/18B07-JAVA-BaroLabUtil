package com.barolab.util.sftp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.rmi.Remote;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

// 일단 싱크에만 집중하자.
// 없는 화일을 검사해서 지워주어야 한다. (쉬운방법은 기존에 있던것을 모두 지우고 새로 넣는 것이다.)
// 결과는 동일하다. 어느 것이 쉬운가?
// 문제는 GIT 정보까지 없어지는 것이 문제이다.
// 결론은 JGIT이다. 금방 다운받아서 쓰고 버리기. 
// 그랫으면 기존에 사용하지

public class SftpSync extends SftpExample {

	Logger logger = Logger.getLogger(this.getClass());

	public SftpSync(String host, Integer port, String user, String password) {
		super(host, port, user, password);
	}

	private void debugf(String msg) {
		System.out.println(msg);
	}

	private void error(String msg) {
		System.out.println("ERROR:" + msg);
	}

//	public void test() {
//		String localPath = "C:/tmp";
//		String remotePath = "/root/BBB";
//
//		connect();
//		try {
//			sftpChannel.cd(remotePath);
//			File localDir = new File(localPath);
//			syncAll(localDir, remotePath);
//		} catch (SftpException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		disconnect();
//
//	}

	public void rmdir(String remotePath) {
		logger.info("rmdir0:=" + remotePath);
		try {
			sftpChannel.cd(remotePath);
			Vector filelist = sftpChannel.ls("*");

			for (int i = 0; i < filelist.size(); i++) {
				FileInfo fileInfo = new FileInfo((LsEntry) filelist.get(i), remotePath, sftpChannel);
				if (fileInfo.isDirectory()) {
					fileInfo.rmdir();
				} else {
					fileInfo.rm();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void syncUpload(File localDir, String remotePath) {

		debugf("\nsyncAll : localDir = " + localDir.getPath() + " " + localDir.listFiles().length);
		debugf("          remoteDir = " + remotePath);

		if (!localDir.isDirectory()) {
			error("arg is not directory. ");
			return;
		}
		FileInfo fi = new FileInfo();
		fi.setSftpChannel(sftpChannel);
		fi.cd_mkdir(remotePath);
		for (File child : localDir.listFiles()) {
			if (child.isDirectory()) {
				syncUpload(child, remotePath + "/" + child.getName());
			} else {
				fi.put(child);
			}
		}
		/**
		 * Sync Time
		 */
		try {
			SftpATTRS attr = sftpChannel.lstat(remotePath);
			// attr.setACMODTIME((int) (localDir.lastModified() / 1000), (int)
			// (localDir.lastModified() / 1000));
			attr.setACMODTIME(attr.getATime(), (int) (localDir.lastModified() / 1000));
			sftpChannel.setStat(remotePath, attr);
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			sftpChannel.cd("..");
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void syncTime(String fileName, String remoteDir) {

		/**
		 * STEP1. get local file time
		 */

		String localPath = "C:/tmp/";
		String localFile = localPath + "/" + fileName;
		File file = new File(localFile);
		Path path = file.toPath();
		BasicFileAttributes fileATTRS = null;

		try {
			fileATTRS = Files.readAttributes(path, BasicFileAttributes.class);
			showTime("local.lastModifiedTime = ", fileATTRS.lastModifiedTime().toMillis());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		connect();

		try {
			sftpChannel.cd(remoteDir);

			/**
			 * Step2. read remote
			 */
			SftpATTRS attr = sftpChannel.lstat(fileName);
			showTime("\nremote1.atime = ", attr.getATime() * (long) 1000);
			showTime("remote1.mtime = ", attr.getMTime() * (long) 1000);

			/**
			 * change time
			 */
			attr.setACMODTIME(attr.getATime(), (int) (fileATTRS.lastModifiedTime().toMillis() / 1000));
			sftpChannel.setStat(fileName, attr);

			/**
			 * Step2. read remote
			 */
			attr = sftpChannel.lstat(fileName);
			showTime("\nremote2.atime = ", attr.getATime() * (long) 1000);
			showTime("remote2.mtime = ", attr.getMTime() * (long) 1000);

		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void syncDownload(File localDir, String remotePath) {
		debugf("\nsyncAll : localDir = " + localDir.getPath() + " " + localDir.listFiles().length);
		debugf("          remoteDir = " + remotePath);

		if (!localDir.isDirectory()) {
			error("arg is not directory. ");
			return;
		}
		FileInfo fi = new FileInfo();
		fi.setSftpChannel(sftpChannel);
		try {
			sftpChannel.cd(remotePath);
			List<FileInfo> list = fi.lsdir(remotePath);
			for ( FileInfo fp : list) {
				if ( fp.isDirectory() ) {
					// local directory make and cd
					fp.syncDownload(localDir.mkdir(), remotePath+"/"+fp.getLsEntry().getFilename());
				} else {
					fp.get();
				}
			}
		} catch (SftpException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
