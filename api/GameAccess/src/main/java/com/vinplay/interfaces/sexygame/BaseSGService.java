package com.vinplay.interfaces.sexygame;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.vinplay.dto.sg.BaseSGRequest;
import com.vinplay.dto.sg.GetLogByUpdateDateReqDto;
import com.vinplay.dto.sg.GetTransactionReqDto;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.utils.DateDeserializer;
import com.vinplay.utils.DateTimeUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BaseSGService {
	protected static final Logger logger = Logger.getLogger(BaseSGService.class);
	//Get config from web.xml
	public static final String HOSTNAME = GameThirdPartyInit.SG_URL;
	public static final String LOG_URL = GameThirdPartyInit.SG_LOG_URL;
	public static final String CERT = GameThirdPartyInit.SG_CERT;
	public static final String AGENTID = GameThirdPartyInit.SG_AGENTID;
	
	protected Gson gson;
	protected GsonBuilder gsonBuilder;
	
	public BaseSGService() {
		gson = new Gson();
		gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
	}
	
	public String callAPI(String method, BaseSGRequest reqDto) {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params = buildParams(reqDto);
		params.put("cert",CERT);
		params.put("agentId", AGENTID);
		return SGUtils.QueryAPI(HOSTNAME,method, params);
	}
	
	public String callAPI_GzipResp(String method, GetTransactionReqDto reqDto) {
		Map<String, String> params = new LinkedHashMap<String, String>();
		//params = buildParams(reqDto);
		params.put("startTime",reqDto.getStartTime());
		params.put("endTime",reqDto.getEndTime());
		params.put("cert",CERT);
		params.put("agentId", AGENTID);
		params.put("platform", "SEXYBCRT");
		return SGUtils.QueryAPI_GzipResp(LOG_URL,method, params);
//		params.put("cert", "TIMUQIfaOT69Xe9q9xJ");
//		params.put("agentId", "hengwinag");
//		return SGUtils.QueryAPI_GzipResp("https://testapi.onlinegames22.com/wallet/",method, params);
	}
	public String callAPI_GzipRespUpdateDate(String method, GetLogByUpdateDateReqDto reqDto) {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("timeFrom",reqDto.getTimeFrom());
		params.put("cert",CERT);
		params.put("agentId", AGENTID);
		params.put("platform", "SEXYBCRT");
		return SGUtils.QueryAPI_GzipResp(LOG_URL,method, params);
	}
	
	private static Map<String, String> buildParams(BaseSGRequest reqDto) {
		Map<String, String> params = new LinkedHashMap<String, String>();
		try {
			Field[] fields = reqDto.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				String fieldName = field.getName();
				Object fieldObject = field.get(reqDto);
				if (fieldObject == null) {
					continue;
				}
				if (fieldName == null || "serialVersionUID".equals(fieldName)) {
					continue;
				}

				String fieldValue;
				if (fieldObject instanceof Date) {
					fieldValue = DateTimeUtil.toISO8601UTC((Date) fieldObject);
					fieldValue = URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()).toLowerCase();
				} else {
					fieldValue = fieldObject.toString();
				}
				params.put(fieldName, fieldValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return params;
	}
}
