package com.barolab.util.sftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

// https://www.javaxp.com/2015/06/jsch-uploaddownload-files-from-remote.html

public class SftpExample {

	private String host;
	private Integer port;
	private String user;
	private String password;

	private JSch jsch;
	private Session session;
	private Channel channel;
	public ChannelSftp sftpChannel;

	public SftpExample(String host, Integer port, String user, String password) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
	}

	public void connect() {

		System.out.println("connecting..." + host);
		try {
			jsch = new JSch();
			session = jsch.getSession(user, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(password);
			session.connect();

			channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;

		} catch (JSchException e) {
			e.printStackTrace();
		}

	}

	public void disconnect() {
		System.out.println("disconnecting...");
		sftpChannel.disconnect();
		channel.disconnect();
		session.disconnect();
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


	public void upload(String fileName, String remoteDir) {

		FileInputStream fis = null;
		connect();
		try {
			// Change to output directory
			String home = sftpChannel.getHome();
			System.out.println("homeDir = " + home);

			sftpChannel.cd(remoteDir);

			// Upload file
			File file = new File(fileName);
			fis = new FileInputStream(file);
			sftpChannel.put(fis, file.getName());
			fis.close();
			System.out.println("File uploaded successfully - " + file.getAbsolutePath());

		} catch (Exception e) {
			e.printStackTrace();
		}
		disconnect();
	}

	public void download(String fileName, String localDir) {

		byte[] buffer = new byte[1024];
		BufferedInputStream bis;
		connect();
		try {
			// Change to output directory
			String cdDir = fileName.substring(0, fileName.lastIndexOf("/") + 1);
			sftpChannel.cd(cdDir);

			File file = new File(fileName);
			bis = new BufferedInputStream(sftpChannel.get(file.getName()));

			File newFile = new File(localDir + "/" + file.getName());

			// Download file
			OutputStream os = new FileOutputStream(newFile);
			BufferedOutputStream bos = new BufferedOutputStream(os);
			int readCount;
			while ((readCount = bis.read(buffer)) > 0) {
				bos.write(buffer, 0, readCount);
			}
			bis.close();
			bos.close();
			System.out.println("File downloaded successfully - " + file.getAbsolutePath());

		} catch (Exception e) {
			e.printStackTrace();
		}
		disconnect();
	}

	public void syncDownload() {
//		String remoteFilePath = "testDir/testFile.txt";
//		SftpATTRS attrs = sftpChannel.lstat(remoteFilePath);
//		SimpleDateFormat format = new SimpleDateFormat(
//		                "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
//		Date modDate = (Date) format.parse(attrs.getMtimeString());
//		String localFilePath = "C:/temp/downloadedFile.txt";
//		sftpChannel.get(remoteFilePath, localFilePath);
//		File downloadedFile = new File(localFilePath);
//		downloadedFile.setLastModified(modDate.getTime());
	}

	public static void main(String[] args) {

		String localPath = "C:/tmp/";
		String remotePath = "/root/BBB/";

		SftpExample ftp = new SftpExample("192.168.25.50", 22, "root", "root123");

		// ftp.upload(localPath + "test4upload.txt", remotePath);
	//	ftp.syncTime("test4upload.txt", remotePath);

		// ftp.download(remotePath + "test4upload.txt", localPath);

	}

}
