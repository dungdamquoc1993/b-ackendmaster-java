package com.vinplay.service.sbo.impl;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.vinplay.usercore.utils.GameThirdPartyInit;

public abstract class SboConfig {

	public SboConfig() {
		getParams();
	}
	protected static final ObjectMapper MAPPER = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	protected static JsonObject globalJsonParams;
	protected static Map<String, String> globalParams;

	private static Map<String, String> getParams() {
		if (globalParams == null || globalParams.isEmpty()) {
			globalParams = new HashMap<String, String>(2);
			globalParams.put("ServerId", GameThirdPartyInit.SBO_SECRET_KEY);
			globalParams.put("CompanyKey", GameThirdPartyInit.SBO_OPERATOR_TOKEN);
			globalParams.put("Agent", GameThirdPartyInit.SBO_AGENT);
		}
		if (globalJsonParams == null || globalJsonParams.isJsonNull()) {
			globalJsonParams = new JsonObject();
			globalJsonParams.addProperty("ServerId", GameThirdPartyInit.SBO_SECRET_KEY);
			globalJsonParams.addProperty("CompanyKey", GameThirdPartyInit.SBO_OPERATOR_TOKEN);
			globalJsonParams.addProperty("Agent", GameThirdPartyInit.SBO_AGENT);
		}
		return globalParams;
	}
}
