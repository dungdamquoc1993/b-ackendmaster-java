package com.vinplay.api.processors.wages;

import com.vinplay.payment.utils.Constant;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.UserWagesService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.usercore.service.impl.UserWagesServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class HistoryUserWagesProcessor implements BaseProcessor<HttpServletRequest, String> {
	private static final Logger logger = Logger.getLogger(HistoryUserWagesProcessor.class);

	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = param.get();
		String nickname = request.getParameter("nn");
		String accessToken = request.getParameter("at");
		String startTime = request.getParameter("ft");
		String endTime = request.getParameter("et");
		if (nickname == null || nickname.trim().isEmpty())
			return BaseResponse.error(Constant.ERROR_PARAM, "Nickname không được để trắng");

		if (accessToken == null || accessToken.trim().isEmpty())
			return BaseResponse.error(Constant.ERROR_PARAM, "Access token không được để trắng");

		int status = -1;
		try {
			status = Integer.parseInt(request.getParameter("st"));
		}catch (Exception e) {
			status = -1;
		}
		
		int pageIndex = 1;
		try {
			pageIndex = Integer.parseInt(request.getParameter("pg"));
		}catch (Exception e) {
			pageIndex = 1;
		}
		
		int limitItem = 10;
		try {
			limitItem = Integer.parseInt(request.getParameter("mi"));
		}catch (Exception e) {
			limitItem = 10;
		}
		
		UserService userService = new UserServiceImpl();
		boolean isToken = userService.isActiveToken(nickname, accessToken);
		if (!isToken) {
			return BaseResponse.error(Constant.ERROR_SESSION, "Phiên làm việc của bạn đã hết hạn , vui lòng tải lại trang !");
		}

		try {
			UserWagesService userWagesService = new UserWagesServiceImpl();
			Map<String, Object> map = new HashMap<>();
			map = userWagesService.history(nickname, startTime, endTime, status, pageIndex, limitItem);
			Long totalRecord = Long.parseLong(map.get("totalRecord").toString());
        	map.remove("totalRecord");
            return BaseResponse.success(map, totalRecord);
		} catch (Exception e) {
			logger.trace(e);
			return BaseResponse.error("1001", "Lỗi hệ thống. Vui lòng liên hệ bộ phận CSKH để được giúp đỡ.");
		}
	}
}