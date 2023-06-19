package com.vinplay.vbee.dao.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.vinplay.vbee.common.messages.minigame.pokego.LogPokeGoMessage;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.dao.GalaxyDAO;
import org.bson.Document;

import java.util.Date;

public class GalaxyGoDaoImpl implements GalaxyDAO {
    @Override
    public void log(LogPokeGoMessage message) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection col = db.getCollection("log_GALAXY");
        Document doc = new Document();
        doc.append("reference_id", (Object)message.referenceId);
        doc.append("user_name", (Object)message.username);
        doc.append("bet_value", (Object)message.betValue);
        doc.append("lines_betting", (Object)message.linesBetting);
        doc.append("lines_win", (Object)message.linesWin);
        doc.append("prizes_on_line", (Object)message.prizesOnLine);
        doc.append("prize", (Object)message.totalPrizes);
        doc.append("result", (Object)message.result);
        doc.append("money_type", (Object)message.moneyType);
        doc.append("time_log", (Object)message.time);
        doc.append("create_time", (Object)new Date());
        col.insertOne((Object)doc);
    }
}
