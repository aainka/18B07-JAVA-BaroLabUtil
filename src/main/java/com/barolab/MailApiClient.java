package com.barolab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;



public class MailApiClient {

	private String url = "http://110.13.71.93:9292/api/V1/mail"; // raspberry
	// private String url = "http://211.239.124.246:19808/api/V1/mail"; // fun25
	HttpClient httpclient = new DefaultHttpClient();
	private String session_token = null;
	// private String apiKey =
	// "6498a8ad1beb9d84d63035c5d1120c007fad6de706734db9689f8996707e0f7d";
	// private ObjectMapper objectMapper = new ObjectMapper();

	public MailApiClient() {
		// objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		// objectMapper.setDateFormat(df);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	}

	private void test() {
		try {
			List<OV_MailContent> list = new LinkedList<OV_MailContent>();
			OV_MailContent item = new OV_MailContent();
			item.subject = "TestMail from MailApiClient";
			item.message = "<html> Body in English 한글<br> xx<br>xxx </html>";
			list.add(item);

			for (int i = 0; i < 1; i++) {
				insert(list);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param tasks
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public int insert(List<OV_MailContent> tasks) throws  IOException {
		HttpPost request = new HttpPost(url);
		String jString = encodeList(tasks);
		StringEntity entity = new StringEntity(jString, "UTF-8");
		entity.setContentType("application/json; charset=utf-8");
		request.setEntity(entity);
		HttpResponse response = httpclient.execute(request);
		HttpEntity resEntity = response.getEntity();
		if (resEntity != null) {
			String s = getEntityString(entity);
			System.out.println("POST: " + s);
		}
		EntityUtils.consumeQuietly(response.getEntity());
		return 0;
	}

//	public void update(List<OV_MailContent> tasks) throws JsonGenerationException, JsonMappingException, IOException {
//		HttpPatch request = new HttpPatch(url);
//		String jString = OV_MailContent.encodeList(tasks);
//		{
//			StringEntity entity = new StringEntity(jString, "UTF-8");
//			entity.setContentType("application/json; charset=utf-8");
//			request.setEntity(entity);
//			HttpResponse response = httpclient.execute(request);
//			printResponse(response, null);
//		}
//
//	}

	public List<OV_MailContent> list() throws ClientProtocolException, IOException {
		System.out.println("FROM WEB");
		HttpGet request = new HttpGet(url + "/");
		// setApiKey(request);
		HttpResponse response = httpclient.execute(request);
		List<OV_MailContent> list = null;
		HttpEntity entity = response.getEntity();
		// System.out.println(response.getStatusLine());
		if (entity != null) {
			// System.out.println("--Response content length: " +
			// entity.getContentLength());
			String s = getEntityString(entity);
			// DebugConsole.println(s);
			list = decode(s);
			for (int i = 0; i < 30; i++) {
				// DebugConsole.println(list.get(i).dump());
			}
			// try {
			// ResourceUnit emp = objectMapper.readValue(s.getBytes(), ResourceUnit.class);
			// System.out.println("Response = " + emp);
			// } catch (IOException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
		}
		return list;
	}

	public void setApiKey77(HttpRequestBase request) {
		// request.addHeader("X-DreamFactory-Api-Key", apiKey);
		if (session_token != null) {
			request.addHeader("X-DreamFactory-Session-Token", session_token);
		}
	}

	public void login(String userId, String passwd) throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(url + "user/session");
		// setApiKey(httpPost);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("app_name", "admin"));
		nameValuePairs.add(new BasicNameValuePair("email", "aainka@naver.com"));
		nameValuePairs.add(new BasicNameValuePair("password", "root123"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
		httpPost.setEntity(entity);
		HttpResponse response = httpclient.execute(httpPost);
		session_token = printResponse(response, "session_token");
	}

	public String printResponse(HttpResponse response, String findKey) {
		String findValue = null;
		HttpEntity entity = response.getEntity();
		// System.out.println(response.getStatusLine());
		if (entity != null) {
			// System.out.println("Response content length: " +
			// entity.getContentLength());
			String s = getEntityString(entity);
//			try {
//				if (findKey != null) {
//					JSONParser parser = new JSONParser();
//					JSONObject jsonObject = (JSONObject) parser.parse(s);
//					findValue = jsonObject.get(findKey).toString();
//				}
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		return findValue;
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

	public static List<OV_MailContent> decode(String s) {
		// List<OV_Task> list = new ArrayList<OV_Task>();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Type listType = new TypeToken<List<OV_MailContent>>() {
		}.getType();
		// Gson gson = new GsonBuilder().setPrettyPrinting().create();
		List<OV_MailContent> list = gson.fromJson(s, listType);
		return list;
	}

	public static String encodeList(List<OV_MailContent> tasks) {
		StringWriter writer = new StringWriter();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Type listType = new TypeToken<List<OV_MailContent>>() {
		}.getType();
		gson.toJson(tasks, listType, writer);
		return writer.toString();
	}

	public static void main(String arg[]) {
		new MailApiClient().test();
	}
}
