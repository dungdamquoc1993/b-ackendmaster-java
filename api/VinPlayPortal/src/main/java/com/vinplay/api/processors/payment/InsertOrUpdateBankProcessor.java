package com.vinplay.api.processors.payment;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.vinplay.payment.entities.Bank;
import com.vinplay.payment.entities.Response;
import com.vinplay.payment.entities.UserBank;
import com.vinplay.payment.utils.Constant;
import com.vinplay.payment.utils.PaymentConstant;
import com.vinplay.usercore.service.UserBankService;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserBankServiceImpl;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.response.BaseResponse;

public class InsertOrUpdateBankProcessor implements BaseProcessor<HttpServletRequest, String>{
	private static final Logger logger = Logger.getLogger(InsertOrUpdateBankProcessor.class);
	@Override
	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = param.get();
		String nickName = request.getParameter("nn");
		String bankName = request.getParameter("bn");
		String customerName = request.getParameter("cn");
		String bankNumber = request.getParameter("bnum");
		String branch = request.getParameter("br");
		String type = request.getParameter("t");// 0 or 1
		String id = request.getParameter("id");// 0 or 1
		String accessToken = request.getParameter("at");
		
		int status = PaymentConstant.BANK_STATUS.ACTIVE;
		Timestamp createDate = null, updateDate = null;
		if ("0".equals(type)) {
			// insert
			createDate = new Timestamp(System.currentTimeMillis());
		} else {
			// update
			updateDate = new Timestamp(System.currentTimeMillis());
			//Xử lý theo yêu cầu ticket: LP-249
			return BaseResponse.error(Constant.ERROR_PARAM, "You can not allow access to update information. Please contact customer care.");
		}
		logger.info("Request InsertOrUpdateBankProcessor nickName= " + nickName + ", bankName: " + bankName
				+ ", customerName: " + customerName + ", " + "bankNumber: " + bankNumber + ", " + "branch: " + branch);
		if (StringUtils.isBlank(nickName)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "nickName is null or empty");
		}
		if (StringUtils.isBlank(bankName)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "bankName is null or empty");
		}
		if (StringUtils.isBlank(customerName)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "customerName is null or empty");
		}
		if (StringUtils.isBlank(bankNumber)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "bankNumber is null or empty");
		}
		if (StringUtils.isBlank(accessToken)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "accessToken is null or empty");
		}
		UserBankService bankService = new UserBankServiceImpl();
		UserService userService = new UserServiceImpl();
		boolean isExist = false;
		for (Bank bank : GameCommon.LIST_BANK_NAME) {
			if (bank.getBank_name().equalsIgnoreCase(bankName)) {
				isExist = true;
				break;
			}
		}
		if(!isExist) {
			return BaseResponse.error(Constant.ERROR_BANK_NAME, "Không hỗ trợ Ngân hàng này . Vui lòng liên hệ CSKH !");
		}
		Response res = new Response(1, "");
		try {
			boolean isToken = userService.isActiveToken(nickName, accessToken);
			if (!isToken) {
				return BaseResponse.error(Constant.ERROR_SESSION, "This session is expried or not exist");
			}
			UserCacheModel user = userService.getUser(nickName);
			if(branch.length() >20) {
				return BaseResponse.error(Constant.ERROR_PARAM, "Chi nhánh ngân hàng có tối đa 20 kí tự");
			}
			if ("0".equals(type)) {
				// insert
				UserBank userBank = new UserBank(null, user.getId(), nickName, bankName, customerName, bankNumber, status,
						createDate, branch, updateDate , "");
				res = bankService.add(userBank);
			} else {
				// update
				UserBank userBank = new UserBank(Long.parseLong(id), user.getId(), nickName, bankName, customerName, bankNumber, status,
						createDate, branch, updateDate, nickName);
				updateDate = new Timestamp(System.currentTimeMillis());
				res = bankService.update(userBank);
			}
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
