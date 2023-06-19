package com.vinplay.api.processors;

import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import com.vinplay.api.utils.PortalUtils;
import com.vinplay.usercore.dao.UserDao;
import com.vinplay.usercore.dao.impl.SecurityDaoImpl;
import com.vinplay.usercore.dao.impl.UserDaoImpl;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.MarketingServiceImpl;
import com.vinplay.usercore.service.impl.SecurityServiceImpl;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.usercore.utils.UserMakertingUtil;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.enums.StatusGames;
import com.vinplay.vbee.common.messages.UserMarketingMessage;
import com.vinplay.vbee.common.response.BaseResponseModel;
import com.vinplay.vbee.common.utils.UserValidaton;
import com.vinplay.vbee.common.utils.VinPlayUtils;

public class QuickRegisterProcessor implements BaseProcessor<HttpServletRequest, String> {
	private static final Logger logger = Logger.getLogger("api");

	public String execute(Param<HttpServletRequest> param) {
		BaseResponseModel res = new BaseResponseModel(false, "1001");
		block10: {
			HttpServletRequest request = (HttpServletRequest) param.get();
			String username = request.getParameter("un");
			String password = request.getParameter("pw");
		//	String captcha = request.getParameter("cp");
		//	String captchaId = request.getParameter("cid");
			String campaign = request.getParameter("utm_campaign");
			String medium = request.getParameter("utm_medium");
			String source = request.getParameter("utm_source");
			String c = request.getParameter("cl");
			String agency_code = request.getParameter("ac");
			logger.debug("Request quickRegister: username: " + username);
			try {
				int statusGame = GameCommon.getValueInt("STATUS_GAME");
				if (statusGame == StatusGames.MAINTAIN.getId() || statusGame == StatusGames.SANDBOX.getId()) {
					res.setErrorCode("1114");
					logger.debug(("Response login: " + res.toJson()));
					return res.toJson();
				}
				if (username == null || password == null)
					break block10;

				
					if (UserValidaton.validateUserName(username)) {
						try {
							UserService userService = new UserServiceImpl();
							res.setErrorCode(userService.insertUser(username, password));
							if (!res.getErrorCode().equals("0"))
								break block10;
							res.setSuccess(true);
							try {
								if (campaign != null && medium != null && source != null) {
									MarketingServiceImpl mktService = new MarketingServiceImpl();
									UserMarketingMessage message = new UserMarketingMessage(username, "", 0,
											VinPlayUtils.getCurrentDateMarketing(), campaign, medium, source);
									mktService.saveUserMarketing(message);
									UserMakertingUtil.newRegisterUser(campaign, medium, source);
								}
								 UserDao dao = new UserDaoImpl();
	                             int userId = dao.getIdByUsername(username);
								SecurityServiceImpl sercuSer = new SecurityServiceImpl();
								sercuSer.saveLoginInfo(userId, username, "", PortalUtils.getIpAddress(request),
										PortalUtils.getUserAgent(request), 0, "web");
								try {
									SecurityDaoImpl securDao = new SecurityDaoImpl();
									if (c != null) {
										if (c.toLowerCase().equals("m") || c.toLowerCase().equals("man")) {
											securDao.updateClient(userId, "M");
										} else if (c.toLowerCase().equals("r")) {
											securDao.updateClient(userId, "R");
										} else if (c.toLowerCase().equals("v")) {
											securDao.updateClient(userId, "V");
										} else if (c.toLowerCase().equals("k")) {
											securDao.updateClient(userId, "K");
										} else {
											securDao.updateClient(userId, "X");
										}
									} else {
										securDao.updateClient(userId, "X");
									}
									securDao.updateReferralCode(userId, agency_code);
								} catch (Exception ex) {
									logger.error(ex + "");
								}
								break block10;
							} catch (Exception e) {
								logger.debug(e);
							}
						} catch (SQLException e2) {
							logger.debug(e2);
						}
						break block10;
					}
					res.setErrorCode("101");
					break block10;
				
				
			} catch (Exception e3) {
				e3.printStackTrace();
			}
		}
		logger.debug(("Response quickRegister: " + res.toJson()));
		return res.toJson();
	}


}
