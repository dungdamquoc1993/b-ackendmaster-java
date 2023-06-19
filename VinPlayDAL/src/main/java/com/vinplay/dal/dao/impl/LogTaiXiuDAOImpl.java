package com.vinplay.dal.dao.impl;

import com.vinplay.dal.dao.LogTaiXiuDAO;
import com.vinplay.vbee.common.pools.ConnectionPool;
import com.vinplay.vbee.common.response.TaiXiuDetailReponse;
import com.vinplay.vbee.common.response.TaiXiuResponse;
import com.vinplay.vbee.common.response.TaiXiuResultResponse;
import com.vinplay.vbee.common.utils.VinPlayUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class LogTaiXiuDAOImpl implements LogTaiXiuDAO {
	
	@Override
	public int deleteLogTaiXiuByDay(int soNgay) throws SQLException {
		long second = soNgay * 86400;
		
		try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");) {
			String sql = "DELETE FROM vinplay_minigame.result_tai_xiu where CURRENT_TIMESTAMP - timestamp > ?  ORDER BY id "
					+ "LIMIT 10000";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setLong(1, second * 14);//4 days
			int row = 1;
			while (row > 0) {
				row = stmt.executeUpdate();
			}
			stmt.close();
		}
		
		try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");) {
			String sql = "DELETE FROM vinplay_minigame.transaction_detail_tai_xiu where CURRENT_TIMESTAMP - timestamp > ? and user_id = 0 ORDER BY id "
					+ "LIMIT 10000";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setLong(1, second);
			int total=0;
			int row = 1;
			while (row > 0) {
				row = stmt.executeUpdate();
				total += row;
			}
			stmt.close();
			return total;
		}
	}
    @Override
    public List<TaiXiuResponse> listLogTaiXiu(String referentId, String userName, String betSide, String moneyType, String timeStart, String timeEnd, String isBot, int page) throws SQLException {
        ArrayList<TaiXiuResponse> results = new ArrayList<TaiXiuResponse>();
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");){
            int num_start = (page - 1) * 50;
            int num_end = 50;
            String condition = "";
            String limit = " ORDER BY reference_id DESC LIMIT " + num_start + ", " + num_end + "";
            if (referentId != null && !referentId.equals("")) {
                condition = condition + " AND reference_id=" + referentId;
            }
            if (userName != null && !userName.equals("")) {
                condition = condition + " AND user_name = '" + userName + "'";
            }
            if (betSide != null && !betSide.equals("")) {
                condition = condition + " AND bet_side=" + betSide;
            }
            if (moneyType != null && !moneyType.equals("")) {
                condition = condition + " AND money_type=" + moneyType;
            }
            if (timeStart != null && !timeStart.equals("") && timeEnd != null && !timeEnd.equals("")) {
                condition = condition + " AND timestamp BETWEEN '" + timeStart + "' AND '" + timeEnd + "'";
            }
            
            if (isBot != null &&  !isBot.equals("") && (isBot.equals("1") ||  isBot.equals("0"))) {
                condition = condition + " AND user_id = " + isBot;
            }
            String sql = "SELECT * FROM vinplay_minigame.transaction_tai_xiu where 1=1 " + condition + limit;
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TaiXiuResponse entry = new TaiXiuResponse();
                entry.referenceId = rs.getLong("reference_id");
                entry.user_id = rs.getInt("user_id");
                entry.user_name = rs.getString("user_name");
                entry.bet_value = rs.getInt("bet_value");
                entry.bet_side = rs.getInt("bet_side");
                entry.total_prize = rs.getLong("total_prize");
                entry.total_refund = rs.getLong("total_refund");
                entry.total_exchange = rs.getLong("total_exchange");
                entry.money_type = rs.getInt("money_type");
                entry.total_jackpot = rs.getLong("total_jackpot");
                entry.time_log = VinPlayUtils.formatDate((String)rs.getString("timestamp"));
                results.add(entry);
            }
            rs.close();
            stmt.close();
        }
        return results;
    }

    @Override
    public int countLogTaiXiu(String referentId, String userName, String betSide, String moneyType, String timeStart, String timeEnd,  String isBot) throws SQLException {
        int count = 0;
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");){
            String condition = "";
            if (referentId != null && !referentId.equals("")) {
                condition = condition + " AND reference_id=" + referentId;
            }
            if (userName != null && !userName.equals("")) {
                condition = condition + " AND user_name = '" + userName + "'";
            }
            if (betSide != null && !betSide.equals("")) {
                condition = condition + " AND bet_side=" + betSide;
            }
            if (moneyType != null && !moneyType.equals("")) {
                condition = condition + " AND money_type=" + moneyType;
            }
            if (timeStart != null && !timeStart.equals("") && timeEnd != null && !timeEnd.equals("")) {
                condition = condition + " AND timestamp BETWEEN '" + timeStart + "' AND '" + timeEnd + "'";
            }

            if (isBot != null &&  !isBot.equals("") && (isBot.equals("1") ||  isBot.equals("0"))) {
                condition = condition + " AND user_id = " + isBot;
            }
            String sql = "SELECT count(id) cnt FROM vinplay_minigame.transaction_tai_xiu where 1=1 " + condition;
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt("cnt");
            }
            rs.close();
            stmt.close();
        }
        return count;
    }

    @Override
    public Map<String, Integer> countPlayerLogTaiXiu(String referentId, String userName, String betSide, String moneyType, String timeStart, String timeEnd, String isBot) throws SQLException {
        Map<String, Integer> map = new HashMap<>();
        int count = 0;
        int countPlayer = 0;
        String condition = "";
        if (referentId != null && !referentId.equals("")) {
            condition = condition + " AND reference_id=" + referentId;
        }
        if (userName != null && !userName.equals("")) {
            condition = condition + " AND user_name = '" + userName + "'";
        }
        if (betSide != null && !betSide.equals("")) {
            condition = condition + " AND bet_side=" + betSide;
        }
        if (moneyType != null && !moneyType.equals("")) {
            condition = condition + " AND money_type=" + moneyType;
        }
        if (timeStart != null && !timeStart.equals("") && timeEnd != null && !timeEnd.equals("")) {
            condition = condition + " AND timestamp BETWEEN '" + timeStart + "' AND '" + timeEnd + "'";
        }

        if (isBot != null &&  !isBot.equals("") && (isBot.equals("1") ||  isBot.equals("0"))) {
            condition = condition + " AND user_id = " + isBot;
        }
        String sql = "SELECT count(user_name) as rc, count(distinct user_name) as pl FROM vinplay_minigame.transaction_tai_xiu where 1=1 " + condition;
        
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");){
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt("rc");
                countPlayer = rs.getInt("pl");
            }
            rs.close();
            stmt.close();
            map.put("totalRecord", count);
            map.put("totalPlayer", countPlayer);
        }
        return map;
    }
    private static final Logger logger = Logger.getLogger("backend");
    @Override
    public List<TaiXiuDetailReponse> getLogTaiXiuDetail(String referent_id, String betSide, String money_type, String nickName, int botType, int page) throws SQLException {
        List<TaiXiuDetailReponse> results = new ArrayList<TaiXiuDetailReponse>();
        int num_start = (page - 1) * 50;
        int num_end = 50;
        String condition = "";
        String limit = " ORDER BY reference_id DESC LIMIT " + num_start + ", " + 50 + "";
        if (referent_id != null && !referent_id.equals("")) {
            condition = condition + " AND reference_id=" + referent_id;
        }
        if (betSide != null && !betSide.equals("")) {
            condition = condition + " AND bet_side=" + betSide;
        }
        if (botType == 0 || botType == 1) {
            condition = condition + " AND user_id= " + botType;
        }
        if (nickName != null && !nickName.equals("")) {
            condition = condition + " AND user_name = '" + nickName + "'";
        }
        condition = condition + " AND money_type=" + money_type;
        String sql = "SELECT reference_id,transaction_code,user_id,user_name,bet_value,bet_side,prize,refund,input_time,money_type,timestamp, jackpot FROM transaction_detail_tai_xiu where 1=1 " + condition + limit;

        logger.debug("ListTaiXiuTransactionDetailNowProcessor sql: " + sql);

        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");
        		PreparedStatement stmt = conn.prepareStatement(sql);){
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TaiXiuDetailReponse entry = new TaiXiuDetailReponse();
                entry.reference_id = rs.getLong("reference_id");
                entry.transaction_code = rs.getString("transaction_code");
                entry.user_id = rs.getInt("user_id");
                entry.user_name = rs.getString("user_name");
                entry.bet_value = rs.getLong("bet_value");
                entry.bet_side = rs.getLong("bet_side");
                entry.prize = rs.getLong("prize");
                entry.refund = rs.getLong("refund");
                entry.input_time = rs.getInt("input_time");
                entry.money_type = rs.getInt("money_type");
                entry.time_log = rs.getString("timestamp");
                entry.jackpot =  rs.getLong("jackpot");
                results.add(entry);
            }
            rs.close();
        }
        return results;
    }

    @Override
    public int countLogTaiXiuDetail(String referent_id, String betSide, String money_type, String nickName, int botType) throws SQLException {
        int res = 0;
        String condition = "";
        if (referent_id != null && !referent_id.equals("")) {
            condition = condition + " AND reference_id=" + referent_id;
        }
        if (betSide != null && !betSide.equals("")) {
            condition = condition + " AND bet_side=" + betSide;
        }
        if (botType == 0 || botType == 1) {
            condition = condition + " AND user_id= " + botType;
        }
        if (nickName != null && !nickName.equals("")) {
            condition = condition + " AND user_name = '" + nickName + "'";
        }
        condition = condition + " AND money_type=" + money_type;
        String sql = "SELECT count(id) as total FROM transaction_detail_tai_xiu where 1=1 " + condition;
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");
        		PreparedStatement stmt = conn.prepareStatement(sql);){
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                res = rs.getInt("total");
            }
            rs.close();
        }
        return res;
    }

    @Override
    public List<TaiXiuResultResponse> listLogTaiXiuResult(String referentId, String moneyType, String timeStart, String timeEnd, int page) throws SQLException {
        List<TaiXiuResultResponse> result = new ArrayList<TaiXiuResultResponse>();
        String sql = "";
        String query = "";
        String condition = "";
        int num_start = (page - 1) * 50;
        int num_end = 50;
        String limit = " ORDER BY reference_id DESC LIMIT " + num_start + ", " + 50 + "";
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");){
            query = "SELECT reference_id,result,dice1,dice2,dice3,total_tai,total_xiu,num_bet_tai,num_bet_xiu,total_prize,total_refund_tai,total_refund_xiu,total_revenue,money_type,timestamp, total_jackpot from result_tai_xiu where 1=1 ";
            if (referentId != null && !referentId.equals("")) {
                condition = condition + " AND reference_id=" + referentId;
            }
            if (moneyType != null && !moneyType.equals("")) {
                condition = condition + " AND money_type=" + moneyType;
            }
            if (timeStart != null && !timeStart.equals("") && timeEnd != null && !timeEnd.equals("")) {
                condition = condition + " AND timestamp BETWEEN '" + timeStart + "' AND '" + timeEnd + "'";
            }
            sql = query + condition + limit;
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TaiXiuResultResponse taixiu = new TaiXiuResultResponse();
                taixiu.reference_id = rs.getLong("reference_id");
                taixiu.result = rs.getInt("result");
                taixiu.dice1 = rs.getInt("dice1");
                taixiu.dice2 = rs.getInt("dice2");
                taixiu.dice3 = rs.getInt("dice3");
                taixiu.total_tai = rs.getLong("total_tai");
                taixiu.total_xiu = rs.getLong("total_xiu");
                taixiu.num_bet_tai = rs.getInt("num_bet_tai");
                taixiu.num_bet_xiu = rs.getInt("num_bet_xiu");
                taixiu.total_prize = rs.getLong("total_prize");
                taixiu.total_refund_tai = rs.getLong("total_refund_tai");
                taixiu.total_refund_xiu = rs.getLong("total_refund_xiu");
                taixiu.total_revenue = rs.getLong("total_revenue");
                taixiu.money_type = rs.getInt("money_type");
                taixiu.timestamp = rs.getString("timestamp");
                taixiu.total_jackpot = rs.getLong("total_jackpot");
                result.add(taixiu);
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException e) {
            throw e;
        }
        return result;
    }

    @Override
    public int countLogTaiXiuResult(String referentId, String moneyType, String timeStart, String timeEnd) throws SQLException {
        String sql = "";
        String query = "";
        String condition = "";
        int res = 0;
        PreparedStatement stmt;
        ResultSet rs;
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");){
            query = "SELECT count(id) as total from result_tai_xiu where 1=1 ";
            if (referentId != null && !referentId.equals("")) {
                condition = condition + " AND reference_id=" + referentId;
            }
            if (moneyType != null && !moneyType.equals("")) {
                condition = condition + " AND money_type=" + moneyType;
            }
            if (timeStart != null && !timeStart.equals("") && timeEnd != null && !timeEnd.equals("")) {
                condition = condition + " AND timestamp BETWEEN '" + timeStart + "' AND '" + timeEnd + "'";
            }
            if ((rs = (stmt = conn.prepareStatement(sql = query + condition)).executeQuery()).next()) {
                res = rs.getInt("total");
            }
            rs.close();
            stmt.close();
        }
        catch (SQLException e) {
            throw e;
        }
        return res;
    }
}

