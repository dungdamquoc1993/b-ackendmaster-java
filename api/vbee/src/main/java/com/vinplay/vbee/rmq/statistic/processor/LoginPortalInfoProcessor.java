package com.vinplay.vbee.rmq.statistic.processor;

import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.messages.BaseMessage;
import com.vinplay.vbee.common.messages.statistic.LoginPortalInfoMsg;
import com.vinplay.vbee.dao.StatisticDao;
import com.vinplay.vbee.dao.impl.StatisticDaoImpl;

public class LoginPortalInfoProcessor implements BaseProcessor<byte[], Boolean> {
	public Boolean execute(Param<byte[]> param) {
		LoginPortalInfoMsg msg = (LoginPortalInfoMsg) BaseMessage.fromBytes((byte[]) ((byte[]) param.get()));
		StatisticDao dao = new StatisticDaoImpl();
		dao.updateLastLogin(msg);
		return dao.saveLoginPortalInfo(msg);
	}
}
