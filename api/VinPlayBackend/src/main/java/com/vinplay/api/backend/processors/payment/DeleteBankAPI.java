package com.vinplay.api.backend.processors.payment;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.vinplay.payment.utils.Constant;
import com.vinplay.usercore.dao.BankDao;
import com.vinplay.usercore.dao.impl.BankDaoImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

public class DeleteBankAPI implements BaseProcessor<HttpServletRequest, String> {
	private static final Logger logger = Logger.getLogger(InsertOrUpdateBankProcessor.class);

	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = param.get();
		Long id = null;
		try {
			id = Long.parseLong(request.getParameter("id"));
		} catch (NumberFormatException e) {
			return BaseResponse.error(Constant.ERROR_PARAM, e.getMessage());
		}
		logger.info("Request delete bank id = " + id);

		BankDao bankDao = new BankDaoImpl();
		try {
			boolean res = bankDao.deleteBank(id);
			if (res) {
				return new BaseResponse<Long>().success(id);
			} else {
				return BaseResponse.error("10", "Chưa thực hiện được");
			}
		} catch (Exception e) {
			logger.error(e);
			return BaseResponse.error(Constant.ERROR_SYSTEM, e.getMessage());
		}
	}
}
