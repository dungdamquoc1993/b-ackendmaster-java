package com.vinplay.api.processors.payment;

import javax.servlet.http.HttpServletRequest;

import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;

public class GetListBankProcessor implements BaseProcessor<HttpServletRequest, String> {

	@Override
	public String execute(Param<HttpServletRequest> var1) {
		return GameCommon.BANK_JSON;
	}

}
