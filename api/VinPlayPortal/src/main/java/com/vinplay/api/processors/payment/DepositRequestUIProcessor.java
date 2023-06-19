package com.vinplay.api.processors.payment;

import java.util.List;
import java.util.Map;
import java.util.SplittableRandom;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.vinplay.dichvuthe.response.RechargePaywellResponse;
import com.vinplay.payment.entities.BankConfig;
import com.vinplay.payment.entities.PaymentConfig;
import com.vinplay.payment.service.PaymentConfigService;
import com.vinplay.payment.service.PaymentManualService;
import com.vinplay.payment.service.RechargePayWellService;
import com.vinplay.payment.service.RechargePrincePayService;
import com.vinplay.payment.service.impl.PaymentConfigServiceImpl;
import com.vinplay.payment.service.impl.PaymentManualServiceImpl;
import com.vinplay.payment.service.impl.RechargePayWellServiceImpl;
import com.vinplay.payment.service.impl.RechargePrincePayServiceImpl;
import com.vinplay.payment.utils.Constant;
import com.vinplay.payment.utils.PayUtils;
import com.vinplay.payment.utils.PaymentConstant;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.response.BaseResponse;

public class DepositRequestUIProcessor implements BaseProcessor<HttpServletRequest, String> {

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
    
	private static final String[] RANDOM_IP = { "127.0.0.1", "0:0:0:0:0:0:0:1" };
	
	private final SplittableRandom ran = new SplittableRandom();
	
	@Override
	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = param.get();
		String fullName = request.getParameter("fn");
		long amount = 0;
		try {
			amount = Long.parseLong(request.getParameter("am"));
		} catch (Exception e) {
			return BaseResponse.error("99", e.getMessage());
		}
		String bankCode = request.getParameter("bc");
		String payType = request.getParameter("pt");
		String nickName = request.getParameter("nn");
		String accessToken = request.getParameter("at");
		String providerName = request.getParameter("pn");// paywell or royalpay
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
		
		if (!validateRequest(nickName)) {
			return BaseResponse.error(Constant.ERROR_DUPLICATE, "Trong 10s chỉ được yêu cầu nạp tiền 1 lần , tên nhân vật =" + nickName);
		}
		
		logger.info("Deposit request nickName: " + nickName + ", accessToken: " + accessToken + ", providerName: "
				+ providerName + ",ipaddress=" + clientIp);
		UserService userService = new UserServiceImpl();

		try {
			if (StringUtils.isBlank(nickName) || StringUtils.isBlank(accessToken)) {
				return BaseResponse.error(Constant.ERROR_PARAM, "input parameter is null or empty");
			}

			if (StringUtils.isBlank(payType)) {
				return BaseResponse.error(Constant.ERROR_PARAM, "Phương thức nạp tiền không đúng");
			}
			if (StringUtils.isBlank(providerName)) {
				return BaseResponse.error(Constant.ERROR_PARAM, "Nhà cung cấp không đúng");
			}
			if (StringUtils.isBlank(fullName)) {
				return BaseResponse.error(Constant.ERROR_PARAM, "Họ tên chủ tài khoản không đúng");
			}
			int payTypeInt = 0;
			try {
				payTypeInt = Integer.parseInt(payType);
			} catch (NumberFormatException e) {
				return BaseResponse.error(Constant.ERROR_SYSTEM, e.getMessage());
			}
			// validation paytype
			if (PaymentConstant.PayType.ONLINE.getKey() != payTypeInt
					&& PaymentConstant.PayType.OFFLINE.getKey() != payTypeInt
					&& PaymentConstant.PayType.MOMO_DEP.getKey() != payTypeInt
					&& PaymentConstant.PayType.ZALO_DEP.getKey() != payTypeInt) {
				return BaseResponse.error(Constant.ERROR_PAYTYPE, "Phương thức thanh toán không đúng");
			}
			boolean isToken = userService.isActiveToken(nickName, accessToken);
			if (isToken) {
				// get provider info
				PaymentConfigService payConfig = new PaymentConfigServiceImpl();
				// check min amount
				PaymentConfig config = payConfig.getConfigByKey(providerName);
				if (config == null)
					return BaseResponse.error(Constant.ERROR_PROVIDERNAME, "Không hỗ trợ cổng thanh toán này trong thời điểm hiện tại");

				long minAmount = config.getConfig().getMinMoney();
				if (amount < 10000) {
					return BaseResponse.error(Constant.MIN_MONEY, "Số tiền nạp quá nhỏ");
				}
				if(PaymentConstant.PayType.MOMO_DEP.getKey() != payTypeInt) {
					if (minAmount > amount) {
						return BaseResponse.error(Constant.MIN_MONEY, "Số tiền nạp phải lớn hơn  " + minAmount +" VNĐ");
					}
				}else {
					if (amount < 20000) {
						return BaseResponse.error(Constant.MIN_MONEY, "Số tiền nạp phải lớn hơn  20.000 VNĐ");
					}
				}
				
				if(amount > 300000000) {
					return BaseResponse.error(Constant.MAX_MONEY, "Số tiền nạp phải nhỏ hơn 300 triệu VNĐ");
				}
				// get usermodel
				UserModel user = userService.getUserByNickName(nickName);
				String userId = user.getId() + "";
				String username = user.getUsername();
				if(user.isBanLogin() ||user.isBanTransferMoney()||user.isBot()) {
					return BaseResponse.error(Constant.ERROR_USERTYPE, "Quý khách đã bị cấm thực hiện chức năng này");
				}
				// switch provider
				RechargePaywellResponse resultResponse = null;
				String paytypeStr = PayUtils.getPayType(payTypeInt, providerName);
				if ("".equals(paytypeStr)) {
					return BaseResponse.error(Constant.ERROR_PAYTYPE, "Hình thức thanh toán không đúng");
				}

				switch (providerName) {
				case PaymentConstant.PROVIDER.PAYWELL:
					if (StringUtils.isBlank(bankCode)) {
						return BaseResponse.error(Constant.ERROR_PARAM, "Mã ngân hàng không chính xác");
					}
					// check bankCode
					List<BankConfig> lstBankConfig = config.getConfig().getBanks();
					boolean isExist = false;
					for (BankConfig bankConfig : lstBankConfig) {
						if (bankConfig.getKey().equals(bankCode)) {
							isExist = true;
							break;
						}
					}
					if (!isExist) {
						return BaseResponse.error(Constant.ERROR_BANKCODE, "Chưa hỗ trợ ngân hàng này");
					}
					RechargePayWellService servicePaywell = new RechargePayWellServiceImpl();
					resultResponse = servicePaywell.createTransaction(userId, username, nickName, fullName, amount,
							bankCode, paytypeStr);
					break;
				case PaymentConstant.PROVIDER.PRINCE_PAY:
					if ("".equals(clientIp) || clientIp.length() > 20) {
						clientIp = RANDOM_IP[ran.nextInt(2 - 0 + 1) + 0];
					}
					RechargePrincePayService servicePrince = new RechargePrincePayServiceImpl();
					resultResponse = servicePrince.createTransaction(userId, username, nickName, amount, paytypeStr,
							fullName, bankCode, clientIp);
					break;

				case PaymentConstant.PROVIDER.CLICK_PAY:
//					RechargeOneClickPayService serviceClick = new RechargeOneClickPayServiceImpl();
//					resultResponse = serviceClick.createTransaction(userId, username, nickName, amount, paytypeStr,
//							fullName, bankCode, clientIp);
					break;
				case PaymentConstant.PROVIDER.MANUAL_BANK:
					String bankAccountNum = request.getParameter("bn");
					
					if (paytypeStr.equals("bank_recharge")) {
						if (StringUtils.isBlank(bankAccountNum)) {
							return BaseResponse.error(Constant.ERROR_PARAM, "Thiếu số tài khoản ngân hàng");
						}
						if (StringUtils.isBlank(bankCode)) {
							return BaseResponse.error(Constant.ERROR_PARAM, "Thiếu tên ngân hàng ");
						}
					} else if (paytypeStr.equals("momo_recharge")) {
						bankCode = "momo";
					} else {
						bankCode = "zalo";
					}
					String desc = request.getParameter("ds");
					PaymentManualService manuService = new PaymentManualServiceImpl();
					resultResponse = manuService.deposit(nickName, fullName, amount, bankCode, bankAccountNum,
							paytypeStr, desc);
				default:
					break;
				}
				
				if (resultResponse == null) {
					return BaseResponse.error(Constant.ERROR_CREATE_TRANSACTION, "Không tạo được transaction");
				} else {
					logger.info("Deposit response nickName: " + nickName + ", response : " + resultResponse.toJson());
				}
				
				if (PaymentConstant.SUCCESS == resultResponse.getCode()) {
					return new BaseResponse<>().success(resultResponse.getData());
				} else if (PaymentConstant.MAINTAINCE == resultResponse.getCode()) {
					return BaseResponse.error(resultResponse.getCode() + "",
							"Cổng thanh toán đang bảo trì , quý khách vui lòng thực hiện lại trong ít phút");
				} else if (PaymentConstant.TOO_MANY_REQUEST == resultResponse.getCode()) {
					return BaseResponse.error(resultResponse.getCode() + "",
							"Quá nhiều yêu cầu gửi tiền , quý khách vui lòng thực hiện lại trong ít phút");
				} else {
					return BaseResponse.error(resultResponse.getCode() + "", resultResponse.getData());
				}

			} else {
				return BaseResponse.error(Constant.ERROR_SESSION, "Phiên giao dịch của quý khách đã hết , vui lòng tải lại trang và đăng nhập");
			}
		} catch (Exception e) {
			logger.error(e);
			return BaseResponse.error(Constant.ERROR_SYSTEM, e.getMessage());
		}

	}

}
