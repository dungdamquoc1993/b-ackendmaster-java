package com.vinplay.interfaces.ibc2.utils;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpUtils {
	private static final Logger logger = Logger.getLogger(HttpUtils.class);

	public static String QueryAPI(String funtion, Map<String, String> params, String apiUrl) {
		HttpURLConnection conn = null;
		BufferedReader br = null;
		StringBuffer response = new StringBuffer();
		try {
			logger.info("funtion -- " + funtion + " apiUrl -- " + apiUrl);
			logger.info("params -- " + params);
			byte[] postDataBytes = buildPostData(params);
			URL url = new URL(apiUrl + funtion);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
			conn.setDoOutput(true);
			conn.getOutputStream().write(postDataBytes);
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
			String line;
			while ((line = br.readLine()) != null) {
				response.append(line);
			}
		} catch (Exception ex) {
			logger.error("exp", ex);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			if (br != null) {
				try {
					br.close();
				} catch (Exception ex) {
					logger.error("exp", ex);
				}
			}
		}
		logger.info("response -- " + response.toString());
		return response.toString();
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
			logger.info("buildPostData -- " + postData);
			postDataBytes = postData.toString().getBytes("UTF-8");
		} catch (Exception e) {
			logger.error("exp", e);
		}
		return postDataBytes;
	}
}
