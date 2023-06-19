package com.vinplay.api.processors.payment;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.vinplay.payment.entities.Response;
import com.vinplay.payment.utils.Constant;
import com.vinplay.usercore.service.UserBankService;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserBankServiceImpl;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

public class BankSearchProcessor implements BaseProcessor<HttpServletRequest, String>{
	private static final Logger logger = Logger.getLogger(InsertOrUpdateBankProcessor.class);

	@Override
	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = param.get();
		String nickName = request.getParameter("nn");
		String bankName = request.getParameter("bn");
		String bankNumber = request.getParameter("bnum");
		String accessToken = request.getParameter("at");
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
		if (StringUtils.isBlank(nickName)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "nickName is null or empty");
		}
		
		logger.info("Request BankSearchProcessor nickName= " + nickName + ", bankName: " + bankName
				+  ", bankNumber: " + bankNumber );
		if (StringUtils.isBlank(nickName)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "nickName is null or empty");
		}
		if (StringUtils.isBlank(bankName)) {
			bankName ="";
		}
		if (StringUtils.isBlank(bankNumber)) {
			bankNumber="";
		}
		if (StringUtils.isBlank(accessToken)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "accessToken is null or empty");
		}
		UserBankService bankService = new UserBankServiceImpl();
		UserService userService = new UserServiceImpl();
		Response res = new Response(1, "");
		try {
			boolean isToken = userService.isActiveToken(nickName, accessToken);
			if (!isToken) {
				return BaseResponse.error(Constant.ERROR_SESSION, "Phiên làm việc của bạn đã hết hạn , vui lòng tải lại trang !");
			}
			res = bankService.search(nickName, bankName, bankNumber, 0, pageNumber, limit);
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
