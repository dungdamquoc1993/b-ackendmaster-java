package com.vinplay.api.processors;

import com.hazelcast.core.IMap;
import com.vinplay.api.utils.PortalUtils;
import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.enums.StatusGames;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.response.LoginResponse;
import com.vinplay.vbee.common.utils.VinPlayUtils;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class LoginTokenProcessor implements BaseProcessor<HttpServletRequest, String> {
	private static final Logger logger = Logger.getLogger((String) "api");

	public String execute(Param<HttpServletRequest> param) {
		
		HttpServletRequest request = param.get();
		LoginResponse res = new LoginResponse(false, "1001");
		//validation input
		String nickname = request.getParameter("nn");
		String accessToken = request.getParameter("at");
		if (StringUtils.isBlank(nickname) || StringUtils.isBlank(accessToken)) {
			return res.toJson();
		}
		
		try {
			int statusGame = GameCommon.getValueInt("STATUS_GAME");
			if (statusGame == StatusGames.MAINTAIN.getId()) {
				res.setErrorCode("1114");
				logger.debug("Response login: " + res.toJson());
				return res.toJson();
			}
			IMap<String,UserModel> userMap = HazelcastClientFactory.getInstance().getMap("users");
			if (!userMap.containsKey(nickname))
				return res.toJson();
			//check token
			UserCacheModel userCache = (UserCacheModel) userMap.get(nickname);
			if (userCache.getAccessToken().equals(accessToken)) {
				res.setErrorCode("1014");
			}
			try {
				userMap.lock(nickname);
				if (statusGame == StatusGames.SANDBOX.getId() && !userCache.isCanLoginSandbox()) {
					res.setErrorCode("1114");
					logger.debug("Response login: " + res.toJson());
					return res.toJson();
				}
				if (!userCache.isBanLogin()) {
					if (!VinPlayUtils.sessionTimeout((long) userCache.getLastActive().getTime())) {
						UserModel user = new UserModel(userCache.getId(), userCache.getUsername(),
								userCache.getNickname(), userCache.getPassword(), userCache.getEmail(),
								userCache.getFacebookId(), userCache.getFacebookId(), userCache.getMobile(),
								userCache.getBirthday(), userCache.isGender(), userCache.getAddress(),
								userCache.getVin(), userCache.getXu(), userCache.getVinTotal(), userCache.getXuTotal(),
								userCache.getSafe(), userCache.getRechargeMoney(), userCache.getVippoint(),
								userCache.getDaily(), userCache.getStatus(), userCache.getAvatar(),
								userCache.getIdentification(), userCache.getVippointSave(), userCache.getCreateTime(),
								userCache.getMoneyVP(), userCache.getSecurityTime(), userCache.getLoginOtp(),
								userCache.isBot(), userCache.isVerifyMobile(),userCache.getUsertype(),userCache.getReferralCode());
						user.setClient(userCache.getClient());
						res = PortalUtils.loginSuccess(user, request);
					} else {
						res.setErrorCode("1015");
					}
				} else {
					res.setErrorCode("1109");
				}
			} catch (Exception e) {
				logger.debug(e);
				return res.toJson();
			} finally {
				userMap.unlock(nickname);
			}
		} catch (Exception e2) {
			logger.debug(e2);
		}
		return res.toJson();
	}
}
