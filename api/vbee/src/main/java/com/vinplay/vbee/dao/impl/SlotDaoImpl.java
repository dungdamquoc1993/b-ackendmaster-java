package com.vinplay.vbee.dao.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.vinplay.vbee.common.messages.minigame.LogNoHuSlotMessage;
import com.vinplay.vbee.common.messages.slot.LogSlotMachineMessage;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.dao.SlotDao;
import java.util.Date;
import org.bson.Document;

public class SlotDaoImpl implements SlotDao {
	@Override
	public void log(LogSlotMachineMessage message) {
		MongoDatabase db = MongoDBConnectionFactory.getDB();
		MongoCollection col = db.getCollection("log_" + message.gameName);
		Document doc = new Document();
		doc.append("reference_id", message.referenceId);
		doc.append("user_name", message.username);
		doc.append("bet_value", message.betValue);
		doc.append("lines_betting", message.linesBetting);
		doc.append("lines_win", message.linesWin);
		doc.append("prizes_on_line", message.prizesOnLine);
		doc.append("prize", message.totalPrizes);
		doc.append("result", message.result);
		doc.append("time_log", message.time);
		doc.append("create_time", new Date());
		col.insertOne(doc);
	}

	@Override
	public void logNoHu(LogNoHuSlotMessage message) {
		MongoDatabase db = MongoDBConnectionFactory.getDB();
		MongoCollection col = db.getCollection("log_no_hu_slot");
		Document doc = new Document();
		doc.append("reference_id", message.referenceId);
		doc.append("game_name", message.gameName);
		doc.append("nick_name", message.nickName);
		doc.append("bet_value", message.betValue);
		doc.append("lines_betting", message.linesBetting);
		doc.append("matrix", message.matrix);
		doc.append("lines_win", message.linesWin);
		doc.append("prizes_on_line", message.prizesOnLine);
		doc.append("prize", message.totalPrizes);
		doc.append("result", message.result);
		doc.append("time_log", message.time);
		doc.append("create_time", new Date());
		col.insertOne(doc);
	}
}
