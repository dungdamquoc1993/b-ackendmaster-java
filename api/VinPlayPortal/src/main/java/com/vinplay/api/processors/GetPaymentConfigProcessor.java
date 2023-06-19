package com.vinplay.api.processors;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.vinplay.api.processors.dto.ConfigUIDto;
import com.vinplay.api.processors.dto.PaymentConfigUiDto;
import com.vinplay.payment.entities.PaymentConfig;
import com.vinplay.payment.service.PaymentConfigService;
import com.vinplay.payment.service.impl.PaymentConfigServiceImpl;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

public class GetPaymentConfigProcessor implements BaseProcessor<HttpServletRequest, String> {

	private static final Logger logger = Logger.getLogger("api");

	public String execute(Param<HttpServletRequest> param) {
		//BaseResponse<List<PaymentConfigUiDto>> result= new BaseResponse<>();
		HttpServletRequest request = param.get();
		String nickName = request.getParameter("nn");
		String accessToken = request.getParameter("at");
		String paymentType = request.getParameter("pt");// 1 deposit, 2 withdraw

		logger.info("Request GetPaymentConfig: nickName: " + nickName + ", accessToken: " + accessToken
				+ ", paymentType: " + paymentType);
		UserService userService = new UserServiceImpl();
		if (StringUtils.isBlank(nickName) || StringUtils.isBlank(accessToken)) {
			return BaseResponse.error("10", "input parameter is null or empty");
			//throw new Exception();
		}
		try {
			
			boolean isToken = userService.isActiveToken(nickName, accessToken);
			if (isToken) {
				//get provider info
				PaymentConfigService payConfig = new PaymentConfigServiceImpl();
				if(paymentType.equals("1")) {
					//deposit
					List<PaymentConfigUiDto> lstConfig = new ArrayList<>();
					
					for (PaymentConfig paymentConfig : payConfig.getConfig()) {
						ConfigUIDto dto = new ConfigUIDto(paymentConfig.getConfig());
						PaymentConfigUiDto pInfo = new PaymentConfigUiDto(paymentConfig.getName(), dto);
						lstConfig.add(pInfo);
					}
					return new BaseResponse<List<PaymentConfigUiDto>>().success(lstConfig);
				}
				
			} else {
				return BaseResponse.error("11", "Phiên làm việc của bạn đã hết hạn , vui lòng tải lại trang !");
			}
		} catch (Exception e) {
			logger.error(e);
			return BaseResponse.error("99", e.getMessage());
		}
		return BaseResponse.error("", "");
	}
}
