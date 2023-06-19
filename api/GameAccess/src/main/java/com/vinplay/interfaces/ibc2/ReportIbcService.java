package com.vinplay.interfaces.ibc2;

import com.vinplay.dto.ibc2.GetReportTokenReqDto;
import com.vinplay.dto.ibc2.GetReportTokenRespDto;
import org.apache.log4j.Logger;

public class ReportIbcService extends BaseIbc2Service {
	private  static final Logger logger = Logger.getLogger(ReportIbcService.class);
	public static void main(String[] args) throws Exception {

	}
	
	public GetReportTokenRespDto GetReportToken(GetReportTokenReqDto reqDto) {
		logger.info(gson.toJson(reqDto));
		String data = callAPI("GetReportToken", reqDto);
		GetReportTokenRespDto resDto = gson.fromJson(data,
				GetReportTokenRespDto.class);
		logger.info(gson.toJson(resDto));
		return resDto;
	}
}
