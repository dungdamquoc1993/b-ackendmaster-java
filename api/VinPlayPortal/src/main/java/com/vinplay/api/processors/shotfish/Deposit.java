package com.vinplay.api.processors.shotfish;

import javax.servlet.http.HttpServletRequest;
import com.vinplay.payment.utils.Constant;
import com.vinplay.utils.ShotFishUtils;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

public class Deposit implements BaseProcessor<HttpServletRequest, String>{
	@Override
	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = (HttpServletRequest) param.get();
		String nickname = request.getParameter("nn");
		String accessToken = request.getParameter("at");
		String moneyStr = request.getParameter("mn");
		if(nickname == null || nickname.trim().isEmpty())
			return BaseResponse.error(Constant.ERROR_PARAM, "Nickname is not empty");
		
		if(accessToken == null || accessToken.trim().isEmpty())
			return BaseResponse.error(Constant.ERROR_PARAM, "Access token is not empty");
		
		Long money = 0l;
		try {
			money = Long.parseLong(moneyStr);
		} catch (Exception e) {
			return BaseResponse.error(Constant.ERROR_PARAM, "Số tiền nhập không đúng");
		}
		
		if(money < 10000)
			return BaseResponse.error(Constant.ERROR_PARAM, "Số tiền nạp tối thiểu là 10k");
		
		String remoteAddr = request.getRemoteAddr();
		ShotFishUtils.LoginGame(nickname, accessToken, remoteAddr, 0l).toJson();
		return ShotFishUtils.Deposit(nickname, accessToken, money).toJson();
	}
}
