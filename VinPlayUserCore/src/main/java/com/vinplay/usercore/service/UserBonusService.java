package com.vinplay.usercore.service;

import java.sql.SQLException;
import java.util.List;

import com.vinplay.vbee.common.models.UserBonusModel;

public interface UserBonusService {
	void insertBonus(UserBonusModel model);
	
	boolean isReceivedBonus(String nickName , int typeBonus);

	boolean isSameIP(String ip,int bonusType);

	List<UserBonusModel> search(String nickName, int bonusType, int page, int totalrecord,String fromTime, String toTime) throws SQLException ;

	List<UserBonusModel> search(String nick_name,String ip,Integer bonusType,String fromTime,String endTime,int page,int maxItem) throws SQLException ;

	Long count(String nick_name,String ip,Integer bonusType,String fromTime,String endTime) throws SQLException;

	double sumAmount(String nick_name,String ip,Integer bonusType,String fromTime,String endTime) throws SQLException;
	
	public boolean checkConditionsByCurrentTime(String nickname);
	
	public boolean checkExit(String nickname, int bonusType);
}
