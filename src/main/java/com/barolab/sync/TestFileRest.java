package com.barolab.sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import lombok.Data;

@Data
public class TestFileRest {

	private String host ;
	private String homeDir;

	private String host1 = "localhost:9292"; // raspberry
	private String host2  = "100.99.14.164:9292"; // 13Floor
	private String test_dir = "/root/restapi/";
	HttpClient httpclient = new DefaultHttpClient();

	Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

	public List<OV_FileInfo> getDir(String dir) throws Exception {
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost(host).setPath("/api/V1/file") //
				.setParameter("dir", dir);
		URI uri = builder.build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = httpclient.execute(request);
		String s = getEntityString(response);
		//s = s.replaceAll("\t", "");
		// System.out.println("Get.Body=" + s);
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

	public OV_FileInfo readFile(String readfile) throws Exception {
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost(host).setPath("/api/V1/file") //
				.setParameter("readfile", readfile);
		URI uri = builder.build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = httpclient.execute(request);
		String s = getEntityString(response);
		if (s != null) {
			OV_FileInfo fileinfo = gson.fromJson(s, OV_FileInfo.class);
			System.out.println(fileinfo.json());
			return fileinfo;
		}
		return null;
	}

	public void writeFileDir(OV_FileInfo finfo) throws Exception {
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost(host).setPath("/api/V1/file");
		URI uri = builder.build();
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
				fileinfo.json();
			}
		}
//		EntityUtils.consumeQuietly(response.getEntity());
	}

	public void test_write() throws Exception {
		OV_FileInfo fi = new OV_FileInfo();
		fi.setName(test_dir + "testmemo.dir");
		fi.set_dir(true);
		fi.setText_in_file("content_note");
		try {
			fi.setCreatedStr("2020-2-2 20:20:20");
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			writeFileDir(fi);
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void test_read() throws Exception {
		OV_FileInfo fi = new OV_FileInfo();
		try {
			fi = readFile(test_dir + "testmemo.txt");
		} catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("fi.s = " + fi.json());
	}

	private String getEntityString(HttpResponse response) throws Exception {
		String sContent = null;
		if (response.getStatusLine().getStatusCode() != 200) {
			System.out.println(response.getStatusLine());
			throw new Exception("HTTP ERROR code="+response.getStatusLine().getStatusCode() );
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
		for (int i = 1; i < 2; i++) {
			System.out.println("======================  " + i);
 			try {
				fr.getDir("/root/restapi");
			} catch (IOException | URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// fr.test_write();
			//fr.test_read();
		}
	}

	
}
