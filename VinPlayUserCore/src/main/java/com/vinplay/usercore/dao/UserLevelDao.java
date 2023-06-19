package com.vinplay.usercore.dao;

import java.sql.SQLException;
import java.util.Map;
import com.vinplay.usercore.entities.UserLevel;

public interface UserLevelDao {
	public String insert(UserLevel userLevel) throws SQLException;
	
	public String Update(UserLevel userLevel) throws SQLException;
	
	public UserLevel getByNickName(String nickname, String parent_user) throws SQLException;
	
	public UserLevel getByNickName(String nickname) throws SQLException;
	
	public UserLevel getById(long id) throws SQLException;
	
	public Map<String, Object> findChilds(String nickname, String startDate, String endDate, int pageIndex, int limit)
			throws SQLException;
}
