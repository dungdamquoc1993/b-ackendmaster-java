///*
// * Decompiled with CFR 0.144.
// * 
// * Could not load the following classes:
// *  com.vinplay.usercore.service.impl.MailBoxServiceImpl
// *  com.vinplay.vbee.common.cp.BaseProcessor
// *  com.vinplay.vbee.common.cp.Param
// *  com.vinplay.vbee.common.response.BaseResponseModel
// *  javax.servlet.http.HttpServletRequest
// */
//package com.vinplay.api.processors;
//
//import com.hazelcast.core.IMap;
//import com.vinplay.api.processors.payment.InsertOrUpdateBankProcessor;
//import com.vinplay.payment.utils.Constant;
//import com.vinplay.utils.TelegramUtil;
//import com.vinplay.vbee.common.cp.BaseProcessor;
//import com.vinplay.vbee.common.cp.Param;
//import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
//import com.vinplay.vbee.common.models.cache.UserCacheModel;
//import com.vinplay.vbee.common.response.BaseResponse;
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.log4j.Logger;
//
//public class SendVerifyCodeProcessor
//implements BaseProcessor<HttpServletRequest, String> {
//	private static final Logger logger = Logger.getLogger(InsertOrUpdateBankProcessor.class);
//	
//    public String execute(Param<HttpServletRequest> param) {
//    	try{
//        HttpServletRequest request = (HttpServletRequest)param.get();
//        String nickName = request.getParameter("nn");
//        String phoneNumber = request.getParameter("pn");
//        String recaptchaToken = request.getParameter("cpt");
//        String accessToken = request.getParameter("at");
//        
//        if (StringUtils.isBlank(nickName)) {
//			return BaseResponse.error(Constant.ERROR_PARAM, "nickName is null or empty");
//		}
//        if (StringUtils.isBlank(phoneNumber)) {
//			return BaseResponse.error(Constant.ERROR_PARAM, "phoneNumber is null or empty");
//		}
//        if (StringUtils.isBlank(recaptchaToken)) {
//			return BaseResponse.error(Constant.ERROR_PARAM, "recaptchaToken is null or empty");
//		}
//        if (StringUtils.isBlank(accessToken)) {
//			return BaseResponse.error(Constant.ERROR_PARAM, "accessToken is null or empty");
//		}
//        
//        IMap userMap = HazelcastClientFactory.getInstance().getMap("users");
//		if (!userMap.containsKey(nickName))
//			return BaseResponse.error(Constant.ERROR_PARAM, "Nickname is invalid");
//		
//        UserCacheModel userCache = (UserCacheModel) userMap.get(nickName);
//		if (userCache.getAccessToken() == accessToken) {
//			return BaseResponse.error("1014", "accessToken is invalid");
//		}
//
//        String result = TelegramUtil.sendVerifyCode(nickName, "+"+phoneNumber.replace(" ", ""), recaptchaToken);
//        if(!result.isEmpty()) {
//        	return new BaseResponse<String>().success(result);
//        }
//        else
//        	return BaseResponse.error(Constant.ERROR_SYSTEM, "Send code verify error");
//    	}catch (Exception ex) {
//    		logger.error(ex);
//			return BaseResponse.error(Constant.ERROR_SYSTEM, ex.getMessage());
//		}
//    }
//}
//
