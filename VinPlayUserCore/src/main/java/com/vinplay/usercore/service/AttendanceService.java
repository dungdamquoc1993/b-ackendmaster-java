package com.vinplay.usercore.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import com.vinplay.usercore.entities.AttendanceConfig;
import com.vinplay.usercore.entities.UserAttendance;

public interface AttendanceService {
	
	boolean checkIp(String nickName, String ip);
    
	public boolean createConfig(long money);
	
	public String createConfig(AttendanceConfig attendanceConfig) throws SQLException;
	
	public AttendanceConfig getConfigLastest();
	
	public AttendanceConfig getConfigLastestFromCached();
	
	public Map<String, Object> Attendance(String nickname,String ip);
	
	public Map<String, Object> CheckAttendance(String nickname,String ip);

	UserAttendance getAttendLastest(String nickname);
	
	public List<Map<String, Object>> historyInRound(String nickname) ;
	
	public Map<String, Object> search(Integer attendId, String nickname, String fromTime, String endTime, int pageIndex,
			int limit);
}

