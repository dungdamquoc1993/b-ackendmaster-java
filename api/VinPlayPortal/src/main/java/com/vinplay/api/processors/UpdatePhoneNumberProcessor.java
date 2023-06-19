package com.vinplay.api.processors;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.payment.utils.Constant;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.response.BaseResponse;

public class UpdatePhoneNumberProcessor implements BaseProcessor<HttpServletRequest, String> {
	private static final Logger logger = Logger.getLogger("api");

	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = (HttpServletRequest) param.get();
		String nickName = request.getParameter("nn");
		String phone = request.getParameter("pn");
		String accessToken = request.getParameter("at");
		if (StringUtils.isBlank(nickName)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "nickName is null or empty");
		}
		if (StringUtils.isBlank(accessToken)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "accessToken is null or empty");
		}

		// BaseResponse<String> res = TelegramUtil.verifyPhoneNumber(nickName,
		// sessionInfo, code);
		UserService userService = new UserServiceImpl();
		boolean isToken = userService.isActiveToken(nickName, accessToken);
		if (!isToken) {
			return BaseResponse.error(Constant.ERROR_SESSION, "Phiên làm việc của bạn đã hết hạn , vui lòng tải lại trang !");
		}
		try {
			if (userService.isXacThucSDT(nickName)) {
				return BaseResponse.error(Constant.ERROR_PARAM, "Tài khoản đã xác thực SĐT");
			}
		} catch (Exception e2) {
			logger.error(e2);
			return BaseResponse.error(Constant.ERROR_PARAM, "Tài khoản đã xác thực SĐT");
		}
		try {
			if (userService.isPhoneUsed(phone, nickName)) {
				// logger.info("phone đã sử dụng");
				return BaseResponse.error(Constant.ERROR_PARAM, "SĐT này đã được sử dụng");
			}
			if (!userService.updateVerifyMobile(nickName, phone, false)) {
				return BaseResponse.error(Constant.ERROR_SYSTEM, "Update data error");
			} else {
				updateCached(nickName, phone);
				return new BaseResponse<>().success(phone);
			}
		} catch (SQLException e1) {
			logger.error(e1);
		}
		return "";
	}

	private void updateCached(String nickName, String mobile) {
		HazelcastInstance client = HazelcastClientFactory.getInstance();
		IMap<String, UserModel> userMap = client.getMap("users");
		if (userMap.containsKey(nickName)) {
			try {
				userMap.lock(nickName);
				UserCacheModel user = (UserCacheModel) userMap.get(nickName);
				user.setMobile(mobile);
				user.setVerifyMobile(false);
				userMap.put(nickName, user);
			} finally {
				userMap.unlock(nickName);
			}
		}
	}

}
