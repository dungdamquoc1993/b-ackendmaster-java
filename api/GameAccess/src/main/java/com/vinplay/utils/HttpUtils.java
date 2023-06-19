package com.vinplay.utils;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.servlet.http.HttpServletRequest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtils {
	private static final Logger logger = Logger.getLogger(HttpUtils.class);

	public static String postData(String url, String json) {
		HttpClient httpClient = new HttpClient();
		HttpClientParams clientParams = new HttpClientParams();
		clientParams.setParameter("http.useragent", "Mozilla/4.0 (compatible; FIREFOX 9.0; IBM AIX 5)");
		clientParams.setHttpElementCharset("UTF-8");
		HttpState httpState = new HttpState();
		httpClient.setParams(clientParams);
		httpClient.getParams().setParameter(HttpClientParams.HTTP_CONTENT_CHARSET, "UTF-8");
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(20000);
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(20000);

		httpClient.setState(httpState);
		clientParams.setVersion(HttpVersion.HTTP_1_1);
		String resStr = null;
		PostMethod postMethod = new PostMethod(url);
		try {
			
			// 使用系统提供的默认的恢复策略
			postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
			StringRequestEntity requestEntity = new StringRequestEntity(json, "application/json", "UTF-8");
			postMethod.setRequestEntity(requestEntity);
			int statusCode = httpClient.executeMethod(postMethod);
			logger.info("statusCode" + statusCode);
			// logger.info("获取图片状态："+statusCode);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Method failed: " + postMethod.getStatusLine());
			}
			// 读取内容
			resStr = postMethod.getResponseBodyAsString();
//			logger.info(resStr);
//			return resStr;

		} catch (Exception ex) {

			logger.error("getWebContentByURL:" + "," + url, ex);
		} finally {
			postMethod.releaseConnection();
		}
		return resStr;
	}



	public static String getData(String url) {
		HttpClient httpClient = new HttpClient();
		HttpClientParams clientParams = new HttpClientParams();
		clientParams.setParameter("http.useragent", "Mozilla/4.0 (compatible; FIREFOX 9.0; IBM AIX 5)");
		// httpClient.getHttpConnectionManager().getParams().setSoTimeout(30 *
		// 1000);
		clientParams.setHttpElementCharset("UTF-8");
		HttpState httpState = new HttpState();
		httpClient.setParams(clientParams);
		httpClient.getParams().setParameter(HttpClientParams.HTTP_CONTENT_CHARSET, "UTF-8");
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(30000);
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(20000);

		// httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
		// Config.20000);//连接时间20s
		// httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
		// 60000);
		httpClient.setState(httpState);
		clientParams.setVersion(HttpVersion.HTTP_1_1);
		PostMethod getMethod = new PostMethod(url);
		// 使用系统提供的默认的恢复策略
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		String resStr = null;
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			// logger.info("获取图片状态："+statusCode);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Method failed: " + getMethod.getStatusLine());
			}
			// 读取内容
			resStr = getMethod.getResponseBodyAsString();
			// logger.info(resStr);
//			return resStr;

		} catch (Exception ex) {

			logger.error("getWebContentByURL:"+ "," + url, ex);
		} finally {
			getMethod.releaseConnection();
		}
		return resStr;
	}

	public static String SSLGetMethod(String urls) throws Exception {
		URL url = new URL(urls);
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String urlHostName, SSLSession session) {
				logger.info("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
				return true;
			}
		};
		try {
			trustAllHttpsCertificates();
			HttpsURLConnection.setDefaultHostnameVerifier(hv);
		} catch (Exception ex) {
			
			logger.error("SSLGetMethod", ex);
		}

		InputStream in = url.openStream();
		BufferedReader bin = new BufferedReader(new InputStreamReader(in, "GB2312"));
		String s = null;
		StringBuilder sb = new StringBuilder("");
		while ((s = bin.readLine()) != null) {
			// logger.infoln(s);
			sb.append(s);
		}
		in.close();
		bin.close();
		return sb.toString();
	}

	public static String sendData(String urls, String xml) {
		URL url = null;
		URLConnection connection = null;
		try {
			
			try {
				url = new URL(urls);// "https://api.jxf2012.org/cgibin/EGameIntegration"
			} catch (MalformedURLException e) {
				logger.error("ex", e);
			}

			HostnameVerifier hv = new HostnameVerifier() {
				public boolean verify(String urlHostName, SSLSession session) {
					logger.info("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
					return true;
				}
			};
			try {
				trustAllHttpsCertificates();
				HttpsURLConnection.setDefaultHostnameVerifier(hv);
			} catch (Exception ex) {
				
				logger.error("sendData", ex);
			}
			connection = url.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "8859_1");
			out.write(xml);
			out.flush();
			out.close();
			InputStream in = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String readLine = "";
			StringBuilder resultStr = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				resultStr.append(readLine);
			}
			in.close();
			br.close();
			return resultStr.toString();
		} catch (IOException e) {
			logger.error("sendData", e);
		} finally {
			if (connection != null)
				((HttpURLConnection)connection).disconnect();
		}
		return null;
	}

	private static void trustAllHttpsCertificates() throws Exception {
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		javax.net.ssl.TrustManager tm = new miTM();
		trustAllCerts[0] = tm;
		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}
	}
	
	public static String getIpAddressCF(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		String clientIp = null;
		if (ipAddress != null && !"".equals(ipAddress)) {
			String[] arrayIp = ipAddress.split(",");
			if(arrayIp.length>0) {
				clientIp = arrayIp[0].trim();
			}
		}
		return clientIp;
	}
//	 public static String getIpAddress(HttpServletRequest request) {
//	        String ip = request.getHeader("x-forwarded-for");
//	        if ((ip == null) || (ip.length() == 0)
//	                || ("unknown".equalsIgnoreCase(ip))) {
//	            ip = request.getHeader("Proxy-Client-IP");
//	        }
//	        if ((ip == null) || (ip.length() == 0)
//	                || ("unknown".equalsIgnoreCase(ip))) {
//	            ip = request.getHeader("WL-Proxy-Client-IP");
//	        }
//	        if ((ip == null) || (ip.length() == 0)
//	                || ("unknown".equalsIgnoreCase(ip))) {
//	            ip = request.getRemoteAddr();
//	        }
//	        return ip;
//	    }
//	 

		public static String getIpAddress(HttpServletRequest request) {
			String ipAddress = request.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
			}
			String clientIp = null;
			if (ipAddress != null && !"".equals(ipAddress)) {
				String[] arrayIp = ipAddress.split(",");
				if(arrayIp.length>0) {
					clientIp = arrayIp[0].trim();
				}
			}
			return clientIp;
		}
}
