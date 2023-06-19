package com.vinplay.interfaces.ibc2;

import com.vinplay.dto.ibc2.BaseReqDto;
import com.vinplay.dto.ibc2.GetSportBetLogReqDto;
import com.vinplay.interfaces.ibc2.utils.DateDeserializer;
import com.vinplay.interfaces.ibc2.utils.DateTimeUtil;
import com.vinplay.interfaces.ibc2.utils.HttpUtils;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
public class BaseIbc2Service {
	
	private  static final Logger logger = Logger.getLogger(BaseIbc2Service.class);
	
	protected Gson gson;
	protected GsonBuilder gsonBuilder;

	protected String Hostname = GameThirdPartyInit.IBC2_URL;
	protected String VendorId = GameThirdPartyInit.IBC2_VENDOR_ID;
	protected String OperatorId = GameThirdPartyInit.IBC2_OPERATORID;
	protected String Domain;
	public BaseIbc2Service() {
		gson = new Gson();
		gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
	}


	public String callAPI(String method, BaseReqDto reqDto) {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params = buildParams(reqDto);
		params.put("vendor_id", VendorId);
		return HttpUtils.QueryAPI(method, params, Hostname);
	}
	public String callAPIDetail(String method, GetSportBetLogReqDto reqDto) {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("version_key", reqDto.getLastVersionKey().toString());
		params.put("vendor_id", VendorId);
		return HttpUtils.QueryAPI(method, params, Hostname);
	}
	private Map<String, String> buildParams(BaseReqDto reqDto) {
		Map<String, String> params = new LinkedHashMap<String, String>();
		try {
			Field[] fields = reqDto.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				String fieldName = field.getName();
				Object fieldObject = field.get(reqDto);
				if (fieldObject == null ||"serialVersionUID".equals(fieldName)) {
					continue;
				}
				String fieldValue = "";
				if (fieldObject instanceof Date) {
					fieldValue = DateTimeUtil.toISO8601UTC((Date) fieldObject);
					fieldValue = URLEncoder.encode(fieldValue, StandardCharsets.ISO_8859_1.toString()).toLowerCase();
				} else {
					fieldValue = fieldObject.toString();
					fieldValue = URLEncoder.encode(fieldValue, StandardCharsets.ISO_8859_1.toString());
				}
				params.put(fieldName, fieldValue);
			}
		} catch (Exception e) {
			logger.error("exp", e);
		}
		return params;
	}
}