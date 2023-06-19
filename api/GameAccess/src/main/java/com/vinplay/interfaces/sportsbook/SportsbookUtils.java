package com.vinplay.interfaces.sportsbook;

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

import com.vinplay.logic.InitServlet;
import com.vinplay.usercore.utils.GameThirdPartyInit;

/**
 * Utils services to hand shake with Sports book platform
 * 
 * @author shanks
 *
 */
public class SportsbookUtils {
	private static final Logger logger = Logger.getLogger(SportsbookUtils.class);

	/**
	 * Send request to Sports book platform
	 * 
	 * @param params
	 * @return
	 */
	public static String callToSportsbook(String method, Map<String, String> params) {
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
		String urlParams = buildURLParams(params);
		String url = GameThirdPartyInit.SPORTS_BOOK_API_SERVER_ROOT + "?" + "Method=" + method + urlParams;
		GetMethod getMethod = new GetMethod(url);
		// 使用系统提供的默认的恢复策略
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			long startTime = System.currentTimeMillis();
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.info("Sportsbook Method failed: " + getMethod.getStatusLine());
			}
			// 读取内容
			String resStr = getMethod.getResponseBodyAsString();
			long elapsedTimeAfterResponse = System.currentTimeMillis() - startTime;
			logger.info("Sportsbook: Total elapsed http request/response time in milliseconds: "
					+ elapsedTimeAfterResponse);
			return resStr;
		} catch (Exception ex) {
			logger.error("Sportsbook getWebContentByURL:", ex);
		} finally {
			if (getMethod != null) {
				getMethod.releaseConnection();
			}
		}
		return null;
	}

	private static String buildURLParams(Map<String, String> params) {
		StringBuilder urlBuilder = new StringBuilder();
		try {
			for (Map.Entry<String, String> param : params.entrySet()) {
				urlBuilder.append('&');
				urlBuilder.append(URLEncoder.encode(param.getKey(), "UTF-8"));
				urlBuilder.append('=');
				urlBuilder.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return urlBuilder.toString();
	}
}
