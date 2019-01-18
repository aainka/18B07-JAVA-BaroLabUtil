package com.barolab.sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class TestFileRest {

	private String url = "http://localhost:9292/api/V1/file"; // raspberry
	HttpClient httpclient = new DefaultHttpClient();
	Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd HH:mm:ss zzz").create();

	public List<OV_FileInfo> getDir(String dir) throws ClientProtocolException, IOException {
		URI uri = null;
		try {
			URIBuilder builder = new URIBuilder();
			builder.setScheme("http").setHost("localhost:9292").setPath("/api/V1/file") //
					.setParameter("dir", dir);
			uri = builder.build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpGet request = new HttpGet(uri);
		HttpResponse response = httpclient.execute(request);
		if (response.getStatusLine().getStatusCode() != 200) {
			System.out.println(response.getStatusLine());
		}
		/*
		 * make return object
		 */
		List<OV_FileInfo> list = null;
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			String s = getEntityString(entity);
			Type listType = new TypeToken<List<OV_FileInfo>>() {
			}.getType();
			list = gson.fromJson(s, listType);
			for (OV_FileInfo fi : list) {
				System.out.println(fi.getName());
			}
		}
		return list;

//		HttpPost request = new HttpPost(url);
//		String json_str = gson.toJson(args);
//		StringEntity entity = new StringEntity(json_str, "UTF-8");
//		entity.setContentType("application/json; charset=utf-8");
//		request.setEntity(entity);
//		HttpResponse response = httpclient.execute(request);
//		HttpEntity resEntity = response.getEntity();
//		if (resEntity != null) {
//			String s = getEntityString(entity);
//			System.out.println("POST: " + s);
//		}
//		EntityUtils.consumeQuietly(response.getEntity());
//		return null;
	}

	public List<OV_FileInfo> readFile(String readfile) throws ClientProtocolException, IOException {
		URI uri = null;
		try {
			URIBuilder builder = new URIBuilder();
			builder.setScheme("http").setHost("localhost:9292").setPath("/api/V1/file") //
					.setParameter("readfile", readfile);
			uri = builder.build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpGet request = new HttpGet(uri);
		HttpResponse response = httpclient.execute(request);
		if (response.getStatusLine().getStatusCode() != 200) {
			System.out.println(response.getStatusLine());
		}
		/*
		 * make return object
		 */
		List<OV_FileInfo> list = null;
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			String s = getEntityString(entity);
			System.out.println("File.content=" + s);
			Type listType = new TypeToken<List<OV_FileInfo>>() {
			}.getType();
			list = gson.fromJson(s, listType);
			for (OV_FileInfo fi : list) {
				System.out.println(fi.getName());
			}
		}
		return list;
	}

	private String getEntityString(HttpEntity entity) {
		String sContent = null;
		// System.out.println("Response content length: " +
		// entity.getContentLength());
		BufferedReader rd;
		try {
			rd = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
			String line = "";
			sContent = line;
			while ((line = rd.readLine()) != null) {
				// System.out.println(line);
				sContent += line;
			}
		} catch (UnsupportedOperationException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("ans=" + sContent);
		return sContent;
	}

	public static void main(String arg[]) {
		TestFileRest fr = new TestFileRest();
		try {
			for (int i = 1; i < 10000; i++) {
				System.out.println("======================  " + i);
				fr.getDir("c:/tmp/Doc");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
