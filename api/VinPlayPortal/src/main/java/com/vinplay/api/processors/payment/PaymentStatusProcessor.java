package com.vinplay.api.processors.payment;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.vinplay.dichvuthe.response.RechargePaywellResponse;
import com.vinplay.payment.service.RechargeOneClickPayService;
import com.vinplay.payment.service.RechargePayWellService;
import com.vinplay.payment.service.RechargePrincePayService;
import com.vinplay.payment.service.impl.RechargeOneClickPayServiceImpl;
import com.vinplay.payment.service.impl.RechargePayWellServiceImpl;
import com.vinplay.payment.service.impl.RechargePrincePayServiceImpl;
import com.vinplay.payment.utils.Constant;
import com.vinplay.payment.utils.PaymentConstant;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

public class PaymentStatusProcessor implements BaseProcessor<HttpServletRequest, String> {

	private static final Logger logger = Logger.getLogger(PaymentStatusProcessor.class);

	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = param.get();
		String cartId = request.getParameter("ci");
		String providerName = request.getParameter("pn");//paywell or royalpay
		
		if (StringUtils.isBlank(providerName)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "Thiếu tên cổng thanh toán");
		}
		if (StringUtils.isBlank(cartId)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "cartId is null or empty");
		}
		RechargePaywellResponse response = null ;
		try {
			switch (providerName) {
			case PaymentConstant.PROVIDER.PAYWELL:
				RechargePayWellService pwellService = new RechargePayWellServiceImpl();
				response = pwellService.checkStatusTrans(cartId);
				break;
			case PaymentConstant.PROVIDER.PRINCE_PAY:
				RechargePrincePayService prinService = new RechargePrincePayServiceImpl();
				response = prinService.checkStatusTrans(cartId);
				break;
			case PaymentConstant.PROVIDER.CLICK_PAY:
				RechargeOneClickPayService clickService = new RechargeOneClickPayServiceImpl();
				response = clickService.getDataTrans(cartId);
				break;
			}
			return new BaseResponse<String>(true, response.getCode() + "", response.getData(), response.getData())
					.toJson();
		} catch (Exception e) {
			logger.error(e);
			return BaseResponse.error(Constant.ERROR_SYSTEM, e.getMessage());
		}

	}

}
