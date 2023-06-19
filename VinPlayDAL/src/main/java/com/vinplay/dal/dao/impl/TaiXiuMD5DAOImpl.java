/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.hazelcast.core.HazelcastInstance
 *  com.hazelcast.core.IMap
 *  com.mongodb.BasicDBObject
 *  com.mongodb.Block
 *  com.mongodb.client.AggregateIterable
 *  com.mongodb.client.FindIterable
 *  com.mongodb.client.MongoCollection
 *  com.mongodb.client.MongoDatabase
 *  com.vinplay.vbee.common.hazelcast.HazelcastClientFactory
 *  com.vinplay.vbee.common.models.cache.ReportModel
 *  com.vinplay.vbee.common.models.cache.ThanhDuTXModel
 *  com.vinplay.vbee.common.models.minigame.TopWin
 *  com.vinplay.vbee.common.models.minigame.taixiu.XepHangRLTLModel
 *  com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory
 *  com.vinplay.vbee.common.pools.ConnectionPool
 *  com.vinplay.vbee.common.utils.CommonUtils
 *  com.vinplay.vbee.common.utils.VinPlayUtils
 *  org.bson.Document
 *  org.bson.conversions.Bson
 */
package com.vinplay.dal.dao.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.dal.dao.TaiXiuDAO;
import com.vinplay.dal.entities.report.ReportMoneySystemModel;
import com.vinplay.dal.entities.taixiu.ResultTaiXiu;
import com.vinplay.dal.entities.taixiu.TransactionTaiXiu;
import com.vinplay.dal.entities.taixiu.TransactionTaiXiuDetail;
import com.vinplay.dal.entities.taixiu.VinhDanhRLTLModel;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.models.cache.ReportModel;
import com.vinplay.vbee.common.models.cache.ThanhDuTXModel;
import com.vinplay.vbee.common.models.minigame.TopWin;
import com.vinplay.vbee.common.models.minigame.taixiu.XepHangRLTLModel;
import com.vinplay.vbee.common.pools.ConnectionPool;
import com.vinplay.vbee.common.utils.CommonUtils;
import com.vinplay.vbee.common.utils.VinPlayUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TaiXiuMD5DAOImpl
implements TaiXiuDAO {
    @Override
    public List<ResultTaiXiu> getLichSuPhien(int number, int moneyType) throws SQLException {
        ArrayList<ResultTaiXiu> results = new ArrayList<ResultTaiXiu>();
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");
        try {
            String sql = "SELECT * FROM result_tai_xiu_md5 WHERE money_type=" + moneyType + " ORDER BY `timestamp` DESC LIMIT 0," + number;
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ResultTaiXiu entry = new ResultTaiXiu();
                entry.referenceId = rs.getLong("reference_id");
                entry.result = rs.getInt("result");
                entry.dice1 = rs.getInt("dice1");
                entry.dice2 = rs.getInt("dice2");
                entry.dice3 = rs.getInt("dice3");
                entry.totalTai = rs.getLong("total_tai");
                entry.totalXiu = rs.getLong("total_xiu");
                entry.numBetTai = rs.getInt("num_bet_tai");
                entry.numBetXiu = rs.getInt("num_bet_xiu");
                entry.totalPrize = rs.getLong("total_prize");
                entry.totalRefundTai = rs.getLong("total_refund_tai");
                entry.totalRefundXiu = rs.getLong("total_refund_xiu");
                entry.totalRevenue = rs.getLong("total_revenue");
                entry.moneyType = rs.getInt("money_type");
                Timestamp timestamp = rs.getTimestamp("timestamp");
                entry.timestamp = CommonUtils.convertTimestampToString((java.util.Date)timestamp);
                results.add(0, entry);
            }
            rs.close();
            stmt.close();
        }catch (SQLException e) {
            throw e;
        }
        finally {
            if (conn != null) {
                conn.close();
            }
        }
        return results;
    }

    @Override
    public List<TransactionTaiXiu> getLichSuGiaoDich(String nickname, int number, int moneyType) throws SQLException {
        ArrayList<TransactionTaiXiu> results = new ArrayList<TransactionTaiXiu>();
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");
        CallableStatement call = null;
        try {
            call = conn.prepareCall("CALL tx_get_lich_su_giao_dich_md5(?,?,?)");
            int param = 1;
            call.setString(param++, nickname);
            call.setInt(param++, number);
            call.setByte(param++, (byte)moneyType);
            ResultSet rs = call.executeQuery();
            while (rs.next()) {
                TransactionTaiXiu entry = new TransactionTaiXiu();
                entry.referenceId = rs.getLong("reference_id");
                entry.userId = rs.getInt("user_id");
                entry.username = rs.getString("user_name");
                entry.betValue = rs.getLong("bet_value");
                entry.betSide = rs.getInt("bet_side");
                entry.totalPrize = rs.getLong("total_prize");
                entry.totalRefund = rs.getLong("total_refund");
                Timestamp date = rs.getTimestamp("timestamp");
                entry.timestamp = CommonUtils.convertTimestampToString((java.util.Date)date);
                byte dice1 = rs.getByte("dice1");
                byte dice2 = rs.getByte("dice2");
                byte dice3 = rs.getByte("dice3");
                int total = dice1 + dice2 + dice3;
                entry.resultPhien = dice1 + " - " + dice2 + " - " + dice3 + "   " + total;
                entry.before_md5 = rs.getString("before_md5");
                entry.md5 = rs.getString("md5");
                results.add(entry);
            }
            rs.close();
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
        return results;
    }

    @Override
    public List<TopWin> getTopTaiXiu(int moneyType) throws SQLException {
        ArrayList<TopWin> result = new ArrayList<TopWin>();
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");
        CallableStatement call = null;
        try {
            call = conn.prepareCall("CALL tx_get_top_win_md5(?)");
            int param = 1;
            call.setByte(param++, (byte)moneyType);
            ResultSet rs = call.executeQuery();
            while (rs.next()) {
                TopWin entry = new TopWin();
                entry.setUsername(rs.getString("user_name"));
                entry.setMoney(rs.getLong("money"));
                result.add(entry);
            }
            rs.close();
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
        return result;
    }

    @Override
    public int countLichSuGiaoDichTX(String nickname, int moneyType) throws SQLException {
        int totalRecords = -1;
//        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");
//        CallableStatement call = null;
//        try {
//            call = conn.prepareCall("CALL tx_count_lich_su_giao_dich(?, ?,?)");
//            int param = 1;
//            call.setString(param++, nickname);
//            call.setByte(param++, (byte)moneyType);
//            call.registerOutParameter(param++, 4);
//            call.execute();
//            totalRecords = call.getInt(3);
//        }
//        catch (SQLException e) {
//            throw e;
//        }
//        finally {
//            if (call != null) {
//                call.close();
//            }
//            if (conn != null) {
//                conn.close();
//            }
//        }
        return totalRecords;
    }

    @Override
    public List<TransactionTaiXiuDetail> getChiTietPhien(long referenceId, int moneyType) throws SQLException {
        ArrayList<TransactionTaiXiuDetail> results = new ArrayList<TransactionTaiXiuDetail>();
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");
        CallableStatement call = null;
        try {
            call = conn.prepareCall("CALL tx_get_chi_tiet_phien_md5(?,?)");
            int param = 1;
            call.setLong(param++, referenceId);
            call.setByte(param++, (byte)moneyType);
            ResultSet rs = call.executeQuery();
            while (rs.next()) {
                TransactionTaiXiuDetail entry = new TransactionTaiXiuDetail();
                entry.referenceId = rs.getLong("reference_id");
                entry.userId = rs.getInt("user_id");
                entry.username = rs.getString("user_name");
                entry.betValue = rs.getLong("bet_value");
                entry.betSide = rs.getInt("bet_side");
                entry.prize = rs.getLong("prize");
                entry.refund = rs.getLong("refund");
                entry.inputTime = rs.getInt("input_time");
                entry.moneyType = rs.getByte("money_type");
                entry.timestamp = rs.getDate("timestamp");
                results.add(entry);
            }
            rs.close();
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
        return results;
    }

    @Override
    public ResultTaiXiu getKetQuaPhien(long referenceId, int moneyType) throws SQLException {
        ResultTaiXiu entry = null;
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");
        try {
            String sql = "SELECT * FROM result_tai_xiu_md5 WHERE reference_id=" + referenceId + " AND money_type=" + moneyType;
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                entry = new ResultTaiXiu();
                entry.referenceId = rs.getLong("reference_id");
                entry.result = rs.getInt("result");
                entry.dice1 = rs.getInt("dice1");
                entry.dice2 = rs.getInt("dice2");
                entry.dice3 = rs.getInt("dice3");
                entry.totalTai = rs.getLong("total_tai");
                entry.totalXiu = rs.getLong("total_xiu");
                entry.numBetTai = rs.getInt("num_bet_tai");
                entry.numBetXiu = rs.getInt("num_bet_xiu");
                entry.totalPrize = rs.getLong("total_prize");
                entry.totalRefundTai = rs.getLong("total_refund_tai");
                entry.totalRefundXiu = rs.getLong("total_refund_xiu");
                entry.totalRevenue = rs.getLong("total_revenue");
                entry.moneyType = rs.getInt("money_type");
                entry.before_md5 = rs.getString("before_md5");
                entry.md5 = rs.getString("md5");
                Timestamp timestamp = rs.getTimestamp("timestamp");
                entry.timestamp = CommonUtils.convertTimestampToString((java.util.Date)timestamp);
            }
            rs.close();
            stmt.close();
        }catch (SQLException e) {
            throw e;
        }
        finally {
            if (conn != null) {
                conn.close();
            }
        }
        return entry;
    }

    @Override
    public List<ThanhDuTXModel> getTopThanhDuDaily(String startTime, String endTime, short type) throws SQLException {
        ArrayList<ThanhDuTXModel> results = new ArrayList<ThanhDuTXModel>();
//        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");
//        CallableStatement call = null;
//        try {
//            call = conn.prepareCall("CALL tx_get_top_thanh_du(?, ?, ?)");
//            int param = 1;
//            call.setString(param++, startTime);
//            call.setString(param++, endTime);
//            call.setByte(param++, (byte)type);
//            ResultSet rs = call.executeQuery();
//            while (rs.next()) {
//                String username = rs.getString("user_name");
//                ThanhDuTXModel entry = new ThanhDuTXModel(username);
//                entry.number = rs.getInt("number");
//                entry.totalValue = rs.getLong("total_betting");
//                entry.currentReferenceId = rs.getLong("last_reference");
//                entry.parseReferences(rs.getString("references"));
//                results.add(entry);
//            }
//            rs.close();
//        }
//        catch (SQLException e) {
//            throw e;
//        }
//        finally {
//            if (call != null) {
//                call.close();
//            }
//            if (conn != null) {
//                conn.close();
//            }
//        }
        return results;
    }

    @Override
    public int getMaxThanhDu(String username, short type) throws SQLException {
        int max = 0;
//        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");){
//            String sql = "SELECT `number` FROM thanh_du WHERE user_name='" + username + "' AND `type`=" + type + " AND DATE(`last_update`)=CURDATE()";
//            PreparedStatement stmt = conn.prepareStatement(sql);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                max = rs.getInt("number");
//            }
//            rs.close();
//            stmt.close();
//        }
        return max;
    }

    

    @Override
    public ReportMoneySystemModel getReportTXToDay() {
        ReportMoneySystemModel res = new ReportMoneySystemModel();
        String today = VinPlayUtils.getCurrentDate();
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        IMap<String, ReportModel> reportMap = client.getMap("cacheReports");
        for (Map.Entry<String, ReportModel> entry : reportMap.entrySet()) {
            if (!((String)entry.getKey()).contains(today) || !((String)entry.getKey()).contains("TaiXiuMD5")) continue;
            ReportModel model = (ReportModel)entry.getValue();
            if (model.isBot) continue;
            ReportMoneySystemModel reportMoneySystemModel = res;
            reportMoneySystemModel.moneyWin += model.moneyWin;
            ReportMoneySystemModel reportMoneySystemModel2 = res;
            reportMoneySystemModel2.moneyLost += model.moneyLost;
            ReportMoneySystemModel reportMoneySystemModel3 = res;
            reportMoneySystemModel3.moneyOther += model.moneyOther;
            ReportMoneySystemModel reportMoneySystemModel4 = res;
            reportMoneySystemModel4.fee += model.fee;
            ReportMoneySystemModel reportMoneySystemModel5 = res;
            reportMoneySystemModel5.revenuePlayGame += model.moneyWin + model.moneyLost;
            ReportMoneySystemModel reportMoneySystemModel6 = res;
            reportMoneySystemModel6.revenue += model.moneyWin + model.moneyLost + model.moneyOther;
        }
        return res;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public ReportMoneySystemModel getReportTX(String startDate, String endDate) {
        ReportMoneySystemModel res = new ReportMoneySystemModel();
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");
        try {
            String sql = "SELECT SUM(money_win) as total_win, SUM(money_lost) as total_lost, SUM(money_other) as total_other, SUM(fee) as total_fee FROM vinplay.report_money_daily WHERE `date` >= '" + startDate + "?' and `date` <= '" + endDate + "' and action_name = 'TaiXiuMD5'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                res.moneyWin = rs.getLong("total_win");
                res.moneyLost = rs.getLong("total_lost");
                res.moneyOther = rs.getLong("total_other");
                res.fee = rs.getLong("total_fee");
            }
            res.revenuePlayGame = res.moneyWin + res.moneyLost;
            res.revenue = res.moneyWin + res.moneyLost + res.moneyOther;
            rs.close();
            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.close();
                }
                catch (SQLException e3) {
                    e3.printStackTrace();
                }
            }
        }
        finally {
            if (conn != null) {
                try {
                    conn.close();
                }
                catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return res;
    }

}

