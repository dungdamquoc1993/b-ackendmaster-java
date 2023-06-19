package com.vinplay.usercore.dao.impl;

import com.vinplay.usercore.dao.AttendanceConfigDao;
import com.vinplay.usercore.entities.AttendanceConfig;
import com.vinplay.vbee.common.pools.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

public class AttendanceConfigDaoImpl implements AttendanceConfigDao {

	@Override
	public String insert(String startDate, long money) throws SQLException {
		try {
			if (StringUtils.isBlank(startDate))
				return "Ngày bắt đầu chu kỳ không được để trắng";

			if (0 == money)
				return "Số tiền không được để trắng";

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date end = DateUtils.addDays(simpleDateFormat.parse(startDate + " 00:00:00"), 7);
			AttendanceConfig attendanceConfig = new AttendanceConfig();
			attendanceConfig.setStart_date(startDate);
			attendanceConfig.setEnd_date(simpleDateFormat.format(end));
			attendanceConfig.setMoney(money);
			attendanceConfig.setCreate_at(simpleDateFormat.format(new Date()));
			return insert(attendanceConfig);
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@Override
	public String insert(AttendanceConfig attendanceConfig) throws SQLException {
		String result = "failed";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		if (StringUtils.isBlank(attendanceConfig.getStart_date()))
			return "Ngày bắt đầu chu kỳ không được để trắng";

		if (StringUtils.isBlank(attendanceConfig.getEnd_date()))
			return "Ngày kết thúc chu kỳ không được để trắng";

		if (0 == attendanceConfig.getMoney())
			return "Số tiền không được để trắng";

		if (StringUtils.isBlank(attendanceConfig.getCreate_at()))
			attendanceConfig.setCreate_at(simpleDateFormat.format(new Date()));

		String sql = "INSERT INTO vinplay.attendance_config (start_date,end_date,money,create_at) VALUE (?,?,?,?)";
		Connection conn = ConnectionPool.getInstance().getConnection(ConnectionPool.USER_POOL);
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			int param = 1;
			stmt.setString(param++, attendanceConfig.getStart_date());
			stmt.setString(param++, attendanceConfig.getEnd_date());
			stmt.setLong(param++, attendanceConfig.getMoney());
			stmt.setString(param, attendanceConfig.getCreate_at());
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
	public AttendanceConfig getLastest() throws SQLException {
		AttendanceConfig attendanceConfig = new AttendanceConfig();
		String sql = "select * from attendance_config order by start_date desc, end_date desc limit 1";
		Connection conn = ConnectionPool.getInstance().getConnection(ConnectionPool.USER_POOL);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				attendanceConfig.setId(rs.getInt("id"));
				attendanceConfig.setStart_date(rs.getString("start_date"));
				attendanceConfig.setEnd_date(rs.getString("end_date"));
				attendanceConfig.setMoney(rs.getLong("money"));
				attendanceConfig.setCreate_at(rs.getString("create_at"));
			}

			rs.close();
			stmt.close();
			return attendanceConfig.getId() < 1 ? null : attendanceConfig;
		} catch (SQLException e) {
			e.printStackTrace();
			attendanceConfig = null;
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	@Override
	public boolean isCheckSameIP() {
		String sql = "select count(*) as cnt from user_attendance where CAST(date_attend AS DATE)  =  CURRENT_DATE() and ip =? ";
		ResultSet rs = null;
		long count = 0;
		try (Connection conn = ConnectionPool.getInstance().getConnection(ConnectionPool.USER_POOL);
				PreparedStatement stmt = conn.prepareStatement(sql);) {
			rs = stmt.executeQuery();
            while (rs.next()) {
                count = rs.getLong("cnt");
            }
			rs.close();
			stmt.close();
			return count >0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
