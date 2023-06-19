package com.vinplay.interfaces.wm;

import com.vinplay.dto.wm.*;
import org.apache.log4j.Logger;

public class FundTransferWmService extends BaseWmService {
	private static final Logger logger = Logger.getLogger(FundTransferWmService.class);

	public WmFundTransferReqDto  FundTransfer(WmFundTransferRespDto reqDto) { 
		String data = getData("ChangeBalance", reqDto);

		logger.info("FundTransferWmService response =" + data);
		try {
			WmFundTransferReqDto resDto = gson.fromJson(data, WmFundTransferReqDto.class);
			return resDto;
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
		
	}
}
