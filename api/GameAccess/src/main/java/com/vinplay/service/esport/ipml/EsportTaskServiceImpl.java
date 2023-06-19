/**
 * Archie
 */
package com.vinplay.service.esport.ipml;

import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.vinplay.connection.ConnectionPoolUtil;
import com.vinplay.dto.esport.EsportRecord;
import com.vinplay.dto.esport.EsportRespose;
import com.vinplay.service.esport.EsportTaskService;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.utils.ThirdpartyConstant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Archie
 *
 */
public class EsportTaskServiceImpl implements EsportTaskService {

	private static final Logger LOGGER = Logger.getLogger(EsportTaskServiceImpl.class);

	private static final ObjectMapper mapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	
	private static final String[] TOKEN_HEADER = { "Authorization", GameThirdPartyInit.ESPORT_PRIVATE_TOKEN };

	@Override
	public boolean isCreateUser(String esportId) throws Exception {
		JSONObject params = new JSONObject();
		params.put("member_code", esportId);
		String url = String.format("%s%s", GameThirdPartyInit.ESPORT_API_URL, ThirdpartyConstant.ESPORT.API_CREATE_PLAYER);
		String data = ConnectionPoolUtil.postData(url, params.toString(), TOKEN_HEADER);
		return data != null && !"".equals(data);
	}

	@Override
	public EsportRespose<EsportRecord> getBetLog(Integer page, Integer pagesize, String fromDate, String toDate)
			throws Exception {
		String urlParam = buildURLParams(page, pagesize, fromDate, toDate);
		String fullUrl = String.format("%s%s%s", GameThirdPartyInit.ESPORT_API_URL, ThirdpartyConstant.ESPORT.API_BET_LOG ,urlParam);
		String data = ConnectionPoolUtil.runGet(fullUrl, TOKEN_HEADER);
		if (data != null && !"".equals(data)) {
			EsportRespose<EsportRecord> result = mapper.readValue(data, new TypeReference<EsportRespose<EsportRecord>>() {});
			LOGGER.info("EsportBetRecordRespose data json parse = " + result.toString());
			return result;
		}else {
			LOGGER.error("EsportBetRecordRespose fullUrl = " + fullUrl);
			return null;
		}
	}
//	public static void main(String[] args) throws Exception {
//		String urlParam = buildURLParams(1, 100, "2020-06-15T01:54:21.491969Z", CommonMethod.convertHCMToUTC(CommonMethod.GetCurDate("yyyy-MM-dd'T'HH:mm:ss'Z'"),
//				"yyyy-MM-dd'T'HH:mm:ss'Z'"));
//		String fullUrl = String.format("%s%s%s", "https://spi-test.r4espt.com/", ThirdpartyConstant.ESPORT.API_BET_LOG ,urlParam);
//		String data = ConnectionPoolUtil.runGet(fullUrl ,TOKEN_HEADER);
//		System.out.println(data);
//	}
	private String buildURLParams(Integer page, Integer pagesize, String fromDate, String toDate)
			throws Exception {
		return new StringBuilder("?")
					.append("from_modified_datetime=")
					.append(URLEncoder.encode(fromDate, "UTF-8"))
					.append("&to_modified_datetime=")
					.append(URLEncoder.encode(String.valueOf(toDate), "UTF-8"))
					.append("&page=")
					.append(page)
					.append("&page_size=")
					.append(pagesize.toString()).toString();
	}
}
