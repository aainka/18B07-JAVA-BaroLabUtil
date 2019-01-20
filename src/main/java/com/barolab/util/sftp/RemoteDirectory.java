package com.barolab.util.sftp;

import java.util.Vector;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpException;

import lombok.extern.java.Log;

@Log
public class RemoteDirectory {

	private ChannelSftp sftpChannel;
	private String remotePath;

	public RemoteDirectory(ChannelSftp sftpChannel) {
		this.sftpChannel = sftpChannel;
	}

	public RemoteDirectory cd(String remotePath) {
		try {
			sftpChannel.cd(remotePath);
			this.remotePath = remotePath;
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}

	public FileSet ls()   {
		FileSet fileSet = new FileSet();
		Vector filelist;
		try {
			filelist = sftpChannel.ls(".*");
			for (int i = 0; i < filelist.size(); i++) {
				fileSet.add(new FileInfo((LsEntry) filelist.get(i), remotePath, sftpChannel));
			}
			filelist = sftpChannel.ls("*");
			for (int i = 0; i < filelist.size(); i++) {
				fileSet.add(new FileInfo((LsEntry) filelist.get(i), remotePath, sftpChannel));
			}
			log.fine("remote.ls:: " + remotePath+ " count="+fileSet.size());
			return fileSet;
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.warning("remote.ls:: " + remotePath);
		return null;
	}

}
