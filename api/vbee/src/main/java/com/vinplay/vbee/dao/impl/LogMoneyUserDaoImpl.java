/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.mongodb.BasicDBObject
 *  com.mongodb.client.FindIterable
 *  com.mongodb.client.MongoCollection
 *  com.mongodb.client.MongoDatabase
 *  com.mongodb.client.model.FindOneAndUpdateOptions
 *  com.vinplay.vbee.common.messages.LogChuyenTienDaiLyMessage
 *  com.vinplay.vbee.common.messages.LogMoneyUserMessage
 *  com.vinplay.vbee.common.messages.gamebai.LogNoHuGameBaiMessage
 *  com.vinplay.vbee.common.messages.pay.ExchangeMessage
 *  com.vinplay.vbee.common.models.TopUserPlayGame
 *  com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory
 *  com.vinplay.vbee.common.pools.ConnectionPool
 *  com.vinplay.vbee.common.utils.DateTimeUtils
 *  org.bson.Document
 *  org.bson.conversions.Bson
 */
package com.vinplay.vbee.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.vinplay.vbee.common.messages.LogChuyenTienDaiLyMessage;
import com.vinplay.vbee.common.messages.LogMoneyUserMessage;
import com.vinplay.vbee.common.messages.gamebai.LogNoHuGameBaiMessage;
import com.vinplay.vbee.common.messages.pay.ExchangeMessage;
import com.vinplay.vbee.common.models.TopUserPlayGame;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.common.pools.ConnectionPool;
import com.vinplay.vbee.common.statics.MongoCollections;
import com.vinplay.vbee.common.utils.DateTimeUtils;
import com.vinplay.vbee.dao.LogMoneyUserDao;

public class LogMoneyUserDaoImpl implements LogMoneyUserDao {
    @Override
    public boolean saveLogMoneyUser(LogMoneyUserMessage message, long transId, boolean isBot, boolean playGame) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection col = null;
        if (message.getMoneyType().equals("vin")) {
            col = db.getCollection(MongoCollections.LOG_MONEY_USER_VIN);
        } else if (message.getMoneyType().equals("xu")) {
            col = db.getCollection("log_money_user_xu");
        }
        Document doc = new Document();
        doc.append("trans_id", transId);
        doc.append("user_id", message.getUserId());
        doc.append("nick_name", message.getNickname());
        doc.append("service_name", message.getServiceName());
        doc.append("current_money", message.getCurrentMoney());
        doc.append("money_exchange", message.getMoneyExchange());
        doc.append("description", message.getDescription());
        doc.append("trans_time", message.getCreateTime());
        doc.append("action_name", message.getActionName());
        doc.append("fee", message.getFee());
        doc.append("is_bot", isBot);
        doc.append("play_game", playGame);
        doc.append("create_time", new Date());
        col.insertOne(doc);
        return true;
    }

    @Override
    public boolean saveLogMoneyUserVinOther(LogMoneyUserMessage message, long transId, int type) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
		MongoCollection col = type == 3 ? db.getCollection(MongoCollections.LOG_MONEY_USER_NAP_VIN)
				: db.getCollection(MongoCollections.LOG_MONEY_USER_TIEU_VIN);
		Document doc = new Document();
        doc.append("trans_id", transId);
        doc.append("user_id", message.getUserId());
        doc.append("nick_name", message.getNickname());
        doc.append("service_name", message.getServiceName());
        doc.append("current_money", message.getCurrentMoney());
        doc.append("money_exchange", message.getMoneyExchange());
        doc.append("description", message.getDescription());
        doc.append("trans_time", message.getCreateTime());
        doc.append("action_name", message.getActionName());
        doc.append("fee", message.getFee());
        doc.append("create_time", new Date());
        col.insertOne(doc);
        return true;
    }

    @Override
    public long getLastReferenceId(String moneyType) {
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        BasicDBObject objsort = new BasicDBObject();
        objsort.put("_id", -1);
        FindIterable iterable = null;
        if (moneyType.equals("vin")) {
            iterable = db.getCollection(MongoCollections.LOG_MONEY_USER_VIN).find().sort(objsort).limit(1);
        } else if (moneyType.equals("xu")) {
            iterable = db.getCollection("log_money_user_xu").find().sort(objsort).limit(1);
        }
        Document document = iterable != null ? (Document)iterable.first() : null;
        long transId = document == null ? 0L : document.getLong("trans_id");
        return transId;
    }

    @Override
    public void saveLogMoneySystem(String name, long currentMoney, long moneyExchange, String transTime) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection col = db.getCollection("log_money_system");
        Document doc = new Document();
        doc.append("name", name);
        doc.append("current_money", currentMoney);
        doc.append("money_exchange", moneyExchange);
        doc.append("trans_time", transTime);
        doc.append("create_time", new Date());
        col.insertOne(doc);
    }

    @Override
    public boolean logTopUserPlayGame(TopUserPlayGame userWin) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection col = null;
        col = userWin.getMoneyType().equals("vin") ? db.getCollection("top_user_play_game_vin") : db.getCollection("top_user_play_game_xu");
        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("money_win", userWin.getMoneyWin());
        BasicDBObject conditions = new BasicDBObject();
        conditions.append("nick_name", userWin.getNickname());
        conditions.append("money_type", userWin.getMoneyType());
        conditions.append("date", userWin.getDate());
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.upsert(true);
        col.findOneAndUpdate(conditions, new Document("$inc", updateFields), options);
        return true;
    }


    @Override
    public boolean logNoHuGameBai(LogNoHuGameBaiMessage message) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection col = db.getCollection("log_no_hu_game_bai");
        Document doc = new Document();
        doc.append("nick_name", message.getNickname());
        doc.append("room", message.getRoom());
        doc.append("pot_value", message.getPotValue());
        doc.append("money_win", message.getMoneyWin());
        doc.append("game_name", message.getGamename());
        doc.append("description", message.getDescription());
        doc.append("trans_time", message.getCreateTime());
        doc.append("create_time", new Date());
        doc.append("tour_id", message.getTourId());
        col.insertOne(doc);
        return true;
    }

	@Override
	public boolean checkBot(String nickname) throws SQLException {
		boolean res = false;
		String sql = "SELECT is_bot FROM users WHERE nick_name=?";
		try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
				PreparedStatement stm = conn.prepareStatement(sql);) {
			stm.setString(1, nickname);
			ResultSet rs = stm.executeQuery();
			if (rs.next() && rs.getInt("is_bot") == 1) {
				res = true;
			}
			rs.close();
		}
		return res;
	}

    @Override
    public boolean logExchangeMoney(ExchangeMessage message) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection col = db.getCollection("log_exchange_money");
        Document doc = new Document();
        doc.append("nick_name", message.nickname);
        doc.append("merchant_id", message.merchantId);
        doc.append("trans_id", message.merchantTransId);
        doc.append("money", message.money);
        doc.append("money_type", message.moneyType);
        doc.append("type", message.type);
        doc.append("money_exchange", message.exchangeMoney);
        doc.append("fee", message.fee);
        doc.append("code", message.code);
        doc.append("ip", message.ip);
        doc.append("trans_time", message.getCreateTime());
        doc.append("create_time", new Date());
        col.insertOne(doc);
        return true;
    }
}

