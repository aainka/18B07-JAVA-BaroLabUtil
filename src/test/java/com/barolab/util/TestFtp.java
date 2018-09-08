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

		host = "192.168.25.50";
		host = "1.241.184.143";
		port = 22;
		user = "root";
		password = "root123";
		
		String project_id = "18B07-BaroLabUtil";
		String localPath = "C:/@SWDevelopment/workspace-java/" + project_id;
		String remotePath = "/root/AAA/" + project_id;
		testSync(localPath, remotePath);

		project_id = "18004-DashConsole";
		localPath = "C:/@SWDevelopment/workspace-java/" + project_id;
		remotePath = "/root/AAA/" + project_id;
		testSync(localPath, remotePath);

		// testClean(remotePath);
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

}
