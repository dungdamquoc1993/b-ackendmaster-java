package com.vinplay.interfaces.ebet;

import org.apache.log4j.Logger;

import com.vinplay.dto.ebet.UserBetHistoriReqDto;
import com.vinplay.dto.ebet.UserBetHistoriRespDto;

public class UserBetHistoryService extends BaseEbetService {
	private static final Logger logger = Logger.getLogger(UserBetHistoryService.class);

	/*public static void main(String[] args) throws Exception {
		UserBetHistoriReqDto req = new UserBetHistoriReqDto();
		req.setUsername("t_100");
		req.setStartTimeStr("2018-11-01T10:10:10Z");
		req.setEndTimeStr("2018-11-21T10:10:10Z");
		req.setChannelId(479);
		req.setSubChannelId(0);
		req.setTimestamp(1542697113);
		req.setSignature("SaHFh6NBrhEGm0Paxpsprc9SKva+v0imP0OBTBAWWSFZss8ajeyqUcqaknTuVX9WyWVhiwiHj2rozo4W+zdLNw==");
		UserBetHistoryService service = new UserBetHistoryService();
		UserBetHistoriRespDto res = service.getUserBetHistory(req);
//		logger.info(res);
	}*/

	public UserBetHistoriRespDto getUserBetHistory(UserBetHistoriReqDto req) {
		logger.info(gson.toJson(req));
		String data = getData("userbethistory", req);
		UserBetHistoriRespDto resp = gson.fromJson(data, UserBetHistoriRespDto.class);
		logger.info(gson.toJson(resp));
		return resp;
	}
}
