/*
 * Decompiled with CFR 0.144.
 *
 * Could not load the following classes:
 *  com.mongodb.client.FindIterable
 *  com.mongodb.client.MongoCollection
 *  com.mongodb.client.MongoDatabase
 *  com.vinplay.vbee.common.models.UserModel
 *  com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory
 *  com.vinplay.vbee.common.pools.ConnectionPool
 *  com.vinplay.vbee.common.utils.VinPlayUtils
 *  org.bson.Document
 *  org.bson.conversions.Bson
 */
package com.vinplay.usercore.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.vinplay.usercore.dao.SecurityDao;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.common.pools.ConnectionPool;
import com.vinplay.vbee.common.utils.VinPlayUtils;

public class SecurityDaoImpl implements SecurityDao {
    @Override
    public boolean checkEmail(String email) throws SQLException {
        boolean res = false;
        String sql = "SELECT COUNT(*) as cnt FROM users WHERE email=?";
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");){
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, email);
            ResultSet rs = stm.executeQuery();
            if (rs.next() && rs.getInt("cnt") == 1) {
                res = true;
            }
            rs.close();
            stm.close();
        }
        return res;
    }

    @Override
    public boolean checkMobile(String mobile) throws SQLException {
        boolean res = false;
        String sql = "SELECT COUNT(*) as cnt FROM users WHERE mobile=?";
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");){
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, mobile);
            ResultSet rs = stm.executeQuery();
            if (rs.next() && rs.getInt("cnt") == 1) {
                res = true;
            }
            rs.close();
            stm.close();
        }
        return res;
    }

    @Override
    public boolean checkIdentification(String identification) throws SQLException {
        boolean res = false;
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");){
            String sql = "SELECT COUNT(*) as cnt FROM users WHERE identification=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, identification);
            ResultSet rs = stm.executeQuery();
            if (rs.next() && rs.getInt("cnt") == 1) {
                res = true;
            }
            rs.close();
            stm.close();
        }
        return res;
    }

    @Override
    public boolean updateUserInfo(int userId, String value, int type) throws SQLException {
        boolean res = false;
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
        CallableStatement call = null;
        try {
            call = conn.prepareCall("CALL update_user_info(?,?,?)");
            int param = 1;
            call.setInt(param++, userId);
            call.setString(param++, value);
            call.setInt(param++, type);
            res = call.executeUpdate() == 1;
        }
        catch (SQLException e) {
            throw e;
        }
        finally {
            if (call != null) {
                call.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return res;
    }

    @Override
    public boolean updateUserInfos(int userId, String identification, String email, String mobile) throws SQLException {
        boolean res = false;
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");){
            String sql = "UPDATE users SET identification=?, email=?, mobile=? WHERE id=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, identification);
            stm.setString(2, email);
            stm.setString(3, mobile);
            stm.setInt(4, userId);
            if (stm.executeUpdate() == 1) {
                res = true;
            }
            stm.close();
        }
        return res;
    }
    
    @Override
    public boolean updateUserInfos(int userId, String identification, String email, String birthday, int gender, String address, boolean status,String referralCode) throws SQLException {
        boolean res = false;
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");){
            String sql = "UPDATE users SET identification=?, email=?, birthday=?, gender=?, address=?, status=? , referral_code=?  WHERE id=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            Date date = Date.valueOf("1900-01-01");
            try {
            	date = Date.valueOf(birthday);
            }catch (Exception e) {
            	date = Date.valueOf("1900-01-01");
			}
            stm.setString(1, identification);
            stm.setString(2, email);
            stm.setDate(3, date);
            stm.setBoolean(4, gender == 0 ? false : true);
            stm.setString(5, address);
            stm.setBoolean(6, status);
            stm.setString(7, referralCode);
            stm.setInt(8, userId);
            if (stm.executeUpdate() == 1) {
                res = true;
            }
            stm.close();
        }
        return res;
    }

    @Override
    public boolean updateClient(int userId, String client) throws SQLException {
        boolean res = false;
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");){
            String sql = "UPDATE users SET client=? WHERE id=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, client);
            stm.setInt(2, userId);
            if (stm.executeUpdate() == 1) {
                res = true;
            }
            stm.close();
        }
        return res;
    }

    @Override
    public boolean updateReferralCode(int userId, String refCode) throws SQLException {
		if (refCode == null || !isExistAgentCode(refCode)) {
			refCode = "roy88";
		}
        boolean res = false;
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");){
            String sql = "UPDATE users SET referral_code=? WHERE id=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, refCode);
            stm.setInt(2, userId);
            if (stm.executeUpdate() == 1) {
                res = true;
            }
            stm.close();
        }
        return res;
    }
    
	private boolean isExistAgentCode(String referralCode) {
		String sql = "select count(*) as cnt from  useragent where code=? ";
		try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_admin");
				PreparedStatement stm = conn.prepareStatement(sql);) {
			stm.setString(1, referralCode);
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				int cnt = rs.getInt("cnt");
				return cnt > 0;
			}
			stm.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

    @Override
    public boolean updateUserVipInfo(int userId, String birthday, boolean gender, String address) throws SQLException, ParseException {
        boolean res = false;
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");){
            String sql = "UPDATE users SET birthday=?, gender=?, address=? WHERE id=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setDate(1, new Date(VinPlayUtils.getDateTimeFromDate((String)birthday).getTime()));
            stm.setBoolean(2, gender);
            stm.setString(3, address);
            stm.setInt(4, userId);
            if (stm.executeUpdate() == 1) {
                res = true;
            }
            stm.close();
        }
        return res;
    }

    @Override
    public UserModel getStatus(String nickname) throws SQLException {
        UserModel user = null;
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");){
            String sql = "SELECT status,id FROM users WHERE nick_name=?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, nickname);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                user = new UserModel();
                user.setStatus(rs.getInt("status"));
                user.setId(rs.getInt("id"));
            }
            rs.close();
            stm.close();
        }
        return user;
    }

    @Override
    public boolean updateInfoLogin(int userId, String username, String nickname, String ip, String agent, int type) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection col = db.getCollection("user_login_info");
        Document doc = new Document();
        doc.append("user_id", (Object)userId);
        doc.append("user_name", (Object)username);
        doc.append("nick_name", (Object)nickname);
        doc.append("ip", (Object)ip);
        doc.append("agent", (Object)agent);
        doc.append("type", (Object)type);
        doc.append("time_log", (Object)VinPlayUtils.getCurrentDateTime());
        col.insertOne((Object)doc);
        return true;
    }
}

