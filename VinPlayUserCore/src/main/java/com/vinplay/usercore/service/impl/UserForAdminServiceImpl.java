/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.hazelcast.core.HazelcastInstance
 *  com.hazelcast.core.IMap
 *  com.vinplay.vbee.common.hazelcast.HazelcastClientFactory
 *  com.vinplay.vbee.common.models.UserAdminInfo
 *  com.vinplay.vbee.common.models.UserModel
 *  com.vinplay.vbee.common.models.cache.UserCacheModel
 *  org.apache.log4j.Logger
 */
package com.vinplay.usercore.service.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.dal.entities.agent.DetailUserModel;
import com.vinplay.usercore.dao.SecurityDao;
import com.vinplay.usercore.dao.impl.SecurityDaoImpl;
import com.vinplay.usercore.dao.impl.UserDaoImpl;
import com.vinplay.usercore.service.UserForAdminService;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.models.UserAdminInfo;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.models.cache.UserCacheModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.vinplay.vbee.common.pools.ConnectionPool;
import org.apache.log4j.Logger;

public class UserForAdminServiceImpl
implements UserForAdminService {
    private static final Logger logger = Logger.getLogger((String)"user_core");

    @Override
    public UserModel getUserNormalByNickName(String nickName) throws SQLException {
        UserDaoImpl userDao = new UserDaoImpl();
        return userDao.getUserNormalByNickName(nickName);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean updateStatusDailyByNickName(String nickname, int status) throws SQLException {
        boolean res = false;
        UserDaoImpl userDao = new UserDaoImpl();
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, UserModel> userMap = client.getMap("users");
        if (userMap.containsKey((Object)nickname)) {
            try {
                 userMap.lock(nickname);
                UserCacheModel user = (UserCacheModel)userMap.get((Object)nickname);
                if (!userDao.updateStatusDailyByNickName(nickname, status)) return res;
                user.setDaily(status);
                userMap.put(nickname, user);
                res = true;
                return res;
            }
            catch (Exception e) {
                logger.debug((Object)e);
                return res;
            }
            finally {
                 userMap.unlock(nickname);
            }
        } else {
            if (!userDao.updateStatusDailyByNickName(nickname, status)) return res;
            return true;
        }
    }

    @Override
    public List<UserAdminInfo> searchUserAdmin(String userName, String nickName, String phone, String field, String sort, String daily, String timeStart, String timeEnd, int page, int totalrecord, String bot, String like, String email) throws SQLException {
        UserDaoImpl userDao = new UserDaoImpl();
        return userDao.searchUserAdmin(userName, nickName, phone, field, sort, daily, timeStart, timeEnd, page, totalrecord, bot, like, email);
    }

    @Override
    public int countSearchUserAdmin(String userName, String nickName, String phone, String field, String sort, String daily, String timeStart, String timeEnd, String bot) throws SQLException {
        UserDaoImpl userDao = new UserDaoImpl();
        return userDao.countSearchUserAdmin(userName, nickName, phone, field, sort, daily, timeStart, timeEnd, bot);
    }

    @Override
    public List<UserAdminInfo> searchUserAdmin(String userName, String nickName, String phone, String field, String sort, String daily, String timeStart, String timeEnd, int page, int totalrecord, String bot, String like, String email, String referral_code) throws SQLException {
        UserDaoImpl userDao = new UserDaoImpl();
        return userDao.searchUserAdmin(userName, nickName, phone, field, sort, daily, timeStart, timeEnd, page, totalrecord, bot, like, email, referral_code);
    }

    @Override
    public int countSearchUserAdmin(String userName, String nickName, String phone, String field, String sort, String daily, String timeStart, String timeEnd, String bot, String referral_code) throws SQLException {
        UserDaoImpl userDao = new UserDaoImpl();
        return userDao.countSearchUserAdmin(userName, nickName, phone, field, sort, daily, timeStart, timeEnd, bot, referral_code);
    }

    @Override
    public DetailUserModel searchDetailUser(String user_name, String nick_name) throws SQLException {
        DetailUserModel model = new DetailUserModel();
        try(Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname")) {
            String sql = "select * from vinplay.users where 1=1 "
                    +(user_name == null || user_name.isEmpty() ? "" : (" and user_name = ? "))
                    +(nick_name == null || nick_name.isEmpty() ? "" : (" and nick_name = ? "));

            PreparedStatement stmt = conn.prepareStatement(sql);
            int index = 1;
            if(!(user_name == null|| user_name.isEmpty())){
                stmt.setString(index++, user_name);
            }
            if(!(nick_name == null || nick_name.isEmpty())){
                stmt.setString(index, nick_name);
            }

            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                model.setUser_name(rs.getString("user_name"));
                model.setNick_name(rs.getString("nick_name"));
                model.setEmail(rs.getString("email"));
                model.setGoogle_id(rs.getString("google_id"));
                model.setFacebook_id(rs.getString("facebook_id"));
                model.setMobile(rs.getString("mobile"));
                model.setIdentification(rs.getString("identification"));
                model.setAvatar(rs.getInt("avatar"));
                model.setBirthday(rs.getDate("birthday"));
                model.setGender(rs.getBoolean("gender"));
                model.setAddress(rs.getString("address"));
                model.setVin(rs.getLong("vin"));
                model.setXu(rs.getLong("xu"));
                model.setVin_total(rs.getLong("vin_total"));
                model.setXu_total(rs.getLong("xu_total"));
                model.setSafe(rs.getLong("safe"));
                model.setRecharge_money(rs.getLong("recharge_money"));
                model.setVip_point(rs.getInt("vip_point"));
                model.setVip_point_save(rs.getInt("vip_point_save"));
                model.setMoney_vp(rs.getInt("money_vp"));
                model.setDai_ly(rs.getInt("dai_ly"));
                model.setStatus(rs.getInt("status"));
                model.setCreate_time(rs.getDate("create_time"));
                model.setSecurity_time(rs.getDate("security_time"));
                model.setLogin_otp(rs.getLong("login_otp"));
                model.setIs_bot(rs.getInt("is_bot"));
                model.setUpdate_pw_time(rs.getDate("update_pw_time"));
                model.setIs_verify_mobile(rs.getBoolean("is_verify_mobile"));
                model.setReferral_code(rs.getString("referral_code"));
                model.setT_nap(rs.getLong("t_nap"));
                model.setT_rut(rs.getLong("t_rut"));
                model.setNap_times(rs.getDate("rut_times"));
                model.setRut_times(rs.getDate("nap_times"));

                // tim so tong user neu dai_li = 1
                if(model.getDai_ly() == 1 && model.getUser_name()!=null && !model.getUser_name().trim().isEmpty()){
                    int countUser = 0;
                    String sql1 = "SELECT cnt FROM ( SELECT u.id, u.nick_name, u.user_name, u.create_time, ag.referral_code as refcode FROM vinplay.agency_code ag LEFT JOIN vinplay.users u ON u.id = ag.user_id )\n" +
                            "  ag  LEFT JOIN ( SELECT u.referral_code, count(*) AS cnt FROM vinplay.users u WHERE referral_code IS NOT NULL GROUP BY u.referral_code ) c ON ag.refcode = c.referral_code\n" +
                            "  where user_name = ?  ORDER BY cnt desc";
                    PreparedStatement stmt1 = conn.prepareStatement(sql1);
                    stmt1.setString(1, model.getUser_name());
                    ResultSet rs1 = stmt1.executeQuery();
                    if(rs1.next()){
                        countUser = rs1.getInt("cnt");
                    }

                    model.setTotalUserOfAgency(countUser);
                }else{
                    model.setTotalUserOfAgency(0);
                }

            }
            rs.close();
            stmt.close();
        }
        return model;
    }

    @Override
    public String changePasswordUser(String nick_name, String old_password, String new_password, String is_bot, String dai_ly, Boolean is_reset) throws SQLException {
        try(Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname")) {
            String sql = "select users.password from vinplay.users where 1=1 "
                    +(nick_name == null || nick_name.isEmpty() ? "" : " and nick_name = ? ")
                    +(is_bot == null || is_bot.isEmpty() ? "" : " and is_bot = ? ")
                    +(dai_ly == null || dai_ly.isEmpty() ? "" : " and dai_ly = ? ")
                    ;

            PreparedStatement stmt = conn.prepareStatement(sql);
            if(!(nick_name == null || nick_name.isEmpty())){
                stmt.setString(1, nick_name);
            }
            if(!(is_bot == null || is_bot.isEmpty())){
                stmt.setString(2, is_bot);
            }
            if(!(dai_ly == null || dai_ly.isEmpty())){
                stmt.setString(3, dai_ly);
            }

            String password = null;
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                password = rs.getString("password");
            }
            if(is_reset || (password!=null && !password.trim().isEmpty() && password.equals(old_password))){
                String sql1 = "UPDATE vinplay.users SET password = ? WHERE nick_name = ?";
                String newpass = is_reset ? "e10adc3949ba59abbe56e057f20f883e" : new_password;
                PreparedStatement stmt1 = conn.prepareStatement(sql1);
                stmt1.setString(1, newpass);
                stmt1.setString(2, nick_name);
                int rowAffected = -1;
                rowAffected = stmt1.executeUpdate();
                stmt1.close();
                if(rowAffected > 0){
                    HazelcastInstance client = HazelcastClientFactory.getInstance();
                    IMap<String, UserModel> userMap = client.getMap("users");
                    if (userMap.containsKey(nick_name)) {
                        try {
                            userMap.lock(nick_name);
                            UserCacheModel user = (UserCacheModel)userMap.get(nick_name);
                            user.setPassword(newpass);
                            userMap.put(nick_name, user);
                        }
                        catch (Exception e) {
                            logger.debug(("changePassword error: " + e.getMessage()));
                        }
                        finally {
                            userMap.unlock(nick_name);
                        }
                    }
                    return "success";
                }
                else
                    return "not_found";
            }
            rs.close();
            stmt.close();
            return "not_same";
        }
    }
}

