package com.vinplay.api.processors.shotfish;

import javax.servlet.http.HttpServletRequest;
import com.vinplay.payment.utils.Constant;
import com.vinplay.utils.ShotFishUtils;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

public class History implements BaseProcessor<HttpServletRequest, String>{
	@Override
	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = (HttpServletRequest) param.get();
		String startTime = request.getParameter("st");
		String endTime = request.getParameter("et");
		if(startTime == null || startTime.trim().isEmpty())
			return BaseResponse.error(Constant.ERROR_PARAM, "Start time is not empty");
		
		if(endTime == null || endTime.trim().isEmpty())
			return BaseResponse.error(Constant.ERROR_PARAM, "End time is not empty");
		
		return ShotFishUtils.History(startTime, endTime).toJson();
	}
}
