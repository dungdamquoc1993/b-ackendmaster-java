package com.vinplay.interfaces.ebet;

import java.util.Date;

import org.apache.log4j.Logger;

import com.vinplay.dto.ebet.BaseReqDto;
import com.vinplay.interfaces.ebet.utils.DateDeserializer;
import com.vinplay.interfaces.ebet.utils.HttpUtils;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class BaseEbetService {
	private static final Logger logger = Logger.getLogger(BaseEbetService.class);
	protected Gson gson;
	protected GsonBuilder gsonBuilder;
	
	public BaseEbetService() {
		gson = new Gson();
		gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
	}

	public String getData(String method, BaseReqDto reqDto) {
		String json = buildJson(reqDto);
		logger.info(method + " request:" + json);
		String data = "";
		data = HttpUtils.postData(GameThirdPartyInit.HOST_URL + "api/" + method, json);
		logger.info(method + " response:" + data);
		return data;
	}

	private String buildJson(BaseReqDto reqDto) {
		String json = gson.toJson(reqDto);
		JsonObject jsonObject = gson.fromJson(json, JsonObject.class); // parse
		jsonObject.addProperty("channelId", GameThirdPartyInit.CHANNEL_ID); // modify
		return jsonObject.toString();
	}
}
