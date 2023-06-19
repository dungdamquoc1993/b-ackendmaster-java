package com.vinplay.interfaces.ibc2;

import com.vinplay.dto.ibc2.*;
import com.vinplay.usercore.utils.GameThirdPartyInit;

import org.apache.log4j.Logger;

public class FundTransferIbc2Service extends BaseIbc2Service {
	private  static final Logger logger = Logger.getLogger(FundTransferIbc2Service.class);
	private static final Integer CURRENCY_UUS = 20;
	
	

//	public Ibc2CheckUserBalanceRespDto CheckUserBalance(Ibc2CheckUserBalanceReqDto reqDto) {
//		logger.info("REQUEST: " + gson.toJson(reqDto));
//		String data = callAPI("CheckUserBalance", reqDto);
//		Ibc2CheckUserBalanceRespDto resDto = gson.fromJson(data,
//				Ibc2CheckUserBalanceRespDto.class);
//		logger.info("RESPONSE: " + gson.toJson(resDto));
//		return resDto;
//	}
	
	/**
	 * If environment = dev, onlu curency = 20(CURRENCY_UUS)
	 * @param reqDto
	 * @return
	 */
	public Ibc2FundTransferRespDto FundTransfer(Ibc2FundTransferReqDto reqDto) {
		if ("dev".equals(GameThirdPartyInit.enviroment)) {
			reqDto.setCurrency(CURRENCY_UUS);
		}
		logger.info("REQUEST: " + gson.toJson(reqDto));
		String data = callAPI("FundTransfer", reqDto);
		Ibc2FundTransferRespDto resDto = gson.fromJson(data, Ibc2FundTransferRespDto.class);
		logger.info("RESPONSE: " + gson.toJson(resDto));
		return resDto;
	}	
	
	public CheckFundTransferRespDto CheckFundTransfer(CheckFundTransferReqDto reqDto) {
		logger.info("REQUEST: " + gson.toJson(reqDto));
		String data = callAPI("CheckFundTransfer", reqDto);
		CheckFundTransferRespDto resDto = gson.fromJson(data,
				CheckFundTransferRespDto.class);
		logger.info("RESPONSE: " + gson.toJson(resDto));
		return resDto;
	}
}
