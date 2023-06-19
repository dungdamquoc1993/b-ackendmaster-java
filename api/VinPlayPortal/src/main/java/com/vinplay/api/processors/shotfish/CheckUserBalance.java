package com.vinplay.api.processors.shotfish;

import javax.servlet.http.HttpServletRequest;
import com.vinplay.payment.utils.Constant;
import com.vinplay.utils.ShotFishUtils;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

public class CheckUserBalance implements BaseProcessor<HttpServletRequest, String>{
	@Override
	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = (HttpServletRequest) param.get();
		String nickname = request.getParameter("nn");
		String accessToken = request.getParameter("at");
		if(nickname == null || nickname.trim().isEmpty())
			return BaseResponse.error(Constant.ERROR_PARAM, "Nickname is not empty");
		
		if(accessToken == null || accessToken.trim().isEmpty())
			return BaseResponse.error(Constant.ERROR_PARAM, "Access token is not empty");
		
		BaseResponse<Object> valid = ShotFishUtils.CheckUserInfo(nickname, accessToken, 0l, false);
		if(!valid.isSuccess())
			return valid.toJson();
		
		return ShotFishUtils.CheckUserBalance(nickname).toJson();

	}
}
