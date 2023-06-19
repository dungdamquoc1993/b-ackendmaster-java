package com.vinplay.utils;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.response.BaseResponse;

public class TelegramUtil {
	
	public static final Logger LOGGER = Logger.getLogger(TelegramUtil.class);
	
    public static boolean sendMessage(String message, String chatId, String bootToken){
    	if(GameThirdPartyInit.enviroment.equals("dev")) {
    		return false;
    	}
        try{
            TelegramBot bot = new TelegramBot(bootToken);
            SendMessage request = new SendMessage(chatId, message)
                    .parseMode(ParseMode.HTML)
                    .disableWebPagePreview(true)
                    .disableNotification(true);
                    //.replyToMessageId(1)
                    //.replyMarkup(new ForceReply());
            SendResponse sendResponse = bot.execute(request);
            boolean ok = sendResponse.isOk();
            return ok;
        }catch (Exception e){
            return false;
        }

    }
    
    public static boolean warningCheat(String message){
    	String chatId ="@qweqwe12dfggg"; 
    	String bootToken ="5215704706:AAGF1nDu1ZaGKOPqLYsn6dpLvuQbaKw_HLAd";
    	
        try{
            TelegramBot bot = new TelegramBot(bootToken);
            SendMessage request = new SendMessage(chatId, message)
                    .parseMode(ParseMode.HTML)
                    .disableWebPagePreview(true)
                    .disableNotification(true);
                    //.replyToMessageId(1);
                    //.replyMarkup(new ForceReply());
            SendResponse sendResponse = bot.execute(request);
            boolean ok = sendResponse.isOk();
            return ok;
        }catch (Exception e){
            return false;
        }

    }
    
	public String pushMessageTelegram(String msg) {
		//"Telegram_chat_id": "@slotnotification","Telegram_boot_token": "1760440761:AAG5Zwtmrh4du8pBeuTqJvaRUP2w8HM3HC8" }
		String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
		// String apiToken = "790982244:AAGnL1sXCvmLDyb_wVuR-1s4utHFvMi16FU";
		// String chatId = "@checkWallet";
		if (msg == null || "".equals(msg))
			return "";
		//, 
		urlString = String.format(urlString, "5215704706:AAGF1nDu1ZaGKOPqLYsn6dpLvuQbaKw_HLAd", "@qweqwe12dfggg",
				msg);
		
		URLConnection conn = null;
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			URL url = new URL(urlString);
			conn = url.openConnection();
			InputStream is = new BufferedInputStream(conn.getInputStream());
			br = new BufferedReader(new InputStreamReader(is));
			String inputLine = "";
			while ((inputLine = br.readLine()) != null) {
				sb.append(inputLine);
			}
		} catch (Exception e) {
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (conn != null)
				((HttpURLConnection) conn).disconnect();
		}

		return sb.toString();
	}
	public static boolean pushNotificationDeposit(String message){
    	String chatId ="@qweqwe12dfggg"; 
    	String bootToken ="5215704706:AAGF1nDu1ZaGKOPqLYsn6dpLvuQbaKw_HLAd";
    	
        try{
            TelegramBot bot = new TelegramBot(bootToken);
            SendMessage request = new SendMessage(chatId, message)
                    .parseMode(ParseMode.HTML)
                    .disableWebPagePreview(true)
                    .disableNotification(true);
                    //.replyToMessageId(1);
                    //.replyMarkup(new ForceReply());
            SendResponse sendResponse = bot.execute(request);
            boolean ok = sendResponse.isOk();
            return ok;
        }catch (Exception e){
            return false;
        }

    }

	public static String sendVerifyCode(String nickname, String phoneNumber, String recaptchaToken) {
		HazelcastInstance instance = HazelcastClientFactory.getInstance();
		IMap<String, String> configCache = instance.getMap("cacheConfig");
		String value = configCache.get("GGFIREBASE").toString();
		Type type = new TypeToken<GoogleFirebaseConfig>() {}.getType();
		GoogleFirebaseConfig googleFirebaseConfig = new Gson().fromJson(value, type);
		HttpClient client = new DefaultHttpClient();
    	BufferedReader reader = null;
		String inputLine;
		StringBuffer response = new StringBuffer();
		try {
			HttpPost request = new HttpPost(googleFirebaseConfig.urlSendCode + googleFirebaseConfig.browerKey);
			StringEntity params = new StringEntity(
					"{\"phoneNumber\":\"" + phoneNumber + "\",\"recaptchaToken\":\"" + recaptchaToken + "\"} ");
			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			HttpResponse httpResponse = client.execute(request);
			reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			while ((inputLine = reader.readLine()) != null) {
				response.append(inputLine);
			}
			JSONObject jsonObj = new JSONObject(response.toString());
			return jsonObj.getString("sessionInfo");
			// return response.toString();
		} catch (Exception ex) {
			LOGGER.error(ex);
			return "";
		}
    }
	
	public static BaseResponse<String> verifyPhoneNumber(String nickname, String sessionInfo, String code) {
    	HazelcastInstance instance = HazelcastClientFactory.getInstance();
		IMap<String, String> configCache = instance.getMap("cacheConfig");
		String value = configCache.get("GGFIREBASE").toString();
		Type type = new TypeToken<GoogleFirebaseConfig>() {}.getType();
		GoogleFirebaseConfig googleFirebaseConfig = new Gson().fromJson(value, type);
		HttpClient client = new DefaultHttpClient();
    	BufferedReader reader = null;
		String inputLine;
		StringBuffer response = new StringBuffer();
		try {
			HttpPost request = new HttpPost(googleFirebaseConfig.urlVerifyPhone + googleFirebaseConfig.serverKey);
			StringEntity params = new StringEntity(
					"{\"sessionInfo\":\"" + sessionInfo + "\",\"code\":\"" + code + "\"} ");
			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			HttpResponse httpResponse = client.execute(request);
			reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			while ((inputLine = reader.readLine()) != null) {
				response.append(inputLine);
			}
			JSONObject jsonObj = new JSONObject(response.toString());
			String phoneNumber = jsonObj.getString("phoneNumber");
			if(phoneNumber==null ||"".equals(phoneNumber)) {
				return new BaseResponse<String>("-1", jsonObj.toString());
			}
			return new BaseResponse<String>("0", phoneNumber);
		} catch (Exception ex) {
			LOGGER.error("verifyphone"+ex);
			return new BaseResponse<String>("99", ex.getMessage());
		}
    }
}
