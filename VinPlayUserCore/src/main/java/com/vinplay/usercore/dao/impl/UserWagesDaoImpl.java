package com.vinplay.usercore.dao.impl;

import com.vinplay.usercore.dao.UserWagesDao;
import com.vinplay.usercore.entities.UserWages;
import com.vinplay.vbee.common.pools.ConnectionPool;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class UserWagesDaoImpl implements UserWagesDao {
	private static final Logger logger = Logger.getLogger(UserWagesDaoImpl.class);
	
	@Override
	public String insert(UserWages userWages) throws SQLException {
		String result = "failed";
		String sql = "INSERT INTO vinplay.user_wages (nick_name,created_at,bonus,status,parent_user) VALUE (?,?,?,?,?)";
		Connection conn = ConnectionPool.getInstance().getConnection(ConnectionPool.USER_POOL);
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			int param = 1;
			stmt.setString(param++, userWages.getNick_name());
			stmt.setString(param++, userWages.getCreated_at());
			stmt.setLong(param++, userWages.getBonus());
			stmt.setInt(param++, userWages.getStatus());
			stmt.setString(param++, userWages.getParent_user());
			int ex = stmt.executeUpdate();
			stmt.close();
			result = ex > 0 ? "success" : "failed";
		} catch (SQLException e) {
			e.printStackTrace();
			result = e.getMessage();
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return result;
	}
	
	@Override
	public boolean insertByJob(String date) throws SQLException {
		boolean result = false;
		Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
        CallableStatement call = null;
        ResultSet rs =null;
        try {
            call = conn.prepareCall("CALL user_wages_insert(?)");
            call.setString(1, date);
            rs = call.executeQuery();
            result = true;
        }
        catch (SQLException e) {
        	logger.error("Error user_wages insertByJob: " + e.getMessage());
        	
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
        
		return result;
	}

	@Override
	public String update(UserWages userWages) throws SQLException {
		String result = "failed";
		String sql = "UPDATE vinplay.user_wages set nick_name=?,bonus=?,status=?,parent_user=? where id=?";
		Connection conn = ConnectionPool.getInstance().getConnection(ConnectionPool.USER_POOL);
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			int param = 1;
			stmt.setString(param++, userWages.getNick_name());
			stmt.setLong(param++, userWages.getBonus());
			stmt.setInt(param++, userWages.getStatus());
			stmt.setLong(param++, userWages.getId());
			stmt.setString(param++, userWages.getParent_user());
			int ex = stmt.executeUpdate();
			stmt.close();
			result = ex > 0 ? "success" : "failed";
		} catch (SQLException e) {
			e.printStackTrace();
			result = e.getMessage();
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return result;
	}

	@Override
	public String updateStatus(long id, int status) throws SQLException {
		String result = "failed";
		String sql = "UPDATE vinplay.user_wages set status=? where id=?";
		Connection conn = ConnectionPool.getInstance().getConnection(ConnectionPool.USER_POOL);
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			int param = 1;
			stmt.setInt(param++, status);
			stmt.setLong(param++, id);
			int ex = stmt.executeUpdate();
			stmt.close();
			result = ex > 0 ? "success" : "failed";
		} catch (SQLException e) {
			e.printStackTrace();
			result = e.getMessage();
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return result;
	}
	
	@Override
	public String updateAllStatusToReceivedBonus(String nickname) throws SQLException {
		String result = "failed";
		String sql = "UPDATE vinplay.user_wages set status=1 where status=0 and parent_user=?";
		Connection conn = ConnectionPool.getInstance().getConnection(ConnectionPool.USER_POOL);
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			int param = 1;
			stmt.setString(param++, nickname);
			int ex = stmt.executeUpdate();
			stmt.close();
			result = ex > 0 ? "success" : "failed";
		} catch (SQLException e) {
			e.printStackTrace();
			result = e.getMessage();
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return result;
	}

	@Override
	public UserWages getById(long id) throws SQLException {
		String sql = "select * from user_wages where id = ?";
		Connection conn = ConnectionPool.getInstance().getConnection(ConnectionPool.USER_POOL);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		UserWages userWages = new UserWages();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				userWages = new UserWages(rs);
			}

			rs.close();
			stmt.close();
			return userWages;
		} catch (SQLException e) {
			e.printStackTrace();
			userWages = null;
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	@Override
	public UserWages getByDate(String date) throws SQLException {
		String sql = "select * from user_wages where date(create_date)=?";
		Connection conn = ConnectionPool.getInstance().getConnection(ConnectionPool.USER_POOL);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		UserWages userWages = new UserWages();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, date);
			rs = stmt.executeQuery();
			while (rs.next()) {
				userWages = new UserWages(rs);
			}

			rs.close();
			stmt.close();
			return userWages;
		} catch (SQLException e) {
			e.printStackTrace();
			userWages = null;
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	@Override
	public long getSumBonusByStatus(String nickname, int status) throws SQLException {
		long totalBonus = 0;
		String sql = "select sum(bonus) as totalBonus from user_wages where parent_user=? and status=?";
		Connection conn = ConnectionPool.getInstance().getConnection(ConnectionPool.USER_POOL);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, nickname);
			stmt.setInt(2, status);
			rs = stmt.executeQuery();
			while (rs.next()) {
				totalBonus = rs.getObject("totalBonus") == null ? 0 : rs.getInt("totalBonus");
			}

			rs.close();
			stmt.close();
			return totalBonus;
		} catch (SQLException e) {
			e.printStackTrace();
			totalBonus = 0;
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	@Override
	public Map<String, Object> history(String nickname, String startDate, String endDate, int status, int pageIndex,
			int limit) throws SQLException {
		Map<String, Object> map = new HashMap<>();
		pageIndex = pageIndex < 1 ? 0 : pageIndex - 1;
		String paginateCondition = (pageIndex == -1 || limit == -1) ? "" : (" limit ?,?");
		String statusCondition = status == -1 ? " status in (0,1) " : " status = " + status;
		String sql = "select * from user_wages where parent_user = ? and "
				+ statusCondition
				+ " and created_at >= ? and created_at <= ? order by bonus desc, created_at desc" + paginateCondition;
		String sqlCount = "select count(id) as total, sum(bonus) as totalBonus"
				+ " from user_wages where parent_user = ? and "
				+ statusCondition
				+ " and created_at >= ? and created_at <= ? order by bonus desc, created_at desc";
		Connection conn = ConnectionPool.getInstance().getConnection(ConnectionPool.USER_POOL);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmtCount = null;
		ResultSet rsCount = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmtCount = conn.prepareStatement(sqlCount);
			int param = 1;
			if (StringUtils.isBlank(nickname)) {
				stmt.setString(param, "(1=1)");
				stmtCount.setString(param, "(1=1)");
				param++;
			}
			else {
				stmt.setString(param, nickname);
				stmtCount.setString(param, nickname);
				param++;
			}

			if (StringUtils.isBlank(startDate)) {
				stmt.setString(param, "1900-01-01 00:00:00");
				stmtCount.setString(param, "1900-01-01 00:00:00");
				param++;
			}
			else {
				stmt.setString(param, startDate + " 00:00:00");
				stmtCount.setString(param, startDate + " 00:00:00");
				param++;
			}

			if (StringUtils.isBlank(endDate)) {
				stmt.setString(param, new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) + "23:59:59");
				stmtCount.setString(param, new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) + "23:59:59");
				param++;
			}
			else {
				stmt.setString(param, endDate + " 23:59:59");
				stmtCount.setString(param, endDate + " 23:59:59");
				param++;
			}
			
			if (-1 != pageIndex && -1 != limit) {
				stmt.setInt(param++, pageIndex * limit);
				stmt.setInt(param++, limit);
			}

			List<UserWages> userWages = new ArrayList<>();
			rs = stmt.executeQuery();
			while (rs.next()) {
				try {
					UserWages userWage = new UserWages(rs);
					userWages.add(userWage);
				} catch (Exception e) {
				}
			}

			map.put("userWages", userWages);
			rsCount = stmtCount.executeQuery();
			while (rsCount.next()) {
				map.put("totalRecord", rsCount.getObject("total") == null ? 0 : rsCount.getInt("total"));
				map.put("totalBonus", rsCount.getObject("totalBonus") == null ? 0 : rsCount.getLong("totalBonus"));
			}

			rs.close();
			stmt.close();
			rsCount.close();
			stmtCount.close();
			return map;
		} catch (SQLException e) {
			e.printStackTrace();
			map.put("userWages", new ArrayList<>());
			map.put("totalRecord", 0);
			map.put("totalBonus", 0);
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}
}
