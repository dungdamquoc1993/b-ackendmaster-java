package com.vinplay.api.processors;

import javax.servlet.http.HttpServletRequest;

import com.vinplay.vbee.common.response.MoneyResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.vinplay.api.processors.verifyphone.VerifyPhoneDescription;
import com.vinplay.payment.utils.Constant;
import com.vinplay.usercore.service.UserBankService;
import com.vinplay.usercore.service.UserBonusService;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserBankServiceImpl;
import com.vinplay.usercore.service.impl.UserBonusServiceImpl;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.enums.Games;
import com.vinplay.vbee.common.models.UserBonusModel;
import com.vinplay.vbee.common.response.BaseResponse;
import com.vinplay.vbee.common.statics.Consts;
import com.vinplay.vbee.common.statics.TransType;

public class ReceiveBonusProcessor implements BaseProcessor<HttpServletRequest, String> {
	private static final Logger logger = Logger.getLogger("api");
	private static Gson gson = new Gson();

	private String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
	public static void main(String[] args) {
		String ip ="14.243.87.92, 172.68.226.139";
		String [] ad = ip.split(",");
		for (String string : ad) {
			System.out.println(string.trim());
		}
	}
	public String execute(Param<HttpServletRequest> param) {
		return BaseResponse.error(Constant.ERROR_SYSTEM, "Quý khách vui lòng nhận KM đợt 2 !");
//		HttpServletRequest request = (HttpServletRequest) param.get();
//		String nickName = request.getParameter("nn");
//		String accessToken = request.getParameter("at");
//		String ip = getIpAddress(request);
//		String clientIp = "";
//		if (ip != null && !"".equals(ip)) {
//			String [] arrayIp = ip.split(",");
//			clientIp = arrayIp[0].trim();
//		}
//
//		if (StringUtils.isBlank(nickName)) {
//			return BaseResponse.error(Constant.ERROR_PARAM, "nickName is null or empty");
//		}
//		if (StringUtils.isBlank(accessToken)) {
//			return BaseResponse.error(Constant.ERROR_PARAM, "accessToken is null or empty");
//		}
//
//		// BaseResponse<String> res = TelegramUtil.verifyPhoneNumber(nickName,
//		// sessionInfo, code);
//		UserService userService = new UserServiceImpl();
//		boolean isToken = userService.isActiveToken(nickName, accessToken);
//		if (!isToken) {
//			return BaseResponse.error(Constant.ERROR_SESSION, "Phiên giao dịch hết hạn , vui lòng tải lại trang ");
//		}
//
//		UserBonusService userBonusService = new UserBonusServiceImpl();
//		UserBonusModel model = new UserBonusModel(nickName, Consts.BONUS_58K, 58000d, null, clientIp, "Khuyến mãi 58K");
//		if (userBonusService.isReceivedBonus(nickName, Consts.BONUS_58K)) {
//			return BaseResponse.error(Constant.ERROR_SYSTEM, "Quý khách đã được nhận loại khuyến mãi 58K rồi");
//		}
//		//check trung IP
//		if(clientIp==null|| "".equals(clientIp)) {
//			return BaseResponse.error(Constant.ERROR_PARAM, "ip is null or empty");
//		}
//		if(userBonusService.isSameIP(clientIp)) {
//			return BaseResponse.error(Constant.ERROR_SYSTEM, "Quý khách vui lòng nhận KM đợt 2 !");
//		}
//		UserBankService userBankService = new UserBankServiceImpl();
//		//check added bank
//		if (!userBankService.isAddBank(nickName)) {
//			return BaseResponse.error("84", "Quý khách vui lòng thêm tài khoản ngân hàng để nhận khuyến mãi ");
//		}
//		try {
//			if (!userService.isXacThucSDT(nickName)) {
//				return BaseResponse.error(Constant.ERROR_PARAM, "Quý khách vui lòng xác thực SĐT để nhận khuyến mãi");
//			}
//		} catch (Exception e2) {
//			logger.error(e2);
//			return BaseResponse.error(Constant.ERROR_PARAM, e2.getMessage());
//		}
//		try {
//			userBonusService.insertBonus(model);
//			MoneyResponse moneyResponse = userService.updateMoney(nickName, 58000, "vin", Games.VERIFY_PHONE.getName(),
//					Games.VERIFY_PHONE.getId() + "", gson.toJson(new VerifyPhoneDescription()), 0L, null,
//					TransType.NO_VIPPOINT);
//			logger.debug("moneyResponse  VERIFY  "+moneyResponse.toJson());
//			long money = userService.getCurrentMoneyUserCache(nickName, "vin");
//			return new BaseResponse<String>().success(money+"" ,"Chúc mừng quý khách đã nhận được khuyến mãi 58K ");
//		} catch (Exception e) {
//			logger.error(e);
//			return BaseResponse.error(Constant.ERROR_PARAM, e.getMessage());
//		}
	}

}
