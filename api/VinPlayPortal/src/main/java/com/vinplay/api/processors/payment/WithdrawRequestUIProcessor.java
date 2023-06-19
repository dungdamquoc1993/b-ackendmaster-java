package com.vinplay.api.processors.payment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.vinplay.api.utils.PortalUtils;
import com.vinplay.dichvuthe.response.RechargePaywellResponse;
import com.vinplay.payment.service.WithDrawPrincePayService;
import com.vinplay.payment.service.impl.WithDrawPrincePayServiceImpl;
import com.vinplay.payment.utils.Constant;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.response.BaseResponse;

public class WithdrawRequestUIProcessor implements BaseProcessor<HttpServletRequest, String> {

	private static final Logger logger = Logger.getLogger("api");
	
	private static Map<String, Long> mapCache = new ConcurrentHashMap<String, Long>();
    
    public static boolean validateRequest(String orderID) {
		if (mapCache.isEmpty()) {
			long t1 = new java.util.Date().getTime();
			mapCache.put(orderID, t1);
		} else {
			if (mapCache.containsKey(orderID)) {

				long t1 = mapCache.get(orderID);
				long t2 =  new java.util.Date().getTime();
				if ((t2 - t1) > 1000 * 20) {
					mapCache.put(orderID, t2);
					return true;
				} else {
					return false;
				}

			} else {
				long t1 =  new java.util.Date().getTime();
				mapCache.put(orderID, t1);
			}
		}
		return true;
	}
    
	@Override
	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = param.get();
		long amount = 0;
		try {
			amount = Long.parseLong(request.getParameter("am"));
		} catch (Exception e) {
			return BaseResponse.error("99", e.getMessage());
		}
		String bankNumber = request.getParameter("bn");
		String nickName = request.getParameter("nn");
		String accessToken = request.getParameter("at");

		String ip = PortalUtils.getIpAddress(request);
		
		logger.info("Withdraw request nickName: " + nickName + ", accessToken: " + accessToken + ",ipaddress=" + ip);
		
		if (!validateRequest(nickName)) {
			return BaseResponse.error(Constant.ERROR_DUPLICATE, "Trong 20s chỉ được yêu cầu rút tiền 1 lần , tên nhân vật =" + nickName);
		}
		
		if (StringUtils.isBlank(nickName) || StringUtils.isBlank(accessToken)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "input parameter is null or empty");
		}

		if (StringUtils.isBlank(bankNumber)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "Tên ngân hàng không được để trống");
		}
		
		UserService userService = new UserServiceImpl();
		try {
			//validation token
			WithDrawPrincePayService withdrawService = new WithDrawPrincePayServiceImpl();
			boolean isToken = userService.isActiveToken(nickName, accessToken);
			if (isToken) {
				// check min amount
				if (amount < 100000) {
					return BaseResponse.error(Constant.MIN_MONEY, "Số tiền rút quá nhỏ");
				}
				UserCacheModel user = userService.getUser(nickName);
				RechargePaywellResponse response = withdrawService.requestWithdrawUser(user.getId() + "",
						user.getUsername(), nickName, amount, bankNumber);
				
				if (response.getCode() == 88) {
					return BaseResponse.error(Constant.NOT_DEPOSIT, "Quý khách vui lòng thực hiện lệnh nạp tiền trước !");
				} else if (response.getCode() == 89) {
					return BaseResponse.error(Constant.OVER_WITHDRAW, "Quý khách được thực hiện tối đa 5 lần rút tiền thành công trong ngày !");
				} else {
					return new BaseResponse<>().success(response);
				}
				
			} else {
				return BaseResponse.error(Constant.ERROR_SESSION, "Phiên làm việc của bạn đã hết hạn , vui lòng tải lại trang !");
			}
		} catch (Exception e) {
			logger.error(e);
			return BaseResponse.error(Constant.ERROR_SYSTEM, e.getMessage());
		}

	}

}
