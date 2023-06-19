package com.vinplay.interfaces.ag.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.Logger;

import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.utils.DESEncrypt;

public final class HttpUtils {
	private static final Logger logger = Logger.getLogger(HttpUtils.class);
	/**
	 * Post data base on URL and params 
	 * 
	 * @param url
	 * @param params
	 * @return response body as string
	 */
	public static String postData(String url, String params) {
		// set param
		HttpClient httpClient = new HttpClient();
		HttpClientParams clientParams = new HttpClientParams();
		clientParams.setHttpElementCharset("UTF-8");
		clientParams.setParameter("http.useragent", "Mozilla/4.0 (compatible; FIREFOX 9.0; IBM AIX 5)");
		clientParams.setParameter(HttpClientParams.HTTP_CONTENT_CHARSET, "UTF-8");
		httpClient.setParams(clientParams);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(20000);
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		// create GetMethod
		PostMethod method = null;
		BufferedReader br =null;
		String resStr = null;
		long startTime = System.currentTimeMillis();
		try {
			DESEncrypt des = new DESEncrypt(GameThirdPartyInit.AG_ENCRYPT);
			String targetParams = des.encrypt(params);
			String key = md5(targetParams + GameThirdPartyInit.AG_MD5);
			String urlRequest = String.format("%s%s%s%s%s", url, "params=", targetParams, "&key=", key);
			logger.info("[HttpUtils] AG Request url :" + urlRequest);
			method = new PostMethod(urlRequest);
			method.addRequestHeader("User-Agent", GameThirdPartyInit.AG_PREFIX_USER_AGENT + "_" + GameThirdPartyInit.AG_CAGENT);
			int statusCode = httpClient.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Method failed: " + method.getStatusLine());
				return resStr;
			}
			long elapsedTimeAfterResponse = System.currentTimeMillis() - startTime;
			logger.info("AG: Total elapsed http request/response time in milliseconds: " + elapsedTimeAfterResponse);
			InputStream response = method.getResponseBodyAsStream();
			br= new BufferedReader(new InputStreamReader(response, "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			return sb.toString();
		} catch (Exception ex) {
			logger.error("getWebContentByURL:" + url, ex);
		} finally {
			if(method!=null)
				method.releaseConnection();
			if(br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.error("ex", e);
				}
			}
		}
		return resStr;
	}

	public static String getReportData(String url, String keyParams) {
		//encrypt
		String key = md5(keyParams + GameThirdPartyInit.AG_DATA_API_CODE);
		url = url.replaceAll(" ", "%20") + "&key=" + key;
		logger.info("[HttpUtils] AG Report request url :" + url);
		//set param
		HttpClient httpClient = new HttpClient();
		HttpClientParams clientParams = new HttpClientParams();
		clientParams.setParameter("http.useragent", "Mozilla/4.0 (compatible; FIREFOX 9.0; IBM AIX 5)");
		clientParams.setHttpElementCharset("UTF-8");
		clientParams.setVersion(HttpVersion.HTTP_1_1);
		clientParams.setParameter(HttpClientParams.HTTP_CONTENT_CHARSET, "UTF-8");
		httpClient.setParams(clientParams);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(20000);
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		GetMethod getMethod = null;
		String resStr = null;
		try {
			getMethod = new GetMethod(url);
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Method failed: " + getMethod.getStatusLine());
			}
			resStr = getMethod.getResponseBodyAsString();
			logger.info("[HttpUtils] Response body when get report data: " + resStr);
		} catch (Exception ex) {
			logger.error("[HttpUtils] AG Exception :getWebContentByURL:", ex);
		} finally {
			if (getMethod != null)
				getMethod.releaseConnection();
		}
		return resStr;
	}
	public static void main(String[] args) {
		String url ="http://jtg1g0.gdcapi.com:3333/getorders.xml?cagent=JG0&startdate=2021-04-23 00:00:00&enddate=2021-04-23 00:05:00&by=ASC&page=1&perpage=1000";
		String keyParams="JG02021-04-23 00:00:002021-04-23 00:05:00ASC11000";
		getReportData(url, keyParams);
		
	}

	public static String md5(String str) {
		StringBuilder result = new StringBuilder();
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			digest.update(str.getBytes());
			BigInteger bigInteger = new BigInteger(1, digest.digest());
			result = new StringBuilder(bigInteger.toString(16));
			while (result.length() < 32) { 
				result.insert(0, "0");
            } 
		} catch (NoSuchAlgorithmException e) {
			
			logger.error("ex", e);
		}
		return result.toString();
	}
}
