package com.vinplay.interfaces.sexygame;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import com.vinplay.dto.sg.CreateMemberDto;
import com.vinplay.logic.InitData;

public class SGUtils {

	public static void main(String[] args) {
//		String url = "https://testapi.onlinegames22.com/wallet/createMember?cert=\"TIMUQIfaOT69Xe9q9xJ\"&agentId=\"hengwinag\"&userId=\"testplayer3\""
//				+ "&currency=\"THB\"&betLimit=\"{\"SEXYBCRT\":{\"LIVE\":{\"limitId\":[260901, 260902, 260903, 260904, 260905]}}}\"";
////		String response = SGUtils.postData(url);
////		System.out.println(response);
		CreateMemberDto reqDto = new CreateMemberDto();
		reqDto.setUserId("testplayer3");
		reqDto.setCurrency("THB");
		reqDto.setBetLimit("{\"SEXYBCRT\":{\"LIVE\":{\"limitId\":[260901]}}}");
//		Map<String, String> params = buildParams(reqDto);
//		params.put("cert", "TIMUQIfaOT69Xe9q9xJ");
//		params.put("agentId", "hengwinag");
//		String res = QueryAPI("https://testapi.onlinegames22.com/wallet/", "createMember", params);
//		System.out.println(res);
	}
	
	public static String QueryAPI(String apiUrl, String funcName, Map<String, String> params) {
		HttpURLConnection conn = null;
		BufferedReader br = null;
		StringBuilder response = new StringBuilder();
		try {
			byte[] postDataBytes = buildPostData(params);
			URL url = new URL(apiUrl + funcName);
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
			
		return response.toString();
	}

	public static String QueryAPI_GzipResp(String apiUrl, String funcName, Map<String, String> params) {
		HttpURLConnection conn = null;
		BufferedReader br = null;
		StringBuilder response = new StringBuilder();
		try {
			byte[] postDataBytes = buildPostData(params);
			URL url = new URL(apiUrl + funcName);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
			conn.setRequestProperty("Accept-Encoding", "gzip");
			conn.setRequestProperty("charset", "utf-8");
			conn.setDoOutput(true);
			conn.getOutputStream().write(postDataBytes);
			InputStream in = new GZIPInputStream(conn.getInputStream());
			Reader decoder = new InputStreamReader(in, "UTF-8");
			br = new BufferedReader(decoder);
			response.append(br.readLine());
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
				postData.append(param.getKey());
				postData.append('=');
				postData.append(param.getValue());
			}
				System.out.println("buildPostData -- " + postData);
			postDataBytes = postData.toString().getBytes("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postDataBytes;
	}
	
}
