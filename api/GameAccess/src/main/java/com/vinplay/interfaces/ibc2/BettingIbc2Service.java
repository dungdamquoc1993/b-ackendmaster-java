package com.vinplay.interfaces.ibc2;

import com.vinplay.dto.ibc2.BetDetailResult;
import com.vinplay.dto.ibc2.GetSportBetLogReqDto;
import org.apache.log4j.Logger;

public class BettingIbc2Service extends BaseIbc2Service {

	public BettingIbc2Service() {
		memberIbcService = new MemberIbc2Service();
	}

	private MemberIbc2Service memberIbcService;
	private  static final Logger logger = Logger.getLogger(BettingIbc2Service.class);

	public BetDetailResult GetSportBetLog(GetSportBetLogReqDto reqDto) {
		logger.info("REQUEST: " + gson.toJson(reqDto));
		String data = memberIbcService.callAPIDetail("GetBetDetail", reqDto);
		BetDetailResult betDetailResult = gsonBuilder.create().fromJson(data, BetDetailResult.class);
		return betDetailResult;
	}
}
