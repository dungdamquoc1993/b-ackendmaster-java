package com.vinplay.api.processors.payment;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.vinplay.payment.service.RechargeOneClickPayService;
import com.vinplay.payment.service.impl.RechargeOneClickPayServiceImpl;
import com.vinplay.payment.utils.Constant;
import com.vinplay.payment.utils.PaymentConstant;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

public class GetBanksOneClickPayProcessor implements BaseProcessor<HttpServletRequest, String> {

	@Override
	public String execute(Param<HttpServletRequest> var1) {
		HttpServletRequest request = var1.get();

		String providerName = request.getParameter("pn");

		if (StringUtils.isBlank(providerName)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "Nhà cung cấp không đúng");
		}

		switch (providerName) {
		case PaymentConstant.PROVIDER.PAYWELL:
			
			break;
		case PaymentConstant.PROVIDER.PRINCE_PAY:

			break;

		case PaymentConstant.PROVIDER.CLICK_PAY:
			RechargeOneClickPayService oneClick = new RechargeOneClickPayServiceImpl();
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			try {
				return ow.writeValueAsString(oneClick.getLstOneClickBank());
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		default:
			break;
		}
		return "";
	}

}
