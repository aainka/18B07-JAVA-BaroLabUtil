package com.barolab.util;

import java.io.File;

import org.junit.Test;

import com.jcraft.jsch.SftpException;

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
		System.out.println("aaaaaaaaaaaaaaaa");
		host = "192.168.25.50";
		port = 22;
		user = "root";
		password = "root123";
		String localPath = "C:/tmp";
		String remotePath = "/root/BBB";
		testSync(localPath, remotePath);
	}

	public void testSync(String localPath, String remotePath) {
		System.out.println("bbbb");
		File localDir = new File(localPath);
		syncFTP = new SftpSync(host, port, user, password);
		syncFTP.connect();
		try {
			syncFTP.sftpChannel.cd(remotePath);

			syncFTP.syncAll(localDir, remotePath);
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		syncFTP.disconnect();
	}

}
