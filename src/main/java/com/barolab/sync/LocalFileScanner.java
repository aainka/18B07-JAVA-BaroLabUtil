package com.barolab.sync;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.List;

public class LocalFileScanner extends FileScanner {

	public OV_FileInfo scanAll(OV_FileInfo parent, OV_FileInfo myfi) {

		String path = myfi.getName();

		if (homeDir == null) {
			homeDir = path;
		}

		System.out.println("path=" + path);

		// OV_FileInfo myfi = new OV_FileInfo(path, parent, this);
		updateTime(myfi);
		File myfp = new File(path);
		if (myfp.isDirectory()) {
			myfi.set_dir(true);
			for (File fp : myfp.listFiles()) {
				String nPath = path + "/" + fp.getName();
				if (!IgnoreFile.ignore(nPath)) {
					OV_FileInfo cfi = new OV_FileInfo(nPath, myfi, this);
					updateTime(cfi);
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

	private void updateTime(OV_FileInfo fi) {
		File file = new File(fi.getName());
		Path p = Paths.get(file.getAbsolutePath());
		try {
			BasicFileAttributes view = Files.getFileAttributeView(p, BasicFileAttributeView.class).readAttributes();
			FileTime fileTime = view.creationTime();
			fi.setCreated(new Date((fileTime.toMillis()/1000)*1000));
			fi.setUpdated(new Date((view.lastModifiedTime().toMillis()/1000)*1000));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void read(OV_FileInfo fi) {
		updateTime(fi);
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
		System.out.println(fi.json());
	}
 

}
