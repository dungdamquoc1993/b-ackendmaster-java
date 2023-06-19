package com.vinplay.vbee.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import com.vinplay.vbee.common.messages.minigame.baucua.ResultBauCuaMsg;
import com.vinplay.vbee.common.messages.minigame.baucua.ToiChonCaMsg;
import com.vinplay.vbee.common.messages.minigame.baucua.TransactionBauCuaDetailMsg;
import com.vinplay.vbee.common.messages.minigame.baucua.TransactionBauCuaMsg;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.common.utils.CommonUtils;
import com.vinplay.vbee.dao.BauCuaDao;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.bson.Document;
import org.bson.conversions.Bson;

public class BauCuaDaoImpl
implements BauCuaDao {
    @Override
    public void saveTransactionBauCua(TransactionBauCuaMsg msg) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeLog = df.format(new Date());
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection col = db.getCollection("bau_cua_transaction");
        Document doc = new Document();
        doc.append("reference_id", msg.referenceId);
        doc.append("user_name", msg.username);
        doc.append("room", msg.room);
        doc.append("dices", msg.dices);
        doc.append("bet_value", CommonUtils.arrayLongToString((long[])msg.betValues));
        doc.append("bet_bau", msg.betValues[0]);
        doc.append("bet_cua", msg.betValues[1]);
        doc.append("bet_tom", msg.betValues[2]);
        doc.append("bet_ca", msg.betValues[3]);
        doc.append("bet_ga", msg.betValues[4]);
        doc.append("bet_huou", msg.betValues[5]);
        doc.append("prize", CommonUtils.arrayLongToString((long[])msg.prizes));
        doc.append("prize_bau", msg.prizes[0]);
        doc.append("prize_cua", msg.prizes[1]);
        doc.append("prize_tom", msg.prizes[2]);
        doc.append("prize_ca", msg.prizes[3]);
        doc.append("prize_ga", msg.prizes[4]);
        doc.append("prize_huou", msg.prizes[5]);
        doc.append("money_exchange", msg.totalExchange);
        doc.append("money_type", msg.moneyType);
        doc.append("time_log", timeLog);
        doc.append("create_time", new Date());
        col.insertOne(doc);
    }

    @Override
    public void saveTransactionBauCuaDetail(TransactionBauCuaDetailMsg msg) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeLog = df.format(new Date());
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection col = db.getCollection("bau_cua_transaction_detail");
        Document doc = new Document();
        doc.append("reference_id", msg.referenceId);
        doc.append("user_name", msg.username);
        doc.append("room", msg.room);
        doc.append("transaction_code", msg.transactionCode);
        doc.append("bet_value", CommonUtils.arrayLongToString((long[])msg.betValues));
        doc.append("bet_bau", msg.betValues[0]);
        doc.append("bet_cua", msg.betValues[1]);
        doc.append("bet_tom", msg.betValues[2]);
        doc.append("bet_ca", msg.betValues[3]);
        doc.append("bet_ga", msg.betValues[4]);
        doc.append("bet_huou", msg.betValues[5]);
        doc.append("money_type", msg.moneyType);
        doc.append("time_log", timeLog);
        doc.append("create_time", new Date());
        col.insertOne(doc);
    }

    @Override
    public void saveResultBauCua(ResultBauCuaMsg msg) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeLog = df.format(new Date());
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection col = db.getCollection("bau_cua_results");
        Document doc = new Document();
        doc.append("reference_id", msg.referenceId);
        doc.append("room", msg.room);
        doc.append("min_bet_value", msg.minBetValue);
        doc.append("dice1", msg.dices[0]);
        doc.append("dice2", msg.dices[1]);
        doc.append("dice3", msg.dices[2]);
        doc.append("x_pot", msg.xPot);
        doc.append("x_value", msg.xValue);
        doc.append("bet_value", CommonUtils.arrayLongToString((long[])msg.totalBetValues));
        doc.append("bet_bau", msg.totalBetValues[0]);
        doc.append("bet_cua", msg.totalBetValues[1]);
        doc.append("bet_tom", msg.totalBetValues[2]);
        doc.append("bet_ca", msg.totalBetValues[3]);
        doc.append("bet_ga", msg.totalBetValues[4]);
        doc.append("bet_huou", msg.totalBetValues[5]);
        doc.append("prize", CommonUtils.arrayLongToString((long[])msg.totalPrizes));
        doc.append("prize_bau", msg.totalPrizes[0]);
        doc.append("prize_cua", msg.totalPrizes[1]);
        doc.append("prize_tom", msg.totalPrizes[2]);
        doc.append("prize_ca", msg.totalPrizes[3]);
        doc.append("prize_ga", msg.totalPrizes[4]);
        doc.append("prize_huou", msg.totalPrizes[5]);
        doc.append("time_log", timeLog);
        doc.append("create_time", new Date());
        col.insertOne(doc);
    }

    @Override
    public void updateToiChonCa(ToiChonCaMsg msg) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeLog = df.format(new Date());
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection col = db.getCollection("bau_cua_toi_chon_ca");
        BasicDBObject conditions = new BasicDBObject();
        conditions.put("user_name", msg.username);
        BasicDBObject obj = new BasicDBObject();
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = df2.format(new Date());
        String startDate = String.valueOf(currentDate) + " 00:00:00";
        String endDate = String.valueOf(currentDate) + " 23:59:59";
        obj.put("$gte", startDate);
        obj.put("$lte", endDate);
        conditions.put("time_log", obj);
        BasicDBObject modiferObj = new BasicDBObject();
        Document docUpdated = new Document();
        docUpdated.append("user_name", msg.username);
        docUpdated.append("so_ca", msg.soCa);
        docUpdated.append("so_van", msg.soVan);
        docUpdated.append("tong_thang", msg.tongThang);
        docUpdated.append("tong_dat", msg.tongDat);
        docUpdated.append("current_phien", msg.currentPhien);
        docUpdated.append("list_phien", msg.listPhien);
        docUpdated.append("time_log", timeLog);
        docUpdated.append("create_time", new Date());
        modiferObj.put("$set", docUpdated);
        UpdateOptions options = new UpdateOptions();
        options.upsert(true);
        col.updateOne((Bson)conditions, (Bson)modiferObj, options);
    }
}

