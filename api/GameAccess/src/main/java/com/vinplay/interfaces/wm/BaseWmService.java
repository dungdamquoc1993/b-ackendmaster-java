package com.vinplay.interfaces.wm;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.apache.log4j.Logger;

import com.vinplay.dto.wm.BaseReqDto;
import com.vinplay.dto.wm.BaseResponseDto;
import com.vinplay.interfaces.wm.utils.DateDeserializer;
import com.vinplay.interfaces.wm.utils.DateTimeUtil;
import com.vinplay.interfaces.wm.utils.HttpUtils;
import com.vinplay.logic.InitServlet;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BaseWmService {
	private static final Logger logger = Logger.getLogger(BaseWmService.class);
	protected Gson gson;
	protected GsonBuilder gsonBuilder;

	protected String Hostname = GameThirdPartyInit.WM_URL;
	protected String VendorId = GameThirdPartyInit.WM_VENDOR_ID;
	protected String OperatorId = GameThirdPartyInit.WM_OPERATORID;
	protected String Domain;

	public BaseWmService() {
		gson = new Gson();
		gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
	}

	public String getData(String method, BaseReqDto reqDto) {
		String params = buildParams(reqDto);
		logger.info("[BaseWmService] " + method + " request:" + params);
		String data = HttpUtils.getData(buildUrl(method, params));
		logger.info("[BaseWmService] " + method + " response:" + data);
		return data;
	}

	private String buildUrl(String method, String params) {
		StringBuilder uri = new StringBuilder();
		uri.append("cmd=").append(method).append("&vendorId=").append(OperatorId).append("&signature=").append(VendorId)
				.append(params);
		String url = GameThirdPartyInit.WM_URL + uri.toString();
		logger.info("[BaseWmService] --REQUEST URL: " + url);
		return url;
	}

	private String buildParams(BaseReqDto reqDto) {
		String result = "";
		StringBuffer params = new StringBuffer();
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
				String fieldValue = "";
				if (fieldObject instanceof Date) {
					fieldValue = DateTimeUtil.toISO8601UTC((Date) fieldObject);
					fieldValue = URLEncoder.encode(fieldValue, StandardCharsets.ISO_8859_1.toString()).toLowerCase();
				} else {
					fieldValue = fieldObject.toString();
					fieldValue = URLEncoder.encode(fieldValue, StandardCharsets.ISO_8859_1.toString());
				}
				params.append("&").append(fieldName).append("=").append(fieldValue);
			}
			result = params.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public <T> BaseResponseDto<T> convertJsonToData(BaseResponseDto<T> resDto, Class<T> type) {
		if (0 != (resDto.getErrorCode())) {
			logger.info(resDto.getErrorMessage());
			return resDto;
		}
		String jsonData = gson.toJson(resDto.getResult());
		logger.info(jsonData);
		resDto.setResult(gson.fromJson(jsonData, type));
		return resDto;
	}
}