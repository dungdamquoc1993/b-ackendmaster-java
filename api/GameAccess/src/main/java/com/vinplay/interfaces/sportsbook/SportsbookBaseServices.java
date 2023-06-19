package com.vinplay.interfaces.sportsbook;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.vinplay.connection.ConnectionPoolUtil;
import com.vinplay.dto.sportsbook.SportsbookBaseReqDto;
import com.vinplay.interfaces.ibc2.utils.DateTimeUtil;
import com.vinplay.logic.InitServlet;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.utils.ThirdpartyConstant;

public class SportsbookBaseServices {
	private static SportsbookBaseServices INSTANCE;

	private SportsbookBaseServices() {
	}

	public static SportsbookBaseServices getInstance() {
		if (Objects.isNull(INSTANCE)) {
			INSTANCE = new SportsbookBaseServices();
		}
		return INSTANCE;
	}

	/**
	 * Post data to IBC CMD platform
	 * 
	 * @param reqDto
	 * @param method
	 * @return
	 */
	public String postData(SportsbookBaseReqDto reqDto, String method) throws Exception{
		Map<String, String> params = buildParams(reqDto);
		params.put("PartnerKey", GameThirdPartyInit.SPORTS_BOOK_PARTNER_KEY);
		//String url = String.format("%s?Method=%s", InitServlet.SPORTS_BOOK_API_SERVER_ROOT, method);
		String url = mapToStringParamUrl(params, method);
		return ConnectionPoolUtil.getData(url, null);
		//return SportsbookUtils.callToSportsbook(method, params);
	}

	public String queryBetRecords(long versionKey, String method) throws Exception{
		Map<String, String> params = new HashMap<>();
		params.put("PartnerKey", GameThirdPartyInit.SPORTS_BOOK_PARTNER_KEY);
		params.put("Version", versionKey + "");
		String url = mapToStringParamUrl(params, method);
		return ConnectionPoolUtil.getData(url, null);
	}

	/**
	 * Create User
	 * 
	 * @param reqDto
	 * @param method
	 * @return
	 */
	public String createUser(String sportsBookId) throws Exception {
		String url = new StringBuilder()
					.append(GameThirdPartyInit.SPORTS_BOOK_API_SERVER_ROOT)
					.append("?Method=")
					.append(ThirdpartyConstant.CMD.CREATE_MEMBER_METHOD)
					.append("&PartnerKey=").append(GameThirdPartyInit.SPORTS_BOOK_PARTNER_KEY)
					.append("&Currency=").append(GameThirdPartyInit.SPORTS_BOOK_CURRENCY)
					.append("&UserName=")
					.append(sportsBookId)
					.toString();
		return ConnectionPoolUtil.getData(url, null);
	}
	
	private String mapToStringParamUrl(Map<String, String> map,String method) throws Exception {
		StringBuilder urlBuilder = new StringBuilder()
				.append(GameThirdPartyInit.SPORTS_BOOK_API_SERVER_ROOT)
				.append("?Method=")
				.append(method);
		for (Map.Entry<String, String> param : map.entrySet()) {
			urlBuilder.append('&');
			urlBuilder.append(URLEncoder.encode(param.getKey(), "UTF-8"));
			urlBuilder.append('=');
			urlBuilder.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
		}
		return urlBuilder.toString();
	}

	private Map<String, String> buildParams(SportsbookBaseReqDto reqDto) {
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
				if ("serialVersionUID".equals(fieldName)) {
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
			e.printStackTrace();
		}

		return params;
	}
}
