package com.vinplay.api.processors.wages;

import com.vinplay.payment.utils.Constant;
import com.vinplay.usercore.entities.UserLevel;
import com.vinplay.usercore.service.UserLevelService;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserLevelServiceImpl;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;

public class UpdateNicknameInviteProcessor implements BaseProcessor<HttpServletRequest, String> {
	private static final Logger logger = Logger.getLogger(UpdateNicknameInviteProcessor.class);

	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = param.get();
		String action = request.getParameter("ac");//get-update
		String accessToken = request.getParameter("at");
		String nickname = request.getParameter("nn");
		String parent_user = request.getParameter("inv");
		if (StringUtils.isBlank(nickname))
			return BaseResponse.error(Constant.ERROR_PARAM, "Nickname không được để trắng");

		if (StringUtils.isBlank(accessToken))
			return BaseResponse.error(Constant.ERROR_PARAM, "Access token không được để trắng");
		
		if("update".equalsIgnoreCase(action)) {
			if (StringUtils.isBlank(parent_user))
				return BaseResponse.error(Constant.ERROR_PARAM, "Mã người giới thiệu không được để trắng");
		}
		
		UserService userService = new UserServiceImpl();
		boolean isToken = userService.isActiveToken(nickname, accessToken);
		if (!isToken) {
			return BaseResponse.error(Constant.ERROR_SESSION, "Phiên làm việc của bạn đã hết hạn , vui lòng tải lại trang !");
		}

		try {
			UserLevelService service = new UserLevelServiceImpl();
			String result = "";
			switch (action) {
			case "update":
				result = service.create(nickname, parent_user);
				if("success".equalsIgnoreCase(result))
					return BaseResponse.success("0", result, result);
				else
					return BaseResponse.error(Constant.ERROR_PARAM, result);
			default:
				UserLevel userLevel = service.getByNickName(nickname);
				result = userLevel == null ? "" : userLevel.getParent_user();
				return BaseResponse.success("0", "success", result);
			}
		} catch (Exception e) {
			logger.trace(e);
			return BaseResponse.error("1001", "Lỗi hệ thống. Vui lòng liên hệ bộ phận CSKH để được giúp đỡ.");
		}
	}
}