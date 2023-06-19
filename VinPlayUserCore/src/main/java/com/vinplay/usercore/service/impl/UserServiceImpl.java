package com.vinplay.usercore.service.impl;
import com.vinplay.vbee.common.messages.LogChuyenTienDaiLyMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import com.vinplay.vbee.common.utils.NumberUtils;
import com.vinplay.vbee.common.utils.NumberUtils;
import java.util.Timer;
import java.util.TimerTask;
import com.vinplay.dichvuthe.service.impl.TransferMoneyBankService;
import com.vinplay.dichvuthe.entities.TransferMoneyBankModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.vinplay.dichvuthe.service.impl.AlertServiceImpl;
import org.apache.log4j.Logger;
import com.vinplay.usercore.entities.TransferMoneyResponse;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.transaction.TransactionContext;
import com.hazelcast.transaction.TransactionOptions;
import com.vinplay.dal.config.CacheConfig;
import com.vinplay.usercore.dao.SecurityDao;
import com.vinplay.usercore.dao.UserDao;
import com.vinplay.usercore.dao.impl.AgentDaoImpl;
import com.vinplay.usercore.dao.impl.SecurityDaoImpl;
import com.vinplay.usercore.dao.impl.UserDaoImpl;
import com.vinplay.usercore.entities.UserFish;
import com.vinplay.usercore.logger.MoneyLogger;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.usercore.utils.VippointUtils;
import com.vinplay.utils.TelegramAlert;
import com.vinplay.utils.TelegramUtil;
import com.vinplay.vbee.common.enums.Games;
import com.vinplay.vbee.common.enums.StatusGames;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.messages.BaseMessage;
import com.vinplay.vbee.common.messages.LogMoneyUserMessage;
import com.vinplay.vbee.common.messages.MoneyMessageInMinigame;
import com.vinplay.vbee.common.messages.VippointMessage;
import com.vinplay.vbee.common.messages.vippoint.VippointEventMessage;
import com.vinplay.vbee.common.models.StatusUser;
import com.vinplay.vbee.common.models.TopCaoThu;
import com.vinplay.vbee.common.models.UserAdminModel;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.models.UserValueModel;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.models.cache.UserExtraInfoModel;
import com.vinplay.vbee.common.pools.ConnectionPool;
import com.vinplay.vbee.common.response.BaseResponseModel;
import com.vinplay.vbee.common.response.MoneyResponse;
import com.vinplay.vbee.common.response.NapXuResponse;
import com.vinplay.vbee.common.response.UserInfoModel;
import com.vinplay.vbee.common.response.UserResponse;
import com.vinplay.vbee.common.rmq.RMQApi;
import com.vinplay.vbee.common.statics.Consts;
import com.vinplay.vbee.common.statics.TransType;
import com.vinplay.vbee.common.utils.MapUtils;
import com.vinplay.vbee.common.utils.VinPlayUtils;

public class UserServiceImpl implements UserService {
    private static final Logger logger = Logger.getLogger("user_core");
    private UserDao userDao = new UserDaoImpl();
    
    public boolean isUserBigWin(String nick_name){
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap userMap = client.getMap(CacheConfig.CACHE_USER_WIN);
        if(userMap.containsKey(nick_name)){
            if(userMap.get(nick_name) instanceof String){
                userMap.put(nick_name, true);
            }
        }
        return userMap.containsKey(nick_name) && (Boolean) userMap.get(nick_name);
    }
    
    public long getUserValue(String nick_name){
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, UserValueModel> userMap = client.getMap("mapUserValue");
        if(!userMap.containsKey(nick_name)){
            UserValueModel userValueModel = this.getUserModelFromDatabase(nick_name);
            if(userValueModel.id == 0){// chua co trong db
                userValueModel = new UserValueModel(nick_name);
                this.insertUserValueModel(userValueModel);
            }
            userMap.put(nick_name,userValueModel);
        }
        try{
            UserValueModel userValueModel = userMap.get(nick_name);
            return userValueModel.deposit - userValueModel.withdraw;
        }catch (Exception e){

        }
        return 0;
    }

    public void changeUserValue(String nick_name, long value , boolean isWithdraw){
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, UserValueModel> userMap = client.getMap("mapUserValue");
        if(!userMap.containsKey(nick_name)){
            UserValueModel userValueModel = this.getUserModelFromDatabase(nick_name);
            if(userValueModel.id == 0){// chua co trong db
                userValueModel = new UserValueModel(nick_name);
                this.insertUserValueModel(userValueModel);
            }
            userMap.put(nick_name,userValueModel);
        }

        try{
            userMap.lock(nick_name);
            UserValueModel userValueModel = userMap.get(nick_name);
			if (isWithdraw) {
				// wd
				userValueModel.withdraw += value;
			} else {
				// dp
				userValueModel.deposit += value;
			}
            userMap.put(nick_name,userValueModel);
            if(!this.updateUserValueModel(userValueModel)){
                this.insertUserValueModel(userValueModel);
            }
        }finally {
            userMap.unlock(nick_name);
        }
    }

    public void insertUserValueModel(UserValueModel userValueModel){
        String sql = "INSERT INTO user_value (nick_name,deposit,withdraw) VALUE (?,?,?)";
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname")) {
            PreparedStatement stm = conn.prepareStatement(sql);
            int param = 1;
            stm.setString(param++, userValueModel.nick_name);
            stm.setLong(param++, userValueModel.deposit);
            stm.setLong(param++, userValueModel.withdraw);
            stm.executeUpdate();
        }catch (Exception e){
            logger.debug(e);
        }
    }

    public boolean updateUserValueModel(UserValueModel userValueModel){
        boolean value = false;
        String sql = "UPDATE  user_value SET deposit = ?, withdraw = ?  WHERE nick_name=?";
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname")) {
            PreparedStatement stm = conn.prepareStatement(sql);
            int param = 1;
            stm.setLong(param++, userValueModel.deposit);
            stm.setLong(param++, userValueModel.withdraw);
            stm.setString(param++, userValueModel.nick_name);
            value = stm.executeUpdate() == 1;
        }catch (Exception e){
            logger.debug(e);
        }
        return value;
    }

    public UserValueModel getUserModelFromDatabase(String nick_name){
        UserValueModel userValueModel = new UserValueModel();
        String sql = "SELECT * FROM user_value WHERE nick_name=?";
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname")) {
            PreparedStatement stm = conn.prepareStatement(sql);
            int param = 1;
            stm.setString(param++, nick_name);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
//                count = rs.getInt("cnt");
                userValueModel.id = rs.getInt("id");
                userValueModel.nick_name = rs.getString("nick_name");
                userValueModel.deposit = rs.getLong("deposit");
                userValueModel.withdraw = rs.getLong("withdraw");
            }
            rs.close();
        }catch (Exception e){
            logger.error(e);
        }
        return userValueModel;
    }

    public boolean isPhoneUsed(String phone) throws SQLException {
		int count = 0;
		String sql = "SELECT count(id) as cnt FROM users WHERE mobile=?";
		try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");) {
			PreparedStatement stm = conn.prepareStatement(sql);
			int param = 1;
			stm.setString(param++, phone);
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				count = rs.getInt("cnt");
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return count > 0;
	}
    
    public boolean isPhoneUsed(String phone, String nickname) throws SQLException {
		int count = 0;
		String sql = "SELECT count(id) as cnt FROM users WHERE mobile=? and nick_name <> ?";
		try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");) {
			PreparedStatement stm = conn.prepareStatement(sql);
			int param = 1;
			stm.setString(param++, phone);
			stm.setString(param++, nickname);
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				count = rs.getInt("cnt");
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return count > 0;
	}

	@Override
	public boolean isXacThucSDT(String userName) throws Exception{
		long count = 0;
		String sql = "SELECT count(*)  as cnt from users WHERE nick_name=? and is_verify_mobile =1 and mobile is not null";
		try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");) {
			PreparedStatement stm = conn.prepareStatement(sql);
			int param = 1;
			stm.setString(param++, userName);
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				count = rs.getInt("cnt");
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return count > 0;
	}

    @Override
    public String insertUser(String username, String password) throws SQLException {
        String err = "1001";
        if (userDao.checkUsername(username)) {
            err = "1006";
        } else {
            SecurityDaoImpl securDao = new SecurityDaoImpl();
            if (securDao.updateUserInfo(0, username + "," + password, 9)) {
                err = "0";
            }
        }
        return err;
    }

    @Override
    public boolean insertUserBySocial(String socialId, String social) throws SQLException {
        SecurityDaoImpl securDao = new SecurityDaoImpl();
        if (social.equals("fb")) {
            return securDao.updateUserInfo(0, socialId, 10);
        }
        return securDao.updateUserInfo(0, socialId, 11);
    }

	@Override
	public String updateNickname(int userId, String nickname) throws SQLException {
		SecurityDao dao = new SecurityDaoImpl();
		if (!userDao.checkNicknameExist(nickname) && dao.updateUserInfo(userId, nickname, 6)) {
			return "0";
		}
		return "1010";
	}

    @Override
    public boolean checkNickname(String nickname) throws SQLException {
        UserDaoImpl dao = new UserDaoImpl();
        return dao.checkNickname(nickname);
    }

    @Override
    public UserModel getUserByUserName(String username) throws SQLException {
        return userDao.getUserByUserName(username);
    }

    @Override
    public UserModel getUserByNickName(String nickname) throws SQLException {
       
        return userDao.getUserByNickName(nickname);
    }

    @Override
    public UserModel getUserBySocialId(String socialId, String social) throws SQLException {
        if (social.equals("fb")) {
            return userDao.getUserByFBId(socialId);
        }
        return userDao.getUserByGGId(socialId);
    }

    @Override
    public boolean updateVerifyMobile(String nickname, String phoneNumber, boolean hasVerify) throws SQLException {
        return userDao.verifyMobile(nickname, phoneNumber, hasVerify);
    }

    @Override
    public long getMoneyUserCache(String nickname, String moneyType) {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, UserModel> userMap = client.getMap("users");
        if (userMap.containsKey(nickname)) {
            UserCacheModel user = (UserCacheModel)userMap.get(nickname);
            return user.getMoney(moneyType);
        }
        return 0L;
    }

    @Override
    public UserCacheModel getMoneyUser(String nickname) {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, UserModel> userMap = client.getMap("users");
        if (userMap.containsKey(nickname)) {
            UserCacheModel user = (UserCacheModel)userMap.get(nickname);
            return user;
        }
        return null;
    }

    @Override
    public long getMoneyCashout(String nickname) throws ParseException {
        UserCacheModel user;
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, UserModel> userMap = client.getMap("users");
        if (userMap.containsKey(nickname) && (user = (UserCacheModel)userMap.get(nickname)).getCashoutTime() != null && VinPlayUtils.compareDate((Date)user.getCashoutTime(), (Date)new Date()) == 0) {
            return user.getCashout();
        }
        return 0L;
    }

    @Override
    public long getCurrentMoneyUserCache(String nickname, String moneyType) {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, UserModel> userMap = client.getMap("users");
        if (userMap.containsKey(nickname)) {
            UserCacheModel user = (UserCacheModel)userMap.get(nickname);
            return user.getCurrentMoney(moneyType);
        }
        return 0L;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public UserResponse checkSessionKey(String nickname, String accessToken, Games game) {
        UserResponse res;
        block19 : {
            logger.debug(("Request checkSessionKey: nickname: " + nickname + ", accessToken: " + accessToken));
            UserModel user = null;
            res = new UserResponse(false, "1001", user);
            try {
                int statusGame = GameCommon.getValueInt("STATUS_GAME");
                if (statusGame == StatusGames.MAINTAIN.getId()) {
                    logger.debug(("Response checkSessionKey: server maintain" + res.toJson()));
                    res.setErrorCode("1114");
                    return res;
                }
                HazelcastInstance instance = HazelcastClientFactory.getInstance();
                IMap userMap = instance.getMap("users");
                if (!userMap.containsKey(nickname)) break block19;
                if (statusGame == StatusGames.SANDBOX.getId() && !((UserCacheModel)userMap.get(nickname)).isCanLoginSandbox()) {
                    logger.debug(("Response checkSessionKey: server maintain" + res.toJson()));
                    res.setErrorCode("1114");
                    return res;
                }
                try {
                     userMap.lock(nickname);
                    UserCacheModel userCache = (UserCacheModel)userMap.get(nickname);
                    if (!userCache.isBanLogin()) {
                        if (game == Games.MINIGAME || userCache.getDaily() == 0 && !StatusUser.checkStatus(userCache.getStatus(), game.getId())) {
                            if (userCache.getAccessToken().equals(accessToken)) {
                                if (!VinPlayUtils.sessionTimeout((long)userCache.getLastActive().getTime())) {
                                    UserCacheModel uc = this.checkMoneyNegative(userCache);
                                    if (uc != null) {
                                        userCache = uc;
                                    } else {
										user = new UserModel(userCache.getId(), userCache.getUsername(),
												userCache.getNickname(), userCache.getPassword(), userCache.getEmail(),
												userCache.getFacebookId(), userCache.getFacebookId(),
												userCache.getMobile(), userCache.getBirthday(), userCache.isGender(),
												userCache.getAddress(), userCache.getVin(), userCache.getXu(),
												userCache.getVinTotal(), userCache.getXuTotal(), userCache.getSafe(),
												userCache.getRechargeMoney(), userCache.getVippoint(),
												userCache.getDaily(), userCache.getStatus(), userCache.getAvatar(),
												userCache.getIdentification(), userCache.getVippointSave(),
												userCache.getCreateTime(), userCache.getMoneyVP(),
												userCache.getSecurityTime(), userCache.getLoginOtp(), userCache.isBot(),
												userCache.isVerifyMobile(), userCache.getUsertype(),
												userCache.getReferralCode());
                                        res.setSuccess(true);
                                        res.setErrorCode("0");
                                        res.setUser(user);
                                        userCache.setLastActive(new Date());
                                        userCache.setOnline(userCache.getOnline() + 1);
                                    }
                                     userMap.put(nickname, userCache);
                                } else {
                                    res.setErrorCode("1015");
                                }
                            } else {
                                res.setErrorCode("1014");
                            }
                        } else {
                            res.setErrorCode("1111");
                        }
                    } else {
                        res.setErrorCode("1109");
                    }
                }
                catch (Exception e) {
                    logger.debug(e);
                }
                finally {
                     userMap.unlock(nickname);
                }
            }
            catch (Exception e2) {
                logger.debug(e2);
            }
        }
        logger.debug(("Response checkSessionKey: " + res.toJson()));
        return res;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void logout(String nickname) {
        IMap userMap = HazelcastClientFactory.getInstance().getMap("users");
        if (userMap.containsKey(nickname)) {
            try {
                 userMap.lock(nickname);
                UserCacheModel userCache = (UserCacheModel)userMap.get(nickname);
                if (userCache.getOnline() > 0) {
                    userCache.setOnline(userCache.getOnline() - 1);
                }
                userCache.setLastActive(new Date());
                 userMap.put(nickname, userCache);
            }
            catch (Exception e) {
                logger.debug(e);
            }
            finally {
                 userMap.unlock(nickname);
            }
        }
    }

    private boolean isUserRateHigh(String nickName, HazelcastInstance client) {
		IMap userMap = client.getMap(CacheConfig.CACHE_USER_WIN);
		return userMap.containsKey(nickName) && (Boolean) userMap.get(nickName);
	}
    
    @Override
    public MoneyResponse updateMoney(String nickname, long money, String moneyType, String gameName, String serviceName, String description, long fee, Long transId, TransType type) {
        //logger.debug(("Request updateMoney:  nickname: " + nickname + ", money: " + money + ", moneyType: " + moneyType + ", gameName: " + gameName + ", serviceName: " + serviceName + ", description: " + description + ", fee: " + fee + ", transId: " + transId + ", TransType: " + type.getId()));
        MoneyResponse response = new MoneyResponse(false, "1001");
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        if (client == null) {
            MoneyLogger.log(nickname, gameName, money, fee, moneyType, serviceName, "1030", "can not connect hazelcast");
            response.setErrorCode("1030");
            return response;
        }
        IMap<String, UserModel> userMap = client.getMap("users");
		if (userMap.containsKey(nickname)) {
			try {
				userMap.lock(nickname);
				UserCacheModel user = (UserCacheModel) userMap.get(nickname);
				if (user.isBot()) {
					response.setSuccess(true);
					response.setErrorCode("0");
					return response;
				}
                long moneyUser = user.getMoney(moneyType);
                long currentMoney = user.getCurrentMoney(moneyType);
                if (money != 0L) {
                    if (moneyUser + money >= 0L) {
                        TransactionContext context = client.newTransactionContext(new TransactionOptions().setTransactionType(TransactionOptions.TransactionType.ONE_PHASE));
                        context.beginTransaction();
                        try {
                            user.setMoney(moneyType, moneyUser += money);
                            user.setCurrentMoney(moneyType, currentMoney += money);
                            long moneyVP = VippointUtils.calculateMoneyVP(moneyType, transId, client, nickname, gameName, money, type);
                            int vp = 0;
                            int moneyVPs = 0;
                            int vpAddEvent = 0;
                            if (moneyVP > 0L) {
                                List<Integer> vpLst = VippointUtils.calculateVP(client, user.getNickname(), (long)user.getMoneyVP() + moneyVP, false);
                                vp = vpLst.get(0);
                                moneyVPs = vpLst.get(1);
                                vpAddEvent = vpLst.get(2);
                                user.setVippoint(user.getVippoint() + vp);
                                user.setVippointSave(user.getVippointSave() + vp);
                                user.setMoneyVP(moneyVPs);
                                if (vpAddEvent > 0) {
                                    int vpReal = user.getVpEventReal();
                                    int vpEvent = user.getVpEvent();
                                    int place = VippointUtils.calculatePlace(vpEvent += vpAddEvent);
                                    int placeMax = place > user.getPlace() ? place : user.getPlace();
                                    user.setVpEventReal(vpReal += vpAddEvent);
                                    user.setVpEvent(vpEvent);
                                    user.setPlace(place);
                                    user.setPlaceMax(placeMax);
                                    VippointEventMessage vpEventMessage = new VippointEventMessage(user.getId(), nickname, vpReal, vpEvent, 0, 0, 0, 0, place, placeMax, 0, 0);
                                    RMQApi.publishMessage("queue_vippoint_event", (BaseMessage)vpEventMessage, 801);
                                }
                            }
                            boolean playgame = false;
                            if (transId != null || type == TransType.VIPPOINT) {
                                playgame = true;
                            }
                            MoneyMessageInMinigame message = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), user.getId(), nickname, gameName, moneyUser, currentMoney, money, moneyType, fee, moneyVPs, vp);
                            LogMoneyUserMessage messageLog = new LogMoneyUserMessage(user.getId(), nickname, gameName, serviceName, currentMoney, money, moneyType, description, fee, playgame, user.isBot());
                            RMQApi.publishMessagePayment(message, 16);
							if (!isUserRateHigh(nickname, client)) {
								RMQApi.publishMessageLogMoney(messageLog);
							}
                            userMap.put(nickname, user);
                            context.commitTransaction();
                            response.setSuccess(true);
                            response.setErrorCode("0");
                        }
                        catch (Exception e) {
                            context.rollbackTransaction();
                            MoneyLogger.log(nickname, gameName, money, fee, moneyType, serviceName, "1031", "error rmq: " + e.getMessage());
                            response.setErrorCode("1031");
                        }
                    } else {
                        response.setErrorCode("1002");
                        MoneyLogger.log(nickname, gameName, money, fee, moneyType, serviceName, "1002", "khong du tien");
                        response.setErrorCode("1002");
                    }
                } else {
                    if (moneyType.equals("vin") && transId != null && type.getId() == TransType.END_TRANS.getId()) {
                        IMap vpCache = client.getMap("VPMinigame");
                        String vpCacheId = nickname + gameName + transId;
                        long moneyVP2 = 0L;
                        if (vpCache.containsKey(vpCacheId)) {
                            moneyVP2 = Math.abs((Long)vpCache.get(vpCacheId));
                            vpCache.remove(vpCacheId);
                            if (moneyVP2 > 0L) {
                                List<Integer> vpLst2 = VippointUtils.calculateVP(client, user.getNickname(), (long)user.getMoneyVP() + moneyVP2, false);
                                int vp2 = vpLst2.get(0);
                                int moneyVPs2 = vpLst2.get(1);
                                int vpAddEvent2 = vpLst2.get(2);
                                user.setVippoint(user.getVippoint() + vp2);
                                user.setVippointSave(user.getVippointSave() + vp2);
                                user.setMoneyVP(moneyVPs2);
                                if (vpAddEvent2 > 0) {
                                    int vpReal2 = user.getVpEventReal();
                                    int vpEvent2 = user.getVpEvent();
                                    int place2 = VippointUtils.calculatePlace(vpEvent2 += vpAddEvent2);
                                    int placeMax2 = place2 > user.getPlace() ? place2 : user.getPlace();
                                    user.setVpEventReal(vpReal2 += vpAddEvent2);
                                    user.setVpEvent(vpEvent2);
                                    user.setPlace(place2);
                                    user.setPlaceMax(placeMax2);
                                    VippointEventMessage vpEventMessage2 = new VippointEventMessage(user.getId(), nickname, vpReal2, vpEvent2, 0, 0, 0, 0, place2, placeMax2, 0, 0);
                                    RMQApi.publishMessage("queue_vippoint_event", (BaseMessage)vpEventMessage2, 801);
                                }
                                VippointMessage message2 = new VippointMessage(user.getId(), nickname, moneyVPs2, vp2);
                                RMQApi.publishMessagePayment((BaseMessage)message2, 18);
                                userMap.put(nickname, user);
                            }
                        }
                    }
                    response.setSuccess(true);
                    response.setErrorCode("0");
                }
                response.setCurrentMoney(currentMoney);
            }
            catch (Exception e2) {
                logger.debug(e2);
                MoneyLogger.log(nickname, gameName, money, fee, moneyType, serviceName, "1030", "error hazelcast: " + e2.getMessage());
                response.setErrorCode("1030");
            }
            finally {
                 userMap.unlock(nickname);
            }
        }
        //logger.debug(("Response updateMoney:" + response.toJson()));
        return response;
    }

    @Override
    public BaseResponseModel updateMoneyFromAdmin(String nickname, long money, String moneyType, String actionName, String serviceName, String description) {
        BaseResponseModel response;
		if ((!serviceName.equals(Games.HOAN_TRA.getId() + "") && !serviceName.equals(Games.DIEM_DANH.getId() + ""))
				|| (serviceName.equals(Games.DIEM_DANH.getId() + "") && money > 50000)
				|| (serviceName.equals(Games.HOAN_TRA.getId() + "") && money >500000)) {
			TelegramAlert.SendMessage("[THÔNG BÁO] CỘNG TRỪ TIỀN USER ADMIN BACKEND ,AMOUNT=" + money + " , ACTIONNAME="
					+ actionName + " , SERVICENAME=" + serviceName + ", nickname=" + nickname);
		}
        block11 : {
            response = new BaseResponseModel(false, "1001", "Không có nickname: " + nickname  + " hoặc tiền hoàn trả bằng 0: " + money);
            if (nickname != null && !nickname.isEmpty() && money != 0L) {
                HazelcastInstance client = HazelcastClientFactory.getInstance();
                if (client == null) {
                    MoneyLogger.log(nickname, actionName, money, 0L, moneyType, serviceName, "1030", "can not connect hazelcast");
                    response.setErrorCode("1030");
                    response.setMessage("can not find user in hazelcast");
                    return response;
                }
                try {
                    UserModel model = userDao.getUserByNickName(nickname);
                    if (model != null) {
                        nickname = model.getNickname();
                        IMap<String, UserModel> userMap = client.getMap("users");
                        if (userMap.containsKey(nickname)) {
                            MoneyResponse moneyRes = this.updateMoney(nickname, money, moneyType, actionName, serviceName, description, 0L, null, TransType.NO_VIPPOINT);
                            response.setSuccess(moneyRes.isSuccess());
                            response.setErrorCode(moneyRes.getErrorCode());
                            break block11;
                        }
                        try {
                            long currentMoney = model.getCurrentMoney(moneyType);
                            long moneyUser = model.getMoney(moneyType);
                            if (moneyUser + money >= 0L) {
                                int userId = model.getId();
                                if (!userDao.updateMoney(userId, money, moneyType)) break block11;
                                currentMoney += money;
                                try {
                                    LogMoneyUserMessage messageLog = new LogMoneyUserMessage(userId, nickname, actionName, serviceName, currentMoney, money, moneyType, description, 0L, false, model.isBot());
                                    RMQApi.publishMessageLogMoney(messageLog);
                                }
                                catch (Exception e) {
                                    MoneyLogger.log(nickname, actionName, money, 0L, moneyType, serviceName, "1031", "error rmq: " + e.getMessage());
                                    response.setErrorCode("1031");
                                    response.setMessage("error rmq: " + e.getMessage());
                                }
                                response.setSuccess(true);
                                response.setErrorCode("0");
                                break block11;
                            }
                            response.setErrorCode("1002");
                            response.setMessage("Cộng tiền xong thì bằng âm");
                            break block11;
                        }
                        catch (Exception e2) {
                            logger.debug(e2);
                            MoneyLogger.log(nickname, actionName, money, 0L, moneyType, serviceName, "1032", "error mysql: " + e2.getMessage());
                            response.setErrorCode("1032");
                            response.setMessage("error mysql: " + e2.getMessage());
                            return response;
                        }
                    }
                    response.setErrorCode("2001");
                    response.setMessage("error mysql không tìm thấy user trong DB: " + nickname);
                }
                catch (Exception e3) {
                    logger.debug(e3);
                    MoneyLogger.log(nickname, actionName, money, 0L, moneyType, serviceName, "1032", "error mysql: " + e3.getMessage());
                    response.setErrorCode("1032");
                    response.setMessage(e3.getMessage());
                }
            }
        }
        //logger.debug(("Response updateMoneyFromAdmin: " + response.toJson()));
        return response;
    }



    @Override
    public boolean UpdateFishMoney(String nickName, long amount) throws SQLException {

        UserFish user = userDao.GetUserFishByNickname(nickName);
        if(user == null)
            return false;
        if(amount > 0){
            if(user.Cash < amount)
                return false;
        }

        amount = -amount;

        boolean result = userDao.updateFishMoney(nickName, amount);
        return result;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public BaseResponseModel updateMoneyCacheToDB(String nickname) throws SQLException {
        BaseResponseModel response = new BaseResponseModel(false, "1001");
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, UserModel> userMap = client.getMap("users");
        if (userMap != null && userMap.containsKey(nickname)) {
            try {
                 userMap.lock(nickname);
                UserCacheModel user = (UserCacheModel)userMap.get(nickname);
                long moneyUseVin = user.getVin();
                long moneyTotalVin = user.getVinTotal();
                long moneySafe = user.getSafe();
                long moneyUseXu = user.getXu();
                long moneyTotalXu = user.getXuTotal();
                UserDaoImpl dao = new UserDaoImpl();
                if (dao.restoreMoneyByAdmin(user.getId(), moneyUseVin, moneyTotalVin, moneySafe, "vin") && dao.restoreMoneyByAdmin(user.getId(), moneyUseXu, moneyTotalXu, 0L, "xu")) {
                    response.setErrorCode("0");
                    response.setSuccess(true);
                }
            }
            catch (Exception e) {
                logger.debug(e);
            }
            finally {
                 userMap.unlock(nickname);
            }
        }
        return response;
    }

    @Override
    public boolean checkMobile(String mobile) throws SQLException {
        UserDaoImpl dao = new UserDaoImpl();
        return dao.checkMobile(mobile);
    }

    @Override
    public boolean checkMobileDaiLy(String mobile) throws SQLException {
        UserDaoImpl dao = new UserDaoImpl();
        return dao.checkMobileDaiLy(mobile);
    }

    @Override
    public boolean checkMobileSecurity(String mobile) throws SQLException {
        UserDaoImpl dao = new UserDaoImpl();
        return dao.checkMobileSecurity(mobile);
    }

    @Override
    public boolean checkEmailSecurity(String email) throws SQLException {
        UserDaoImpl dao = new UserDaoImpl();
        return dao.checkEmailSecurity(email);
    }

    @Override
    public byte checkUser(String nickname) {
        int res = -1;
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, UserModel> userMap = client.getMap("users");
        int agent = -1;
        if (userMap.containsKey(nickname)) {
            UserCacheModel user = (UserCacheModel)userMap.get(nickname);
            agent = user.getDaily();
        } else {
            UserDaoImpl dao = new UserDaoImpl();
            try {
                agent = dao.checkAgent(nickname);
            }
            catch (SQLException sQLException) {
                // empty catch block
            }
        }
        res = agent == -1 ? -1 : (agent == 1 ? 1 : (agent == 2 ? 2 : 0));
        return (byte)res;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public NapXuResponse napXu(String nickname, long moneyVinToXu, boolean check) {
        NapXuResponse response;
        block20 : {
            response = new NapXuResponse();
            response.setResult((byte)1);
            try {
                IMap userMap;
                if (GameCommon.getValueInt("IS_NAP_XU") == 1) {
                    return response;
                }
                HazelcastInstance client = HazelcastClientFactory.getInstance();
                if (client == null) {
                    MoneyLogger.log(nickname, "NapXu", moneyVinToXu, 0L, "vin", "Nap xu", "1030", "can not connect hazelcast");
                }
                if (!(userMap = client.getMap("users")).containsKey(nickname)) break block20;
                try {
                     userMap.lock(nickname);
                    UserCacheModel user = (UserCacheModel)userMap.get(nickname);
                    long moneyUserVin = user.getMoney("vin");
                    long currentMoneyVin = user.getCurrentMoney("vin");
                    long moneyUserXu = user.getMoney("xu");
                    long currentMoneyXu = user.getCurrentMoney("xu");
                    if (user.getMobile() != null && user.isHasMobileSecurity()) {
                        if (user.getSecurityTime() != null && VinPlayUtils.cashoutBlockTimeout((Date)user.getSecurityTime(), GameCommon.getValueInt("CASHOUT_TIME_BLOCK"))) {
                            if (moneyVinToXu > 0L) {
                                if (moneyUserVin - moneyVinToXu >= 0L) {
                                    if (check) {
                                        response.setResult((byte)0);
                                    } else {
                                        TransactionContext context = client.newTransactionContext(new TransactionOptions().setTransactionType(TransactionOptions.TransactionType.ONE_PHASE));
                                        context.beginTransaction();
                                        try {
                                            user.setMoney("vin", moneyUserVin -= moneyVinToXu);
                                            user.setCurrentMoney("vin", currentMoneyVin -= moneyVinToXu);
                                            long moneyXuAdded = Math.round((double)moneyVinToXu * GameCommon.getValueDouble("RATIO_NAP_XU"));
                                            user.setMoney("xu", moneyUserXu += moneyXuAdded);
                                            user.setCurrentMoney("xu", currentMoneyXu += moneyXuAdded);
                                            MoneyMessageInMinigame message = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), user.getId(), nickname, "NapXu", moneyUserVin, currentMoneyVin, -moneyVinToXu, "vin", 0L, 0, 0);
                                            LogMoneyUserMessage messageLogVin = new LogMoneyUserMessage(user.getId(), nickname, "NapXu", "N\u1ea1p Xu", currentMoneyVin, -moneyVinToXu, "vin", "Chuy\u1ec3n Vin sang Xu", 0L, false, user.isBot());
                                            RMQApi.publishMessagePayment((BaseMessage)message, 16);
                                            RMQApi.publishMessageLogMoney((LogMoneyUserMessage)messageLogVin);
                                            message = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), user.getId(), nickname, "NapXu", moneyUserXu, currentMoneyXu, moneyXuAdded, "xu", 0L, 0, 0);
                                            LogMoneyUserMessage messageLogXu = new LogMoneyUserMessage(user.getId(), nickname, "NapXu", "N\u1ea1p Xu", currentMoneyXu, moneyXuAdded, "xu", "Chuy\u1ec3n Vin sang Xu", 0L, false, user.isBot());
                                            RMQApi.publishMessagePayment((BaseMessage)message, 16);
                                            RMQApi.publishMessageLogMoney((LogMoneyUserMessage)messageLogXu);
                                            userMap.put(nickname, user);
                                            context.commitTransaction();
                                            response.setResult((byte)0);
                                        }
                                        catch (Exception e) {
                                            logger.debug(e);
                                            MoneyLogger.log(nickname, "NapXu", moneyVinToXu, 0L, "vin", "Nap xu", "1031", "rmq error: " + e.getMessage());
                                            context.rollbackTransaction();
                                        }
                                    }
                                } else {
                                    response.setResult((byte)2);
                                }
                            }
                            response.setCurrentMoneyVin(currentMoneyVin);
                            response.setCurrentMoneyXu(currentMoneyXu);
                        } else {
                            response.setResult((byte)10);
                        }
                    } else {
                        response.setResult((byte)3);
                    }
                }
                catch (Exception e2) {
                    logger.debug(e2);
                    MoneyLogger.log(nickname, "NapXu", moneyVinToXu, 0L, "vin", "Nap xu", "1001", e2.getMessage());
                }
                finally {
                     userMap.unlock(nickname);
                }
            }
            catch (Exception e3) {
                logger.debug(e3);
            }
        }
        return response;
    }

    @Override
    public List<TopCaoThu> getTopCaoThu(String date, String moneyType, int num) {
        List<TopCaoThu> res = new ArrayList<TopCaoThu>();
        if (moneyType.equals("vin")) {
            HazelcastInstance client = HazelcastClientFactory.getInstance();
            IMap<String, HashMap<String, Long>> topMap = client.getMap("cacheTopCaoThuVin");
            if (topMap.containsKey(date)) {
                HashMap<String, Long> map = (HashMap<String, Long>)topMap.get(date);
                TreeMap<String, Long> sortedMap = MapUtils.sortMapByValue((HashMap)map);
                int n = 0;
                for (Map.Entry entry : sortedMap.entrySet()) {
                    if ((Long)entry.getValue() > 0L) {
                        TopCaoThu top = new TopCaoThu((String)entry.getKey(), ((Long)entry.getValue()).longValue());
                        res.add(top);
                    }
                    if (++n < num) continue;
                    break;
                }
            } else {
                UserDaoImpl dao = new UserDaoImpl();
                res = dao.getTopCaoThu(date, moneyType, num);
            }
        }
        return res;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public byte checkMoney(String nickname, long money, byte type) {
        int res = 1;
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, UserModel> userMap = client.getMap("users");
        if (userMap.containsKey(nickname)) {
            try {
                 userMap.lock(nickname);
                UserCacheModel user = (UserCacheModel)userMap.get(nickname);
                if (user.isHasMobileSecurity() && user.getSecurityTime() != null) {
                    if (VinPlayUtils.checkSecurityTimeout((Date)user.getSecurityTime())) {
                        if (type == 2 && !user.isBanTransferMoney()) {
                            if (user.getVin() >= money) {
                                res = 0;
                            }
                        } else {
                            res = 2;
                        }
                        if (type == 3 && !user.isBanCashOut()) {
                            if (user.getVin() >= money) {
                                res = 0;
                            }
                        } else {
                            res = 3;
                        }
                        if (type == 4 && user.getVin() >= money) {
                            res = 0;
                        }
                    } else {
                        res = 6;
                    }
                } else {
                    res = 5;
                }
            }
            catch (Exception e) {
                logger.debug(e);
            }
            finally {
                 userMap.unlock(nickname);
            }
        }
        return (byte)res;
    }

    @Override
    public TransferMoneyResponse transferMoney(String nicknameSend, String nicknameReceive, long vin, String description, boolean check) {
        long moneyReceive;
        TransferMoneyResponse res;
        boolean updateVP;
        int status;
        block71 : {
            logger.debug((Object)("Request transferMoney: nicknameSend: " + nicknameSend + ", nicknameReceive: " + nicknameReceive + ", money: " + vin + ", description: " + description + ", check: " + check));
            res = new TransferMoneyResponse((byte)1, 0L, 0L);
            updateVP = false;
            status = 0;
            moneyReceive = 0L;
            try {
                if (GameCommon.getValueInt("IS_TRANSFER_MONEY") == 1) {
                    logger.debug((Object)"Khoa chuyen tien");
                    return res;
                }
                if (nicknameSend == null || nicknameReceive == null || description == null || nicknameSend.equals(nicknameReceive)) {
                    logger.debug((Object)"Missing param");
                    return res;
                }
                // check hạn mức
                if(vin<10000)
                {
                    logger.debug((Object)"check hạn mức <200000");
                    res.setCode((byte)2);
                    return res;
                }
                logger.debug("transferMoney 1 End Check");
                // end check
                UserModel userReceive = this.getUserByNickName(nicknameReceive);
                AgentDaoImpl agentDao = new AgentDaoImpl();
                AlertServiceImpl alertSer = new AlertServiceImpl();
                if (userReceive != null) {
                    nicknameReceive = userReceive.getNickname();
                    res.setNicknameReceive(nicknameReceive);
//                    int feeSMS = GameCommon.getValueInt("SMS_FEE");
                    int feeSMS = 0;
                    boolean sendSMS = false;
                    try {
                        if ((userReceive.getDaily() == 1 || userReceive.getDaily() == 2) && userReceive.getMobile() != null && userReceive.isHasMobileSecurity() && agentDao.checkSMSAgent(nicknameReceive, vin)) {
                            sendSMS = true;
                        }
                        logger.debug("transferMoney 2 End Check sendSMS: " + sendSMS);
                    }
                    catch (Exception e) {
                        logger.debug((Object)e);
                    }
                    if (vin >= (long)GameCommon.getValueInt("TRANSFER_MONEY_MIN")) {
                        logger.debug("transferMoney 3 End Check vin: " + vin);
                        HazelcastInstance client = HazelcastClientFactory.getInstance();
                        if (client == null) {
                            MoneyLogger.log(nicknameSend, "TransferMoney", vin, 0L, "vin", "chuyen khoan", "1030", "can not connect hazelcast");
                            return res;
                        }
                        IMap<String, UserModel> userMap = client.getMap("users");
                        if (userMap.containsKey((Object)nicknameSend)) {
                            try {
                                 userMap.lock(nicknameSend);
                                UserCacheModel userSend = (UserCacheModel)userMap.get((Object)nicknameSend);
                                String superAgent = GameCommon.getValueStr("SUPER_AGENT");
                                long dl1Max = GameCommon.getValueLong("DL1_TO_SUPER_MAX");
                                long dl1Min = GameCommon.getValueLong("DL1_TO_SUPER_MIN");
                                long dl1MinX = GameCommon.getValueLong("DL1_TO_SUPER_MIN_X");
                                boolean dl1ToSuperAgent = userSend.getDaily() == 1 && nicknameReceive.equals(superAgent);
                                long moneyUser = userSend.getVin();
                                long currentMoney = userSend.getVinTotal();
                                logger.debug("transferMoney 4 End Check moneyUser: " + moneyUser + " currentMoney: " + currentMoney + " dl1ToSuperAgent: " +dl1ToSuperAgent);
                                if (!dl1ToSuperAgent || dl1ToSuperAgent && dl1Min <= vin && dl1Max >= vin) {
                                    if (!dl1ToSuperAgent || dl1ToSuperAgent && currentMoney - vin >= dl1MinX) {
                                        logger.debug("transferMoney 5 End Check moneyUser: ");
                                        res.setMoneyUse(moneyUser);
                                        res.setCurrentMoney(currentMoney);
                                        if (!userSend.isBanTransferMoney()) {
                                            if (!userSend.isHasMobileSecurity()) {
                                                logger.debug("transferMoney 6 End Check moneyUser: ");
                                                if (moneyUser >= vin) {
                                                    if (check) {
                                                        res.setCode((byte)0);
                                                        break block71;
                                                    }
                                                    TransactionContext context = client.newTransactionContext(new TransactionOptions().setTransactionType(TransactionOptions.TransactionType.ONE_PHASE));
                                                    if (userMap.containsKey((Object)nicknameReceive)) {
                                                        try {
                                                            context.beginTransaction();
                                                             userMap.lock(nicknameReceive);
                                                            UserCacheModel userCacheReceive = (UserCacheModel)userMap.get((Object)nicknameReceive);
                                                            status = this.getStatusChuyenTienDaiLy(userSend.getDaily(), userCacheReceive.getDaily());
                                                            long fee = Math.round((double)vin * this.getFeeTransfer(status));
                                                            moneyReceive = vin - fee;
                                                            res.setMoneyReceive(moneyReceive);
                                                            long moneyUserReceive = userCacheReceive.getVin();
                                                            long currentMoneyReceive = userCacheReceive.getVinTotal();
                                                            userSend.setVin(moneyUser -= vin);
                                                            userSend.setVinTotal(currentMoney -= vin);
                                                            userCacheReceive.setVin(moneyUserReceive += moneyReceive);
                                                            userCacheReceive.setVinTotal(currentMoneyReceive += moneyReceive);
                                                            MoneyMessageInMinigame messageSend = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), userSend.getId(), nicknameSend, "TransferMoney", moneyUser, currentMoney, -vin, "vin", fee, 0, 0);
                                                            String desSend = "Chuy\u1ec3n t\u1edbi " + nicknameReceive + ": " + description;
                                                            LogMoneyUserMessage messageLogSend = new LogMoneyUserMessage(userSend.getId(), nicknameSend, "TransferMoney", "Chuy\u1ec3n kho\u1ea3n", currentMoney, -vin, "vin", desSend, fee, false, userSend.isBot());
                                                            int recharge = 0;
                                                            if (status == 3 || status == 6) {
                                                                recharge = -1;
                                                                userCacheReceive.setRechargeMoney(userCacheReceive.getRechargeMoney() + moneyReceive);
                                                            }
                                                            MoneyMessageInMinigame messageReceive = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), userCacheReceive.getId(), nicknameReceive, "TransferMoney", moneyUserReceive, currentMoneyReceive, moneyReceive, "vin", 0L, recharge, 0);
                                                            String desReceive = "Nh\u1eadn t\u1eeb " + nicknameSend + ": " + description;
                                                            LogMoneyUserMessage messageLogReceive = new LogMoneyUserMessage(userCacheReceive.getId(), nicknameReceive, "TransferMoney", "Chuy\u1ec3n kho\u1ea3n", currentMoneyReceive, moneyReceive, "vin", desReceive, 0L, false, userCacheReceive.isBot());
                                                            RMQApi.publishMessagePayment((BaseMessage)messageSend, (int)16);
                                                            RMQApi.publishMessageLogMoney((LogMoneyUserMessage)messageLogSend);
                                                            RMQApi.publishMessagePayment((BaseMessage)messageReceive, (int)16);
                                                            RMQApi.publishMessageLogMoney((LogMoneyUserMessage)messageLogReceive);
                                                            if (status != 0) {
                                                                LogChuyenTienDaiLyMessage messageDaily = new LogChuyenTienDaiLyMessage(nicknameSend, nicknameReceive, vin, moneyReceive, fee, VinPlayUtils.getCurrentDateTime(), status, desSend, desReceive, VinPlayUtils.genTransactionId((int)userSend.getId()), 1, this.getAgentLevel1(nicknameSend, nicknameReceive), "");
                                                                RMQApi.publishMessage((String)"queue_log_chuyen_tien_dai_ly", (BaseMessage)messageDaily, (int)602);
                                                            }
                                                            if (sendSMS) {
                                                                if (feeSMS > 0) {
                                                                    userCacheReceive.setVin(moneyUserReceive -= (long)feeSMS);
                                                                    userCacheReceive.setVinTotal(currentMoneyReceive -= (long)feeSMS);
                                                                    MoneyMessageInMinigame messageReceiveSMSFee = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), userCacheReceive.getId(), nicknameReceive, "ChargeSMS", moneyUserReceive, currentMoneyReceive, (long)(-feeSMS), "vin", 0L, 0, 0);
                                                                    LogMoneyUserMessage messageLogReceiveSMSFee = new LogMoneyUserMessage(userCacheReceive.getId(), nicknameReceive, "ChargeSMS", "Ph\u00ed SMS", currentMoneyReceive, (long)(-feeSMS), "vin", "Tr\u1eeb ph\u00ed d\u1ecbch v\u1ee5 SMS", 0L, false, userCacheReceive.isBot());
                                                                    RMQApi.publishMessagePayment((BaseMessage)messageReceiveSMSFee, (int)16);
                                                                    RMQApi.publishMessageLogMoney((LogMoneyUserMessage)messageLogReceiveSMSFee);
                                                                }
                                                                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
                                                                String time = format.format(new Date());
                                                                String content = nicknameSend + " da chuyen " + NumberUtils.formatNumber((String)String.valueOf(vin)) + " vin cho ban luc " + time + ". So vin nhan: " + NumberUtils.formatNumber((String)String.valueOf(moneyReceive)) + ". So du vin: " + NumberUtils.formatNumber((String)String.valueOf(currentMoneyReceive));
                                                                alertSer.sendSMS2One(userCacheReceive.getMobile(), content, false);
                                                            }
                                                             userMap.put(nicknameSend, userSend);;
                                                             userMap.put(nicknameReceive, userCacheReceive);
                                                            context.commitTransaction();
                                                            res.setCode((byte)0);
                                                            res.setMoneyUse(moneyUser);
                                                            res.setCurrentMoney(currentMoney);
                                                            res.setCurrentMoneyReceive(currentMoneyReceive);
                                                            updateVP = true;
                                                            if (dl1ToSuperAgent) {
                                                                Timer timer = new Timer();
                                                                TransferMoneyBankModel model = new TransferMoneyBankModel(nicknameSend, moneyReceive);
                                                                timer.schedule((TimerTask)new TransferMoneyBankService(model), 5000L);
                                                            }
                                                            break block71;
                                                        }
                                                        catch (Exception e2) {
                                                            logger.debug((Object)e2);
                                                            MoneyLogger.log(nicknameSend, "TransferMoney", vin, 0L, "vin", "chuyen khoan", "1001", e2.getMessage());
                                                            context.rollbackTransaction();
                                                            break block71;
                                                        }
                                                        finally {
                                                             userMap.unlock(nicknameReceive);
                                                        }
                                                    }
                                                    if (userReceive == null) break block71;
                                                    try {
                                                        context.beginTransaction();
                                                        long currentMoneyReceive2 = userReceive.getVinTotal();
                                                        status = this.getStatusChuyenTienDaiLy(userSend.getDaily(), userReceive.getDaily());
                                                        long fee2 = Math.round((double)vin * this.getFeeTransfer(status));
                                                        moneyReceive = vin - fee2;
                                                        res.setMoneyReceive(moneyReceive);
                                                        currentMoneyReceive2 += moneyReceive;
                                                        userSend.setVin(moneyUser -= vin);
                                                        userSend.setVinTotal(currentMoney -= vin);
                                                        MoneyMessageInMinigame messageSend2 = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), userSend.getId(), nicknameSend, "TransferMoney", moneyUser, currentMoney, -vin, "vin", fee2, 0, 0);
                                                        String desSend2 = "Chuy\u1ec3n t\u1edbi " + nicknameReceive + ": " + description;
                                                        LogMoneyUserMessage messageLogSend2 = new LogMoneyUserMessage(userSend.getId(), nicknameSend, "TransferMoney", "Chuy\u1ec3n kho\u1ea3n", currentMoney, -vin, "vin", desSend2, fee2, false, userSend.isBot());
                                                        RMQApi.publishMessagePayment((BaseMessage)messageSend2, (int)16);
                                                        RMQApi.publishMessageLogMoney((LogMoneyUserMessage)messageLogSend2);
                                                         userMap.put(nicknameSend, userSend);;
                                                        context.commitTransaction();
                                                        res.setCode((byte)0);
                                                        res.setMoneyUse(moneyUser);
                                                        res.setCurrentMoney(currentMoney);
                                                        updateVP = true;
                                                        UserDaoImpl dao = new UserDaoImpl();
                                                        if (!dao.updateMoney(userReceive.getId(), moneyReceive, "vin")) break block71;
                                                        if (status == 3 || status == 6) {
                                                            try {
                                                                dao.updateRechargeMoney(userReceive.getId(), moneyReceive);
                                                            }
                                                            catch (Exception e3) {
                                                                logger.debug((Object)e3);
                                                            }
                                                        }
                                                        res.setCurrentMoneyReceive(currentMoneyReceive2);
                                                        String desReceive2 = "Nh\u1eadn t\u1eeb " + nicknameSend + ": " + description;
                                                        LogMoneyUserMessage messageLogReceive2 = new LogMoneyUserMessage(userReceive.getId(), nicknameReceive, "TransferMoney", "Chuy\u1ec3n kho\u1ea3n", currentMoneyReceive2, moneyReceive, "vin", desReceive2, 0L, false, userReceive.isBot());
                                                        RMQApi.publishMessageLogMoney((LogMoneyUserMessage)messageLogReceive2);
                                                        if (status != 0) {
                                                            LogChuyenTienDaiLyMessage messageDaily2 = new LogChuyenTienDaiLyMessage(nicknameSend, nicknameReceive, vin, moneyReceive, fee2, VinPlayUtils.getCurrentDateTime(), status, desSend2, desReceive2, VinPlayUtils.genTransactionId((int)userSend.getId()), 1, this.getAgentLevel1(nicknameSend, nicknameReceive), "");
                                                            RMQApi.publishMessage((String)"queue_log_chuyen_tien_dai_ly", (BaseMessage)messageDaily2, (int)602);
                                                        }
                                                        if (sendSMS) {
                                                            if (feeSMS > 0 && dao.updateMoney(userReceive.getId(), -feeSMS, "vin")) {
                                                                LogMoneyUserMessage messageLogReceiveSMSFee2 = new LogMoneyUserMessage(userReceive.getId(), nicknameReceive, "ChargeSMS", "Ph\u00ed SMS", currentMoneyReceive2 -= (long)feeSMS, (long)(-feeSMS), "vin", "Tr\u1eeb ph\u00ed d\u1ecbch v\u1ee5 SNS", 0L, false, userReceive.isBot());
                                                                RMQApi.publishMessageLogMoney((LogMoneyUserMessage)messageLogReceiveSMSFee2);
                                                            }
                                                            SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
                                                            String time2 = format2.format(new Date());
                                                            String content2 = nicknameSend + " da chuyen " + NumberUtils.formatNumber((String)String.valueOf(vin)) + " vin cho ban luc " + time2 + ". So vin nhan: " + NumberUtils.formatNumber((String)String.valueOf(moneyReceive)) + ".So du vin: " + NumberUtils.formatNumber((String)String.valueOf(currentMoneyReceive2));
                                                            alertSer.sendSMS2One(userReceive.getMobile(), content2, false);
                                                        }
                                                        if (dl1ToSuperAgent) {
                                                            Timer timer2 = new Timer();
                                                            TransferMoneyBankModel model2 = new TransferMoneyBankModel(nicknameSend, moneyReceive);
                                                            timer2.schedule((TimerTask)new TransferMoneyBankService(model2), 5000L);
                                                        }
                                                        break block71;
                                                    }
                                                    catch (Exception e2) {
                                                        logger.debug((Object)e2);
                                                        MoneyLogger.log(nicknameSend, "TransferMoney", vin, 0L, "vin", "chuyen khoan", "1001", e2.getMessage());
                                                        context.rollbackTransaction();
                                                    }
                                                    break block71;
                                                }
                                                res.setCode((byte)4);
                                                break block71;
                                            }
                                            res.setCode((byte)3);
                                            break block71;
                                        }
                                        res.setCode((byte)5);
                                        break block71;
                                    }
                                    res.setCode((byte)12);
                                    break block71;
                                }
                                res.setCode((byte)11);
                                break block71;
                            }
                            catch (Exception e4) {
                                logger.debug((Object)e4);
                                MoneyLogger.log(nicknameSend, "TransferMoney", vin, 0L, "vin", "chuyen khoan", "1001", e4.getMessage());
                                break block71;
                            }
                            finally {
                                 userMap.unlock(nicknameSend);
                            }
                        }
                        logger.debug("transferMoney 7 End Check moneyUser: ");
                        try {
                            UserDaoImpl userDao = new UserDaoImpl();
                            UserCacheModel userSend2 = userDao.getUserByNickNameCache(nicknameSend);
                            if (userSend2 == null) break block71;
                            String superAgent2 = GameCommon.getValueStr("SUPER_AGENT");
                            long dl1Max2 = GameCommon.getValueLong("DL1_TO_SUPER_MAX");
                            long dl1Min2 = GameCommon.getValueLong("DL1_TO_SUPER_MIN");
                            long dl1MinX2 = GameCommon.getValueLong("DL1_TO_SUPER_MIN_X");
                            boolean dl1ToSuperAgent2 = userSend2.getDaily() == 1 && nicknameReceive.equals(superAgent2);
                            long moneyUser2 = userSend2.getVin();
                            long currentMoney2 = userSend2.getVinTotal();
                            if (!dl1ToSuperAgent2 || dl1ToSuperAgent2 && dl1Min2 <= vin && dl1Max2 >= vin) {
                                if (!dl1ToSuperAgent2 || dl1ToSuperAgent2 && currentMoney2 - vin >= dl1MinX2) {
                                    res.setMoneyUse(moneyUser2);
                                    res.setCurrentMoney(currentMoney2);
                                    if (!userSend2.isBanTransferMoney()) {
                                        if (userSend2.getMobile() != null && !userSend2.getMobile().isEmpty() && userSend2.isHasMobileSecurity()) {
                                            if (moneyUser2 >= vin) {
                                                if (check) {
                                                    res.setCode((byte)0);
                                                    break block71;
                                                }
                                                TransactionContext context2 = client.newTransactionContext(new TransactionOptions().setTransactionType(TransactionOptions.TransactionType.ONE_PHASE));
                                                if (userMap.containsKey((Object)nicknameReceive)) {
                                                    try {
                                                        context2.beginTransaction();
                                                         userMap.lock(nicknameReceive);
                                                        UserCacheModel userCacheReceive2 = (UserCacheModel)userMap.get((Object)nicknameReceive);
                                                        status = this.getStatusChuyenTienDaiLy(userSend2.getDaily(), userCacheReceive2.getDaily());
                                                        long fee2 = Math.round((double)vin * this.getFeeTransfer(status));
                                                        moneyReceive = vin - fee2;
                                                        res.setMoneyReceive(moneyReceive);
                                                        long moneyUserReceive2 = userCacheReceive2.getVin();
                                                        long currentMoneyReceive3 = userCacheReceive2.getVinTotal();
                                                        userSend2.setVin(moneyUser2 -= vin);
                                                        userSend2.setVinTotal(currentMoney2 -= vin);
                                                        userCacheReceive2.setVin(moneyUserReceive2 += moneyReceive);
                                                        userCacheReceive2.setVinTotal(currentMoneyReceive3 += moneyReceive);
                                                        MoneyMessageInMinigame messageSend3 = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), userSend2.getId(), nicknameSend, "TransferMoney", moneyUser2, currentMoney2, -vin, "vin", fee2, 0, 0);
                                                        String desSend3 = "Chuy\u1ec3n t\u1edbi " + nicknameReceive + ": " + description;
                                                        LogMoneyUserMessage messageLogSend3 = new LogMoneyUserMessage(userSend2.getId(), nicknameSend, "TransferMoney", "Chuy\u1ec3n kho\u1ea3n", currentMoney2, -vin, "vin", desSend3, fee2, false, userSend2.isBot());
                                                        int recharge2 = 0;
                                                        if (status == 3 || status == 6) {
                                                            recharge2 = -1;
                                                            userCacheReceive2.setRechargeMoney(userCacheReceive2.getRechargeMoney() + moneyReceive);
                                                        }
                                                        MoneyMessageInMinigame messageReceive2 = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), userCacheReceive2.getId(), nicknameReceive, "TransferMoney", moneyUserReceive2, currentMoneyReceive3, moneyReceive, "vin", 0L, recharge2, 0);
                                                        String desReceive3 = "Nh\u1eadn t\u1eeb " + nicknameSend + ": " + description;
                                                        LogMoneyUserMessage messageLogReceive3 = new LogMoneyUserMessage(userCacheReceive2.getId(), nicknameReceive, "TransferMoney", "Chuy\u1ec3n kho\u1ea3n", currentMoneyReceive3, moneyReceive, "vin", desReceive3, 0L, false, userCacheReceive2.isBot());
                                                        RMQApi.publishMessagePayment((BaseMessage)messageSend3, (int)16);
                                                        RMQApi.publishMessageLogMoney((LogMoneyUserMessage)messageLogSend3);
                                                        RMQApi.publishMessagePayment((BaseMessage)messageReceive2, (int)16);
                                                        RMQApi.publishMessageLogMoney((LogMoneyUserMessage)messageLogReceive3);
                                                        if (status != 0) {
                                                            LogChuyenTienDaiLyMessage messageDaily3 = new LogChuyenTienDaiLyMessage(nicknameSend, nicknameReceive, vin, moneyReceive, fee2, VinPlayUtils.getCurrentDateTime(), status, desSend3, desReceive3, VinPlayUtils.genTransactionId((int)userSend2.getId()), 1, this.getAgentLevel1(nicknameSend, nicknameReceive), "");
                                                            RMQApi.publishMessage((String)"queue_log_chuyen_tien_dai_ly", (BaseMessage)messageDaily3, (int)602);
                                                        }
                                                        if (sendSMS) {
                                                            if (feeSMS > 0) {
                                                                userCacheReceive2.setVin(moneyUserReceive2 -= (long)feeSMS);
                                                                userCacheReceive2.setVinTotal(currentMoneyReceive3 -= (long)feeSMS);
                                                                MoneyMessageInMinigame messageReceiveSMSFee2 = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), userCacheReceive2.getId(), nicknameReceive, "ChargeSMS", moneyUserReceive2, currentMoneyReceive3, (long)(-feeSMS), "vin", 0L, 0, 0);
                                                                LogMoneyUserMessage messageLogReceiveSMSFee3 = new LogMoneyUserMessage(userCacheReceive2.getId(), nicknameReceive, "ChargeSMS", "Ph\u00ed SMS", currentMoneyReceive3, (long)(-feeSMS), "vin", "Tr\u1eeb ph\u00ed d\u1ecbch v\u1ee5 SMS", 0L, false, userCacheReceive2.isBot());
                                                                RMQApi.publishMessagePayment((BaseMessage)messageReceiveSMSFee2, (int)16);
                                                                RMQApi.publishMessageLogMoney((LogMoneyUserMessage)messageLogReceiveSMSFee3);
                                                            }
                                                            SimpleDateFormat format3 = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
                                                            String time3 = format3.format(new Date());
                                                            String content3 = nicknameSend + " da chuyen " + NumberUtils.formatNumber((String)String.valueOf(vin)) + " vin cho ban luc " + time3 + ". So vin nhan: " + NumberUtils.formatNumber((String)String.valueOf(moneyReceive)) + ". So du vin: " + NumberUtils.formatNumber((String)String.valueOf(currentMoneyReceive3));
                                                            alertSer.sendSMS2One(userCacheReceive2.getMobile(), content3, false);
                                                        }
                                                         userMap.put(nicknameSend, userSend2);
                                                         userMap.put(nicknameReceive, userCacheReceive2);
                                                        context2.commitTransaction();
                                                        res.setCode((byte)0);
                                                        res.setMoneyUse(moneyUser2);
                                                        res.setCurrentMoney(currentMoney2);
                                                        res.setCurrentMoneyReceive(currentMoneyReceive3);
                                                        updateVP = true;
                                                        if (dl1ToSuperAgent2) {
                                                            Timer timer3 = new Timer();
                                                            TransferMoneyBankModel model3 = new TransferMoneyBankModel(nicknameSend, moneyReceive);
                                                            timer3.schedule((TimerTask)new TransferMoneyBankService(model3), 5000L);
                                                        }
                                                        break block71;
                                                    }
                                                    catch (Exception e5) {
                                                        logger.debug((Object)e5);
                                                        MoneyLogger.log(nicknameSend, "TransferMoney", vin, 0L, "vin", "chuyen khoan", "1001", e5.getMessage());
                                                        context2.rollbackTransaction();
                                                        break block71;
                                                    }
                                                    finally {
                                                         userMap.unlock(nicknameReceive);
                                                    }
                                                }
                                                if (userReceive == null) break block71;
                                                try {
                                                    context2.beginTransaction();
                                                    long currentMoneyReceive4 = userReceive.getVinTotal();
                                                    status = this.getStatusChuyenTienDaiLy(userSend2.getDaily(), userReceive.getDaily());
                                                    long fee3 = Math.round((double)vin * this.getFeeTransfer(status));
                                                    moneyReceive = vin - fee3;
                                                    res.setMoneyReceive(moneyReceive);
                                                    currentMoneyReceive4 += moneyReceive;
                                                    userSend2.setVin(moneyUser2 -= vin);
                                                    userSend2.setVinTotal(currentMoney2 -= vin);
                                                    MoneyMessageInMinigame messageSend4 = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), userSend2.getId(), nicknameSend, "TransferMoney", moneyUser2, currentMoney2, -vin, "vin", fee3, 0, 0);
                                                    String desSend4 = "Chuy\u1ec3n t\u1edbi " + nicknameReceive + ": " + description;
                                                    LogMoneyUserMessage messageLogSend4 = new LogMoneyUserMessage(userSend2.getId(), nicknameSend, "TransferMoney", "Chuy\u1ec3n kho\u1ea3n", currentMoney2, -vin, "vin", desSend4, fee3, false, userSend2.isBot());
                                                    RMQApi.publishMessagePayment((BaseMessage)messageSend4, (int)16);
                                                    RMQApi.publishMessageLogMoney((LogMoneyUserMessage)messageLogSend4);
                                                     userMap.put(nicknameSend, userSend2);
                                                    context2.commitTransaction();
                                                    res.setCode((byte)0);
                                                    res.setMoneyUse(moneyUser2);
                                                    res.setCurrentMoney(currentMoney2);
                                                    updateVP = true;
                                                    UserDaoImpl dao2 = new UserDaoImpl();
                                                    if (!dao2.updateMoney(userReceive.getId(), moneyReceive, "vin")) break block71;
                                                    if (status == 3 || status == 6) {
                                                        try {
                                                            dao2.updateRechargeMoney(userReceive.getId(), moneyReceive);
                                                        }
                                                        catch (Exception e6) {
                                                            logger.debug((Object)e6);
                                                        }
                                                    }
                                                    res.setCurrentMoneyReceive(currentMoneyReceive4);
                                                    String desReceive4 = "Nh\u1eadn t\u1eeb " + nicknameSend + ": " + description;
                                                    LogMoneyUserMessage messageLogReceive4 = new LogMoneyUserMessage(userReceive.getId(), nicknameReceive, "TransferMoney", "Chuy\u1ec3n kho\u1ea3n", currentMoneyReceive4, moneyReceive, "vin", desReceive4, 0L, false, userReceive.isBot());
                                                    RMQApi.publishMessageLogMoney((LogMoneyUserMessage)messageLogReceive4);
                                                    if (status != 0) {
                                                        LogChuyenTienDaiLyMessage messageDaily4 = new LogChuyenTienDaiLyMessage(nicknameSend, nicknameReceive, vin, moneyReceive, fee3, VinPlayUtils.getCurrentDateTime(), status, desSend4, desReceive4, VinPlayUtils.genTransactionId((int)userSend2.getId()), 1, this.getAgentLevel1(nicknameSend, nicknameReceive), "");
                                                        RMQApi.publishMessage((String)"queue_log_chuyen_tien_dai_ly", (BaseMessage)messageDaily4, (int)602);
                                                    }
                                                    if (sendSMS) {
                                                        if (feeSMS > 0 && dao2.updateMoney(userReceive.getId(), -feeSMS, "vin")) {
                                                            LogMoneyUserMessage messageLogReceiveSMSFee4 = new LogMoneyUserMessage(userReceive.getId(), nicknameReceive, "ChargeSMS", "Ph\u00ed SMS", currentMoneyReceive4 -= (long)feeSMS, (long)(-feeSMS), "vin", "Tr\u1eeb ph\u00ed d\u1ecbch v\u1ee5 SNS", 0L, false, userReceive.isBot());
                                                            RMQApi.publishMessageLogMoney((LogMoneyUserMessage)messageLogReceiveSMSFee4);
                                                        }
                                                        SimpleDateFormat format4 = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
                                                        String time4 = format4.format(new Date());
                                                        String content4 = nicknameSend + " da chuyen " + NumberUtils.formatNumber((String)String.valueOf(vin)) + " vin cho ban luc " + time4 + ". So vin nhan: " + NumberUtils.formatNumber((String)String.valueOf(moneyReceive)) + ".So du vin: " + NumberUtils.formatNumber((String)String.valueOf(currentMoneyReceive4));
                                                        alertSer.sendSMS2One(userReceive.getMobile(), content4, false);
                                                    }
                                                    if (dl1ToSuperAgent2) {
                                                        Timer timer4 = new Timer();
                                                        TransferMoneyBankModel model4 = new TransferMoneyBankModel(nicknameSend, moneyReceive);
                                                        timer4.schedule((TimerTask)new TransferMoneyBankService(model4), 5000L);
                                                    }
                                                    break block71;
                                                }
                                                catch (Exception e5) {
                                                    logger.debug((Object)e5);
                                                    MoneyLogger.log(nicknameSend, "TransferMoney", vin, 0L, "vin", "chuyen khoan", "1001", e5.getMessage());
                                                    context2.rollbackTransaction();
                                                }
                                                break block71;
                                            }
                                            res.setCode((byte)4);
                                            break block71;
                                        }
                                        res.setCode((byte)3);
                                        break block71;
                                    }
                                    res.setCode((byte)5);
                                    break block71;
                                }
                                res.setCode((byte)12);
                                break block71;
                            }
                            res.setCode((byte)11);
                        }
                        catch (Exception e4) {
                            logger.debug((Object)e4);
                            MoneyLogger.log(nicknameSend, "TransferMoney", vin, 0L, "vin", "chuyen khoan", "1001", e4.getMessage());
                        }
                        break block71;
                    }
                    res.setCode((byte)2);
                    break block71;
                }
                res.setCode((byte)6);
            }
            catch (Exception e7) {
                logger.debug((Object)e7);
                MoneyLogger.log(nicknameSend, "TransferMoney", vin, 0L, "vin", "chuyen khoan", "1001", e7.getMessage());
            }
        }
        if (updateVP) {
            VippointServiceImpl vpSer = new VippointServiceImpl();
            vpSer.updateVippointAgent(nicknameSend, nicknameReceive, vin, moneyReceive, status);
        }
        logger.debug((Object)("Response transferMoney: " + res.getCode()));
        return res;
    }

    private String getAgentLevel1(String nicknameSend, String nicknameReceive) {
        AgentDaoImpl agentDao = new AgentDaoImpl();
        String agentLevel1 = "";
        try {
            agentLevel1 = agentDao.getAgentLevel1ByNickName(nicknameSend);
            if (agentLevel1.equals("")) {
                agentLevel1 = agentDao.getAgentLevel1ByNickName(nicknameReceive);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return agentLevel1;
    }

    private int getStatusChuyenTienDaiLy(int user1, int user2) {
        int status = 0;
        if ((user1 == 0 || user1 == 100) && user2 == 1) {
            status = 1;
        } else if ((user1 == 0 || user1 == 100) && user2 == 2) {
            status = 2;
        } else if (user1 == 1 && (user2 == 0 || user2 == 100)) {
            status = 3;
        } else if (user1 == 1 && user2 == 1) {
            status = 4;
        } else if (user1 == 1 && user2 == 2) {
            status = 5;
        } else if (user1 == 2 && (user2 == 0 || user2 == 100)) {
            status = 6;
        } else if (user1 == 2 && user2 == 1) {
            status = 7;
        } else if (user1 == 2 && user2 == 2) {
            status = 8;
        }
        return status;
    }

    @Override
    public double getFeeTransfer(int status) {
        double fee = 0.98;
        try {
            switch (status) {
                case 0: {
                    fee = 1.0 - GameCommon.getValueDouble("RATIO_TRANSFER");
                    break;
                }
                case 1: {
                    fee = 1.0 - GameCommon.getValueDouble("RATIO_TRANSFER_01");
                    break;
                }
                case 2: {
                    fee = 1.0 - GameCommon.getValueDouble("RATIO_TRANSFER_02");
                    break;
                }
                case 3: {
                    fee = 1.0 - GameCommon.getValueDouble("RATIO_TRANSFER_DL_1");
                    break;
                }
                case 4: {
                    fee = 1.0 - GameCommon.getValueDouble("RATIO_TRANSFER_11");
                    break;
                }
                case 5: {
                    fee = 1.0 - GameCommon.getValueDouble("RATIO_TRANSFER_12");
                    break;
                }
                case 6: {
                    fee = 1.0 - GameCommon.getValueDouble("RATIO_TRANSFER_20");
                    break;
                }
                case 7: {
                    fee = 1.0 - GameCommon.getValueDouble("RATIO_TRANSFER_21");
                    break;
                }
                case 8: {
                    fee = 1.0 - GameCommon.getValueDouble("RATIO_TRANSFER_22");
                    break;
                }
                default: {
                    fee = 1.0 - GameCommon.getValueDouble("RATIO_TRANSFER");
                    break;
                }
            }
        }
        catch (Exception e) {
            logger.debug(e);
        }
        return fee;
    }

    @Override
    public byte calFeeTransfer(int user1, int user2) {
        byte res = 0;
        int status = this.getStatusChuyenTienDaiLy(user1, user2);
        double fee = this.getFeeTransfer(status);
        res = (byte)(fee * 100.0);
        return res;
    }

    @Override
    public UserCacheModel getUser(String nickname) {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, UserModel> userMap = client.getMap("users");
        if (userMap.containsKey(nickname)) {
            return (UserCacheModel)userMap.get(nickname);
        }
        return null;
    }

    @Override
    public boolean insertBot(String un, String nn, String pw, long vin, long xu, int status) throws SQLException {
        UserDaoImpl dao = new UserDaoImpl();
        return dao.insertBot(un, nn, pw, vin, xu, status);
    }

    @Override
    public long getTotalRechargeMoney(String username) {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, UserModel> userMap = client.getMap("users");
        if (userMap.containsKey(username)) {
            UserCacheModel model = (UserCacheModel)userMap.get(username);
            return model.getRechargeMoney();
        }
        return 0L;
    }

    @Override
    public int getVipPointSave(String username) {
        UserCacheModel model = this.getUser(username);
        if (model != null) {
            return model.getVippointSave();
        }
        return 0;
    }

	@Override
	public boolean isActiveToken(String nickname, String accessToken) {
		if (nickname != null && accessToken != null && !nickname.isEmpty() && !accessToken.isEmpty()) {
			try {
				HazelcastInstance instance = HazelcastClientFactory.getInstance();
				IMap userMap = instance.getMap("users");
				UserCacheModel userCache = (UserCacheModel) userMap.get(nickname);
				if (userMap.containsKey(nickname) && userCache.getAccessToken().equals(accessToken)) {
					return true;
				}
			} catch (Exception e) {
				logger.debug(e);
			}
		}
		return false;
	}

    @Override
    public int checkBot(String nickname) {
        int res = 0;
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, UserModel> userMap = client.getMap("users");
        if (userMap.containsKey(nickname)) {
            UserCacheModel user = (UserCacheModel)userMap.get(nickname);
            if (user.isBot()) {
                res = 1;
            }
        } else {
            try {
                UserDaoImpl dao = new UserDaoImpl();
                res = dao.checkBotByNickname(nickname);
            }
            catch (Exception e) {
                logger.debug(e);
            }
        }
        return res;
    }

    @Override
    public UserExtraInfoModel getUserExtraInfo(String nickname) {
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap userExtraMap = client.getMap("cache_user_extra_info");
        if (userExtraMap.containsKey(nickname)) {
            return (UserExtraInfoModel)userExtraMap.get(nickname);
        }
        return null;
    }

    @Override
    public UserModel getNicknameExactly(String nickname, IMap<String, UserCacheModel> userMap) throws SQLException {
        UserModel model = null;
        if (userMap.containsKey(nickname)) {
            model = new UserModel();
            model.setNickname(nickname);
            model.setBot(((UserCacheModel)userMap.get(nickname)).isBot());
        } else {
            model = this.getUserByNickName(nickname);
        }
        return model;
    }

    @Override
    public List<UserInfoModel> checkPhoneByUser(String phone) throws SQLException {
        return userDao.checkPhoneByUser(phone);
    }

    @Override
    public UserInfoModel checkPhoneExists(String phone) throws SQLException {
        
        return userDao.checkPhoneExists(phone);
    }

    @Override
    public UserCacheModel checkMoneyNegative(UserCacheModel user) throws SQLException {
        if (user.getVin() < 0L || user.getVinTotal() < 0L || user.getXu() < 0L || user.getXuTotal() < 0L) {
            user.setBanLogin(true);
            user.setBanCashOut(true);
            user.setBanTransferMoney(true);
            int statusNew = user.getStatus();
            statusNew = StatusUser.changeStatus(statusNew, 0, "1");
            statusNew = StatusUser.changeStatus(statusNew, 1, "1");
            statusNew = StatusUser.changeStatus(statusNew, 3, "1");
            user.setStatus(statusNew);
            SecurityDaoImpl dao = new SecurityDaoImpl();
            dao.updateUserInfo(user.getId(), String.valueOf(statusNew), 7);
			TelegramUtil.warningCheat("User am tien : " + user.getNickname());
            return user;
        }
        return null;
    }



    public boolean refundWhenError(String nickname, long amount, long fee ){
        try{
            
            UserModel user = userDao.getUserByNickName(nickname);
            if(user == null)
                return false;
            HazelcastInstance client = HazelcastClientFactory.getInstance();
            if(client == null)
                return false;
            IMap<String, UserModel> userMap = client.getMap("users");

            if(!userMap.containsKey(nickname))
                return false;
            MoneyResponse moneyRes = this.updateMoney(nickname, amount, "vin", Consts.REFUND_RECHARGE_ERROR, "Hoan tien", "Hoan tien", fee, null, TransType.NO_VIPPOINT);
            if(!moneyRes.isSuccess()){
                return false;
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

	@Override
	public UserAdminModel getUserAdminByNickName(String nickName) throws SQLException {
        return userDao.getUserAdminByNickName(nickName);
	}
	@Override
	public MoneyResponse updateMoneyFromAdmin(String nickname, long money, String moneyType, String actionName,
			String serviceName, String description, long fee,  boolean playGame) {
		logger.debug("Request updateMoneyUser:  nickname: " + nickname + ", money: " + money + ", moneyType: "
				+ moneyType + ", actionName: " + actionName + ", serviceName: " + serviceName + ", description: "
				+ description + ", fee: " + fee );
		TelegramAlert.SendMessage("[THÔNG BÁO] CỘNG TRỪ TIỀN USER ADMIN BACKEND ,AMOUNT=" + money + " , ACTIONNAME=" + actionName
				+ " , SERVICENAME=" + serviceName +", nickname="+nickname);
		MoneyResponse response = new MoneyResponse(false, "1001");
		HazelcastInstance client = HazelcastClientFactory.getInstance();
		if (client == null) {
			MoneyLogger.log(nickname, actionName, money, fee, moneyType, serviceName, "1030", "can not connect hazelcast");
			response.setErrorCode("1030");
			return response;
		}
		IMap<String, UserModel> userMap = client.getMap("users");
		if (userMap.containsKey(nickname)) {
			try {
				userMap.lock(nickname);
				UserCacheModel user = (UserCacheModel) userMap.get(nickname);
				long moneyUser = user.getMoney(moneyType);
				long currentMoney = user.getCurrentMoney(moneyType);
				if (money != 0L) {
					if (moneyUser + money >= 0L) {
						TransactionContext context = client.newTransactionContext(new TransactionOptions().setTransactionType(TransactionOptions.TransactionType.ONE_PHASE));
						context.beginTransaction();
						try {
							user.setMoney(moneyType, moneyUser += money);
							user.setCurrentMoney(moneyType, currentMoney += money);
							int vp = 0;
							int moneyVPs = 0;
							MoneyMessageInMinigame message = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(),
									user.getId(), nickname, actionName, moneyUser, currentMoney, money, moneyType, fee, moneyVPs, vp);
							LogMoneyUserMessage messageLog = new LogMoneyUserMessage(user.getId(), nickname, actionName,
									serviceName, currentMoney, money, moneyType, description, fee, playGame, user.isBot());
							RMQApi.publishMessagePayment(message, 16);
							
							if(!user.isBot()) {
								RMQApi.publishMessageLogMoney(messageLog);
							}
							userMap.put(nickname, user);
							context.commitTransaction();
							response.setSuccess(true);
							response.setErrorCode("0");
						} catch (Exception e) {
							context.rollbackTransaction();
							MoneyLogger.log(nickname, actionName, money, fee, moneyType, serviceName, "1031", "error rmq: " + e.getMessage());
							response.setErrorCode("1031");
						}
					} else {
						MoneyLogger.log(nickname, actionName, money, fee, moneyType, serviceName, "1002", "khong du tien");
						response.setErrorCode("1002");
					}
				}
				response.setCurrentMoney(currentMoney);
			} catch (Exception e2) {
				logger.info(e2);
				MoneyLogger.log(nickname, actionName, money, fee, moneyType, serviceName, "1030", "error hazelcast: " + e2.getMessage());
				response.setErrorCode("1030");
			} finally {
				userMap.unlock(nickname);
			}
		}
		logger.debug("Response updateMoneyUser:" + response.toJson());
		return response;
	}

	@Override
	public boolean updateUserType(String nickName, int usertype) throws SQLException {
		String sql = "update users set usertype =? where nick_name=? ";
		try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname")) {
			PreparedStatement stm = conn.prepareStatement(sql);
			stm.setInt(1, usertype);
			stm.setString(2, nickName);
			int count = stm.executeUpdate();
			return count > 0;
		} catch (Exception e) {
			logger.debug(e);
		}
		return false;
	}
}

