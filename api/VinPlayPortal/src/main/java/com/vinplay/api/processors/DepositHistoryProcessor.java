package com.vinplay.api.processors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.vinplay.payment.utils.Constant;
import com.vinplay.payment.entities.DepositPaygateReponse;
import com.vinplay.payment.service.RechargePayWellService;
import com.vinplay.payment.service.impl.RechargePayWellServiceImpl;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

public class DepositHistoryProcessor implements BaseProcessor<HttpServletRequest, String> {

	private static final Logger logger = Logger.getLogger(DepositHistoryProcessor.class);
	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = param.get();
		String nickname = request.getParameter("nn");
		int status = 0;
		try {
			status = Integer.parseInt(request.getParameter("st"));
		} catch (Exception e) {
			return BaseResponse.error("99", e.getMessage());
		}
		int page = 0;
		try {
			page = Integer.parseInt(request.getParameter("p"));
		} catch (Exception e) {
			return BaseResponse.error("99", e.getMessage());
		}
		int maxItem = 0;
		try {
			maxItem = Integer.parseInt(request.getParameter("mi"));
		} catch (Exception e) {
			return BaseResponse.error("99", e.getMessage());
		}
		String fromTime = request.getParameter("ft");
		String endTime = request.getParameter("et");
		String accessToken = request.getParameter("at");
		
		logger.info("Request payment history nickname= " + nickname + ", status: " + status + ", page: " + page + ", "
				+ "maxItem: " + maxItem + ", " + "fromTime: " + fromTime + "," + " endTime: " + endTime + ", "
				+ "accessToken: " + accessToken);
		if (StringUtils.isBlank(nickname)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "nickname is required");
		}
		if (page<0) {
			return BaseResponse.error(Constant.ERROR_PARAM, "page <0");
		}
		if (maxItem<0) {
			return BaseResponse.error(Constant.ERROR_PARAM, "maxItem <0");
		}
		if (StringUtils.isBlank(fromTime)||StringUtils.isBlank(endTime)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "endTime , fromtime is required");
		}
		if (StringUtils.isBlank(accessToken)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "accessToken is required");
		}
		UserService userService = new UserServiceImpl();
		boolean isToken = userService.isActiveToken(nickname, accessToken);
		if (isToken) {
			RechargePayWellService service = new RechargePayWellServiceImpl();
			DepositPaygateReponse response= service.search(nickname, status, page, maxItem, fromTime, endTime, accessToken);
			return new BaseResponse<>().success(response);
		}else {
			return BaseResponse.error(Constant.ERROR_SESSION, "Phiên làm việc của bạn đã hết hạn , vui lòng tải lại trang !");
		}
	}

}
