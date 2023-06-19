package com.vinplay.api.processors.giftCode;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.vinplay.giftcode.GiftCodeErrorCode;
import com.vinplay.giftcode.GiftCodeModel;
import com.vinplay.giftcode.GiftCodeUtil;
import com.vinplay.payment.utils.Constant;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.enums.Games;
import com.vinplay.vbee.common.response.BaseResponse;
import com.vinplay.vbee.common.statics.TransType;

public class GiftCodeProcessor
        implements BaseProcessor<HttpServletRequest, String> {
    private Logger logger = Logger.getLogger("api");

    public String execute(Param<HttpServletRequest> param) {
        GiftCodeResponse response = new GiftCodeResponse(false, GiftCodeErrorCode.WRONG_GIFT_CODE+"");
        Gson gson = new Gson();
        HttpServletRequest request = param.get();
        String username = request.getParameter("un");
        String giftCode = request.getParameter("giftcode");
        String ip = getIpAddress(request);
        UserService userService = new UserServiceImpl();
        GiftCodeModel giftCodeModel = GiftCodeUtil.getGiftCode(giftCode);

        logger.debug("GiftCodeProcessor " + request.getQueryString());
        if (giftCodeModel != null) {
        	//validation giftcode
            int errorCode = GiftCodeUtil.isUsedGiftCode(giftCodeModel,username,ip,userService);
            //switch case
            if (String.valueOf(errorCode).equals(Constant.ERROR_SYSTEM)) {
            	response.setErrorCode(errorCode+"");
            	response.setMessage("Quý khách đã được nhận giftcode đợt này rồi");
            	return response.toJson();
    		}else if (String.valueOf(errorCode).equals(Constant.ERROR_SAMEIP)) {
    			response.setErrorCode(errorCode+"");
            	response.setMessage( "Quý khách vui lòng nhận giftcode đợt sau !");
            	return response.toJson();
			}else if (String.valueOf(errorCode).equals(Constant.ERROR_BANK_ADD)) {
				response.setErrorCode(errorCode+"");
            	response.setMessage("Quý khách vui lòng thêm tài khoản ngân hàng để nhận giftcode ");
            	return response.toJson();
			}else if(String.valueOf(errorCode).equals(Constant.ERROR_VERIFYPHONE)){
				response.setErrorCode(errorCode+"");
            	response.setMessage("Quý khách vui lòng xác thực SĐT để nhận giftcode");
            	return response.toJson();
			}else {
				response.setErrorCode(errorCode + "");
				if (errorCode == GiftCodeErrorCode.SUCCESS) {
					response = new GiftCodeResponse(true, "0");
					userService.updateMoney(username, giftCodeModel.money, "vin", Games.GIFT_CODE.getName(),
							Games.GIFT_CODE.getId() + "", gson.toJson(new GiftCodeDescription(giftCodeModel.giftcode)), 0L,
							null, TransType.NO_VIPPOINT);
					response.currentMoney = userService.getCurrentMoneyUserCache(username, "vin");
				}
			}
        }
        return response.toJson();
    }
    private String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
   
}

