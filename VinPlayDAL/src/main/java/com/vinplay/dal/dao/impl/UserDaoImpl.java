/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.mongodb.BasicDBObject
 *  com.mongodb.Block
 *  com.mongodb.client.FindIterable
 *  com.mongodb.client.MongoCollection
 *  com.mongodb.client.MongoDatabase
 *  com.vinplay.vbee.common.models.UserModel
 *  com.vinplay.vbee.common.models.vippoint.UserVPEventModel
 *  com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory
 *  com.vinplay.vbee.common.pools.ConnectionPool
 *  com.vinplay.vbee.common.response.userMission.LogReceivedRewardObj
 *  com.vinplay.vbee.common.utils.DateTimeUtils
 *  com.vinplay.vbee.common.utils.UserUtil
 *  org.bson.Document
 *  org.bson.conversions.Bson
 */
package com.vinplay.dal.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.vinplay.dal.dao.UserDao;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.models.vippoint.UserVPEventModel;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.common.pools.ConnectionPool;
import com.vinplay.vbee.common.response.userMission.LogReceivedRewardObj;
import com.vinplay.vbee.common.utils.DateTimeUtils;
import com.vinplay.vbee.common.utils.UserUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.Document;
import org.bson.conversions.Bson;

public class UserDaoImpl
implements UserDao {
    @Override
    public UserModel getUserByNickName(String nickname) throws SQLException {
        UserModel user = null;
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");){
            String sql = "SELECT * FROM users WHERE nick_name=?";
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM users WHERE nick_name=?");
            stm.setString(1, nickname);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                user = UserUtil.parseResultSetToUserModel((ResultSet)rs);
            }
            rs.close();
            stm.close();
        }
        return user;
    }

    @Override
    public boolean updateRechargeMoney(String nickname, long money) throws SQLException {
        boolean res = false;
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");){
            String sql = "UPDATE users SET recharge_money=? WHERE nick_name=?";
            PreparedStatement stm = conn.prepareStatement("UPDATE users SET recharge_money=? WHERE nick_name=?");
            stm.setLong(1, money);
            stm.setString(2, nickname);
            if (stm.executeUpdate() == 1) {
                boolean bl = true;
                return bl;
            }
            stm.close();
        }
        return false;
    }

    @Override
    public UserVPEventModel getUserVPByNickName(String nickname) throws SQLException {
        UserVPEventModel user = new UserVPEventModel();
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");){
            String sql = "SELECT * FROM users_vp_event WHERE nick_name=?";
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM users_vp_event WHERE nick_name=?");
            stm.setString(1, nickname);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                user = UserUtil.parseResultSetToUserVPEventModel((ResultSet)rs);
            }
            rs.close();
            stm.close();
        }
        return user;
    }

    @Override
    public boolean updateUserMission(String nickName, String missionName, String moneyType, int matchWin) throws SQLException {
        boolean success = false;
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");){
            String tableName = "";
            tableName = moneyType.equals("vin") ? "user_mission_vin" : "user_mission_xu";
            String sql = " UPDATE " + tableName + " SET match_win = ?,      update_time = ?  WHERE nick_name = ?    AND mission_name = ? ";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, matchWin);
            stm.setString(2, DateTimeUtils.getCurrentTime());
            stm.setString(3, nickName);
            stm.setString(4, missionName);
            stm.executeUpdate();
        }
        return false;
    }

    @Override
    public Long countListUserRechargeInDay(Date date) throws SQLException {
        long count = 0;
        try(Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname")) {
            String sql = "Select count(*) as cnt from vinplay.log_count_user_play where time_report = ? and deposit > 0";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDate(1, date);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt("cnt");
            }
            stmt.close();
            return count;
        } catch (SQLException e) {
            return count;
        }
    }

    @Override
    public boolean checkUserBelongAgent(int user_id, String referral_code) throws SQLException {
        try(Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname")) {
            String sql = "Select count(*) as cnt from vinplay.users where users.id = ? and users.referral_code = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, user_id);
            stmt.setString(2, referral_code);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("cnt");
                if(count>0) return true;
            }
            stmt.close();
        } catch (SQLException e) {
        }
        return false;
    }

    @Override
    public List<LogReceivedRewardObj> getLogReceivedReward(String nickName, String gameName, String moneyType, String timeStart, String timeEnd, int page) throws SQLException {
        final ArrayList<LogReceivedRewardObj> results = new ArrayList<LogReceivedRewardObj>();
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        HashMap<String, Object> conditions = new HashMap<String, Object>();
//        HashMap<String, String> conditions = new HashMap<String, String>();
        BasicDBObject obj = new BasicDBObject();
        BasicDBObject objsort = new BasicDBObject();
        int numStart = (page - 1) * 50;
        int numEnd = 50;
        objsort.put("_id", -1);
        if (nickName != null && !nickName.equals("")) {
            conditions.put("nick_name", nickName);
        }
        if (gameName != null && !gameName.equals("")) {
            conditions.put("game_name", gameName);
        }
        if (moneyType != null && !moneyType.equals("")) {
            conditions.put("money_type", moneyType);
        }
        if (timeStart != null && !timeStart.equals("") && timeEnd != null && !timeEnd.equals("")) {
            obj.put("$gte", (Object)timeStart);
            obj.put("$lte", (Object)timeEnd);
            conditions.put("time_log", obj);
        }
        FindIterable iterable = db.getCollection("log_received_reward_mission").find((Bson)new Document(conditions)).sort((Bson)objsort).skip(numStart).limit(50);
        iterable.forEach((Block)new Block<Document>(){

            public void apply(Document document) {
                LogReceivedRewardObj obj = new LogReceivedRewardObj(document.getInteger((Object)"user_id", 0), document.getString((Object)"nick_name"), document.getString((Object)"game_name"), document.getInteger((Object)"level_received_reward", 0), document.getLong((Object)"money_bonus").longValue(), document.getLong((Object)"money_user").longValue(), document.getString((Object)"money_type"), document.getString((Object)"time_log"));
                results.add(obj);
            }
        });
        return results;
    }

}

