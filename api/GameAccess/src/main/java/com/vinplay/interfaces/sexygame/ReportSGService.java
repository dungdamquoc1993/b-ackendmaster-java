package com.vinplay.interfaces.sexygame;
import org.apache.log4j.Logger;

import com.vinplay.dto.sg.GetLogByUpdateDateReqDto;
import com.vinplay.dto.sg.GetTransactionReqDto;
import com.vinplay.dto.sg.GetTransactionRespDto;
import com.vinplay.logic.InitData;
import com.vinplay.logic.InitServlet;
import com.vinplay.usercore.utils.GameThirdPartyInit;

public class ReportSGService extends BaseSGService{
	private  static final Logger logger = Logger.getLogger(ReportSGService.class);
	
	public GetTransactionRespDto getTransactionByTxTime(GetTransactionReqDto reqDto) {
		reqDto.setCurrency(GameThirdPartyInit.SG_CURRENCY);
		String data = callAPI_GzipResp("getTransactionByTxTime", reqDto);
		GetTransactionRespDto resDto = gson.fromJson(data,
				GetTransactionRespDto.class);
		return resDto;
	}
	public GetTransactionRespDto getTransactionByUpdateDate(GetLogByUpdateDateReqDto reqDto) {
		reqDto.setCurrency(GameThirdPartyInit.SG_CURRENCY);
		String data = callAPI_GzipRespUpdateDate("getTransactionByUpdateDate", reqDto);
		GetTransactionRespDto resDto = gson.fromJson(data,
				GetTransactionRespDto.class);
		return resDto;
	}
	public static void main(String[] args) {
		
	}
}
