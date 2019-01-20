package com.barolab.sync;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.List;

import lombok.extern.java.Log;

@Log
public class LocalFileScanner extends FileScanner {

	public OV_FileInfo scanAll(OV_FileInfo parent, OV_FileInfo myfi) throws IOException {

		String path = myfi.getName();

		if (homeDir == null) {
			homeDir = path;
		}

		// System.out.println("path=" + path);

		// OV_FileInfo myfi = new OV_FileInfo(path, parent, this);
		readTime(myfi);
		File myfp = new File(path);
		if (myfp.isDirectory()) {
			myfi.set_dir(true);
			for (File fp : myfp.listFiles()) {
				String nPath = path + "/" + fp.getName();
				if (!IgnoreFile.ignore(nPath)) {
					OV_FileInfo cfi = new OV_FileInfo(nPath, myfi, this);
					readTime(cfi);
					scanAll(myfi, cfi);
				} else {
				}
			}
		}
		return myfi;
	}

	@Override
	public List<OV_FileInfo> getDir(String dir) {
		// TODO Auto-generated method stub
		return null;
	}

	// #########################################################################
	// ## Read / Write
	// #########################################################################

	@Override
	public void read(OV_FileInfo fi) {
		BufferedReader br = null;
		String msg = null;
		try {
			br = new BufferedReader(new FileReader(fi.getName()));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			fi.setText_in_file(sb.toString());
			readTime(fi);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("xxxxxxxxxxxxxxxxxxxxxx");
		log.info("xxxx"+fi.json());
	}

	private void readTime(OV_FileInfo fi) throws IOException {
		File file = new File(fi.getName());
		Path p = Paths.get(file.getAbsolutePath());
		BasicFileAttributes view = Files.getFileAttributeView(p, BasicFileAttributeView.class).readAttributes();
		FileTime fileTime = view.creationTime();
		fi.setCreated(new Date((view.creationTime().toMillis() / 1000) * 1000));
		fi.setUpdated(new Date((view.lastModifiedTime().toMillis() / 1000) * 1000));
	}

	@Override
	public void write(OV_FileInfo fi) {
		File file = new File(fi.getName());
		FileWriter writer = null;
		try {
			// 기존 파일의 내용에 이어서 쓰려면 true를, 기존 내용을 없애고 새로 쓰려면 false를 지정한다.
			writer = new FileWriter(file, false);
			writer.write(fi.getText_in_file());
			writer.flush();
			writeTime(fi);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void writeTime(OV_FileInfo fi) throws IOException {
		File file = new File(fi.getName());
		Path filePath = Paths.get(file.getAbsolutePath());
		BasicFileAttributeView attributes = Files.getFileAttributeView(filePath, BasicFileAttributeView.class);
		FileTime created = FileTime.fromMillis(fi.getCreated().getTime());
		FileTime updated = FileTime.fromMillis(fi.getUpdated().getTime());
		attributes.setTimes(updated, updated, created); // lastModified, lastAccess, Created
	}

}
