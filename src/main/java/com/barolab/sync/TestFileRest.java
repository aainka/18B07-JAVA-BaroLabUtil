package com.barolab.sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.Data;

@Data
public class TestFileRest {

	// private String host;
	private String homeDir;
	private String host;

	private String host1 = "localhost:9292"; // raspberry
	private String host2 = "100.99.14.164:9292"; // 13Floor
	private String test_dir = "/root/restapi/";
	HttpClient httpclient = new DefaultHttpClient();

	Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	RemoteFileScanner scanner = new RemoteFileScanner("localhost:9292", "C:/tmp");

	public void test() {
		// test_dir();
		try {
			//test_read();
			  test_write();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * test dir
	 */

	public void test_dir() {
		try {
			List<OV_FileInfo> list = scanner.getDir("");
			for (OV_FileInfo fi : list) {
				// System.out.println(fi.getPath());
				test_recur(fi, "    ");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void test_recur(OV_FileInfo fi, String indent) throws Exception {
		System.out.println(indent + fi.getPath());
		if (fi.is_dir()) {
			// System.out.println("D#"+indent + fi.getPath());
			List<OV_FileInfo> list = scanner.getDir(fi.getPath());
			for (OV_FileInfo c : list) {
				System.out.println(c.getPath());
				test_recur(c, "    " + indent);
			}
		}
	}

	/*
	 * test read
	 */
	public void test_read() throws Exception {
		System.out.println("############ Read Test");
		OV_FileInfo fi = new OV_FileInfo();
		try {
			fi = scanner.readFile("first_foot.txt");
		} catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("fi.s = " + fi.json());
	}

	/*
	 * test write
	 */

	public void test_write() throws Exception {
		OV_FileInfo fi = new OV_FileInfo();
		fi.setPath("first_foot4.txt");
		fi.set_dir(true);
		fi.setText_in_file("content_note");
		try {
			fi.setCreatedStr("2020-2-2 20:20:20");
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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

	public static void main(String arg[]) throws Exception {
		TestFileRest fr = new TestFileRest();
		fr.test();
		for (int i = 1; i < 2; i++) {
			System.out.println("======================  " + i);

			// fr.test_write();
			// fr.test_read();
		}
	}

}
