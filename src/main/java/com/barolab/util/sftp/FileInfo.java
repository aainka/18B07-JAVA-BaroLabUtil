package com.barolab.util.sftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

import lombok.Data;

@Data

public class FileInfo {

	private Logger logger = Logger.getLogger(this.getClass());
	private LsEntry lsEntry;
	private ChannelSftp sftpChannel;
	private String remotePath;

	public FileInfo(LsEntry lsEntry, String remotePath, ChannelSftp sftpChannel) {
		this.sftpChannel = sftpChannel;
		logger.info(("fileInfo = " + lsEntry.getFilename()));
		this.lsEntry = lsEntry;
		this.remotePath = remotePath;
	}

	public FileInfo() {
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return lsEntry.getFilename();
	}

	public boolean isDirectory() {
		SftpATTRS attr = lsEntry.getAttrs();
		return attr.isDir();
	}

	public void rm() throws SftpException {
		sftpChannel.rm(lsEntry.getFilename());
	}

	public void rmdir() throws SftpException {
		logger.info("rmdir : " + remotePath);
		/**
		 * check directory empty
		 */
		List<FileInfo> fileInfos = lsdir(remotePath + "/" + lsEntry.getFilename());
		sftpChannel.cd(remotePath + "/" + lsEntry.getFilename());
		for (FileInfo fi : fileInfos) {
			if (fi.isDirectory()) {
				fi.rmdir();
			} else {
				fi.rm();
			}
		}
		sftpChannel.cd("..");
		sftpChannel.rmdir(lsEntry.getFilename());
	}

	public List<FileInfo> lsdir(String remotePath) throws SftpException {
		LinkedList<FileInfo> fileInfos = new LinkedList<FileInfo>();
		logger.info("lsdir : " + remotePath);
		sftpChannel.cd(remotePath);
		Vector filelist = sftpChannel.ls("*");
		for (int i = 0; i < filelist.size(); i++) {
			fileInfos.add(new FileInfo((LsEntry) filelist.get(i), remotePath, sftpChannel));
		}
		sftpChannel.cd("..");
		return fileInfos;
	}

	public void cd_mkdir(String remotePath) {
		try {
			SftpATTRS attr = sftpChannel.lstat(remotePath);
		} catch (SftpException e) {
			if (e.id == sftpChannel.SSH_FX_NO_SUCH_FILE) {
				try {
					sftpChannel.mkdir(remotePath);
				} catch (SftpException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			sftpChannel.cd(remotePath);
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void put(File file) {
		try {
			System.out.println("put: " + file.getAbsolutePath());
			FileInputStream fis = new FileInputStream(file);
			sftpChannel.put(fis, file.getName());
			fis.close();
			/**
			 * Sync Time
			 */
			SftpATTRS attr = sftpChannel.lstat(file.getName());
			attr.setACMODTIME(attr.getATime(), (int) (file.lastModified()  / 1000));
			sftpChannel.setStat(file.getName(), attr);
		} catch (SftpException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
