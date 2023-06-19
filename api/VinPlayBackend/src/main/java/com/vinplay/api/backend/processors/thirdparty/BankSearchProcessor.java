package com.vinplay.api.backend.processors.thirdparty;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.vinplay.payment.entities.Response;
import com.vinplay.payment.utils.Constant;
import com.vinplay.usercore.service.UserBankService;
import com.vinplay.usercore.service.impl.UserBankServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

public class BankSearchProcessor implements BaseProcessor<HttpServletRequest, String>{
	private static final Logger logger =  Logger.getLogger("backend");// Logger.getLogger(BankSearchProcessor.class);

	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = param.get();
		String nickName = request.getParameter("nn");
		String bankName = request.getParameter("bn");
		String bankNumber = request.getParameter("bnum");
		String pageNumberStr= request.getParameter("pn");
		String limitStr= request.getParameter("l");
		int pageNumber =0;
		int limit = 0;
		try {
			pageNumber = Integer.parseInt(pageNumberStr);
			limit = Integer.parseInt(limitStr);
		} catch (NumberFormatException e) {
			return BaseResponse.error(Constant.ERROR_PARAM, "pageNumber or limit format");
		}
		logger.info("Request BankSearchProcessor nickName= " + nickName + ", bankName: " + bankName
				+  ", bankNumber: " + bankNumber );
		if (StringUtils.isBlank(nickName)) {
			nickName="";
		}
		if (StringUtils.isBlank(bankName)) {
			bankName ="";
		}
		if (StringUtils.isBlank(bankNumber)) {
			bankNumber ="";
		}
		UserBankService bankService = new UserBankServiceImpl();
		Response res = new Response(1, "");
		try {
			res = bankService.search(nickName, bankName, bankNumber, 1, pageNumber, limit);
			if (res.getCode() == 0) {
				return new BaseResponse<String>().success(res.getData());
			} else {
				return BaseResponse.error(res.getCode() + "", res.getMessage());
			}

		} catch (Exception e) {
			logger.error(e);
			return BaseResponse.error(Constant.ERROR_SYSTEM, e.getMessage());
		}

	}
}
