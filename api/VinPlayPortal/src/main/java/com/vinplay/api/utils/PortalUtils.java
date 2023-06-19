package com.vinplay.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hazelcast.client.HazelcastClientNotActiveException;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.api.processors.GetAppConfigProcesscor;
import com.vinplay.dichvuthe.service.impl.AlertServiceImpl;
import com.vinplay.usercore.dao.impl.VippointDaoImpl;
import com.vinplay.usercore.service.MarketingService;
import com.vinplay.usercore.service.impl.GameConfigServiceImpl;
import com.vinplay.usercore.service.impl.MailBoxServiceImpl;
import com.vinplay.usercore.service.impl.MarketingServiceImpl;
import com.vinplay.usercore.service.impl.SecurityServiceImpl;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.models.MarketingModel;
import com.vinplay.vbee.common.models.UserClientInfo;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.models.cache.UserExtraInfoModel;
import com.vinplay.vbee.common.models.vippoint.UserVPEventModel;
import com.vinplay.vbee.common.response.LoginResponse;
import com.vinplay.vbee.common.utils.DateTimeUtils;
import com.vinplay.vbee.common.utils.VinPlayUtils;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.json.JSONException;

public class PortalUtils {
	private static final Logger logger = Logger.getLogger((String) "api");
	private static MarketingService mktService = new MarketingServiceImpl();

	public static LoginResponse loginSuccess(UserModel userModel, HttpServletRequest request)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, JsonProcessingException, SQLException {
		boolean success = true;
		String accessToken = "";
		String sessionKey = "";
		UserServiceImpl ser = new UserServiceImpl();
		int luckyRotate = 0;
//		if (userModel.getDaily() == 0) {
//			LuckyServiceImpl luckySer = new LuckyServiceImpl();
//			luckyRotate = luckySer.receiveRotateDaily(userModel.getId(), userModel.getNickname());
//		}
		String ip = PortalUtils.getIpAddress(request);
		String platform = request.getParameter("pf");
		try {
			IMap userExtraMap;
			UserCacheModel userCache;
			block18: {
				HazelcastInstance instance = HazelcastClientFactory.getInstance();
				IMap userMap = instance.getMap("users");
				userExtraMap = instance.getMap("cache_user_extra_info");
				IMap tokenMap = instance.getMap("cacheToken");
				userCache = null;
				if (userMap.containsKey(userModel.getNickname())) {
					try {
						userMap.lock(userModel.getNickname());
						userCache = (UserCacheModel) userMap.get(userModel.getNickname());
						if (userCache.getAccessToken() == null
								|| VinPlayUtils.sessionTimeout((long) userCache.getLastActive().getTime())) {
							accessToken = VinPlayUtils.genAccessToken((int) userModel.getId());
							userCache.setAccessToken(accessToken);
						} else {
							// accessToken = userCache.getAccessToken();
							accessToken = VinPlayUtils.genAccessToken((int) userModel.getId());
							userCache.setAccessToken(accessToken);
							if (tokenMap.containsValue(userModel.getNickname()))
								tokenMap.remove(userModel.getNickname());
						}
						userCache.setLastActive(new Date());
						userCache.setIp(ip);
						userCache.setMobile(userModel.getMobile());
						userCache.setVerifyMobile(userModel.isVerifyMobile());
						userCache.setTeleId(userModel.getTeleId());
						UserCacheModel uc = ser.checkMoneyNegative(userCache);
						if (uc != null) {
							userCache = uc;
							success = false;
						}
						tokenMap.put(userModel.getNickname(), accessToken, 180L, TimeUnit.MINUTES);
						userMap.put(userModel.getNickname(), userCache);
					} catch (Exception e) {
						logger.debug(e);
						success = false;
						break block18;
					}finally {
						userMap.unlock(userModel.getNickname());
					}
				} else {
					UserCacheModel uc2;
					MarketingModel mktModel = mktService.getMKTInfo(userModel.getUsername());
					VippointDaoImpl vpDao = new VippointDaoImpl();
					UserVPEventModel vpModel = vpDao.getUserVPByNickName(userModel.getNickname());
					userCache = new UserCacheModel(userModel.getId(), userModel.getUsername(), userModel.getNickname(),
							userModel.getPassword(), userModel.getEmail(), userModel.getFacebookId(),
							userModel.getGoogleId(), userModel.getMobile(), userModel.getBirthday(),
							userModel.isGender(), userModel.getAddress(), userModel.getVin(), userModel.getXu(),
							userModel.getVinTotal(), userModel.getXuTotal(), userModel.getSafe(),
							userModel.getRechargeMoney(), userModel.getVippoint(), userModel.getDaily(),
							userModel.getStatus(), userModel.getAvatar(), userModel.getIdentification(),
							userModel.getVippointSave(), userModel.getCreateTime(), userModel.getMoneyVP(),
							userModel.getSecurityTime(), userModel.getLoginOtp(), userModel.isBot(),userModel.isVerifyMobile(),
							0, null, vpModel.getVpEvent(), vpModel.getVpReal(), vpModel.getVpAdd(),
							vpModel.getVpSub(), vpModel.getNumAdd(), vpModel.getNumSub(), vpModel.getPlace(),
							vpModel.getPlaceMax(),userModel.getUsertype(),userModel.getReferralCode());
					userCache.setClient(userModel.getClient());
					accessToken = VinPlayUtils.genAccessToken((int) userModel.getId());
					userCache.setAccessToken(accessToken);
					userCache.setLastMessageId(0L);
					userCache.setLastActive(new Date());
					userCache.setOnline(0);
					userCache.setIp(ip);
					if (mktModel != null) {
						userCache.setCampaign(mktModel.campaign);
						userCache.setMedium(mktModel.medium);
						userCache.setSource(mktModel.source);
					}
					if ((uc2 = ser.checkMoneyNegative(userCache)) != null) {
						userCache = uc2;
						success = false;
					}
					tokenMap.put(accessToken, userModel.getNickname(), 180L, TimeUnit.MINUTES);
					userMap.put(userCache.getNickname(), userCache);
				}
			}
			int mobileSecure = userCache.isHasMobileSecurity() ? 1 : 0;
			int appSecure = userCache.isHasAppSecurity() ? 1 : 0;
			String birthday = "";
			if (userCache.getBirthday() != null && !userCache.getBirthday().isEmpty()) {
				birthday = userCache.getBirthday();
			}
			UserClientInfo userInfo = new UserClientInfo(userCache.getNickname(), userCache.getAvatar(),
					userCache.getVinTotal(), userCache.getXuTotal(), userCache.getVippoint(),
					userCache.getVippointSave(), VinPlayUtils.parseDateToString((Date) userCache.getCreateTime()), ip,
					false, luckyRotate, userCache.getDaily(), mobileSecure, birthday, appSecure,
					userCache.getUsername(), userCache.isVerifyMobile(), userCache.getEmail(), userCache.getAddress());
			sessionKey = VinPlayUtils.genSessionKey((UserClientInfo) userInfo);
			try {
				SecurityServiceImpl sercuSer = new SecurityServiceImpl();
				sercuSer.saveLoginInfo(userCache.getId(), userCache.getUsername(), userCache.getNickname(), ip,
						PortalUtils.getUserAgent(request), 1, platform);
				UserExtraInfoModel userExtraModel = new UserExtraInfoModel(userCache.getNickname(), platform);
				userExtraMap.put(userCache.getNickname(), userExtraModel);
				logger.debug(("User " + userCache.getNickname() + " login success in platform: " + platform));
			} catch (Exception e2) {
				logger.error(e2);
			}
		} catch (HazelcastClientNotActiveException he) {
			AlertServiceImpl alert = new AlertServiceImpl();
			alert.sendSMS2One("0984574749", "Hazelcast is shutdown, " + DateTimeUtils.getCurrentTime(), true);
			success = false;
		}
		if (success) {
			return new LoginResponse(true, "0", sessionKey, accessToken);
		}
		return new LoginResponse(false, "1001");
	}

	public static String getIpAddress(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		String clientIp = null;
		if (ipAddress != null && !"".equals(ipAddress)) {
			String[] arrayIp = ipAddress.split(",");
			if(arrayIp.length>0) {
				clientIp = arrayIp[0].trim();
			}
		}
		return clientIp;
	}
	
	public static String getUserAgent(HttpServletRequest request) {
		String agent = request.getHeader("USER-AGENT");
		if (agent == null) {
			agent = "";
		}
		return agent;
	}

	public static void loadGameConfig() throws SQLException, JSONException, ParseException {
		GameConfigServiceImpl ser = new GameConfigServiceImpl();
		GetAppConfigProcesscor.configs = ser.getGameConfig();
		GameCommon.init();
		 //add game3rd config
		GameThirdPartyInit.init();
	}

	public static List<String> parseStringToList(String urlHelp) {
		ArrayList<String> res = new ArrayList<String>();
		if (urlHelp != null && !urlHelp.isEmpty()) {
			if (urlHelp.contains(",")) {
				String[] arr = urlHelp.trim().split(",");
				for (int i = 0; i < arr.length; ++i) {
					res.add(arr[i]);
				}
			} else {
				res.add(urlHelp);
			}
		}
		return res;
	}

	public static boolean checkCaptcha(String captcha, String captchaId) {
		IMap captchaCache;
		boolean isCaptcha = false;
		if (!(captcha = captcha.trim().toLowerCase()).isEmpty()
				&& (captchaCache = HazelcastClientFactory.getInstance().getMap("cacheCaptcha")).containsKey(captchaId)
				&& ((String) captchaCache.get(captchaId)).equals(captcha)) {
			captchaCache.remove(captchaId);
			isCaptcha = true;
		}
		return isCaptcha;
	}

	public static void sendMessageKichHoatBaoMatToUser(String nickname) {
		MailBoxServiceImpl mail = new MailBoxServiceImpl();
		mail.sendMailBoxFromByNickNameAdmin(nickname,
				"K\u00edch ho\u1ea1t b\u1ea3o m\u1eadt \u0111\u1ec3 s\u1eed d\u1ee5ng t\u00ednh n\u0103ng \u0111\u1ed5i th\u01b0\u1edfng",
				"Nh\u1eb1m m\u1ee5c \u0111\u00edch h\u1ed7 tr\u1ee3 t\u1ed1t nh\u1ea5t khi ng\u01b0\u1eddi ch\u01a1i g\u1eb7p s\u1ef1 c\u1ed1 c\u0169ng nh\u01b0 \u0111\u1ea3m b\u1ea3o \u0111\u1ea7y \u0111\u1ee7 quy\u1ec1n l\u1ee3i cho ng\u01b0\u1eddi ch\u01a1i. VINPLAY y\u00eau c\u1ea7u t\u1ea5t c\u1ea3 c\u00e1c t\u00e0i kho\u1ea3n mu\u1ed1n s\u1eed d\u1ee5ng Giftcode hay d\u00f9ng t\u00ednh n\u0103ng Ti\u00eau Vin \u0111\u1ec1u ph\u1ea3i \u0111\u0103ng k\u00fd b\u1ea3o m\u1eadt S\u0110T. Sau 24h k\u1ec3 t\u1eeb th\u1eddi \u0111i\u1ec3m k\u00edch ho\u1ea1t ng\u01b0\u1eddi ch\u01a1i s\u1ebd s\u1eed d\u1ee5ng \u0111\u01b0\u1ee3c c\u00e1c t\u00ednh n\u0103ng \u0111\u1ed5i th\u01b0\u1edfng. Chi ti\u1ebft xem t\u1ea1i vinplay.net");
	}

	public static LoginResponse registerSuccess(UserModel userModel, HttpServletRequest request)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, JsonProcessingException, SQLException {
		boolean success = true;
		String accessToken = "";
		String sessionKey = "";
		UserServiceImpl ser = new UserServiceImpl();
		String ip = PortalUtils.getIpAddress(request);
		IMap userExtraMap;
		UserCacheModel userCache;
		HazelcastInstance instance = HazelcastClientFactory.getInstance();
		try {
			IMap userMap = instance.getMap("users");
			userExtraMap = instance.getMap("cache_user_extra_info");
			IMap tokenMap = instance.getMap("cacheToken");
			userCache = null;
			if (userMap.containsKey(userModel.getNickname())) {
				try {
					userMap.lock(userModel.getNickname());
					userCache = (UserCacheModel) userMap.get(userModel.getNickname());
					if (userCache.getAccessToken() == null
							|| VinPlayUtils.sessionTimeout((long) userCache.getLastActive().getTime())) {
						accessToken = VinPlayUtils.genAccessToken((int) userModel.getId());
						userCache.setAccessToken(accessToken);
					} else {
						accessToken = userCache.getAccessToken();
					}
					userCache.setLastActive(new Date());
					userCache.setIp(ip);
					UserCacheModel uc = ser.checkMoneyNegative(userCache);
					if (uc != null) {
						userCache = uc;
						success = false;
					}
					tokenMap.put(accessToken, userModel.getNickname(), 180L, TimeUnit.MINUTES);
					userMap.put(userModel.getNickname(), userCache);
				} catch (Exception e) {
					logger.debug(e);
					success = false;
				} finally {
					userMap.unlock(userModel.getNickname());
				}
			}

		} catch (HazelcastClientNotActiveException he) {
			AlertServiceImpl alert = new AlertServiceImpl();
			alert.sendSMS2One("0984574749", "Hazelcast is shutdown, " + DateTimeUtils.getCurrentTime(), true);
			success = false;
		}
		if (success) {
			return new LoginResponse(true, "0", sessionKey, accessToken);
		}
		return new LoginResponse(false, "1001");
	}
}
