package com.vinplay.usercore.dao;

import java.sql.SQLException;
import com.vinplay.usercore.entities.AttendanceConfig;

public interface AttendanceConfigDao {

	public String insert(String startDate, long money) throws SQLException;

	public String insert(AttendanceConfig attendanceConfig) throws SQLException;

	public AttendanceConfig getLastest() throws SQLException;
	
	boolean isCheckSameIP();
}
