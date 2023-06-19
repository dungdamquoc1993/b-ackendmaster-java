package com.vinplay.vbee.dao;

import java.sql.SQLException;

import com.vinplay.vbee.common.messages.minigame.ResultTaiXiuMessage;
import com.vinplay.vbee.common.messages.minigame.TransactionTaiXiuDetailMessage;
import com.vinplay.vbee.common.messages.minigame.TransactionTaiXiuMessage;
import com.vinplay.vbee.common.messages.minigame.UpdateFundMessage;
import com.vinplay.vbee.common.messages.minigame.UpdatePotMessage;
import com.vinplay.vbee.common.messages.minigame.ThanhDuMessage;

public interface TaiXiuDao {
	public boolean saveResultTaiXiu(ResultTaiXiuMessage var1) throws SQLException;

	public boolean saveTransactionTaiXiu(TransactionTaiXiuMessage var1) throws SQLException;

	public boolean saveTransactionTaiXiuDetail(TransactionTaiXiuDetailMessage var1) throws SQLException;

	public boolean updateTransactionTaiXiuDetail(TransactionTaiXiuDetailMessage var1) throws SQLException;

	public boolean updatePot(UpdatePotMessage var1) throws SQLException;
	
	public boolean updateThanhDu(ThanhDuMessage var1) throws SQLException;

	public boolean updateFund(UpdateFundMessage var1) throws SQLException;
}
