/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.vinplay.vbee.common.pools.ConnectionPool
 */
package com.vinplay.dal.dao.impl;

import com.vinplay.dal.dao.GetUserIndexDAO;
import com.vinplay.vbee.common.pools.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetUserIndexDAOImpl
implements GetUserIndexDAO {
    @Override
    public int getRegister(String timeStart, String timeEnd, String refferal_code) throws SQLException {
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
        int res = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            sql = " SELECT COUNT(*) AS total  FROM vinplay.users  WHERE is_bot = 0 and dai_ly = 0 and `create_time` >= ? AND `create_time` <= ? " +
                    ((refferal_code==null|| refferal_code.isEmpty())? "": " AND refferal_code = ?");
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, timeStart);
            stmt.setString(2, timeEnd);
            if (!(refferal_code==null|| refferal_code.isEmpty())){
                stmt.setString(3, refferal_code);
            }
            rs = stmt.executeQuery();
            if (rs.next()) {
                res = rs.getInt("total");
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        finally {
            if (conn != null) {
                conn.close();
            }
        }
        return res;
    }

    @Override
    public int getRecharge(String timeStart, String timeEnd, String refferal_code) throws SQLException {
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
        int res = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            sql = " SELECT COUNT(*) AS total  FROM vinplay.users  WHERE is_bot = 0 and dai_ly = 0 and `create_time` >= ? AND `create_time` <= ? AND recharge_money > 0 " +
                    ((refferal_code==null|| refferal_code.isEmpty())? "": " and refferal_code = ?");
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, timeStart);
            stmt.setString(2, timeEnd);
            if (!(refferal_code==null|| refferal_code.isEmpty())){
                stmt.setString(3, refferal_code);
            }
            rs = stmt.executeQuery();
            if (rs.next()) {
                res = rs.getInt("total");
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        finally {
            if (conn != null) {
                conn.close();
            }
        }
        return res;
    }

    @Override
    public int getSecMobile(String timeStart, String timeEnd, String refferal_code) throws SQLException {
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
        int res = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            sql = " SELECT COUNT(*) AS total  FROM vinplay.users  WHERE is_bot = 0 and dai_ly = 0 and `create_time` >= ? AND `create_time` <= ? AND (`status` = 16) " +
                    ((refferal_code==null|| refferal_code.isEmpty())? "": " and refferal_code = ?");
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, timeStart);
            stmt.setString(2, timeEnd);
            if (!(refferal_code==null|| refferal_code.isEmpty())){
                stmt.setString(3, refferal_code);
            }
            rs = stmt.executeQuery();
            if (rs.next()) {
                res = rs.getInt("total");
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        finally {
            if (conn != null) {
                conn.close();
            }
        }
        return res;
    }

    @Override
    public int getBoth(String timeStart, String timeEnd, String refferal_code) throws SQLException {
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
        int res = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            sql = " SELECT COUNT(*) AS total  FROM vinplay.users  WHERE is_bot = 0 and dai_ly = 0 and `create_time` >= ? AND `create_time` <= ? AND recharge_money > 0 AND (`status` = 16) " +
                    ((refferal_code==null|| refferal_code.isEmpty())? "": " and refferal_code = ?");
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, timeStart);
            stmt.setString(2, timeEnd);
            if (!(refferal_code==null|| refferal_code.isEmpty())){
                stmt.setString(3, refferal_code);
            }
            rs = stmt.executeQuery();
            if (rs.next()) {
                res = rs.getInt("total");
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        finally {
            if (conn != null) {
                conn.close();
            }
        }
        return res;
    }
}

