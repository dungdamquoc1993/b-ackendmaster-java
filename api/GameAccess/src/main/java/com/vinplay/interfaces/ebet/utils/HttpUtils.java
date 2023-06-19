package com.vinplay.interfaces.ebet.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import com.vinplay.logic.CommonMethod;

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
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);

		httpClient.setState(httpState);
		clientParams.setVersion(HttpVersion.HTTP_1_1);
		long t1 = System.currentTimeMillis();
		PostMethod postMethod = new PostMethod(url);
		String responSt = null;
		try {
			postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
			StringRequestEntity requestEntity = new StringRequestEntity(json, "application/json", "UTF-8");
			postMethod.setRequestEntity(requestEntity);
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Method failed: " + postMethod.getStatusLine());
			}
			long t2 = System.currentTimeMillis();
			logger.info("EBET: Total elapsed http request/response time in milliseconds: " + (t2 - t1));
			responSt =  CommonMethod.convInputStream2String(postMethod.getResponseBodyAsStream());
		} catch (Exception ex) {
			logger.error("getWebContentByURL:", ex);
		} finally{
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return responSt;
	}



	public static String getData(String url) {
		HttpClient httpClient = new HttpClient();
		HttpClientParams clientParams = new HttpClientParams();
		clientParams.setParameter("http.useragent", "Mozilla/4.0 (compatible; FIREFOX 9.0; IBM AIX 5)");
		clientParams.setHttpElementCharset("UTF-8");
		HttpState httpState = new HttpState();
		httpClient.setParams(clientParams);
		httpClient.getParams().setParameter(HttpClientParams.HTTP_CONTENT_CHARSET, "UTF-8");
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(10000);
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		httpClient.setState(httpState);
		
		clientParams.setVersion(HttpVersion.HTTP_1_1);
		PostMethod getMethod = new PostMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		String responSt = null;
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Method failed: " + getMethod.getStatusLine());
			}
			responSt = getMethod.getResponseBodyAsString();
		} catch (Exception ex) {

			logger.error("getWebContentByURL:"+ "," + url, ex);
		} finally{
			if (getMethod!= null) {
				getMethod.releaseConnection();
			}
		}
		return responSt;
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
		bin.close();
		in.close();
		return sb.toString();
	}

	public static String sendData(String urls, String xml) {
		URL url = null;
		OutputStreamWriter out  = null;
		BufferedReader br = null;
		URLConnection connection = null;
		try {
			
			try {
				url = new URL(urls);// "https://api.jxf2012.org/cgibin/EGameIntegration"
			} catch (MalformedURLException e) {
				logger.error("URL ERROR", e);
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
			out = new OutputStreamWriter(connection.getOutputStream(), "8859_1");
			out.write(xml);
			br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String readLine = "";
			StringBuilder resultStr = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				resultStr.append(readLine);
			}
			
			return resultStr.toString();
		} catch (IOException e) {
			
			logger.error("sendData", e);
		} finally {
			if(out!=null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					logger.error("ex", e);
				}
			}
			if(br!=null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.error("ex", e);
				}
			}
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
		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
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
}
