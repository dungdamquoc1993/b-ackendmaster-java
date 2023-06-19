package com.vinplay.usercore.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.transaction.TransactionContext;
import com.hazelcast.transaction.TransactionOptions;
import com.vinplay.dichvuthe.service.impl.AlertServiceImpl;
import com.vinplay.usercore.dao.SecurityDao;
import com.vinplay.usercore.dao.impl.SecurityDaoImpl;
import com.vinplay.usercore.dao.impl.UserDaoImpl;
import com.vinplay.usercore.entities.EmailActive;
import com.vinplay.usercore.service.OtpService;
import com.vinplay.usercore.service.SecurityService;
import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.vbee.common.enums.Games;
import com.vinplay.vbee.common.exceptions.KeyNotFoundException;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.messages.BaseMessage;
import com.vinplay.vbee.common.messages.LogMoneyUserMessage;
import com.vinplay.vbee.common.messages.SafeMoneyMessage;
import com.vinplay.vbee.common.messages.statistic.LoginPortalInfoMsg;
import com.vinplay.vbee.common.models.ConfigGame;
import com.vinplay.vbee.common.models.StatusUser;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.response.BaseResponseModel;
import com.vinplay.vbee.common.response.MoneyResponse;
import com.vinplay.vbee.common.response.UserInfoModel;
import com.vinplay.vbee.common.rmq.RMQApi;
import com.vinplay.vbee.common.utils.UserValidaton;
import com.vinplay.vbee.common.utils.VinPlayUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

public class SecurityServiceImpl
        implements SecurityService {
    private static final Logger logger = Logger.getLogger((String) "user_core");

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public byte updateEmail(String nickname, String email) {
        int res = 1;
        if (!UserValidaton.validateEmail((String) email)) return (byte) 2;
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, UserModel> userMap = client.getMap("users");
        if (!userMap.containsKey(nickname)) return (byte) res;
        try {
            userMap.lock(nickname);
            UserCacheModel user = (UserCacheModel) userMap.get(nickname);
            //if (!this.checkSecureInfo(user)) return (byte)res;
            //if (user.isHasEmailSecurity()) return (byte)res;
            UserServiceImpl userService = new UserServiceImpl();
            if (!userService.checkEmailSecurity(email)) {
                SecurityDaoImpl dao = new SecurityDaoImpl();
                if (!dao.updateUserInfo(user.getId(), email, 5)) return (byte) res;
                user.setEmail(email);
                userMap.put(nickname, user);
                res = 0;
                return (byte) res;
            } else {
                res = 3;
            }
            return (byte) res;
        } catch (Exception e) {
            logger.debug(("updateEmail error: " + e.getMessage()));
            return (byte) res;
        } finally {
            userMap.unlock(nickname);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public byte updateMobile(String nickname, String mobile) {
        int res = 1;
        if (!UserValidaton.validateMobileVN((String) mobile)) return (byte) 2;
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, UserModel> userMap = client.getMap("users");
        if (!userMap.containsKey(nickname)) return (byte) res;
        try {
            // check phone exists
            UserServiceImpl userService = new UserServiceImpl();
            List<UserInfoModel> users = userService.checkPhoneByUser(mobile);
            if (users != null && users.size() > 0) {
                // phone exists
                return (byte) 5;
            }
            userMap.lock(nickname);
            UserCacheModel user = (UserCacheModel) userMap.get(nickname);
            if (!this.checkSecureInfo(user)) return (byte) res;
            if (user.isHasMobileSecurity()) return (byte) res;
            if (!userService.checkMobileSecurity(mobile)) {
                SecurityDaoImpl dao = new SecurityDaoImpl();
                if (!dao.updateUserInfo(user.getId(), mobile, 4)) return (byte) res;
                user.setMobile(mobile);
                userMap.put(nickname, user);
                res = 0;
                return (byte) res;
            } else {
                res = 3;
            }
            return (byte) res;
        } catch (Exception e) {
            logger.error(("updateMobile error: " + e.getMessage()));
            return (byte) res;
        } finally {
            userMap.unlock(nickname);
        }
    }

    private boolean checkSecureInfo(UserCacheModel user) {
        return user != null && user.getMobile() != null && !user.getMobile().isEmpty() && user.getEmail() != null && !user.getEmail().isEmpty() && user.getIdentification() != null && !user.getIdentification().isEmpty();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public byte updateNewMobile(String nickname, String mobile, boolean check) {
        int res = 1;
        if (!UserValidaton.validateMobileVN((String) mobile)) return (byte) 2;
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, UserModel> userMap = client.getMap("users");
        if (!userMap.containsKey(nickname)) return (byte) res;
        try {
            // check phone exists
            UserServiceImpl userService = new UserServiceImpl();
            userMap.lock(nickname);
            List<UserInfoModel> users = userService.checkPhoneByUser(mobile);
            if (users != null && users.size() > 0) {
                // phone exists
                return (byte) 5;
            }
            UserCacheModel user = (UserCacheModel) userMap.get(nickname);
            if (user.getMobile() == null) return (byte) res;
            if (!user.isHasMobileSecurity()) return (byte) res;
            if (!mobile.equals(user.getMobile())) {
                if (!userService.checkMobileSecurity(mobile)) {
                    SecurityDaoImpl dao = new SecurityDaoImpl();
                    if (check) {
                        res = 0;
                        return (byte) res;
                    } else {
                        if (!dao.updateUserInfo(user.getId(), mobile, 4)) return (byte) res;
                        user.setMobile(mobile);
                        user.setSecurityTime(new Date());
                        userMap.put(nickname, user);
                        res = 0;
                    }
                    return (byte) res;
                } else {
                    res = 4;
                }
                return (byte) res;
            } else {
                res = 3;
            }
            return (byte) res;
        } catch (Exception e) {
            logger.debug(("updateNewMobile error: " + e.getMessage()));
            return (byte) res;
        } finally {
            userMap.unlock(nickname);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public byte updateUserInfo(String nickname, String identification, String email, String mobile) {
        int res = 1;
        //if (!UserValidaton.validateIdentification((String)identification)) return (byte)2;
        //if (!UserValidaton.validateEmail((String)email)) return (byte)3;
        if (!UserValidaton.validateMobileVN((String) mobile)) return (byte) 4;
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, UserModel> userMap = client.getMap("users");
        if (!userMap.containsKey(nickname)) return (byte) res;
        try {
            UserServiceImpl userService = new UserServiceImpl();
            userMap.lock(nickname);
            // check phone exists
            UserInfoModel exists = userService.checkPhoneExists(mobile);
            if (exists != null) {
                // phone exists
                res = 5;
                return (byte) res;
            }
            UserCacheModel user = (UserCacheModel) userMap.get(nickname);
            if (user.isHasEmailSecurity()) return (byte) res;
            if (user.isHasMobileSecurity()) return (byte) res;
            if (!userService.checkMobileSecurity(mobile)) {
                if (!userService.checkEmailSecurity(email)) {
                    SecurityDaoImpl dao = new SecurityDaoImpl();
                    if (!dao.updateUserInfos(user.getId(), identification, email, mobile)) return (byte) res;
                    user.setMobile(mobile);
                    user.setEmail(email);
                    user.setIdentification(identification);
                    userMap.put(nickname, user);
                    res = 0;
                    return (byte) res;
                } else {
                    res = 6;
                }
                return (byte) res;
            } else {
                res = 5;
            }
            return (byte) res;
        } catch (Exception e) {
            logger.debug(("updateUserInfo error: " + e.getMessage()));
            return (byte) res;
        } finally {
            userMap.unlock(nickname);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public byte updateUserInfo(String nickname, String identification, String email, String birthday, int gender, String address, String referralCode) {
        int res = 1;
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, UserModel> userMap = client.getMap("users");
        if (!userMap.containsKey(nickname)) return (byte) res;
        try {
            UserServiceImpl userService = new UserServiceImpl();
            userMap.lock(nickname);
            UserCacheModel user = (UserCacheModel) userMap.get(nickname);
            if ("roy88".equals(user.getReferralCode()) || user.getReferralCode() == null
                    || "".equals(user.getReferralCode())) {
                user.setReferralCode(referralCode);
            } else {
                user.setReferralCode(user.getReferralCode());
            }
            if (!email.equals(user.getEmail()))
                user.setHasEmailSecurity(false);

            if (!userService.checkEmailSecurity(email)) {
                SecurityDaoImpl dao = new SecurityDaoImpl();
                if (!dao.updateUserInfos(user.getId(), identification, email, birthday, gender, address, user.isHasEmailSecurity(), user.getReferralCode()))
                    return (byte) res;

                user.setEmail(email);
                user.setIdentification(identification);
                user.setBirthday(birthday);
                user.setGender(gender == 0 ? false : true);
                user.setAddress(address);
                userMap.put(nickname, user);
                res = 0;
                return (byte) res;
            } else {
                res = 6;
            }

            return (byte) res;
        } catch (Exception e) {
            logger.debug(("updateUserInfo error: " + e.getMessage()));
            return (byte) res;
        } finally {
            userMap.unlock(nickname);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public int updateUserVipInfo(String nickname, String birthday, String gender, String address) {
        int res = 1;
        boolean bGender = gender.equals("1");
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, UserModel> userMap = client.getMap("users");
        SecurityDaoImpl dao = new SecurityDaoImpl();
        if (userMap.containsKey(nickname)) {
            try {
                userMap.lock(nickname);
                UserCacheModel user = (UserCacheModel) userMap.get(nickname);
                if (!dao.updateUserVipInfo(user.getId(), birthday, bGender, address)) return res;
                user.setBirthday(birthday);
                user.setGender(bGender);
                user.setAddress(address);
                userMap.put(nickname, user);
                res = 0;
                return res;
            } catch (Exception e) {
                logger.debug(("updateUserVipInfo error: " + e.getMessage()));
                return res;
            } finally {
                userMap.unlock(nickname);
            }
        } else {
            UserDaoImpl userDao = new UserDaoImpl();
            try {
                int userId = userDao.getIdByNickname(nickname);
                if (userId <= 0) return 2;
                if (!dao.updateUserVipInfo(userId, birthday, bGender, address)) return res;
                return 0;
            } catch (Exception e2) {
                logger.debug(("updateUserVipInfo error: " + e2.getMessage()));
            }
        }
        return res;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public byte activeMobile(String nickname, boolean check) {
        int res = 1;
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, UserModel> userMap = client.getMap("users");
        if (userMap.containsKey(nickname)) {
            try {
                userMap.lock(nickname);
                UserCacheModel user = (UserCacheModel) userMap.get(nickname);
                if (user.getMobile() != null && !user.isHasMobileSecurity()) {
                    UserServiceImpl userService = new UserServiceImpl();
                    if (!userService.checkMobileSecurity(user.getMobile())) {
                        if (check) {
                            logger.debug("this.activeMobile(user, dataCmd);" + user.getMobile());
                            res = 0;
                        } else {
                            int statusNew = StatusUser.changeStatus((int) user.getStatus(), (int) 4, (String) "1");
                            SecurityDaoImpl dao = new SecurityDaoImpl();
                            if (dao.updateUserInfo(user.getId(), String.valueOf(statusNew), 13)) {
                                user.setStatus(statusNew);
                                user.setHasMobileSecurity(true);
                                user.setSecurityTime(new Date());
                                user.setVerifyMobile(true);
                                userMap.put(nickname, user);
                                res = 0;
                            }
                        }
                    } else {
                        res = 2;
                    }
                }
            } catch (Exception e) {
                logger.debug(("activeMobile error: " + e.getMessage()));
            } finally {
                userMap.unlock(nickname);
            }
        }
        return (byte) res;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public byte activeEmail(String nickname) {
        int res = 1;
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, UserModel> userMap = client.getMap("users");
        if (userMap.containsKey(nickname)) {
            try {
                userMap.lock(nickname);
                UserCacheModel user = (UserCacheModel) userMap.get(nickname);
                if (user.getEmail() != null && !user.isHasEmailSecurity()) {
                    UserServiceImpl userService = new UserServiceImpl();
                    if (!userService.checkEmailSecurity(user.getEmail())) {
                        String datetime = VinPlayUtils.getCurrentDateTime();
                        String checksum = nickname + user.getEmail() + datetime + "@Lott79#6102$817";
                        checksum = VinPlayUtils.getMD5Hash((String) checksum);
                        String token = VinPlayUtils.encodeBase64((String) new EmailActive(nickname, user.getEmail(), datetime, checksum).toJson());
                        String url = GameCommon.getValueStr("URL_ACTIVE_EMAIL") + token;
                        AlertServiceImpl alertService = new AlertServiceImpl();
                        ArrayList<String> receives = new ArrayList<String>();
                        receives.add(user.getEmail());
                        String content = " Dear " + nickname + ".<br><br> C\u1ea3m \u01a1n b\u1ea1n \u0111\u00e3 \u0111\u0103ng k\u00fd s\u1eed d\u1ee5ng d\u1ecbch v\u1ee5 c\u1ee7a ch\u00fang t\u00f4i. B\u1ea1n h\u00e3y th\u1ef1c hi\u1ec7n theo m\u1ed9t s\u1ed1 h\u01b0\u1edbng d\u1eabn sau \u0111\u1ec3 ho\u00e0n th\u00e0nh \u0111\u0103ng k\u00fd b\u1ea3o m\u1eadt b\u1eb1ng email.<br> B\u1ea1n h\u00e3y click v\u00e0o li\u00ean k\u1ebft sau (ch\u1ec9 c\u00f3 hi\u1ec7u l\u1ef1c trong 24h) \u0111\u1ec3 k\u00edch ho\u1ea1t b\u1ea3o m\u1eadt b\u1eb1ng email " + url + "<br><br>" + GameCommon.getValueStr("SIGN_EMAIL");
                        if (alertService.sendEmail("[VinPlay] K\u00edch ho\u1ea1t b\u1ea3o m\u1eadt b\u1eb1ng email.", content, receives)) {
                            res = 0;
                        }
                    } else {
                        res = 2;
                    }
                }
            } catch (Exception e) {
                logger.debug(("activeEmail error: " + e.getMessage()));
            } finally {
                userMap.unlock(nickname);
            }
        }
        return (byte) res;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public String receiveActiveEmail(String token) {
        String res;
        block9:
        {
            res = "Li\u00ean k\u1ebft n\u00e0y \u0111\u00e3 qu\u00e1 th\u1eddi gian x\u1eed l\u00fd";
            try {
                if (token == null) break block9;
                HazelcastInstance client = HazelcastClientFactory.getInstance();
                IMap<String, UserModel> userMap = client.getMap("users");
                ObjectMapper mapper = new ObjectMapper();
                token = VinPlayUtils.decodeBase64((String) token);
                EmailActive emailActive = (EmailActive) mapper.readValue(token, EmailActive.class);
                String nickname = emailActive.getNickname();
                String email = emailActive.getEmail();
                Date datetime = VinPlayUtils.getDateTime((String) emailActive.getTime());
                String checksum = nickname + email + emailActive.getTime() + "@VinPlay#6102$817";
                if (!(checksum = VinPlayUtils.getMD5Hash((String) checksum)).equals(emailActive.getChecksum()) || !userMap.containsKey(nickname) || VinPlayUtils.emailTimeout((Date) datetime))
                    break block9;
                try {
                    UserServiceImpl userService;
                    userMap.lock(nickname);
                    UserCacheModel user = (UserCacheModel) userMap.get(nickname);
                    if (user.getEmail().equals(email) && user.getEmail() != null && !user.isHasEmailSecurity() && !(userService = new UserServiceImpl()).checkEmailSecurity(email)) {
                        int statusNew = StatusUser.changeStatus((int) user.getStatus(), (int) 5, (String) "1");
                        SecurityDaoImpl dao = new SecurityDaoImpl();
                        if (dao.updateUserInfo(user.getId(), String.valueOf(statusNew), 7)) {
                            user.setStatus(statusNew);
                            user.setHasEmailSecurity(true);
                            userMap.put(nickname, user);
                            String web = GameCommon.getValueStr("WEB");
                            res = "B\u1ea1n \u0111\u00e3 k\u00edch ho\u1ea1t b\u1ea3o m\u1eadt email th\u00e0nh c\u00f4ng. Vui l\u00f2ng \u0111\u0103ng nh\u1eadp <a href='" + web + "'>" + web + "</a> \u0111\u1ec3 ki\u1ec3m tra.";
                        }
                    }
                } catch (Exception e) {
                    logger.debug(("receiveActiveEmail error: " + e.getMessage()));
                } finally {
                    userMap.unlock(nickname);
                }
            } catch (Exception e2) {
                logger.debug(("receiveActiveEmail error: " + e2.getMessage()));
            }
        }
        return res;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public MoneyResponse sendMoneyToSafe(String nickname, long money, boolean check) {
        IMap userMap;
        HazelcastInstance client;
        logger.debug(("Request sendMoneyToSafe: nickname: " + nickname + ", money: " + money));
        MoneyResponse response = new MoneyResponse(false, "1001");
        if (money > 0L && (userMap = (client = HazelcastClientFactory.getInstance()).getMap("users")).containsKey(nickname)) {
            try {
                userMap.lock(nickname);
                UserCacheModel user = (UserCacheModel) userMap.get(nickname);
                long moneyUser = user.getVin();
                long currentMoney = user.getVinTotal();
                long safeMoney = user.getSafe();
                if (moneyUser < money) {
                    response.setErrorCode("1002");
                } else if (check) {
                    response.setSuccess(true);
                    response.setErrorCode("0");
                } else {
                    TransactionContext context = client.newTransactionContext(new TransactionOptions().setTransactionType(TransactionOptions.TransactionType.ONE_PHASE));
                    context.beginTransaction();
                    try {
                        user.setVin(moneyUser -= money);
                        user.setVinTotal(currentMoney -= money);
                        user.setSafe(safeMoney += money);
                        SafeMoneyMessage message = new SafeMoneyMessage(VinPlayUtils.genMessageId(), user.getId(), nickname, "SafeMoney", moneyUser, currentMoney, money, "vin", safeMoney);
                        RMQApi.publishMessagePayment((BaseMessage) message, (int) 14);
                        LogMoneyUserMessage messageLog = new LogMoneyUserMessage(user.getId(), nickname, "SafeMoney", "\u0110\u00f3ng b\u0103ng Vin", currentMoney, -money, "vin", "Chuy\u1ec3n Vin v\u00e0o k\u00e9t s\u1eaft", 0L, false, user.isBot());
                        RMQApi.publishMessageLogMoney((LogMoneyUserMessage) messageLog);
                        userMap.put(nickname, user);
                        context.commitTransaction();
                        response.setSuccess(true);
                        response.setErrorCode("0");
                    } catch (Exception e) {
                        logger.debug(("sendMoneyToSafe error: " + e.getMessage()));
                        context.rollbackTransaction();
                    }
                }
                response.setCurrentMoney(currentMoney);
                response.setSafeMoney(safeMoney);
                response.setMoneyUse(moneyUser);
            } catch (Exception e2) {
                logger.debug(("sendMoneyToSafe error: " + e2.getMessage()));
            } finally {
                userMap.unlock(nickname);
            }
        }
        logger.debug(("Response sendMoneyToSafe: " + response.toJson()));
        return response;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public MoneyResponse takeMoneyInSafe(String nickname, long money, String otp, boolean check) {
        IMap userMap;
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        logger.debug(("Request takeMoneyInSafe: nickname: " + nickname + ", money: " + money + ", otp: " + otp));
        MoneyResponse response = new MoneyResponse(false, "1001");
        userMap = client.getMap("users");
        if (money > 0L && userMap.containsKey(nickname)) {
            OtpService otpService = new OtpServiceImpl();
            try {
                userMap.lock(nickname);
                UserCacheModel user = (UserCacheModel) userMap.get(nickname);
                int code = otpService.checkOtp(otp, nickname, "0", user.getMobile());
                if (code == 0) {
                    long moneyUser = user.getVin();
                    long currentMoney = user.getVinTotal();
                    long safeMoney = user.getSafe();
                    if (safeMoney < money) {
                        response.setErrorCode("1002");
                    } else if (check) {
                        response.setSuccess(true);
                        response.setErrorCode("0");
                    } else {
                        TransactionContext context = client.newTransactionContext(new TransactionOptions().setTransactionType(TransactionOptions.TransactionType.ONE_PHASE));
                        context.beginTransaction();
                        try {
                            user.setVin(moneyUser += money);
                            user.setVinTotal(currentMoney += money);
                            user.setSafe(safeMoney -= money);
                            SafeMoneyMessage message = new SafeMoneyMessage(VinPlayUtils.genMessageId(), user.getId(), nickname, "SafeMoney", moneyUser, currentMoney, money, "vin", safeMoney);
                            RMQApi.publishMessagePayment((BaseMessage) message, (int) 14);
                            LogMoneyUserMessage messageLog = new LogMoneyUserMessage(user.getId(), nickname, "SafeMoney", "M\u1edf \u0111\u00f3ng b\u0103ng Vin", currentMoney, money, "vin", "R\u00fat Vin t\u1eeb k\u00e9t s\u1eaft", 0L, false, user.isBot());
                            RMQApi.publishMessageLogMoney((LogMoneyUserMessage) messageLog);
                            userMap.put(nickname, user);
                            context.commitTransaction();
                            response.setSuccess(true);
                            response.setErrorCode("0");
                        } catch (Exception e) {
                            logger.debug(("takeMoneyInSafe error: " + e.getMessage()));
                            context.rollbackTransaction();
                        }
                    }
                    response.setCurrentMoney(currentMoney);
                    response.setSafeMoney(safeMoney);
                    response.setMoneyUse(moneyUser);
                } else {
                    response.setErrorCode("1003");
                }
            } catch (Exception e2) {
                logger.debug(("takeMoneyInSafe error: " + e2.getMessage()));
            } finally {
                userMap.unlock(nickname);
            }
        }
        logger.debug(("Response takeMoneyInSafe: " + response.toJson()));
        return response;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public BaseResponseModel updateAvatar(String nickname, String avatar) {
        BaseResponseModel res = new BaseResponseModel(false, "1001");
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, UserModel> userMap = client.getMap("users");
        if (userMap.containsKey(nickname)) {
            try {
                userMap.lock(nickname);
                UserCacheModel user = (UserCacheModel) userMap.get(nickname);
                SecurityDaoImpl dao = new SecurityDaoImpl();
                if (dao.updateUserInfo(user.getId(), avatar, 1)) {
                    user.setAvatar(avatar);
                    userMap.put(nickname, user);
                    res.setErrorCode("0");
                    res.setSuccess(true);
                }
            } catch (Exception e) {
                logger.debug(("updateAvatar error: " + e.getMessage()));
            } finally {
                userMap.unlock(nickname);
            }
        }
        return res;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public byte changePassword(String nickname, String oldPassword, String newPassword, boolean check) {
        int res = 1;
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, UserModel> userMap = client.getMap("users");
        if (userMap.containsKey(nickname)) {
            try {
                userMap.lock(nickname);
                UserCacheModel user = (UserCacheModel) userMap.get(nickname);
                if (user.getFacebookId() != null && !user.getFacebookId().isEmpty()) {
                    res = 4;
                } else if (user.getGoogleId() != null && !user.getGoogleId().isEmpty()) {
                    res = 5;
                } else {
                    if (user.getPassword().equals(oldPassword)) {
                        SecurityDao dao = new SecurityDaoImpl();
                        if (dao.updateUserInfo(user.getId(), newPassword, 2)) {
                            user.setPassword(newPassword);
                            userMap.put(nickname, user);
                            res = 0;
                        }
                    } else {
                        res = 3;
                    }
                }
//                if (user.getFacebookId() != null && !user.getFacebookId().isEmpty()) {
//                    res = 4;
//                } else if (user.getGoogleId() != null && !user.getGoogleId().isEmpty()) {
//                    res = 5;
//                } else if (user.isHasMobileSecurity()) {
//                    if (user.getPassword().equals(oldPassword)) {
//                        if (check) {
//                            res = 0;
//                        } else {
//                            SecurityDaoImpl dao = new SecurityDaoImpl();
//                            if (dao.updateUserInfo(user.getId(), newPassword, 2)) {
//                                user.setPassword(newPassword);
//                                userMap.put(nickname, user);
//                                res = 0;
//                            }
//                        }
//                    } else {
//                        res = 3;
//                    }
//                } else {
//                    res = 2;
//                }
            } catch (Exception e) {
                logger.debug(("changePassword error: " + e.getMessage()));
            } finally {
                userMap.unlock(nickname);
            }
        }
        return (byte) res;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean checkMobileSecurity(String nickname) {
        boolean res = false;
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, UserModel> userMap = client.getMap("users");
        if (userMap.containsKey(nickname)) {
            try {
                userMap.lock(nickname);
                UserCacheModel user = (UserCacheModel) userMap.get(nickname);
                if (user.getMobile() != null && !user.getMobile().isEmpty() && user.isHasMobileSecurity()) {
                    res = true;
                }
            } catch (Exception e) {
                logger.debug(("checkMobileSecurity error: " + e.getMessage()));
            } finally {
                userMap.unlock(nickname);
            }
        }
        return res;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public byte loginWithOTP(String nickname, long money, byte type) {
        IMap userMap;
        HazelcastInstance client;
        byte res = 1;
        if (money >= 0L && (type == 0 || type == 1) && (userMap = (client = HazelcastClientFactory.getInstance()).getMap("users")).containsKey(nickname)) {
            try {
                userMap.lock(nickname);
                UserCacheModel user = (UserCacheModel) userMap.get(nickname);
                if (user.getMobile() != null && !user.getMobile().isEmpty() && user.isHasMobileSecurity()) {
                    SecurityDaoImpl dao = new SecurityDaoImpl();
                    if (type == 0) {
                        int statusNew = StatusUser.changeStatus((int) user.getStatus(), (int) 7, (String) "0");
                        if (user.isHasLoginSecurity() && dao.updateUserInfo(user.getId(), "-1," + statusNew, 12)) {
                            user.setHasLoginSecurity(false);
                            user.setLoginOtp(-1L);
                            user.setStatus(statusNew);
                            userMap.put(nickname, user);
                            res = 0;
                        }
                    } else {
                        int statusNew = StatusUser.changeStatus((int) user.getStatus(), (int) 7, (String) "1");
                        if (dao.updateUserInfo(user.getId(), String.valueOf(money) + "," + statusNew, 12)) {
                            user.setHasLoginSecurity(true);
                            user.setLoginOtp(money);
                            user.setStatus(statusNew);
                            userMap.put(nickname, user);
                            res = 0;
                        }
                    }
                }
            } catch (Exception e) {
                logger.debug(("loginWithOTP error: " + e.getMessage()));
            } finally {
                userMap.unlock(nickname);
            }
        }
        return res;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public byte configGame(String nickname, String lisrGame) {
        IMap userMap;
        HazelcastInstance client;
        byte res = 1;
        if (lisrGame != null && !lisrGame.isEmpty() && (userMap = (client = HazelcastClientFactory.getInstance()).getMap("users")).containsKey(nickname)) {
            try {
                userMap.lock(nickname);
                UserCacheModel user = (UserCacheModel) userMap.get(nickname);
                if (user.getMobile() != null && !user.getMobile().isEmpty() && user.isHasMobileSecurity()) {
                    ObjectMapper mapper = new ObjectMapper();
                    List<ConfigGame> configGame = (List) mapper.readValue(lisrGame, (TypeReference) new TypeReference<List<ConfigGame>>() {
                    });
                    String[] listGame = GameCommon.getValueStr("LIST_GAME_BAI").split(",");
                    if (configGame != null) {
                        SecurityDaoImpl dao;
                        int statusNew = user.getStatus();
                        boolean ck = true;
                        for (ConfigGame game : configGame) {
                            if (!Arrays.asList(listGame).contains(String.valueOf(game.getId()))) {
                                ck = false;
                                break;
                            }
                            String newBit = game.getStatus() == 1 ? "1" : "0";
                            statusNew = StatusUser.changeStatus((int) statusNew, (int) game.getId(), (String) newBit);
                        }
                        if (ck && (dao = new SecurityDaoImpl()).updateUserInfo(user.getId(), String.valueOf(statusNew), 7)) {
                            user.setStatus(statusNew);
                            userMap.put(nickname, user);
                            res = 0;
                        }
                    }
                }
            } catch (Exception e) {
                logger.debug(("configGame error: " + e.getMessage()));
            } finally {
                userMap.unlock(nickname);
            }
        }
        return res;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean updateStatusUser(String nickname, int action, String type) {
        IMap userMap;
        HazelcastInstance client;
        boolean res = false;
        SecurityDaoImpl dao = new SecurityDaoImpl();
        if (type == null) return res;
        if (!type.equals("0")) {
            if (!type.equals("1")) return res;
        }
        if ((userMap = (client = HazelcastClientFactory.getInstance()).getMap("users")).containsKey(nickname)) {
            try {
                userMap.lock(nickname);
                UserCacheModel user = (UserCacheModel) userMap.get(nickname);
                int statusNew = StatusUser.changeStatus((int) user.getStatus(), (int) action, (String) type);
                if (!dao.updateUserInfo(user.getId(), String.valueOf(statusNew), 7)) return res;
                user.setStatus(statusNew);
                boolean ban = false;
                if (type.equals("1")) {
                    ban = true;
                }
                switch (action) {
                    case 0: {
                        user.setBanLogin(ban);
                        break;
                    }
                    case 1: {
                        user.setBanCashOut(ban);
                        break;
                    }
                    case 2: {
                        user.setCanLoginSandbox(ban);
                        break;
                    }
                    case 3: {
                        user.setBanTransferMoney(ban);
                    }
                }
                userMap.put(nickname, user);
                res = true;
                return res;
            } catch (Exception e) {
                logger.debug(("updateStatusUser error: " + e.getMessage()));
                return res;
            } finally {
                userMap.unlock(nickname);
            }
        } else {
            try {
                UserModel user2 = dao.getStatus(nickname);
                if (user2 == null) return res;
                int statusNew = StatusUser.changeStatus((int) user2.getStatus(), (int) action, (String) type);
                if (!dao.updateUserInfo(user2.getId(), String.valueOf(statusNew), 7)) return res;
                return true;
            } catch (Exception e) {
                logger.debug(("updateStatusUser error: " + e.getMessage()));
            }
        }
        return res;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean changeSecurityUser(String nickname, int action, String type) {
        IMap userMap;
        HazelcastInstance client;
        boolean res = false;
        SecurityDaoImpl dao = new SecurityDaoImpl();
        if (action != 4 && action != 5 && action != 6) {
            return res;
        }
        if ((userMap = (client = HazelcastClientFactory.getInstance()).getMap("users")).containsKey(nickname)) {
            try {
                userMap.lock(nickname);
                UserCacheModel user = (UserCacheModel) userMap.get(nickname);
                int statusNew = StatusUser.changeStatus((int) user.getStatus(), (int) action, (String) type);
                if (!dao.updateUserInfo(user.getId(), String.valueOf(statusNew), 7)) return res;
                user.setStatus(statusNew);
                switch (action) {
                    case 4: {
                        if (type.equals("0")) {
                            user.setHasMobileSecurity(false);
                            break;
                        }
                        user.setHasMobileSecurity(true);
                        break;
                    }
                    case 5: {
                        if (type.equals("0")) {
                            user.setHasEmailSecurity(false);
                            break;
                        }
                        user.setHasEmailSecurity(true);
                    }
                    case 6: {
                        if (type.equals("0")) {
                            user.setHasAppSecurity(false);
                            break;
                        }
                        user.setHasAppSecurity(true);
                    }
                }
                userMap.put(nickname, user);
                res = true;
                return res;
            } catch (Exception e) {
                logger.debug(("changeSecurityUser error: " + e.getMessage()));
                return res;
            } finally {
                userMap.unlock(nickname);
            }
        } else {
            try {
                UserModel user2 = dao.getStatus(nickname);
                if (user2 == null) return res;
                int statusNew = StatusUser.changeStatus((int) user2.getStatus(), (int) action, (String) type);
                if (!dao.updateUserInfo(user2.getId(), String.valueOf(statusNew), 7)) return res;
                return true;
            } catch (Exception e) {
                logger.debug(("changeSecurityUser error: " + e.getMessage()));
            }
        }
        return res;
    }

    @Override
    public List<ConfigGame> getListGameBai(int status) throws KeyNotFoundException {
        String[] listGame = GameCommon.getValueStr("LIST_GAME_BAI").split(",");
        ArrayList<ConfigGame> listGameBai = new ArrayList<ConfigGame>();
        for (String sid : listGame) {
            int id = Integer.parseInt(sid);
            Games game = Games.findGameById((int) id);
            ConfigGame configGame = new ConfigGame(id, game.getDescription(), (byte) 0);
            if (StatusUser.checkStatus((int) status, (int) id)) {
                configGame.setStatus((byte) 1);
            }
            listGameBai.add(configGame);
        }
        return listGameBai;
    }

    @Override
    public boolean saveLoginInfo(int userId, String username, String nickname, String ip, String agent,
                                 int type, String platform) {
        LoginPortalInfoMsg msg = new LoginPortalInfoMsg(userId, username, nickname, ip, agent, type, platform);
        try {
            RMQApi.publishMessage("queue_login_info", msg, 50);
        } catch (IOException | InterruptedException | TimeoutException ex2) {
            Exception e = ex2;
            return false;
        }
        return true;
    }

}

