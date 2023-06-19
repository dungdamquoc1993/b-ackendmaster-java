package com.vinplay.vbee.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.vinplay.vbee.common.messages.minigame.ThanhDuMessage;

import com.vinplay.vbee.common.messages.minigame.ResultTaiXiuMessage;
import com.vinplay.vbee.common.messages.minigame.TransactionTaiXiuDetailMessage;
import com.vinplay.vbee.common.messages.minigame.TransactionTaiXiuMessage;
import com.vinplay.vbee.common.messages.minigame.UpdateFundMessage;
import com.vinplay.vbee.common.messages.minigame.UpdatePotMessage;
import com.vinplay.vbee.common.pools.ConnectionPool;
import com.vinplay.vbee.dao.TaiXiuDao;

public class TaiXiuDaoImpl implements TaiXiuDao {
    @Override
    public boolean saveResultTaiXiu(ResultTaiXiuMessage message) throws SQLException {
        boolean success = false;
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");) {
            CallableStatement call = null;
            call = conn.prepareCall("{CALL save_result_tai_xiu(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?)}");
            int param = 1;
            call.setLong(param++, message.referenceId);
            call.setByte(param++, (byte)message.result);
            call.setByte(param++, (byte)message.dice1);
            call.setByte(param++, (byte)message.dice2);
            call.setByte(param++, (byte)message.dice3);
            call.setLong(param++, message.totalTai);
            call.setLong(param++, message.totalXiu);
            call.setInt(param++, message.numBetTai);
            call.setInt(param++, message.numBetXiu);
            call.setLong(param++, message.totalPrize);
            call.setLong(param++, message.totalRefundTai);
            call.setLong(param++, message.totalRefundXiu);
            call.setLong(param++, message.totalRevenue);
            call.setByte(param++, (byte)message.moneyType);
            call.setLong(param++, message.totalJackpot);
            success = call.execute();
            if (call != null) {
                call.close();
            }
            if (conn != null) {
                conn.close();
            }
        }catch (Exception e){

        }

        return success;
    }

    @Override
    public boolean saveTransactionTaiXiu(TransactionTaiXiuMessage message) throws SQLException {
        boolean success = false;
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");) {
            CallableStatement call = null;
            call = conn.prepareCall("CALL save_transaction_tai_xiu(?, ?, ?, ?, ?, ?, ?, ?, ?)");
            int param = 1;
            call.setLong(param++, message.referenceId);
            call.setInt(param++, message.userId);
            call.setString(param++, message.username);
            call.setLong(param++, message.betValue);
            call.setByte(param++, (byte) message.betSide);
            call.setLong(param++, message.prize);
            call.setLong(param++, message.refund);
            call.setByte(param++, (byte) message.moneyType);
            call.setLong(param++, message.jackpot);
            success = call.execute();
            if (call != null) {
                call.close();
            }
            if (conn != null) {
                conn.close();
            }
        }catch (Exception e){

        }
        return success;
    }

    @Override
    public boolean saveTransactionTaiXiuDetail(TransactionTaiXiuDetailMessage message) throws SQLException {
        boolean success = false;
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");) {
            CallableStatement call = null;
            call = conn.prepareCall("CALL save_transaction_detail_tai_xiu(?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?)");
            int param = 1;
            call.setLong(param++, message.referenceId);
            call.setString(param++, message.transactionCode);
            call.setInt(param++, message.userId);
            call.setString(param++, message.username);
            call.setLong(param++, message.betValue);
            call.setInt(param++, message.betSide);
            call.setLong(param++, message.prize);
            call.setLong(param++, message.refund);
            call.setInt(param++, message.inputTime);
            call.setByte(param++, (byte) message.moneyType);
            call.setLong(param++, message.jackpot);
            success = call.execute();
            if (call != null) {
                call.close();
            }
            if (conn != null) {
                conn.close();
            }
        }catch (Exception e){

        }
        return success;
    }

    @Override
    public boolean updateTransactionTaiXiuDetail(TransactionTaiXiuDetailMessage message) throws SQLException {
        boolean success = false;
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");) {
            CallableStatement call = null;
            call = conn.prepareCall("CALL update_transaction_tai_xiu_detail(?, ?, ?)");
            int param = 1;
            call.setString(param++, message.transactionCode);
            call.setLong(param++, message.prize);
            call.setLong(param++, message.refund);
            success = call.execute();
            if (call != null) {
                call.close();
            }
            if (conn != null) {
                conn.close();
            }
        }catch (Exception e){

        }
        return success;
    }
	
	@Override
    public boolean updateThanhDu(ThanhDuMessage message) throws SQLException {
        boolean success = false;
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");
        CallableStatement call = null;
        call = conn.prepareCall("CALL tx_update_thanh_du(?, ?, ?, ?, ?, ?)");
        int param = 1;
        call.setString(param++, message.getUsername());
        call.setInt(param++, message.getNumber());
        call.setLong(param++, message.getTotalValue());
        call.setLong(param++, message.getCurrentReferenceId());
        call.setString(param++, message.getReferences());
        call.setByte(param++, (byte)message.getType());
        success = call.execute();
        if (call != null) {
            call.close();
        }
        if (conn != null) {
            conn.close();
        }
        return success;
    }


    @Override
    public boolean updatePot(UpdatePotMessage message) throws SQLException {
        try{
            boolean success = false;
            Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");
            CallableStatement call = null;
            call = conn.prepareCall("CALL update_pot(?, ?)");
            int param = 1;
            call.setString(param++, message.potName);
            call.setLong(param++, message.newValue);
            success = call.execute();
            if (call != null) {
                call.close();
            }
            if (conn != null) {
                conn.close();
            }
            return success;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean updateFund(UpdateFundMessage message) throws SQLException {
        boolean success = false;
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");){
            CallableStatement call = null;
            call = conn.prepareCall("CALL update_fund(?, ?)");
            int param = 1;
            call.setString(param++, message.fundName);
            call.setLong(param++, message.newValue);
            success = call.execute();
            if (call != null) {
                call.close();
            }
            if (conn != null) {
                conn.close();
            }
        }catch (Exception e){

        }
        return success;
    }

    public int getTotalTrans() throws SQLException {
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");
        try{

            int res = 0;
            PreparedStatement stmt = null;
            ResultSet rs = null;

            String sql = "SELECT COUNT(*) AS total  FROM vinplay_minigame.transaction_tai_xiu;";
            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();
            if (rs.next()) {
                res = rs.getInt("total");
            }
            rs.close();
            stmt.close();
            conn.close();
            return res;
        }catch (Exception e){
            if (conn != null) {
                conn.close();
            }
            return 0;
        }
    }
    public int getTotalTransDetail() throws SQLException {
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");
        try{

            int res = 0;
            PreparedStatement stmt = null;
            ResultSet rs = null;

            String sql = "SELECT COUNT(*) AS total  FROM vinplay_minigame.transaction_detail_tai_xiu;";
            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();
            if (rs.next()) {
                res = rs.getInt("total");
            }
            rs.close();
            stmt.close();
            conn.close();
            return res;
        }catch (Exception e){
            if (conn != null) {
                conn.close();
            }
            return 0;
        }
    }
    public boolean deleteTopTrans(){
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");
        try{
            int totalUp = 0;
            PreparedStatement stmt = null;
            String sql = "DELETE FROM vinplay_minigame.transaction_tai_xiu WHERE id = (SELECT id FROM vinplay_minigame.transaction_tai_xiu order by id ASC limit 1);";
            stmt = conn.prepareStatement(sql);
            totalUp = stmt.executeUpdate();
            stmt.close();
            conn.close();
            return totalUp == 1;

        }catch (Exception e){

        }finally {
            try {
                conn.close();
            }catch (Exception e){

            }
        }
        return false;
    }
    public boolean deleteTopTransDetail(){
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_minigame");
        try{
            int totalUp = 0;

            PreparedStatement stmt = null;
            String sql = "DELETE FROM vinplay_minigame.transaction_detail_tai_xiu WHERE id = (SELECT id FROM vinplay_minigame.transaction_detail_tai_xiu order by id ASC limit 1);";
            stmt = conn.prepareStatement(sql);
            totalUp = stmt.executeUpdate();
            stmt.close();
            conn.close();
            return totalUp == 1;

        }catch (Exception e){

        }finally {
            try {
                conn.close();
            }catch (Exception e){

            }
        }
        return false;
    }
}

