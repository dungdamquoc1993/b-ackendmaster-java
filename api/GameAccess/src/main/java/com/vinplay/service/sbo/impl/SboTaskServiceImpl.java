/**
 * Archie
 */
package com.vinplay.service.sbo.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.JsonObject;
import com.vinplay.connection.ConnectionPoolUtil;
//import com.vinplay.connection.HttpConnectionPool;
import com.vinplay.dto.sbo.AbsSboBaseResponse;
import com.vinplay.dto.sbo.SboError;
import com.vinplay.dto.sbo.SboRecordDetail;
import com.vinplay.dto.sbo.SboRecordResponse;
import com.vinplay.service.sbo.SboTaskService;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.utils.ThirdpartyConstant;

/**
 * @author Archie
 *
 */
public class SboTaskServiceImpl  extends SboConfig  implements SboTaskService {

	private static final Logger logger = LoggerFactory.getLogger(SboTaskServiceImpl.class);
	
	private final List<String> lstPortfolio = Arrays.asList(new String[] { "SportsBook"});
	
	@Override
	public boolean createUser(String sboId) throws Exception {
		// map params
		JsonObject json =globalJsonParams.deepCopy();
		json.addProperty("Username", sboId);
		// url
		String url = String.format("%s%s", GameThirdPartyInit.SBO_API_URL, ThirdpartyConstant.SBO.API_CREATE_PLAYER);
		String data = ConnectionPoolUtil.postData(url, json.toString(), null);
		if (data != null) {
			AbsSboBaseResponse<String, SboError> result = MAPPER.readValue(data,
					new TypeReference<AbsSboBaseResponse<String, SboError>>() {
					});
			//{"serverId":"YY-demoanduat","error":{"id":4103,"msg":"User Exists"}}
			if (result.getError() != null && (result.getError().getId() == 0 || result.getError().getId() == 4103)) {
				// success
				return true;
			} else {
				logger.error("SBO creatUser error , code ={} , message ={}", result.getError().getId(),
						result.getError().getMsg());
			}
		}
		return false;

	}

	@Override
	public List<SboRecordDetail> getBetLogDetail(String startDate,String endDate) throws Exception {
		
		List<SboRecordDetail> resultData = new ArrayList<>();
		
		for (String portfolio : lstPortfolio) {
			JsonObject json = globalJsonParams.deepCopy();
			json.addProperty("StartDate", startDate);
			json.addProperty("EndDate",endDate);
			json.addProperty("Portfolio",portfolio);
			// url
			String url = String.format("%s%s", GameThirdPartyInit.SBO_API_URL, ThirdpartyConstant.SBO.API_BET_DETAILS);
			// invoke API
			String data = ConnectionPoolUtil.postData(url, json.toString(),null);
			if(data==null) {
				logger.error("SBOCollectLog error , url ={} , params ={}", url, json.toString());
				continue;
			}
			SboRecordResponse sboLog = MAPPER.readValue(data, new TypeReference<SboRecordResponse>() {});
			if (sboLog != null && sboLog.getError() != null && sboLog.getError().getId() == 0) {
				// success
				List<SboRecordDetail> rs = sboLog.getResult();
				if (rs != null && !rs.isEmpty()) {
					resultData.addAll(sboLog.getResult());
				}
			} else {
				logger.error("SBOCollectLog error , code ={} , message ={}", sboLog.getError().getId(),
						sboLog.getError().getMsg());
			}
		}
		return resultData;
		
	}

}
