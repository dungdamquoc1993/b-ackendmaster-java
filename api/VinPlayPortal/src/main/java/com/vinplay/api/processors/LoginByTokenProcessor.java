/*
 * Decompiled with CFR 0.144.
 *
 * Could not load the following classes:
 *  com.hazelcast.core.IMap
 *  com.vinplay.usercore.utils.GameCommon
 *  com.vinplay.vbee.common.cp.BaseProcessor
 *  com.vinplay.vbee.common.cp.Param
 *  com.vinplay.vbee.common.enums.StatusGames
 *  com.vinplay.vbee.common.hazelcast.HazelcastClientFactory
 *  com.vinplay.vbee.common.models.UserModel
 *  com.vinplay.vbee.common.models.cache.UserCacheModel
 *  com.vinplay.vbee.common.response.LoginResponse
 *  com.vinplay.vbee.common.utils.VinPlayUtils
 *  javax.servlet.http.HttpServletRequest
 *  org.apache.log4j.Logger
 */
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
import org.apache.log4j.Logger;

public class LoginByTokenProcessor implements BaseProcessor<HttpServletRequest, String> {
	private static final Logger logger = Logger.getLogger("api");

	public String execute(Param<HttpServletRequest> param) {
		LoginResponse res;
		HttpServletRequest request = (HttpServletRequest) param.get();
		String nickname = request.getParameter("u");
		String accessToken = request.getParameter("at");
		res = new LoginResponse(false, "1001");
		try {
			if (nickname == null || accessToken == null || nickname.isEmpty() || accessToken.isEmpty())
				return res.toJson();

			//TODO: Asenelupin add in case cached have not data (Keynotfound)
            int statusGame = StatusGames.RUN.getId();
            try{
            	statusGame = GameCommon.getValueInt("STATUS_GAME");
            }catch (Exception e) {
            	statusGame = StatusGames.RUN.getId(); 
			}
            
			if (statusGame == StatusGames.MAINTAIN.getId()) {
				res.setErrorCode("1114");
				logger.debug(("Response login: " + res.toJson()));
				return res.toJson();
			}
			IMap<String, UserCacheModel> userMap = HazelcastClientFactory.getInstance().getMap("users");
			if (!userMap.containsKey(nickname))
				return res.toJson();
			try {
				userMap.lock(nickname);
				UserCacheModel userCache = userMap.get(nickname);
				if (statusGame == StatusGames.SANDBOX.getId() && !userCache.isCanLoginSandbox()) {
					res.setErrorCode("1114");
					logger.debug("Response login: " + res.toJson());
					return res.toJson();
				}
				if (!userCache.isBanLogin()) {
					if (userCache.getAccessToken().equals(accessToken)) {
						if (!VinPlayUtils.sessionTimeout((long) userCache.getLastActive().getTime())) {
							UserModel user = new UserModel(userCache.getId(), userCache.getUsername(),
									userCache.getNickname(), userCache.getPassword(), userCache.getEmail(),
									userCache.getFacebookId(), userCache.getFacebookId(), userCache.getMobile(),
									userCache.getBirthday(), userCache.isGender(), userCache.getAddress(),
									userCache.getVin(), userCache.getXu(), userCache.getVinTotal(),
									userCache.getXuTotal(), userCache.getSafe(), userCache.getRechargeMoney(),
									userCache.getVippoint(), userCache.getDaily(), userCache.getStatus(),
									userCache.getAvatar(), userCache.getIdentification(), userCache.getVippointSave(),
									userCache.getCreateTime(), userCache.getMoneyVP(), userCache.getSecurityTime(),
									userCache.getLoginOtp(), userCache.isBot(), userCache.isVerifyMobile(),
									userCache.getUsertype(),userCache.getReferralCode());
							user.setClient(userCache.getClient());
							res = PortalUtils.loginSuccess(user, request);
						} else {
							res.setErrorCode("1015");
						}
					} else {
						res.setErrorCode("1014");
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
