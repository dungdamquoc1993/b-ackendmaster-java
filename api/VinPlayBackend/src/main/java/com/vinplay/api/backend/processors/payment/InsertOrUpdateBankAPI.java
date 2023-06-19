package com.vinplay.api.backend.processors.payment;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.vinplay.payment.entities.Bank;
import com.vinplay.payment.utils.Constant;
import com.vinplay.payment.utils.PaymentConstant;
import com.vinplay.usercore.dao.BankDao;
import com.vinplay.usercore.dao.impl.BankDaoImpl;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.response.BaseResponse;

public class InsertOrUpdateBankAPI implements BaseProcessor<HttpServletRequest, String>{
	private static final Logger logger = Logger.getLogger(InsertOrUpdateBankAPI.class);
	@Override
	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = param.get();

		String bank_name= request.getParameter("bn");
		String code= request.getParameter("bc");
		String logo= request.getParameter("lg");
		String nickName= request.getParameter("nn");
		String str_id = request.getParameter("id");
		
		Integer status, type = 0;
		Long id = 0l;
		try {
			status = Integer.parseInt(request.getParameter("st"));
			type = Integer.parseInt(request.getParameter("ty"));
			if(!StringUtils.isBlank(str_id)) {
				id = Long.parseLong(str_id);
			}

		} catch (NumberFormatException e) {
			return null;
		}
		//validation
		if (StringUtils.isBlank(bank_name)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "bank_name is null or empty");
		}
		if (StringUtils.isBlank(code)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "code is null or empty");
		}
		if (StringUtils.isBlank(logo)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "logo is null or empty");
		}
		if (StringUtils.isBlank(nickName)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "nickName is null or empty");
		}
		
		BankDao bankService = new BankDaoImpl();
		UserService userService = new UserServiceImpl();
		try {
			UserCacheModel user = userService.getUser(nickName);
			if (user == null)
				return BaseResponse.error("-1", "Không tồn tại user này");
			boolean isInsert = false;
			Bank bank = null;
			if (type == 0) {
				// insert
//				status = PaymentConstant.BANK_STATUS.ACTIVE;
				bank = new Bank(id, bank_name, status, nickName, bank_name, code, logo, nickName);
				isInsert = bankService.addBank(bank);
			} else {
				// update
				bank = new Bank(id, bank_name, status, nickName, bank_name, code, logo, nickName);
				isInsert = bankService.editBank(bank);
			}
			if (isInsert) {
				return new BaseResponse<Bank>().success(bank);
			} else {
				return BaseResponse.error("10", "Không thành công");
			}
		} catch (Exception e) {
			logger.error(e);
			return BaseResponse.error(Constant.ERROR_SYSTEM, e.getMessage());
		}

	}
}
