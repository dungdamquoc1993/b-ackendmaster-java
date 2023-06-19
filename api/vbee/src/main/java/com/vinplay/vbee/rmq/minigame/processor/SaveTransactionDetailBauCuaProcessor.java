package com.vinplay.vbee.rmq.minigame.processor;

import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.messages.BaseMessage;
import com.vinplay.vbee.common.messages.minigame.baucua.TransactionBauCuaDetailMsg;
import com.vinplay.vbee.dao.impl.BauCuaDaoImpl;

public class SaveTransactionDetailBauCuaProcessor implements BaseProcessor<byte[], Boolean> {
	public Boolean execute(Param<byte[]> param) {
		byte[] body = (byte[]) param.get();
		TransactionBauCuaDetailMsg message = (TransactionBauCuaDetailMsg) BaseMessage.fromBytes((byte[]) body);
		BauCuaDaoImpl dao = new BauCuaDaoImpl();
		dao.saveTransactionBauCuaDetail(message);
		return true;
	}
}
