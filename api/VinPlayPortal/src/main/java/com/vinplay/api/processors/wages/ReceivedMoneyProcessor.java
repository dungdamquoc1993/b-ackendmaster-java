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

public class ReceivedMoneyProcessor implements BaseProcessor<HttpServletRequest, String> {
	private static final Logger logger = Logger.getLogger(ReceivedMoneyProcessor.class);

	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = param.get();
		String idStr = request.getParameter("id");
		String action = request.getParameter("ac");
		String accessToken = request.getParameter("at");
		String nickname = request.getParameter("nn");
		if (nickname == null || nickname.trim().isEmpty())
			return BaseResponse.error(Constant.ERROR_PARAM, "Nickname không được để trắng");

		if (accessToken == null || accessToken.trim().isEmpty())
			return BaseResponse.error(Constant.ERROR_PARAM, "Access token không được để trắng");
		
		UserService userService = new UserServiceImpl();
		boolean isToken = userService.isActiveToken(nickname, accessToken);
		if (!isToken) {
			return BaseResponse.error(Constant.ERROR_SESSION, "Phiên làm việc của bạn đã hết hạn , vui lòng tải lại trang !");
		}

		try {
			UserWagesService service = new UserWagesServiceImpl();
			String result = "";
			if("all".equalsIgnoreCase(action)) {
				result = service.receivedAllMoney(nickname);
			}else {
				long id = 0;
				try {
					id = Long.parseLong(idStr);
				}catch (Exception e) {
					id = 0;
				}
				
				if (id == 0)
					return BaseResponse.error(Constant.ERROR_PARAM, "Access token không được để trắng");
				
				result = service.receivedMoney(id);
			}
			
			return "success".equalsIgnoreCase(result) ? BaseResponse.success(result, 0) : BaseResponse.error("1001", result);
		} catch (Exception e) {
			logger.trace(e);
			return BaseResponse.error("1001", "Lỗi hệ thống. Vui lòng liên hệ bộ phận CSKH để được giúp đỡ.");
		}
	}
}