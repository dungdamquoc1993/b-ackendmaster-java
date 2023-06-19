package com.vinplay.usercore.dao;

import java.sql.SQLException;
import java.util.Map;
import com.vinplay.usercore.entities.UserAttendance;

public interface UserAttendanceDao {

	public String insert(UserAttendance userAttendance) throws SQLException;
	
	public String delete(int id) throws SQLException;

	public UserAttendance getLastest(String nickname) throws SQLException;
	
	public UserAttendance getDetail(String nickname, int attendanceId, String date) throws SQLException;

	public Map<String, Object> search(Integer attendId, String nickname, String fromTime, String endTime, int pageIndex,
			int limit) throws SQLException;
	
	public Map<String, Object> search4BO(Integer attendId, String nickname, String fromTime, String endTime, int pageIndex,
			int limit) throws SQLException;
}
