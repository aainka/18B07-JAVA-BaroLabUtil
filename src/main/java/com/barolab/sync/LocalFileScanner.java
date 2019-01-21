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

	public LocalFileScanner(String homeDir) {
		this.homeDir = homeDir;
	}

	public OV_FileInfo scanAll() {
		OV_FileInfo root = new OV_FileInfo("", null, this);
		scan(root);
		OV_FileInfo.dumpTree(root);
		return root;
	}

	public void scan(OV_FileInfo node) {
		// System.out.println(node.getFullPath());
		readTime(node);
		File fp = new File(node.getFullPath());
		if (fp.isDirectory()) {
			node.set_dir(true);
			for (File fpChild : fp.listFiles()) {
				String pathChild = node.getFullPath() + "/" + fpChild.getName();
				if (!IgnoreFile.ignore(pathChild)) {
					OV_FileInfo child = null;
					if (node.getPath().length() > 0) {
						child = new OV_FileInfo(node.getPath() + "/" + fpChild.getName(), node, this);
					} else {
						child = new OV_FileInfo(fpChild.getName(), node, this);
					}
					scan(child);
				}
			}
		}

	}

	// ##########################################################

	public OV_FileInfo scanAll(OV_FileInfo parent, OV_FileInfo node) throws IOException {

		if (node == null) {
			node = new OV_FileInfo("", parent, this);
		}

		System.out.println("l.path= " + node.getFullPath());
		readTime(node);
		File myfp = new File(node.getFullPath());

		if (myfp.isDirectory()) {
			node.set_dir(true);
			for (File fp : myfp.listFiles()) {
				String nPath = node.getPath() + "/" + fp.getName();
				if (!IgnoreFile.ignore(nPath)) {
					OV_FileInfo child = new OV_FileInfo(nPath, node, this);
					readTime(child);
					// System.out.println("l.path.c= " + getName(child) +" npath="+nPath );
					scanAll(node, child);
				}
			}
		}
		return node;
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
		String name = fi.getFullPath();
		BufferedReader br = null;
		String msg = null;
		try {
			System.out.println("Local.read file=" + name);
			br = new BufferedReader(new FileReader(name));
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
		log.info(fi.json());
	}

	private void readTime(OV_FileInfo fi) {
		try {
			File file = new File(homeDir + "/" + fi.getPath());
			Path p = Paths.get(file.getAbsolutePath());
			BasicFileAttributes view = Files.getFileAttributeView(p, BasicFileAttributeView.class).readAttributes();
			FileTime fileTime = view.creationTime();
			fi.setCreated(new Date((view.creationTime().toMillis() / 1000) * 1000));
			fi.setUpdated(new Date((view.lastModifiedTime().toMillis() / 1000) * 1000));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public void write(OV_FileInfo fi) {

		log.info("write =" + fi.json());

		File file = new File(homeDir + "/" + fi.getPath());
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

		File file = new File(homeDir + "/" + fi.getPath());
		Path filePath = Paths.get(file.getAbsolutePath());
		BasicFileAttributeView attributes = Files.getFileAttributeView(filePath, BasicFileAttributeView.class);
		FileTime created = FileTime.fromMillis(fi.getCreated().getTime());
		FileTime updated = FileTime.fromMillis(fi.getUpdated().getTime());
		attributes.setTimes(updated, updated, created); // lastModified, lastAccess, Created
	}

}
