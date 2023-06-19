package com.vinplay.vbee.dao;

import com.vinplay.vbee.common.messages.statistic.LoginPortalInfoMsg;

public interface StatisticDao {
	public boolean saveLoginPortalInfo(LoginPortalInfoMsg var1);

	public boolean updateLastLogin(LoginPortalInfoMsg msg);
}
