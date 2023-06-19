package com.vinplay.api.backend.processors.payment;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import com.vinplay.payment.entities.Bank;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

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
		String adminNickName = request.getParameter("ann");
		Integer status = null;
		try {
			status = Integer.parseInt(request.getParameter("st"));
		} catch (NumberFormatException e) {
			
		}
		Timestamp createDate = null, updateDate = null;
		if ("0".equals(type)) {
			// insert
			createDate = new Timestamp(System.currentTimeMillis());
		} else {
			// update
			updateDate = new Timestamp(System.currentTimeMillis());
		}
		logger.info("Request InsertOrUpdateBankProcessor nickName= " + nickName + ", bankName: " + bankName
				+ ", customerName: " + customerName + ", " + "bankNumber: " + bankNumber + ", " + "branch: " + branch);
		
		//validation
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
		
		if (customerName.length() > 120) {
			return BaseResponse.error(Constant.ERROR_PARAM, "Tên khách hàng không đúng");
		}
		if (bankNumber.length() > 60) {
			return BaseResponse.error(Constant.ERROR_PARAM, "Số tài khoản không đúng");
		}
		if (!StringUtils.isBlank(branch) && branch.length() > 120) {
			return BaseResponse.error(Constant.ERROR_PARAM, "Tên chi nhánh quá dài");
		}

		Boolean isExist = false;
		for (Bank bank : GameCommon.LIST_BANK_NAME) {
			if (bank.getBank_name().equalsIgnoreCase(bankName)) {
				isExist = true;
				break;
			}
		}
		if(!isExist) {
			return BaseResponse.error(Constant.ERROR_BANK_NAME, "Không hỗ trợ Ngân hàng này . Vui lòng liên hệ CSKH !");
		}
		
		UserBankService bankService = new UserBankServiceImpl();
		UserService userService = new UserServiceImpl();
		Response res = new Response(1, "");
		try {
			UserCacheModel user = userService.getUser(nickName);
			if (user == null)
				return BaseResponse.error("-1", "Không tồn tại user này");
			if ("0".equals(type)) {
				// insert
				status = PaymentConstant.BANK_STATUS.ACTIVE;
				UserBank userBank = new UserBank(null, user.getId(), nickName, bankName, customerName, bankNumber, status,
						createDate, branch, updateDate ,adminNickName);
				res = bankService.add(userBank);
			} else {
				// update
				UserBank userBank = new UserBank(Long.parseLong(id), user.getId(), nickName, bankName, customerName, bankNumber, status,
						createDate, branch, updateDate,adminNickName);
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
