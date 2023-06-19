package com.vinplay.api.processors;

import com.vinplay.payment.service.RechargePayWellService;
import com.vinplay.payment.service.WithDrawOneClickPayService;
import com.vinplay.payment.service.impl.RechargePayWellServiceImpl;
import com.vinplay.payment.service.impl.WithDrawOneClickPayServiceImpl;
import com.vinplay.payment.utils.Constant;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepositHistory4UserplayProcessor implements BaseProcessor<HttpServletRequest, String> {

	private static final Logger logger = Logger.getLogger(DepositHistory4UserplayProcessor.class);
	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = param.get();
		String nickname = request.getParameter("nn");
		if (StringUtils.isBlank(nickname)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "Nickname not empty");
		}

		//Loại giao dịch: 0-DEPOSIT | 1-WITHDRAW
		String transactionType = request.getParameter("tt");
		if(transactionType == null || transactionType.trim().isEmpty())
			return BaseResponse.error(Constant.ERROR_PARAM, "Transaction type not empty");

		if(!transactionType.equals("0") && !transactionType.equals("1"))
			return BaseResponse.error(Constant.ERROR_PARAM, "Value of transaction type is invalid");

		int status = -1;
		try {
			status = Integer.parseInt(request.getParameter("st"));
		} catch (Exception e) { }

		int page = 0;
		try {
			page = Integer.parseInt(request.getParameter("p"));
		} catch (Exception e) {
			return BaseResponse.error(Constant.ERROR_PARAM, "Page index not empty");
		}

		int maxItem = 0;
		try {
			maxItem = Integer.parseInt(request.getParameter("mi"));
		} catch (Exception e) {
			return BaseResponse.error(Constant.ERROR_PARAM, "Limit item per page not empty");
		}

		if (page<0) {
			return BaseResponse.error(Constant.ERROR_PARAM, "page <0");
		}

		if (maxItem<0) {
			return BaseResponse.error(Constant.ERROR_PARAM, "maxItem <0");
		}

		String fromTime = request.getParameter("ft");
		String endTime = request.getParameter("et");
//		if (StringUtils.isBlank(fromTime)||StringUtils.isBlank(endTime)) {
//			return BaseResponse.error(Constant.ERROR_PARAM, "From time and end time not empty");
//		}

		String accessToken = request.getParameter("at");
		if (StringUtils.isBlank(accessToken)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "AccessToken not empty");
		}

		logger.info("Request payment history nickname= " + nickname + ", status: " + status + ", page: " + page + ", "
				+ "maxItem: " + maxItem + ", " + "fromTime: " + fromTime + "," + " endTime: " + endTime + ", "
				+ "accessToken: " + accessToken + ", transactionType: " + transactionType);
		UserService userService = new UserServiceImpl();
		boolean isToken = true;//userService.isActiveToken(nickname, accessToken);
		if (isToken) {
			Map<String, Object> data= new HashMap<>();
			if(transactionType.equals("0")) {
				RechargePayWellService rechargeService = new RechargePayWellServiceImpl();
				data = rechargeService.FindTransaction(nickname, status, page, maxItem, fromTime, endTime, "");
			}

			if(transactionType.equals("1")) {
				WithDrawOneClickPayService withdrawService = new WithDrawOneClickPayServiceImpl();
				data = withdrawService.FindTransaction(nickname, status, page, maxItem, fromTime, endTime, "");
			}
			
			try {
				if(data.isEmpty())
					return new BaseResponse(true, "0", null, null, 0).toJson();
				
				int totalRecord = Integer.parseInt(data.get("totalRecord").toString());
				data.remove("totalRecord");
				BaseResponse res = new BaseResponse(true, "0", null, data.get("data"), totalRecord);
				return res.toJson();
			}catch (Exception e) {
				logger.error(e);
				return new BaseResponse(true, "1001", null, null, 0).toJson();
			}
		}else {
			return BaseResponse.error(Constant.ERROR_SESSION, "Phiên làm việc của bạn đã hết hạn , vui lòng tải lại trang !");
		}
	}

}
