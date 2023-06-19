package com.vinplay.api.processors;

import javax.servlet.http.HttpServletRequest;

import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;

public class ConfigGameStatusProcessor implements BaseProcessor<HttpServletRequest, String> {

	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = (HttpServletRequest) param.get();
		String platform = request.getParameter("pl");
		return GameCommon.getConfigVersionStatus(platform);
	}
}