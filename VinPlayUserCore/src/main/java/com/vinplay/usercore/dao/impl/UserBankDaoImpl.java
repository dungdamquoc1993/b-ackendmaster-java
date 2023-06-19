package com.vinplay.usercore.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.vinplay.payment.entities.UserBank;
import com.vinplay.usercore.dao.UserBankDao;
import com.vinplay.vbee.common.pools.ConnectionPool;

public class UserBankDaoImpl implements UserBankDao {

	private static final Logger logger = Logger.getLogger(UserBankDaoImpl.class);

	/**
	 * Add bank info of user
	 * @param userBank
	 * @return id object
	 */
	@Override
	public long add(UserBank userBank) throws SQLException {
		long id = 0;
		Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
        CallableStatement call = null;
        ResultSet rs =null;
        try {
            call = conn.prepareCall("CALL usersbank_insert(?,?,?,?,?,?,?)");
            int param = 1;
            call.setLong(param++, userBank.getUserId());
            call.setString(param++, userBank.getNickName());
            call.setString(param++, userBank.getBankName());
            call.setString(param++, userBank.getCustomerName());
            call.setString(param++, userBank.getBankNumber());
            call.setInt(param++, userBank.getStatus());
            call.setString(param++, userBank.getBranch());
            rs = call.executeQuery();
            if (rs.next()) {
            	id = rs.getLong("id");
            }

        }
        catch (SQLException e) {
        	UserBankDaoImpl.logger.error(e);
            throw e;
        }
        finally {
			if (rs != null) {
				rs.close();
			}
            if (call != null) {
                call.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
		return id;
	}

	/**
	 * Update bank info of user
	 * @param userBank
	 * @return true: update success; false: update failure
	 */
	@Override
	public boolean update(UserBank userBank) throws SQLException {
		boolean result = false;
		Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
        CallableStatement call = null;
        try {
            call = conn.prepareCall("CALL usersbank_update(?,?,?,?,?,?,?,?,?)");
            int param = 1;
            call.setLong(param++, userBank.getId());
            call.setLong(param++, userBank.getUserId());
            call.setString(param++, userBank.getNickName());
            call.setString(param++, userBank.getBankName());
            call.setString(param++, userBank.getCustomerName());
            call.setString(param++, userBank.getBankNumber());
            call.setInt(param++, userBank.getStatus());
            call.setString(param++, userBank.getBranch());
            call.setString(param++, userBank.getLastEditor());
            call.executeUpdate();
            result = true;
        }
        catch (SQLException e) {
        	UserBankDaoImpl.logger.error(e);
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

	/**
	 * Update bank info of user
	 * @param id
	 * @return true: delete success; false: delete failure
	 */
	@Override
	public boolean delete(long id) throws SQLException {
		boolean result = false;
		Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
        CallableStatement call = null;
        try {
            call = conn.prepareCall("CALL usersbank_deletebyid(?)");
            int param = 1;
            call.setLong(param++, id);
            call.executeUpdate();
            result = true;
        }
        catch (SQLException e) {
        	UserBankDaoImpl.logger.error(e);
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

	private UserBank parseToUserModel(ResultSet rs) {
		UserBank userBank = new UserBank();
		try {
			userBank.setId(rs.getLong("id"));
			userBank.setUserId(rs.getInt("user_id"));
			userBank.setNickName(rs.getString("nick_name"));
			userBank.setBankName(rs.getString("bank_name"));
			userBank.setCustomerName(rs.getString("customer_name"));
			userBank.setBankNumber(rs.getString("bank_number"));
			userBank.setStatus(rs.getInt("status"));
			userBank.setCreateDate(rs.getTimestamp("create_date"));
			userBank.setBranch(rs.getString("branch"));
			userBank.setUpdateDate(rs.getTimestamp("update_date"));
            userBank.setLastEditor(rs.getString("last_editor"));
			return userBank;
		}catch (Exception e) {
			UserBankDaoImpl.logger.error(e);
			return null;
		}
	}

	/**
	 * Get bank info of user by id
	 * @param id
	 * @return UserBank
	 */
	@Override
	public UserBank getById(Long id) throws SQLException {
		UserBank user = null;
        CallableStatement call = null;
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
        int param = 1;
        try{
        	call = conn.prepareCall("CALL usersbank_getbyid(?)");
        	call.setLong(param++, id);
            ResultSet rs = call.executeQuery();
            if (rs.next()) {
                user = parseToUserModel((ResultSet)rs);
            }
            rs.close();
        }
        catch (SQLException e) {
        	UserBankDaoImpl.logger.error(e);
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
        return user;
    }

	/**
	 * Get bank info of user by bank number
	 * @param nickname
	 * @param bankNumber
	 * @return UserBank
	 */
	@Override
	public UserBank getByBankNumber(String nickname, String bankNumber) throws SQLException {
		UserBank user = null;
        CallableStatement call = null;
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
        int param = 1;
        try{
        	call = conn.prepareCall("CALL usersbank_getbybanknumber(?,?)");
        	call.setString(param++, nickname);
        	call.setString(param++, bankNumber);
            ResultSet rs = call.executeQuery();
            if (rs.next()) {
                user = parseToUserModel((ResultSet)rs);
            }
            rs.close();
        }
        catch (SQLException e) {
        	UserBankDaoImpl.logger.error(e);
            return null;
        }
        finally {
            if (call != null) {
                call.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return user;
    }

	/**
	 * Get count bank info of user by nick name
	 * @param bankNumber
	 * @return UserBank
	 */
	@Override
	public int getCountByNickName(String nickName) throws SQLException {
		int count = 0;
        CallableStatement call = null;
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
        int param = 1;
        try{
        	call = conn.prepareCall("CALL usersbank_count(?)");
        	call.setString(param++, nickName);
            ResultSet rs = call.executeQuery();
            if (rs.next()) {
            	count = rs.getInt("count");
            }

            rs.close();
        }
        catch (SQLException e) {
        	UserBankDaoImpl.logger.error(e);
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
        return count;
    }

	/**
	 * Get list bank info of user by nick name and customer name bank
	 * @param nickName
	 * @param customerName
	 * @return UserBank
	 */
	@Override
	public List<UserBank> getByCustomerName(String nickName, String customerName) throws SQLException {
		List<UserBank> userBanks = new ArrayList<UserBank>();
        CallableStatement call = null;
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
        int param = 1;
        try{
        	call = conn.prepareCall("CALL usersbank_getbycustomename(?,?)");
        	call.setString(param++, nickName);
        	call.setString(param++, customerName);
            ResultSet rs = call.executeQuery();
            while (rs.next()) {
            	UserBank user = null;
                user = parseToUserModel((ResultSet)rs);
                if(user != null)
                	userBanks.add(user);
            }
            rs.close();
        }
        catch (SQLException e) {
        	UserBankDaoImpl.logger.error(e);
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
        return userBanks;
	}

	/**
	 * Search user bank
	 * @param nickName
	 * @param bankName
	 * @param bankNumber
	 * @param isAdmin : 0-Play user;1-Admin
	 * @param pageNumber : page index
	 * @param limit : record number per page
	 * @return List<UserBank>
	 * @throws SQLException
	 */
	@Override
	public List<UserBank> search(String nickName, String bankName, String bankNumber, int isAdmin, int pageNumber, int limit) throws SQLException {
	    String customerName = "";
		return search(nickName, customerName, bankName, bankNumber, isAdmin, pageNumber, limit);
	}

    @Override
    public List<UserBank> search(String nickName, String customerName, String bankName, String bankNumber, int isAdmin, int pageNumber, int limit) throws SQLException {
        List<UserBank> userBanks = new ArrayList<UserBank>();
        CallableStatement call = null;
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
        int param = 1;
        try{
            call = conn.prepareCall("CALL usersbank_search_1(?,?,?,?,?,?,?)");
            call.setInt(param++, pageNumber);
            call.setInt(param++, limit);
            call.setString(param++, nickName);
            call.setString(param++, customerName);
            call.setString(param++, bankName);
            call.setString(param++, bankNumber);
            call.setInt(param++, isAdmin);
            ResultSet rs = call.executeQuery();
            while (rs.next()) {
                UserBank user = null;
                user = parseToUserModel((ResultSet)rs);
                if(user != null)
                    userBanks.add(user);
            }
            rs.close();
        }
        catch (SQLException e) {
            UserBankDaoImpl.logger.error(e);
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
        return userBanks;
    }

	/**
	 * Get count search user bank
	 * @param nickName
	 * @param bankName
	 * @param bankNumber
	 * @param isAdmin : 0-Play user;1-Admin
     * @return int
	 * @throws SQLException
	 */
	public int search_count(String nickName, String bankName, String bankNumber, int isAdmin) throws SQLException {
        String customerName = "";
        return search_count(nickName, customerName, bankName, bankNumber, isAdmin);
    }

    @Override
    public int search_count(String nickName, String customerName, String bankName, String bankNumber, int isAdmin) throws SQLException {
        int count = 0;
        CallableStatement call = null;
        Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
        int param = 1;
        try{
            call = conn.prepareCall("CALL usersbank_search_count_1(?,?,?,?,?)");
            call.setString(param++, nickName);
            call.setString(param++, customerName);
            call.setString(param++, bankName);
            call.setString(param++, bankNumber);
            call.setInt(param++, isAdmin);
            ResultSet rs = call.executeQuery();
            if (rs.next()) {
                count = rs.getInt("count");
            }

            rs.close();
        }
        catch (SQLException e) {
            UserBankDaoImpl.logger.error(e);
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

        return count;
    }

	@Override
	public boolean isAddedBank(String nickName) {
		long count = 0;
		String sql = "SELECT count(*)  as cnt from users_bank WHERE nick_name=? ";
		try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");) {
			PreparedStatement stm = conn.prepareStatement(sql);
			int param = 1;
			stm.setString(param++, nickName);
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
	public boolean isExistBankNumber(String bankNumber) throws SQLException{
		long count = 0;
		String sql = "SELECT count(*) as cnt FROM vinplay.users_bank where 1 =1 and bank_number=?";
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");) {
            PreparedStatement stm = conn.prepareStatement(sql);
            if (bankNumber != null) {
                stm.setString(1, bankNumber);
            }

            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                count = rs.getInt("cnt");
            }

            return count >0;
        }
	}
}
