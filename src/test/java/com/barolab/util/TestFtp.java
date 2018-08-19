package com.barolab.util;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.barolab.util.sftp.SftpSync;

// 최종목표 : syncGit( "project-a" )
// 부가목표 : syncDir(상호데이타 싱크) : USB

public class TestFtp {

	String host;
	Integer port;
	String user;
	String password;
	String localPath = "C:/tmp";
	String remotePath = "/root/BBB";
	SftpSync syncFTP = null;

	@Test
	public void testHost() {
		Logger logger = Logger.getLogger("com.barolab");
		logger.addAppender(new ConsoleAppender());
		logger.setLevel(Level.INFO);

		port = 22;
		user = "root";
		password = "root123";

		host = "1.241.184.143";
		localPath = "C:/@Workspace17A/18B07-BaroLabUtil-master";
		remotePath = "/root/AAA/18B07-BaroLabUtil";
		
		homeEnv();
		
		testDownload(localPath, remotePath);

		// testSync(localPath, remotePath);
		// testClean(remotePath);
	}

	public void homeEnv() {
		host = "192.168.25.50";
		localPath = "C:/tmp2";
		remotePath = "/root/BBB";
	}

	public void testClean(String remotePath) {
		System.out.println("testClean #############################");
		syncFTP = new SftpSync(host, port, user, password);
		syncFTP.connect();
		syncFTP.rmdir(remotePath);
		syncFTP.disconnect();
	}

	public void testSync(String localPath, String remotePath) {
		System.out.println("testSync #############################");
		File localDir = new File(localPath);
		syncFTP = new SftpSync(host, port, user, password);
		syncFTP.connect();
		syncFTP.syncUpload(localDir, remotePath);
		syncFTP.disconnect();
	}
	
	public void testDownload(String localPath, String remotePath) {
		System.out.println("testDownload #############################");
		File localDir = new File(localPath);
		syncFTP = new SftpSync(host, port, user, password);
		syncFTP.connect();
		syncFTP.syncDownload(localDir, remotePath);
		syncFTP.disconnect();
	}

}
