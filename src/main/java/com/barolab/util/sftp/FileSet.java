package com.barolab.util.sftp;

import java.io.File;
import java.util.LinkedList;

import lombok.extern.java.Log;

@Log
public class FileSet extends LinkedList<FileInfo> {

	public FileInfo findByNameTime(File localfile) {
		for (FileInfo fi : this) {
			if (localfile.getName().equals(fi.getLsEntry().getFilename())) {
				long localModified = localfile.lastModified() / 1000;
				if (localModified == fi.getLsEntry().getAttrs().getMTime()) {
					log.fine("match=" + localfile.getName());
					return fi;
				}

			}
// can't support create time
// FYI BasicFileAttributes attr = Files.readAttributes(localfile.toPath(),
// BasicFileAttributes.class);
		}
		return null;
	}

//	public void build() {
//	log.info("getFiles.Remote" + remotePath);
//	LinkedList<FileInfo> fileInfos = new LinkedList<FileInfo>();
//	Vector filelist = sftpChannel.ls("*");
//	for (int i = 0; i < filelist.size(); i++) {
//		fileInfos.add(new FileInfo((LsEntry) filelist.get(i), remotePath, sftpChannel));
//	}
//	return fileInfos;
//	}
//
//	public void getFile(File localfile) {
//		// name and create date matching
//		
//	}

}
