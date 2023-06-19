/**
 * Archie
 */
package com.vinplay.connection;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import org.apache.log4j.Logger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;

/**
 * @author Archie
 *
 */

public class ConnectionPoolUtil {

	private static final Logger LOGGER = Logger.getLogger(ConnectionPoolUtil.class);
	
	private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
	public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

	public final static OkHttpClient client = new OkHttpClient
			.Builder()
			.connectTimeout(30,TimeUnit.SECONDS)
		    .readTimeout(30, TimeUnit.SECONDS)
		    .writeTimeout(30, TimeUnit.SECONDS)
		    .connectionPool(new ConnectionPool(220, 3, TimeUnit.MINUTES))
		    .hostnameVerifier(new HostnameVerifier() {
				
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			})
		    .build();
	
	//inner class
	private static class CallbackFuture extends CompletableFuture<Response> implements Callback {
		/**
		 * @author Archie
		 *
		 */
		@Override
		public void onResponse(Call call, Response response) {
	         super.complete(response);
	    }
		@Override
	    public void onFailure(Call call, IOException e){
	         super.completeExceptionally(e);
	    }
		
	}

	public static String runStream(String url) throws Exception {
		RequestBody requestBody = new RequestBody() {
			
			@Override
			public MediaType contentType() {
				return MEDIA_TYPE_MARKDOWN;
			}

			@Override
			public void writeTo(BufferedSink sink) throws IOException {
				sink.writeUtf8("Numbers\n");
				sink.writeUtf8("-------\n");
//				for (int i = 2; i <= 997; i++) {
//					sink.writeUtf8(String.format(" * %s = %s\n", i, factor(i)));
//				}
			}

//			private String factor(int n) {
//				for (int i = 2; i < n; i++) {
//					int x = n / i;
//					if (x * i == n)
//						return factor(x) + " × " + i;
//				}
//				return Integer.toString(n);
//			}
		};

		Request request = new Request.Builder()
				.url(url)
				.post(requestBody)
				.build();

		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful())
				throw new IOException("Unexpected code " + response);

			System.out.println(response.body().string());
			return response.body().string();
		}
	}
	public static String postData(String url, String json, String[] token) throws Exception {
		LOGGER.info("request , url=" + url + " ,json =" + json + " ,token=" + token);
		
		RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);
		if(token==null) {
			Request request = new Request.Builder()
					.url(url)
					.method("POST", body)
					.addHeader("Content-Type", "application/json")
					.build();
			try (Response response = client.newCall(request).execute()) {
				int statusCode =response.code();
				if(response.isSuccessful() && (statusCode==200 || statusCode==201)) {
					return response.body().string();
				}
				LOGGER.error(response.body().string());
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error(e);
				return null;
			}
			
		}else {
			Request request = new Request.Builder()
					.url(url)
					.method("POST", body)
					.addHeader("Content-Type", "application/json")
					.addHeader(token[0], token[1] == null ? "" : token[1])// "Authorization"
					.build();
			try (Response response = client.newCall(request).execute()) {
				int statusCode =response.code();
				if(response.isSuccessful() && (statusCode==200 || statusCode==201)) {
					return response.body().string();
				}
				LOGGER.error(response.body().string());
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error(e);
				return null;
			}
			
		}
		
		return null;
	}

	public static String getData(String url, String[] privateToken) throws Exception {
		Request request = null;
		if(privateToken==null || privateToken.length==0) {
			request = new Request.Builder()
					.url(url)
					.method("GET",null)
					.addHeader("Content-Type", "application/json")
					.build();
		}else {
			request = new Request.Builder()
					.url(url)
					.method("GET",null)
					.addHeader("Content-Type", "application/json")
					.addHeader(privateToken[0], privateToken[1] == null ? "" : privateToken[1])
					.build();
		}
		LOGGER.info("request get url=" + url + " ,privateToken=" + privateToken);
		try (Response response = client.newCall(request).execute()) {
			int statusCode =response.code();
			if(response.isSuccessful() && (statusCode==200 || statusCode==201)) {
				return response.body().string();
			}
			LOGGER.error(response.body().string());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e);
			return null;
		}
		
		return null;
	}
	
	// asynchronize
	public static String runGet(String url, String[] privateToken) throws Exception {
		LOGGER.info("request get url=" + url + " ,privateToken=" + privateToken[1]);
		Request request = new Request.Builder()
				.url(url)
				.method("GET",null)
				.addHeader("Content-Type", "application/json")
				.addHeader(privateToken[0], privateToken[1] == null ? "" : privateToken[1])
				.build();
		
		CallbackFuture future = new CallbackFuture();
		
		client.newCall(request).enqueue(future);
		
		try (Response response = future.get()) {
			int statusCode = response.code();
			if (response.isSuccessful() && (statusCode == 200 || statusCode == 201)) {
				return response.body().string();
			}
			LOGGER.error(response.body().string());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}
		return null;		
	}
	
	public static String runPostParam(String url, java.util.Map<String, String> params, String privateToken) throws Exception {
		LOGGER.info("request post url=" + url + " ,param=" + params + " ,privateToken=" + privateToken);
		FormBody.Builder builder = new FormBody.Builder();
		
		// Add Params to Builder
		for (java.util.Map.Entry<String, String> entry : params.entrySet()) {
			builder.add(entry.getKey(), entry.getValue());
		}
		
		RequestBody formBody = builder.build();
		Request request = null;
		if("".equals(privateToken)) {
			request = new Request.Builder()
			        .url(url)
			        .addHeader("Content-Type", "application/json")
			        .post(formBody)
			        .build();
		}else {
			request = new Request.Builder()
			        .url(url)
			        .addHeader("Content-Type", "application/json")
					.addHeader("Authorization", privateToken)
			        .post(formBody)
			        .build();
		}
		

		try (Response response = client.newCall(request).execute()) {
			int statusCode =response.code();
			if(response.isSuccessful() && (statusCode==200 || statusCode==201)) {
				ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
				return responseBodyCopy.string();
			}
			LOGGER.error(response.body().string());
			System.out.println(response.body().string());
			
			return response.body().string();
		}catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e);
			return null;
		}
		
	}
	
}
