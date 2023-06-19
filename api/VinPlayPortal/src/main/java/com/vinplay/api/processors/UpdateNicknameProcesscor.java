package com.vinplay.api.processors;

import com.hazelcast.core.IMap;
import com.vinplay.api.utils.PortalUtils;
import com.vinplay.api.utils.SocialUtils;
import com.vinplay.usercore.service.MailBoxService;
import com.vinplay.usercore.service.UserLevelService;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.MailBoxServiceImpl;
import com.vinplay.usercore.service.impl.UserLevelServiceImpl;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.enums.StatusGames;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.models.SocialModel;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.response.LoginResponse;
import com.vinplay.vbee.common.statics.Consts;
import com.vinplay.vbee.common.utils.UserValidaton;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.eclipse.jetty.util.StringUtil;

public class UpdateNicknameProcesscor
implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger("api");

    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = (HttpServletRequest)param.get();
        String username = request.getParameter("un");
        String password = request.getParameter("pw");
        String nickname = request.getParameter("nn");
        String social = request.getParameter("s");
        String accessToken = request.getParameter("at");
        String parent_user = request.getParameter("inv");
        logger.debug("Request updateNickname: username: " + username + ",  social: " + social + ", accessToken: " + accessToken + ", nickname: " + nickname);
        if ((username != null && password != null || social != null && (social.equals("fb") || social.equals("gg")) && accessToken != null) && nickname != null) {
            LoginResponse res = new LoginResponse(false, "1001");
            try {
                int statusGame = GameCommon.getValueInt("STATUS_GAME");
                if (statusGame == StatusGames.MAINTAIN.getId()) {
                    res.setErrorCode("1114");
                    logger.debug(("Response login: " + res.toJson()));
                    return res.toJson();
                }
                if (UserValidaton.validateNickname(nickname)) {
                    if (UserValidaton.validateNicknameSpecial(nickname)) {
                        UserService userService = new UserServiceImpl();
                        if (social != null && (social.equals("fb") || social.equals("gg"))) {
                            String cache = social.equals("fb") ? "cacheFacebook" : "cacheGoogle";
                            IMap socialMap = HazelcastClientFactory.getInstance().getMap(cache);
                            String socialId = SocialUtils.getSocialId((IMap<String, SocialModel>)socialMap, accessToken, social);
                            if (socialId == null) {
                                logger.debug(("Response login: " + res.toJson()));
                                return res.toJson();
                            }
                            if (socialId.isEmpty()) {
                                res.setErrorCode("1009");
                                logger.debug(("Response login: " + res.toJson()));
                                return res.toJson();
                            }
                            UserModel userModel = userService.getUserBySocialId(socialId, social);
                            if (userModel != null) {
                                if (statusGame == StatusGames.SANDBOX.getId() && !userModel.isCanLoginSandbox()) {
                                    res.setErrorCode("1114");
                                    logger.debug(("Response login: " + res.toJson()));
                                    return res.toJson();
                                }
                                
                                if (!userModel.isBanLogin()) {
                                    if (userModel.getNickname() == null || userModel.getNickname().isEmpty()) {
                                        String errorCode = userService.updateNickname(userModel.getId(), nickname);
                                        if (errorCode == "0") {
                                            SocialUtils.socialSuccess((IMap<String, SocialModel>)socialMap, socialId, accessToken);
                                            userModel.setNickname(nickname);
                                            res = PortalUtils.loginSuccess(userModel, request);
                                            if(!StringUtil.isBlank(parent_user)) {
                								UserLevelService userLevelService = new UserLevelServiceImpl();
                								try{
                									userLevelService.create(nickname, parent_user);
                								}catch (Exception e) { }
                							}
                                            //send email
                                            sendRegisterEmail(nickname);
                                        } else {
                                            res.setErrorCode(errorCode);
                                        }
                                    } else {
                                        res.setErrorCode("1013");
                                    }
                                } else {
                                    res.setErrorCode("1109");
                                }
                            }
                        } else {
                            UserModel userModel2 = userService.getUserByUserName(username);
                            if (userModel2 != null) {
                                if (statusGame == StatusGames.SANDBOX.getId() && !userModel2.isCanLoginSandbox()) {
                                    res.setErrorCode("1114");
                                    logger.debug(("Response login: " + res.toJson()));
                                    return res.toJson();
                                }
                                if (!userModel2.isBanLogin()) {
                                    if (!userModel2.getUsername().toLowerCase().equals(nickname.toLowerCase())) {
                                        if (userModel2.getPassword().equals(password)) {
                                            if (userModel2.getNickname() == null || userModel2.getNickname().isEmpty()) {
                                                String errorCode2 = userService.updateNickname(userModel2.getId(), nickname);
                                                if (errorCode2 == "0") {
                                                    userModel2.setNickname(nickname);
                                                    res = PortalUtils.loginSuccess(userModel2, request);
                                                    if(!StringUtil.isBlank(parent_user)) {
                        								UserLevelService userLevelService = new UserLevelServiceImpl();
                        								try{
                        									userLevelService.create(nickname, parent_user);
                        								}catch (Exception e) { }
                        							}
													// send email
													sendRegisterEmail(nickname);
                                                } else {
                                                    res.setErrorCode(errorCode2);
                                                }
                                            } else {
                                                res.setErrorCode("1013");
                                            }
                                        } else {
                                            res.setErrorCode("1007");
                                        }
                                    } else {
                                        res.setErrorCode("1011");
                                    }
                                } else {
                                    res.setErrorCode("1109");
                                }
                            } else {
                                res.setErrorCode("1005");
                            }
                        }
                    } else {
                        res.setErrorCode("116");
                    }
                } else {
                    res.setErrorCode("106");
                }
            }
            catch (Exception e) {
                logger.debug(e);
            }
            logger.debug(("Response updateNickname: " + res.toJson()));
            return res.toJson();
        }
        return "MISSING PARAMETTER";
    }
    
	private void sendRegisterEmail(String nickname) {
		MailBoxService mailBoxService = new MailBoxServiceImpl();
		List<String> lstNickName = new ArrayList<>();
		lstNickName.add(nickname);
		mailBoxService.sendMailBoxFromByNickName(lstNickName, Consts.MAIL.TITLE_WELCOME,
				Consts.MAIL.CONTENT_WELCOME + String.format(Consts.MAIL.REGEX, Consts.MAIL.LINK_WELCOME));

		mailBoxService.sendMailBoxFromByNickName(lstNickName, Consts.MAIL.TITLE_NAPDAU,
				Consts.MAIL.CONTENT_NAPDAU + String.format(Consts.MAIL.REGEX, Consts.MAIL.LINK_NAPDAU));

		mailBoxService.sendMailBoxFromByNickName(lstNickName, Consts.MAIL.TITLE_BONUS_DAU,
				Consts.MAIL.CONTENT_BONUS_DAU + String.format(Consts.MAIL.REGEX, Consts.MAIL.LINK_BONUS_DAU));

		mailBoxService.sendMailBoxFromByNickName(lstNickName, Consts.MAIL.TITLE_GIOVANG,
				Consts.MAIL.CONTENT_GIOVANG + String.format(Consts.MAIL.REGEX, Consts.MAIL.LINK_GIOVANG));

		mailBoxService.sendMailBoxFromByNickName(lstNickName, Consts.MAIL.TITLE_TAIXIU,
				Consts.MAIL.CONTENT_TAIXIU + String.format(Consts.MAIL.REGEX, Consts.MAIL.LINK_TAIXIU));
	}
    
}

