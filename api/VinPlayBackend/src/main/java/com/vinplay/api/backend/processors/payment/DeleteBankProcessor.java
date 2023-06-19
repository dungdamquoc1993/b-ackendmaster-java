package com.vinplay.api.backend.processors.payment;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.vinplay.payment.entities.Response;
import com.vinplay.payment.utils.Constant;
import com.vinplay.usercore.service.UserBankService;
import com.vinplay.usercore.service.impl.UserBankServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

public class DeleteBankProcessor implements BaseProcessor<HttpServletRequest, String>{
	private static final Logger logger = Logger.getLogger(InsertOrUpdateBankProcessor.class);

	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = param.get();
		Long id =null;
		try {
			id = Long.parseLong(request.getParameter("id"));
		} catch (NumberFormatException e) {
			return BaseResponse.error(Constant.ERROR_PARAM, e.getMessage());
		}
		logger.info("Request delete bank id = " + id);

		UserBankService bankService = new UserBankServiceImpl();
		Response res = new Response(1, "");
		try {
			res = bankService.delete(id);
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
