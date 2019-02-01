package com.barolab.sync;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import lombok.extern.java.Log;

@Log
public class RemoteFileScanner extends FileScanner {

//	TestFileRest httpApi = null;
	HttpClient httpclient = new DefaultHttpClient();
	Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

	private String host;

	public RemoteFileScanner(String host, String homeDir) {
		this.host = host;
		this.homeDir = homeDir;
	}

	public OV_FileInfo scanAll() {
		OV_FileInfo root = new OV_FileInfo("", null, this);
		root.set_dir(true);
		try {
			scan(root);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// OV_FileInfo.dumpTree(root);
		return root;
	}

	public void scan(OV_FileInfo node) {
		if (node.is_dir()) {
			try {
				List<OV_FileInfo> list = getDir(node.getPath());
				for (OV_FileInfo child : list) {
					if (!IgnoreFile.ignore(child.getFullPath(this))) {
						node.add(child);
						scan(child);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public List<OV_FileInfo> getDir(String path) throws Exception {
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost(host).setPath("/api/V1/file") //
				.setParameter("home", homeDir) //
				.addParameter("path", path);
		URI uri = builder.build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = httpclient.execute(request);
		String s = getEntityString(response);
		if (s != null) {
			Type listType = new TypeToken<List<OV_FileInfo>>() {
			}.getType();
			List<OV_FileInfo> list = gson.fromJson(s, new TypeToken<List<OV_FileInfo>>() {
			}.getType());
//			for (OV_FileInfo fi : list) {
//				System.out.println(fi.json());
//			}
			return list;
		}
		return null;
	}

	@Override
	public void read(OV_FileInfo fi) {
		// System.out.println("Remote: Read");
		// String name = getName(fi);
		OV_FileInfo a;
		try {
			a = httpGetRead(fi.getPath());
			fi.copyFrom(a);
			log.fine(a.json());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public OV_FileInfo httpGetRead(String path) throws Exception {
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost(host).setPath("/api/V1/file") //
				.setParameter("home", homeDir) //
				.setParameter("readfile", path);
		URI uri = builder.build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = httpclient.execute(request);
		String s = getEntityString(response);
		log.finer("httpGetRead.body = " + s);
		if (s != null) {
			OV_FileInfo node = gson.fromJson(s, OV_FileInfo.class);
			log.finer(node.json());
			// System.out.println(fileinfo.json());
			return node;
		}
		return null;
	}

	/*
	 * Remote에 화일생성
	 */
	public OV_FileInfo write(OV_FileInfo finfo) {
		OV_FileInfo a = null;
		try {
			a = httpWrite(finfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	public OV_FileInfo httpWrite(OV_FileInfo finfo) throws Exception {
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost(host).setPath("/api/V1/file") //
				.setParameter("home", homeDir);
		URI uri = builder.build();
		log.fine(uri.toString());
		HttpPost request = new HttpPost(uri);
		if (finfo != null) {
			String json_string = gson.toJson(finfo);
			StringEntity entity = new StringEntity(json_string, "UTF-8");
			entity.setContentType("application/json; charset=utf-8");
			request.setEntity(entity);

			HttpResponse response = httpclient.execute(request);
			String s = getEntityString(response);
			if (s != null) {
				OV_FileInfo fileinfo = gson.fromJson(s, OV_FileInfo.class);
				// fileinfo.json();
				log.fine("OK=" + s);
				return fileinfo;
			}

		}
//		EntityUtils.consumeQuietly(response.getEntity());
		return null;
	}

	private String getEntityString(HttpResponse response) throws Exception {
		String sContent = null;
		if (response.getStatusLine().getStatusCode() != 200) {
			System.out.println(response.getStatusLine());
			throw new Exception("HTTP ERROR code=" + response.getStatusLine().getStatusCode());
		} else {
			HttpEntity entity = response.getEntity();
			BufferedReader reader;
			reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
			String line = "";
			sContent = line;
			while ((line = reader.readLine()) != null) {
				sContent += line;
			}
		}
		return sContent;
	}

	@Override
	public OV_FileInfo scanAll(OV_FileInfo parent, OV_FileInfo myfi) {
		String name = myfi.getFullPath();
		// String path = myfi.getName();

		/*
		 * make node and scan
		 */
		// System.out.println("host="+host+" path=" + path);
		// OV_FileInfo myfi = new OV_FileInfo(path, parent, this);
		try {
			for (OV_FileInfo cfi : getDir(name)) {
				if (IgnoreFile.ignore(name)) {
					continue;
				}
				myfi.add(cfi);
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

}
