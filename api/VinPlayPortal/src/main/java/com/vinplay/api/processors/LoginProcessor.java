package com.vinplay.api.processors;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import bitzero.server.BitZeroServer;
import bitzero.server.entities.User;
import com.google.gson.Gson;
import com.vinplay.usercore.dao.UserDao;
import com.vinplay.usercore.dao.impl.SecurityDaoImpl;
import com.vinplay.usercore.dao.impl.UserDaoImpl;
import com.vinplay.usercore.service.UserLevelService;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.*;
import org.apache.log4j.Logger;
import org.eclipse.jetty.util.StringUtil;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import com.hazelcast.core.IMap;
import com.vinplay.api.utils.PortalUtils;
import com.vinplay.api.utils.SocialUtils;
import com.vinplay.usercore.service.OtpService;
import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.usercore.utils.UserMakertingUtil;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.enums.StatusGames;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.messages.UserMarketingMessage;
import com.vinplay.vbee.common.models.SocialModel;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.response.LoginResponse;
import com.vinplay.vbee.common.utils.UserValidaton;
import com.vinplay.vbee.common.utils.VinPlayUtils;

import bitzero.util.common.business.Debug;

public class LoginProcessor
        implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger("api");

    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = (HttpServletRequest) param.get();
        String username = request.getParameter("un");
        String password = request.getParameter("pw");
        String social = request.getParameter("s");
        String accessToken = request.getParameter("at");
        String cp = request.getParameter("cp");
        String appid = request.getParameter("appid");
        request.getHeader("user-agent");
        logger.debug(("Request login: username: " + username + ", social: " + social + "," +
                " accessToken: " + accessToken));
        if ((username != null && password != null) || (social != null && ((social.equals("fb") ||
                social.equals("gg"))) && accessToken != null)) {
            LoginResponse res = new LoginResponse(false, "1001");
            try {
                //TODO: Asenelupin add in case cached have not data (Keynotfound)
                int statusGame = StatusGames.RUN.getId();
                try {
                    statusGame = GameCommon.getValueInt("STATUS_GAME");
                } catch (Exception e) {
                    statusGame = StatusGames.RUN.getId();
                }

                if (statusGame == StatusGames.MAINTAIN.getId()) {
                    res.setErrorCode("1114");
                    logger.debug(("Response login: " + res.toJson()));
                    return res.toJson();
                }

                logger.debug("Request login: 1");

                UserServiceImpl userService = new UserServiceImpl();
                if (social != null && ((social.equals("fb") || social.equals("gg")))) {
                    String cache = social.equals("fb") ? "cacheFacebook" : "cacheGoogle";
                    IMap<String, SocialModel> socialMap = HazelcastClientFactory.getInstance().getMap(cache);
                    String socialId = SocialUtils.getSocialId(socialMap, accessToken, social);
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
                    if (userModel == null) {
                        if (statusGame == StatusGames.SANDBOX.getId()) {
                            res.setErrorCode("1114");
                            return res.toJson();
                        }

                        // todo sua stagging tren function mysql
                        if (userService.insertUserBySocial(socialId, social)) {
                            socialMap.put(socialId, new SocialModel(accessToken, socialId, new Date()));
                            String campaign = request.getParameter("utm_campaign");
                            String medium = request.getParameter("utm_medium");
                            String source = request.getParameter("utm_source");
                            if (campaign != null && medium != null && source != null) {
                                MarketingServiceImpl mktService = new MarketingServiceImpl();
                                UserMarketingMessage message = new UserMarketingMessage(username, "", 0, VinPlayUtils.getCurrentDateMarketing(), campaign, medium, source);
                                mktService.saveUserMarketing(message);
                                UserMakertingUtil.newRegisterUser(campaign, medium, source);
                            }
                            //todo sua lai, tra ma loi 2001 de update nick name voi khi login thi check nickname da ton tai hay chua tra ra 2001
                            userModel = userService.getUserBySocialId(socialId, social);
                            res.setErrorCode("2001");
                            return res.toJson();
//                            SocialUtils.socialSuccess((IMap<String, SocialModel>) socialMap, socialId, accessToken);
//                            res = PortalUtils.loginSuccess(userModel, request);
                        }

                    } else {
                        if (statusGame == StatusGames.SANDBOX.getId() && !userModel.isCanLoginSandbox()) {
                            res.setErrorCode("1115");
                            logger.debug(("Response login: " + res.toJson()));
                            return res.toJson();
                        }

                        if (userModel.isBanLogin()) {
                            res.setErrorCode("1109");
                            return res.toJson();
                        }

                        if (userModel.getNickname() == null || userModel.getNickname().trim().isEmpty()) {
                            res.setErrorCode("2001");
                            return res.toJson();
                        }

                        if (userModel.isHasLoginSecurity() && userModel.getLoginOtp() >= 0L && userModel.getLoginOtp() <= userModel.getVinTotal()) {
                            // send otp
                            OtpService otpService = new OtpServiceImpl();
                            int ret = otpService.sendVoiceOtp(userModel.getNickname(), "", true);
                            if (ret != 0) {
                                Debug.trace("Cannot send OTP message!");
                                res.setErrorCode("116");
                                return res.toJson();
                            }

                            res.setErrorCode("1012");
                        } else {
                            SocialUtils.socialSuccess((IMap<String, SocialModel>) socialMap, socialId, accessToken);
                            res = PortalUtils.loginSuccess(userModel, request);
                        }
                    }
                } else {
                    logger.debug("Request login: 2");
                    UserModel userModel2 = userService.getUserByUserName(username);
                    if (userModel2 != null) {
                        logger.debug("Request login: 3");
                        if (userModel2.isBot()) {
                            res.setErrorCode("1007");
                            //logger.debug(("Response login: " + res.toJson()));
                            return res.toJson();
                        }

                        if (statusGame == StatusGames.SANDBOX.getId() && !userModel2.isCanLoginSandbox()) {
                            res.setErrorCode("1114");
                            logger.debug(("Response login: " + res.toJson()));
                            return res.toJson();
                        }

                        if (userModel2.isBanLogin()) {
                            res.setErrorCode("1109");
                            return res.toJson();
                        }

                        if (!userModel2.getPassword().equals(password)) {
                            res.setErrorCode("1007");
                            return res.toJson();
                        }
                        if (userModel2.getNickname() == null || userModel2.getNickname().trim().isEmpty()) {
                            res.setErrorCode("2001");
                            return res.toJson();
                        }
                        logger.debug("Request login: 4");
                        if (userModel2.isHasLoginSecurity() && userModel2.getLoginOtp() >= 0L && userModel2.getLoginOtp() <= userModel2.getVinTotal()) {
                            // send otp
                            OtpService otpService = new OtpServiceImpl();
                            int ret = otpService.sendVoiceOtp(userModel2.getNickname(), "", true);
                            if (ret != 0) {
                                Debug.trace("Cannot send OTP message!");
                                res.setErrorCode("116");
                                return res.toJson();
                            }
                            res.setErrorCode("1012");
                        } else {
                            logger.debug("Request login: 5");
                            res = PortalUtils.loginSuccess(userModel2, request);
                        }
                    } else {
                        res.setErrorCode("1005");
                    }
                }
            } catch (Exception e1) {
                logger.debug(e1);
            }

            // test user online
            List<User> User = BitZeroServer.getInstance().getUserManager().getAllUsers();
            Gson gson = new Gson();
            logger.debug(("login thanh cong: " + gson.toJson(User)));
            // end test user online
            logger.debug(("Response login: " + res.toJson()));
            return res.toJson();
        }
        return "MISSING PARAMETTER";
    }

    private String checkAccessTokenFB(String accessToken) {
        try {
            //String messageText = URLEncoder.encode(messge, "UTF-8");
            String surl = "https://graph.facebook.com/me?access_token=" + accessToken;
            URL url = new URL(surl);
            //logger.debug("key:" + PartnerConfig.ESMSApiKey);
            //logger.debug("secret:" + PartnerConfig.ESMSSecretKey);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.setConnectTimeout(90000);
            request.setUseCaches(false);
            request.setDoOutput(true);
            request.setDoInput(true);
            HttpURLConnection.setFollowRedirects(true);
            request.setInstanceFollowRedirects(true);
            request.setRequestProperty("Content-Type", "application/json");
            request.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String result = "";
            String line = "";
            while ((line = rd.readLine()) != null) {
                result = result.concat(line);
            }

            logger.debug("json fb claim:" + result);
            JSONObject json = (JSONObject) new JSONParser().parse(result);
            return String.valueOf(json.get("id"));
        } catch (Exception ex) {
            logger.debug("ex:" + ex.getMessage());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString();
            logger.debug("trace:" + sStackTrace);
            return "";
        }
    }

    private String insertUserSocial(String idUserFB) {
        String username = "user" + idUserFB;
        logger.debug(("QuickRegister user FB: idUserFB: " + idUserFB));
        try {
            int statusGame = GameCommon.getValueInt("STATUS_GAME");
            if (statusGame == StatusGames.MAINTAIN.getId() || statusGame == StatusGames.SANDBOX.getId())
                return "1114";

            if (UserValidaton.validateUserName(username))
                return "101";

            try {
                UserServiceImpl userService = new UserServiceImpl();
                return userService.insertUser(username, UUID.randomUUID().toString());
            } catch (SQLException e2) {
                logger.debug(e2);
                return "1001";
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }

        return "1001";
    }

    private String genNickName(List<String> strings, Random random, String userName, boolean isFirst) {
        String nickName = "";
            if (strings.size() > 0) {
                int randomIndex = random.nextInt(strings.size());
                nickName = userName + strings.get(randomIndex);
                strings.remove(randomIndex);
            } else {
                nickName = userName + random.nextInt(1000);
            }

        if (UserValidaton.validateNickname(nickName)) {
            if (UserValidaton.validateNicknameSpecial(nickName)) {
                return nickName;
            } else {
                if (isFirst)
                    nickName = "go" + nickName;
                genNickName(strings, random, nickName, false);
            }
        } else {
            genNickName(strings, random, userName, true);
        }
        return nickName;
    }
}
