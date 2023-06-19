package com.vinplay.api.backend.processors.money;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.vinplay.payment.utils.Constant;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.OtpServiceImpl;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.response.BaseResponse;
import com.vinplay.vbee.common.response.BaseResponseModel;
import com.vinplay.vbee.common.response.MoneyResponse;

public class UpdateMoneyUserProcessor implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger("backend");

    private static Map<String, Long> mapCache = new ConcurrentHashMap<String, Long>();

    private static boolean validateRequest(String nickName) {
        if (mapCache.isEmpty()) {
            long t1 = new java.util.Date().getTime();
            mapCache.put(nickName, t1);
        } else {
            if (mapCache.containsKey(nickName)) {

                long t1 = mapCache.get(nickName);
                long t2 = new java.util.Date().getTime();
                if ((t2 - t1) > 1000 * 20) {
                    mapCache.put(nickName, t2);
                    return true;
                } else {
                    return false;
                }

            } else {
                long t1 = new java.util.Date().getTime();
                mapCache.put(nickName, t1);
            }
        }
        return true;
    }

    private String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = param.get();
        BaseResponseModel response = new BaseResponseModel(false, "1001");
        try {
            String actionName = request.getParameter("ac");
            String nickname = request.getParameter("nn");
            long money = Long.valueOf(request.getParameter("mn"));
            String moneyType = request.getParameter("mt");
            String reason = request.getParameter("rs");
            String otp = request.getParameter("otp");
            String type = request.getParameter("type");
            String nicknameSend = request.getParameter("nns");
            if (!validateRequest(nickname)) {
                return BaseResponse.error(Constant.ERROR_DUPLICATE, "Trong 20s chỉ được thao tác user này 1 lần , nickname=" + nickname);
            }
            UserService service = new UserServiceImpl();
            UserCacheModel ucheck = service.getUser(nicknameSend);
            if (ucheck == null) {
                return BaseResponse.error(Constant.ERROR_NOT_EXIST + "",
                        "Tài khoản này không tồn tại nickname=" + nickname);
            }
            String ip = getIpAddress(request);
            logger.info("Request UpdateMoneyUser: nickname: " + nickname + ", money: " + money + ", moneyType: "
                    + moneyType + ", reason: " + reason + ", otp: " + otp + ", otpType: " + type + ", ip=" + ip);
            if (nickname != null && reason != null && !reason.isEmpty() && money != 0L && moneyType != null
                    && (moneyType.equals("vin") || moneyType.equals("xu")) && otp != null && type != null
                    && (type.equals("1") || type.equals("0"))) {
                OtpServiceImpl otpService = new OtpServiceImpl();
                int code = 3;
                if (moneyType.equals("vin")) {
                    if (otp.equals("1") || "".equals(otp)) {
                        code = 0;
                    } else {
                        String[] arr = GameCommon.getValueStr("SUPER_ADMIN").split(",");
                        int i = 0;
                        String[] array = arr;
                        int length = array.length;
                        for (int j = 0; j < length && (code = otpService.checkOdp(array[j], otp)) != 0; ++j) {
                            if (i > 0 && money > 2000000L) {
                                return response.toJson();
                            }
                            ++i;
                        }
                    }

                } else if (nicknameSend != null) {
                    String[] arr = GameCommon.getValueStr("SUPER_ADMIN").split(",");
                    code = otpService.checkOdp(arr[0], otp);
                }
                if (code == 0) {
                    if (actionName == null) {
                        actionName = "Admin";
                    }

                    MoneyResponse mr = service.updateMoneyFromAdmin
                            (nickname, money, moneyType, "Admin", "Admin", reason, 0, false);
                    response.setSuccess(mr.isSuccess());
                    response.setErrorCode(mr.getErrorCode());
                } else if (code == 3) {
                    response.setErrorCode("1008");
                } else if (code == 4) {
                    response.setErrorCode("1021");
                }

                logger.debug("Code UpdateMoneyUser: " + code);
            }
        } catch (Exception e) {
            logger.error(e);
        }
        logger.debug("Response UpdateMoneyUser: " + response.toJson());
        return response.toJson();
    }
}
