package com.barolab.util.sftp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

import lombok.extern.java.Log;

// 일단 싱크에만 집중하자.
// 없는 화일을 검사해서 지워주어야 한다. (쉬운방법은 기존에 있던것을 모두 지우고 새로 넣는 것이다.)
// 결과는 동일하다. 어느 것이 쉬운가?
// 문제는 GIT 정보까지 없어지는 것이 문제이다.
// 결론은 JGIT이다. 금방 다운받아서 쓰고 버리기. 
// 그랫으면 기존에 사용하지

@Log
public class SftpSync {

	private JSch jsch = new JSch();
	private Session session = null;
	
//	private OutputStream output = null;
//	private InputStream input = null;
	private Channel channel = null;
	public ChannelSftp sftpChannel;

	public void testHost() {
		configLog();
		connect("root", "root123", "1.241.184.143", 22);
		String project_id = "18B07-BaroLabUtil";
		String localPath = "C:/@SWDevelopment/workspace-java/" + project_id;
		String remotePath = "/root/AAA/" + project_id;
		testSync(localPath, remotePath);
		disconnect();

//		project_id = "18004-DashConsole";
//		localPath = "C:/@SWDevelopment/workspace-java/" + project_id;
//		remotePath = "/root/AAA/" + project_id;
//		testSync(localPath, remotePath);

	}

	public void configLog() {
		System.out.println("logConfig");
		Logger log0 = Logger.getLogger("Platfrom.DashConsole.*");
		Logger p = log0.getParent();
		for (Handler h : p.getHandlers()) {
			p.removeHandler(h);
		}
		p.addHandler(new com.barolab.log.ConcoleHandler());
		log0.setUseParentHandlers(false);
		log0 = Logger.getLogger("com.barolab.*");
		log0.setLevel(Level.INFO);
	}

	public void testSync(String localPath, String remotePath) {
		File localDir = new File(localPath);
		syncUpload(localDir, remotePath);
	}

	public void testClean(String remotePath) {
		System.out.println("testClean #############################");
		rmdir(remotePath);
		disconnect();
	}

	public void test() {
		String localPath = "C:/tmp";
		String remotePath = "/root/BBB";

		// connect();
		try {
			sftpChannel.cd(remotePath);
			File localDir = new File(localPath);
			// syncAll(localDir, remotePath);
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();

	}

	/*
	 * --------------------------------------- connect, disconnect
	 * ---------------------------------------
	 */

	public void connect(String user, String passwd, String host, int port) {
		log.info("connecting..." + host);
		try {
			jsch = new JSch();
			session = jsch.getSession(user, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(passwd);
			session.connect();

			channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;
		} catch (JSchException e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		sftpChannel.disconnect();
		channel.disconnect();
		session.disconnect();
		log.info("disconnecting...OK");
	}

	public void rmdir(String remotePath) {
		log.info("rmdir0:=" + remotePath);
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
		int count = 0;
//		log.info("LocalDir = " + localDir.getPath() + " #" + localDir.listFiles().length);
//		log.info("RemotePath = " + remotePath);

		if (!localDir.isDirectory()) {
			log.warning("arg is not directory. ");
			return;
		}
		FileInfo fi = new FileInfo();
		fi.setSftpChannel(sftpChannel);
		fi.cd_mkdir(remotePath);

		/**
		 * get Remote file list and check
		 */

		RemoteDirectory remote = new RemoteDirectory(sftpChannel);
		FileSet fileSet = remote.cd(remotePath).ls();

		for (File localfile : localDir.listFiles()) {
			if (localfile.isDirectory()) {
				syncUpload(localfile, remotePath + "/" + localfile.getName());
			} else {
				if (fileSet.findByNameTime(localfile) == null) {
					fi.put(localfile);
					count++;
				} else {
					log.fine("no move file = " + localfile.getName());
				}
			}
		}
		/**
		 * Sync Time
		 */
	
		try {
			SftpATTRS attr = sftpChannel.lstat(remotePath);
			int timeModified = (int) (localDir.lastModified() / 1000);
			attr.setACMODTIME(attr.getATime(), timeModified);
			sftpChannel.setStat(remotePath, attr);
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			if (e.getMessage().indexOf("Failure") >= 0) {
				log.fine("RemoteDir.timeset: " + remotePath + " 4: Failure Exception");
			} else {
				e.printStackTrace();
			}
		}

		try {
			sftpChannel.cd("..");
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (count > 0) {
			log.info("LocalDir = " + localDir.getPath() + " #" + count + "/" + localDir.listFiles().length);
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

	protected void showTime(String debug, long longTime) {
		java.util.Date date = new Date(longTime);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String strTime = simpleDateFormat.format(longTime);
		System.out.println(debug + " : " + strTime);

//	    ZonedDateTime t = Instant.ofEpochMilli(cTime).atZone(ZoneId.of("Asia/Seoul"));
//        String dateCreated = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(t);
//        System.out.println("Creation time:   "+dateCreated);
	}

	static public void main(String[] args) {
		new SftpSync().testHost();
	}

}
