///**
// * Archie
// */
//package com.vinplay.service.esport.ipml;
//
//import java.util.concurrent.TimeUnit;
//
//import javax.net.ssl.HostnameVerifier;
//import javax.net.ssl.SSLSession;
//
//import org.apache.log4j.Logger;
//
//import com.vinplay.logic.InitServlet;
//
//import okhttp3.ConnectionPool;
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
///**
// * @author Archie
// *
// */
//public class EsportUtil {
//
//	private static final Logger LOGGER = Logger.getLogger(EsportUtil.class);
//	
//	public final static OkHttpClient client = new OkHttpClient
//			.Builder()
//			.connectTimeout(30,TimeUnit.SECONDS)
//		    .readTimeout(30, TimeUnit.SECONDS)
//		    .writeTimeout(30, TimeUnit.SECONDS)
//		    .connectionPool(new ConnectionPool(50, 3, TimeUnit.MINUTES))
//		    .hostnameVerifier(new HostnameVerifier() {
//				
//				@Override
//				public boolean verify(String hostname, SSLSession session) {
//					return true;
//				}
//			})
//		    .retryOnConnectionFailure(true)
//		    .build();
//	
//	public static String postData(String url, String json) throws Exception {
//		//String json = new JSONObject(params).toString();
//		LOGGER.info("EsportUtil request , url=" + url + " ,json =" + json);
//		
//		MediaType mediaType = MediaType.parse("application/json");
//		RequestBody body = RequestBody.create(mediaType, json);
//		
//		Request request = new Request.Builder()
//				.url(url)
//				.method("POST", body)
//				.addHeader("Content-Type", "application/json")
//				.addHeader("Authorization", InitServlet.ESPORT_PRIVATE_TOKEN)
//				.build();
//		try (Response response = client.newCall(request).execute()) {
//			int statusCode =response.code();
//			if(statusCode==200 || statusCode==201) {
//				return response.body().string();
//			}
//			LOGGER.error(response.body().string());
//		} catch (Exception e) {
//			e.printStackTrace();
//			LOGGER.error(e);
//			return null;
//		}
//		
//		return null;
//	}
//	
//	public static String getData(String url) throws Exception {
//		LOGGER.info("EsportUtil request , url=" + url);
//		
//		Request request = new Request.Builder()
//				.url(url)
//				.method("GET",null)
//				.addHeader("Content-Type", "application/json")
//				.addHeader("Authorization", InitServlet.ESPORT_PRIVATE_TOKEN)
//				.build();
//		try (Response response = client.newCall(request).execute()) {
//			int statusCode =response.code();
//			if(statusCode==200 || statusCode==201) {
//				return response.body().string();
//			}
//			LOGGER.error(response.body().string());
//		} catch (Exception e) {
//			e.printStackTrace();
//			LOGGER.error(e);
//			return null;
//		}
//		
//		return null;
//	}
//}
