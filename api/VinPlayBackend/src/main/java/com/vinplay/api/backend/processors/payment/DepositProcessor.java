package com.vinplay.api.backend.processors.payment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.vinplay.dichvuthe.response.RechargePaywellResponse;
import com.vinplay.payment.service.PaymentManualService;
import com.vinplay.payment.service.impl.PaymentManualServiceImpl;
import com.vinplay.payment.utils.Constant;
import com.vinplay.payment.utils.PayCommon;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;
import com.vinplay.vbee.common.utils.CommonUtils;

public class DepositProcessor implements BaseProcessor<HttpServletRequest, String>{
	private static final Logger logger = Logger.getLogger("backend");
	private static Map<String, Long> mapCache = new ConcurrentHashMap<String, Long>();
	private static boolean validateRequest(String nickName) {
		if (mapCache.isEmpty()) {
			long t1 = new java.util.Date().getTime();
			mapCache.put(nickName, t1);
		} else {
			if (mapCache.containsKey(nickName)) {

				long t1 = mapCache.get(nickName);
				long t2 =  new java.util.Date().getTime();
				if ((t2 - t1) > 1000 * 20) {
					mapCache.put(nickName, t2);
					return true;
				} else {
					return false;
				}

			} else {
				long t1 =  new java.util.Date().getTime();
				mapCache.put(nickName, t1);
			}
		}
		return true;
	}
	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = param.get();
		String nickName = request.getParameter("nn");
		String approvedName = request.getParameter("nns");
		String orderId = request.getParameter("oid");
		String status = request.getParameter("st");
		String reason = request.getParameter("rs");
		String ip = request.getParameter("ip");
		logger.info("Request WithdrawalProcessor nickName= " + nickName + ", orderId: " + orderId
				+ ", approvedName: " + approvedName  + " , ip: " + ip);
		if (!validateRequest(orderId)) {
			return BaseResponse.error(Constant.ERROR_DUPLICATE, "Trong 20s chỉ được thao tác orderId này 1 lần , orderId=" + orderId);
		}
		if (StringUtils.isBlank(nickName)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "nickName is null or empty");
		}
		if (StringUtils.isBlank(approvedName)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "nickName of staff is null or empty");
		}
		if (StringUtils.isBlank(orderId)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "orderId is null or empty");
		}
		
		if (StringUtils.isBlank(ip)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "ip is null or empty");
		}
		int statusInt = -1;
		try {
			statusInt = Integer.parseInt(status);
		} catch (NumberFormatException e) {
			return BaseResponse.error(Constant.ERROR_PARAM, e.getMessage());
		}
		if (!CommonUtils.validateRequest(orderId)) {
			return BaseResponse.error(Constant.ERROR_DUPLICATE, "Trong 20s chỉ được thao tác order này 1 lần , orderId=" + orderId);
		}
		//check status
		if(PayCommon.PAYSTATUS.FAILED.getId()==statusInt) {
			if (StringUtils.isBlank(reason)) {
				return BaseResponse.error(Constant.ERROR_PARAM, "bạn cần nhập lý do");
			}
		}else {
			if(PayCommon.PAYSTATUS.COMPLETED.getId()!=statusInt) {
				return BaseResponse.error(Constant.ERROR_PARAM, "chỉ có thể chuyển về trạng thái complete/ fail ");
			}
		}
		
		try {
			PaymentManualService withdrawManual = new PaymentManualServiceImpl();
			RechargePaywellResponse res = withdrawManual.depositConfirm(orderId, approvedName, ip, statusInt, reason);
			if(0 == res.getCode()) {
				return new BaseResponse<String>().success(res.getData());
			}
			else {
				return BaseResponse.error(res.getCode() + "", res.getData());
			}
		} catch (Exception e) {
			logger.error(e);
			return BaseResponse.error(Constant.ERROR_SYSTEM, e.getMessage());
		}

	}
}
