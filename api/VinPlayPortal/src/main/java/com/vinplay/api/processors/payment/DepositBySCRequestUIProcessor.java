package com.vinplay.api.processors.payment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.vinplay.dichvuthe.response.RechargePaywellResponse;
import com.vinplay.payment.entities.PaymentConfig;
import com.vinplay.payment.service.PaymentConfigService;
import com.vinplay.payment.service.RechargePayaSecService;
import com.vinplay.payment.service.impl.PaymentConfigServiceImpl;
import com.vinplay.payment.service.impl.RechargePayaSecServiceImpl;
import com.vinplay.payment.utils.Constant;
import com.vinplay.payment.utils.PaymentConstant;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.response.BaseResponse;

public class DepositBySCRequestUIProcessor implements BaseProcessor<HttpServletRequest, String> {

	private static final Logger logger = Logger.getLogger("api");
	
	private String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
	private static Map<String, Long> mapCache = new ConcurrentHashMap<String, Long>();
    
    public static boolean validateRequest(String orderID) {
		if (mapCache.isEmpty()) {
			long t1 = new java.util.Date().getTime();
			mapCache.put(orderID, t1);
		} else {
			if (mapCache.containsKey(orderID)) {

				long t1 = mapCache.get(orderID);
				long t2 =  new java.util.Date().getTime();
				if ((t2 - t1) > 1000 * 10) {
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
		String typeCard = request.getParameter("tc");
		String serial = request.getParameter("sn");
		String pin = request.getParameter("p");
		String accessToken = request.getParameter("at");
		String nickName = request.getParameter("nn");
		String ip = getIpAddress(request);
		logger.info("ipaddress1 :" + ip);
		String clientIp = "";
		if (ip != null && !"".equals(ip)) {
			String[] arrayIp = ip.split(",");
			for (int i = 0; i < (arrayIp.length > 2 ? 2 : arrayIp.length); i++) {
				if (arrayIp[i].length() <= 40) {
					clientIp = arrayIp[i].trim();
					break;
				}
			}
		}
		
		if (StringUtils.isBlank(nickName) || StringUtils.isBlank(accessToken)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "input parameter is null or empty");
		}
		
		if (!validateRequest(nickName)) {
			return BaseResponse.error(Constant.ERROR_DUPLICATE, "Trong 10s chỉ được yêu cầu nạp tiền 1 lần , tên nhân vật =" + nickName);
		}
		
		logger.info("Deposit SC request nickName: " + nickName + ", accessToken: " + accessToken + ", providerName: payasec.com"
				+ ",ipaddress=" + clientIp);
		UserService userService = new UserServiceImpl();

		try {
			boolean isToken = userService.isActiveToken(nickName, accessToken);
			if (isToken) {
				// check min amount
				PaymentConfigService payConfig = new PaymentConfigServiceImpl();
				PaymentConfig config = payConfig.getConfigByKey("payasec");
				if (config == null)
					return BaseResponse.error(Constant.ERROR_PROVIDERNAME, "Không hỗ trợ cổng thanh toán này trong thời điểm hiện tại");

				long minAmount = config.getConfig().getMinMoney();
				if (amount < minAmount) {
					return BaseResponse.error(Constant.MIN_MONEY, "Số tiền nạp quá nhỏ");
				}
				
				if(amount > 1000000) {
					return BaseResponse.error(Constant.MAX_MONEY, "Số tiền nạp phải nhỏ hơn 1 triệu VNĐ");
				}
				
				// get usermodel
				UserModel user = userService.getUserByNickName(nickName);
				if(user.isBanLogin() ||user.isBanTransferMoney()||user.isBot()) {
					return BaseResponse.error(Constant.ERROR_USERTYPE, "Quý khách đã bị cấm thực hiện chức năng này");
				}
				
				RechargePayaSecService manuService = new RechargePayaSecServiceImpl();
				RechargePaywellResponse resultResponse = manuService.createTransaction(user.getId() + "", user.getUsername(), 
						user.getNickname(), user.getNickname(), amount, typeCard, serial, pin);
				
				if (resultResponse == null) {
					return BaseResponse.error(Constant.ERROR_CREATE_TRANSACTION, "Không tạo được transaction");
				} else {
					logger.info("Deposit response nickName: " + nickName + ", response : " + resultResponse.toJson());
				}
				
				if (PaymentConstant.SUCCESS == resultResponse.getCode()) {
					return new BaseResponse<>().success(resultResponse.getData());
				} else if (PaymentConstant.MAINTAINCE == resultResponse.getCode()) {
					return BaseResponse.error(resultResponse.getCode() + "",
							"Cổng thanh toán đang bảo trì, quý khách vui lòng thực hiện lại trong ít phút");
				} else if (PaymentConstant.TOO_MANY_REQUEST == resultResponse.getCode()) {
					return BaseResponse.error(resultResponse.getCode() + "",
							"Quá nhiều yêu cầu gửi tiền, quý khách vui lòng thực hiện lại trong ít phút");
				} else {
					return BaseResponse.error(resultResponse.getCode() + "", resultResponse.getData());
				}

			} else {
				return BaseResponse.error(Constant.ERROR_SESSION, "Phiên giao dịch của quý khách đã hết, vui lòng tải lại trang và đăng nhập");
			}
		} catch (Exception e) {
			logger.error(e);
			return BaseResponse.error(Constant.ERROR_SYSTEM, e.getMessage());
		}

	}
}
