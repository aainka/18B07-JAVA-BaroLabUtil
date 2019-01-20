package com.barolab.sync;

import java.util.List;

public class RemoteFileScanner extends FileScanner {

	TestFileRest httpApi = new TestFileRest();
	private String host;

	public RemoteFileScanner(String host) {
		this.host = host;
	}

	@Override
	public List<OV_FileInfo> getDir(String dir) {
		return null;
	}

	@Override
	public OV_FileInfo scanAll(OV_FileInfo parent, OV_FileInfo myfi) {
		String name = getName(myfi);

		// String path = myfi.getName();

		httpApi.setHost(host);
		/*
		 * make node and scan
		 */
		// System.out.println("host="+host+" path=" + path);
		// OV_FileInfo myfi = new OV_FileInfo(path, parent, this);
		try {
			for (OV_FileInfo cfi : httpApi.getDir(name)) {
				if (IgnoreFile.ignore(name)) {
					continue;
				}
				myfi.add(cfi, this);
				if (!cfi.is_dir()) {
					// myfi.add(cfi);
				} else {
					scanAll(myfi, cfi);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myfi;
	}

	/*
	 * Remote에 화일생성
	 */
	public void write(OV_FileInfo finfo) {
		try {
			httpApi.writeFileDir(finfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void read(OV_FileInfo fi) {
		System.out.println("Remote: Read");
		String name = getName(fi);
		OV_FileInfo a;
		try {
			a = httpApi.readFile(name);
			fi.copyFrom(a);
			System.out.println(a.json());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
