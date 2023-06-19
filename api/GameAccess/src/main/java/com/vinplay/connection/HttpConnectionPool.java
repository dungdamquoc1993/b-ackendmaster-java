///**
// * Archie
// */
//package com.vinplay.connection;
//
//import java.io.IOException;
//import java.net.URLEncoder;
//import java.security.KeyManagementException;
//import java.security.KeyStoreException;
//import java.security.NoSuchAlgorithmException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import javax.net.ssl.HostnameVerifier;
//import javax.net.ssl.SSLSession;
//
//import org.apache.commons.httpclient.HttpStatus;
//import org.apache.http.HttpEntity;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.config.CookieSpecs;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.config.Registry;
//import org.apache.http.config.RegistryBuilder;
//import org.apache.http.conn.socket.ConnectionSocketFactory;
//import org.apache.http.conn.socket.PlainConnectionSocketFactory;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.ssl.SSLContextBuilder;
//import org.apache.http.util.EntityUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//
//
///**
// * @author Archie
// *
// */
//public class HttpConnectionPool {
//	
//	private static final Logger logger = LoggerFactory.getLogger(HttpConnectionPool.class);
//	public static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 30;
//    public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 5;
//    
//	
//	private final static RequestConfig defaultRequestConfig = RequestConfig.custom()
//            .setSocketTimeout(30000)
//            .setConnectTimeout(30000)
//            .setConnectionRequestTimeout(30000)
//            .setContentCompressionEnabled(true)
//            .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
//            .build();
//
//	private final static CloseableHttpClient getClosableHttpClient() {
//        return HttpClients.custom()
//                .useSystemProperties()
//                .setConnectionManager(getConnectionManager())
//                .setDefaultRequestConfig(defaultRequestConfig)
//                .setMaxConnPerRoute(DEFAULT_MAX_CONNECTIONS_PER_ROUTE)
//                .setMaxConnTotal(DEFAULT_MAX_TOTAL_CONNECTIONS)
//                .build();
//    }
//    
//	private static PoolingHttpClientConnectionManager getConnectionManager(){
//    	//manage pool connection
//        Registry<ConnectionSocketFactory> socketFactoryRegistry = null;
//        try {
//            SSLConnectionSocketFactory trustSelfSignedSocketFactory = new SSLConnectionSocketFactory(
//                        new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
//                        new TrustAllHostNameVerifier());
//            socketFactoryRegistry = RegistryBuilder
//                    .<ConnectionSocketFactory> create()
//                    .register("http", new PlainConnectionSocketFactory())
//                    .register("https", trustSelfSignedSocketFactory)
//                    .build();
//        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
//			logger.error("PoolingHttpClientConnectionManager", e);
//        }
//        
//        PoolingHttpClientConnectionManager cm = (socketFactoryRegistry != null) ? 
//                new PoolingHttpClientConnectionManager(socketFactoryRegistry):
//                new PoolingHttpClientConnectionManager();
//        
//        //set
//        cm.setMaxTotal(DEFAULT_MAX_CONNECTIONS_PER_ROUTE);
//        cm.setDefaultMaxPerRoute(20);
//        
//        return cm;
//    }
//    
//    private static class TrustAllHostNameVerifier implements HostnameVerifier {
//        public boolean verify(String hostname, SSLSession session) {
//            return true;
//        }
//    }
//    
//	  private static CloseableHttpClient httpClient;
//    private static CloseableHttpClient getHttpClient() {
//		if (httpClient == null) {
//			PoolingHttpClientConnectionManager pool = new PoolingHttpClientConnectionManager();
//			pool.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTIONS_PER_ROUTE);
//			pool.setMaxTotal(DEFAULT_MAX_TOTAL_CONNECTIONS);
//			httpClient = HttpClients.custom().setConnectionManager(pool).build();
//		}
//		return httpClient;
//	}
//	
//	
//	
//
//	
//	public static String postData(String url , Map<String, Object> params) {
//        CloseableHttpResponse response = null;
//        List<NameValuePair> pairs = null;
//		if (params != null && !params.isEmpty()) {
//        	pairs = new ArrayList<>(params.size());
//            for (Map.Entry<String, Object> entry : params.entrySet()) {
//                String value = entry.getValue().toString();
//                if (value != null) {
//                    pairs.add(new BasicNameValuePair(entry.getKey(), value));
//                }
//            }
//        }
//		HttpPost httpPost = new HttpPost(url);
//        try {
//            if (pairs != null && pairs.size() > 0) {
//                httpPost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
//            }
//            response = getHttpClient().execute(httpPost);
//            HttpEntity entity = response.getEntity();
//			
//			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
//				httpPost.abort();
//				logger.error("Method failed status ={} " , response.getStatusLine());
//				logger.error(EntityUtils.toString(entity));
//				return null;
//			}
//			
//			if (entity != null) {
//				String responseStr = EntityUtils.toString(entity, "UTF-8");
//				EntityUtils.consume(entity);
//				if (responseStr != null && !responseStr.isEmpty()) {
//					return responseStr;
//				}
//			}
//            EntityUtils.consume(entity);
//        } catch (Exception e) {
//        	logger.error(e.getMessage());
//        	return null;
//        }finally {
//			if(response!=null) {
//				try {
//					response.close();
//				} catch (IOException e) {
//					logger.error(e.getMessage());
//				}
//			}
//		}
//        return null;
//    }
//	
//	public static String getDatas(String url ,Map<String, String> headerParam) {
//		CloseableHttpResponse response = null;
//		HttpGet httpGet = new HttpGet(url);
//		// add header
//		if (headerParam != null && !headerParam.isEmpty()) {
//			for (Map.Entry<String, String> param : headerParam.entrySet()) {
//				httpGet.setHeader(param.getKey(), param.getValue());
//			}
//		}
//		try {
//			response = getClosableHttpClient().execute(httpGet);
//			HttpEntity entity = response.getEntity();
//			
//			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
//				httpGet.abort();
//				logger.error("Method failed {} " , response.getStatusLine());
//				logger.error(EntityUtils.toString(entity));
//			}
//			
//			if (entity != null) {
//				String responseStr = EntityUtils.toString(entity, "UTF-8");
//				EntityUtils.consume(entity);
//				if (responseStr != null && !responseStr.isEmpty()) {
//					return responseStr;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e.getMessage());
//			return "";
//		} finally {
//			if (response != null) {
//				try {
//					response.close();
//				} catch (IOException e) {
//					logger.error(e.getMessage());
//				}
//			}
//		}
//		return "";
//	}
//	
//	
//public static String getData(String url ,Map<String, String> params) {
//		
//		CloseableHttpResponse response = null;
//		HttpGet httpGet = null;
//		
//		if (params != null && !params.isEmpty()) {
//			String urlParams = buildURLParams(params);
//			httpGet = new HttpGet(url + urlParams);
//		} else {
//			httpGet = new HttpGet(url);
//		}
//		try {
//			response = getClosableHttpClient().execute(httpGet);
//			HttpEntity entity = response.getEntity();
//			
//			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
//				httpGet.abort();
//				logger.error("Method failed , status code ={} " + response.getStatusLine());
//				logger.error(EntityUtils.toString(entity));
//				EntityUtils.consume(entity);
//				return null;
//			}
//			
//			if (entity != null) {
//				String responseStr = EntityUtils.toString(entity, "UTF-8");
//				EntityUtils.consume(entity);
//				if (responseStr != null && !responseStr.isEmpty()) {
//					return responseStr;
//				}
//			}
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			return null;
//		} finally {
//			if (response != null) {
//				try {
//					response.close();
//				} catch (IOException e) {
//					logger.error(e.getMessage());
//				}
//			}
//		}
//		return null;
//	}
//	
//	
//	private static String buildURLParams(Map<String, String> params) {
//		StringBuilder urlBuilder = new StringBuilder();
//		try {
//			for (Map.Entry<String, String> param : params.entrySet()) {
//				urlBuilder.append('&');
//				urlBuilder.append(URLEncoder.encode(param.getKey(), "UTF-8"));
//				urlBuilder.append('=');
//				urlBuilder.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
//			}
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			return "";
//		}
//		return urlBuilder.toString();
//	}
//}
