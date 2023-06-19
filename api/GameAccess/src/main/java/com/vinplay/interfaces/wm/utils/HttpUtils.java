package com.vinplay.interfaces.wm.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

public class HttpUtils {
	private static final Logger logger = Logger.getLogger(HttpUtils.class);

	public static String queryAPI(String function, Map<String, String> params, String apiUrl) {
		HttpURLConnection conn = null;
		BufferedReader br = null;
		StringBuilder response = new StringBuilder();
		try {
			logger.info("[BaseWmService] function -- " + function + " apiUrl -- " + apiUrl);
			logger.info("[BaseWmService] params -- " + params);
			byte[] postDataBytes = buildPostData(params);
			URL url = new URL(apiUrl + function);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
			conn.setDoOutput(true);
			conn.getOutputStream().write(postDataBytes);
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = br.readLine()) != null) {
				response.append(line);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			if (br != null) {
				try {
					br.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		System.out.println("response -- " + response.toString());
		logger.info("response -- " + response.toString());
		return response.toString();
	}
	
	public static String getData(String url) {
		HttpClient httpClient = new HttpClient();
		HttpClientParams clientParams = new HttpClientParams();
		clientParams.setParameter("http.useragent", "Mozilla/4.0 (compatible; FIREFOX 9.0; IBM AIX 5)");
		clientParams.setHttpElementCharset("UTF-8");
		HttpState httpState = new HttpState();
		httpClient.setParams(clientParams);
		httpClient.getParams().setParameter(HttpClientParams.HTTP_CONTENT_CHARSET, "UTF-8");
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(30000);
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(20000);

		httpClient.setState(httpState);
		clientParams.setVersion(HttpVersion.HTTP_1_1);
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		getMethod.setRequestHeader("Content-Type", "application/json");
		getMethod.setRequestHeader("Accept", "application/json");
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + getMethod.getStatusLine());
			}
			String resStr = getMethod.getResponseBodyAsString();
			return resStr;

		} catch (Exception ex) {
			logger.error("WMService games error：" + ex.getMessage() + "," + url);
		} finally {
			if (getMethod != null) {
				getMethod.releaseConnection();
			}
		}
		return null;
	}
	
	private static byte[] buildPostData(Map<String, String> params) {
		StringBuilder postData = new StringBuilder();
		byte[] postDataBytes = new byte[] {};
		try {
			for (Map.Entry<String, String> param : params.entrySet()) {
				if (postData.length() != 0) {
					postData.append('&');
				}
				postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
				postData.append('=');
				postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
			}
			System.out.println("buildPostData -- " + postData);
			postDataBytes = postData.toString().getBytes("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postDataBytes;
	}
}
