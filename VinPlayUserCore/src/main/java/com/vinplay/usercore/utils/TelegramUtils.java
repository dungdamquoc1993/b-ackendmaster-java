package com.vinplay.usercore.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.shotfish.entites.TeleBotConfig;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.statics.Consts;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;

public class TelegramUtils {

    public static int postRequest( String chatId, String content) throws IOException {
        try {
            TeleBotConfig botConfig = getConfig();
            String url = "https://api.telegram.org/bot" + botConfig.secretKey + "/sendMessage";
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("chat_id", chatId).addFormDataPart("text", content).build();
            Request request = new Request.Builder().url(url).method("POST", body).build();
            Response response = client.newCall(request).execute();
            return response.code();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static TeleBotConfig getConfig() {
        try {
            HazelcastInstance instance = HazelcastClientFactory.getInstance();
            IMap<String, String> configCache = instance.getMap(Consts.CACHE_CONFIG);
            String value = configCache.get(CacheConfigName.TELEGRAMBOTCONFIGCACHE).toString();
            Type type = new TypeToken<TeleBotConfig>() {
            }.getType();
            TeleBotConfig teleBotConfig = new Gson().fromJson(value, type);
            return teleBotConfig;
        } catch (Exception e) {
            return null;
        }
    }
}
