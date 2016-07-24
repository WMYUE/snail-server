package com.snail.fitment.common.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HttpUtils {
	private static final Logger logger = Logger.getLogger(HttpUtils.class);

	private static final String APPLICATION_JSON = "application/json";
	private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

	public static String connect(String urlString) throws Exception {
		HttpURLConnection connection = openConnection(urlString, "GET");

		if (connection.getResponseCode() > 400) {
			logger.error("request " + urlString + " error, response code "
					+ connection.getResponseCode());
		}

		String response = readContents(connection);
		connection.disconnect();
		if (logger.isDebugEnabled()) {
			logger.debug("request " + urlString + " ,response " + response);
		}

		return response;
	}

	public static String readContents(HttpURLConnection connection)
			throws Exception {
		BufferedReader in = null;
		in = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));

		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}

	public static HttpURLConnection openConnection(String url,
			String requestMethod) throws IOException {
		HttpURLConnection httpConnection = null;

		URL url_ = new URL(url);
		httpConnection = (HttpURLConnection) url_.openConnection();
		httpConnection.setConnectTimeout(5 * 1000);
		httpConnection.setReadTimeout(10 * 1000);
		httpConnection.setRequestMethod(requestMethod);
		httpConnection.setDoOutput(true);
		httpConnection.setDoInput(true);

		return httpConnection;
	}

	public static String post(String url, Map<String, String> postParamMap)
			throws Exception {
		HttpURLConnection connect = openConnection(url, "POST");
		if (postParamMap != null) {
			StringBuffer buffer = new StringBuffer();
			Iterator<String> keyItor = postParamMap.keySet().iterator();
			while (keyItor.hasNext()) {
				String key = keyItor.next();
				String obj = postParamMap.get(key);
				buffer.append(key).append("=").append(obj).append("&");
			}
			connect.getOutputStream().write(buffer.toString().getBytes());
		}
		String response = HttpUtils.readContents(connect);
		connect.disconnect();
		return response;
	}

	public static String URLEncode(String str, String charset) {
		try {
			return URLEncoder.encode(str, charset);
		} catch (UnsupportedEncodingException e) {
			logger.debug("URLEncode error: ", e);
			return str;
		}
	}
/**
 * 针对先要传入一个byte数组的接口，如360的手机归属地接口
 * @param url
 * @param header
 * @param body
 * @return
 * @throws Exception
 */
	public static String postWithByteArrayAndJson(String url, byte[] header, String body)
			throws Exception {
		HttpURLConnection connect = null;
		OutputStream outputStream = null;
		String response = null;
		try {
			connect = HttpUtils.openConnection(url, "POST");
			if (StringUtils.isNotEmpty(body)) {
				outputStream = connect.getOutputStream();
				if (outputStream != null) {
					outputStream.write(header);
					outputStream.write(body.getBytes());
					outputStream.flush();
				}
			}
			response = HttpUtils.readContents(connect);
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
			if (connect != null) {
				connect.disconnect();
			}
		}
		return response;
	}
	
	public static String postWithData(String url, byte[] header, String body) throws Exception {
		HttpURLConnection connect = null;
		String response = null;
		OutputStream outputStream = null;
		try {
			connect = HttpUtils.openConnection(url, "POST");
			if (StringUtils.isNotEmpty(body)) {
				outputStream = connect.getOutputStream();
				outputStream.write(header);
				outputStream.write(body.getBytes());
				outputStream.flush();
			}
			response = HttpUtils.readContents(connect);

		} finally {
			if(outputStream != null){
				outputStream.close();
			}
			if (connect != null) {
				connect.disconnect();
			}
		}

		return response;
	}

	public static String postWithJSON(String url, String json)
			throws ClientProtocolException, IOException {
		String encoderJson = URLEncode(json, "UTF-8");
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
		StringEntity se = new StringEntity(encoderJson);
		String result = "";
		se.setContentType(CONTENT_TYPE_TEXT_JSON);
		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
				APPLICATION_JSON));
		httpPost.setEntity(se);
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			if (httpEntity != null) {
				result = EntityUtils.toString(httpEntity, "UTF-8");
			}
		} finally {
			try {
				if(response != null){					
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

//	public static void main(String[] args){
//		String url = "http://send.gudongqun.com:9080/phoneArea.do";
//		Map<String,String> map  = new HashMap<String,String>();
//		map.put("appid", "ec6ad0a6188a9985");
//		map.put("secret", "c60bf55755fa775a2c8361715e47cc5d");
//		map.put("url", "http://182.118.28.111/OpenapiPhoneArea?key=15810632411");
//		try {
//			String result = post(url,map);
//			System.out.println(result);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
