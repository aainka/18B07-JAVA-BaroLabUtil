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
		// OV_FileInfo.dumpTree(root);
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
	public void read(OV_FileInfo node) {
		String name = node.getFullPath();
		readTime(node);

		if (!node.is_dir()) {
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
				node.setText_in_file(sb.toString());

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		log.info(node.json());
	}

	private void readTime(OV_FileInfo node) {
		try {
			File file = new File(homeDir + "/" + node.getPath());
			Path p = Paths.get(file.getAbsolutePath());
			BasicFileAttributes view = Files.getFileAttributeView(p, BasicFileAttributeView.class).readAttributes();
			FileTime fileTime = view.creationTime();
			node.setCreated(new Date((view.creationTime().toMillis() / 1000) * 1000));
			node.setUpdated(new Date((view.lastModifiedTime().toMillis() / 1000) * 1000));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	// #########################################################################
	// ## Read / Write
	// #########################################################################

	@Override
	public OV_FileInfo write(OV_FileInfo node) {
		log.info("write =" + node.json());
		File file = new File(homeDir + "/" + node.getPath());
		FileWriter writer = null;
		try {
			writer = new FileWriter(file, false); // append mode true
			writer.write(node.getText_in_file());
			writer.flush();
			// writeTime(fi);
			{ // Adjust time
				// File file = new File(homeDir + "/" + fi.getPath());
				Path filePath = Paths.get(file.getAbsolutePath());
				BasicFileAttributeView attributes = Files.getFileAttributeView(filePath, BasicFileAttributeView.class);
				FileTime created = FileTime.fromMillis(node.getCreated().getTime());
				FileTime updated = FileTime.fromMillis(node.getUpdated().getTime());
				attributes.setTimes(updated, updated, created); // lastModified, lastAccess, Created
			}
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
		return node;

	}

}
