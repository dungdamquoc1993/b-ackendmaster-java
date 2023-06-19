package com.vinplay.api.backend.processors.logSlotGame.logSlotModel;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.vinplay.vbee.common.enums.Games;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import org.bson.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LogSlotDAO {

    public List<LogSlotModel> getListSlot(String nickName, String transId, String bet_value,
                                          String timeStart, String timeEnd, int gameType, int page) {
        return getListSlot(nickName, transId, bet_value, timeStart, timeEnd, gameType, page, 50);
    }

    public List<LogSlotModel> getListSlot(String nickName, String transId, String bet_value,
                                          String timeStart, String timeEnd, int gameType, int page, int limit) {
        final ArrayList<LogSlotModel> results = new ArrayList<>();
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();

        BasicDBObject objsort = new BasicDBObject();
        objsort.put("_id", -1);

        int num_start = (page - 1) * limit;

        HashMap<String, Object> conditions = this.getCondition(nickName, transId, bet_value, timeStart, timeEnd);
        FindIterable iterable = db.getCollection(Games.findTableNameById(gameType)).
                find(new Document(conditions)).sort(objsort).skip(num_start).limit(limit);
        
        iterable.forEach((Block<Document>) document -> {
            LogSlotModel slotModel = new LogSlotModel();
            slotModel.reference_id = document.getLong("reference_id");
            slotModel.user_name = document.getString("user_name");
            slotModel.bet_value = document.getLong("bet_value");
            slotModel.line_betting = document.getString("lines_betting");
            slotModel.lines_win = document.getString("lines_win");
            slotModel.prizes_on_line = document.getString("prizes_on_line");
            slotModel.prize = document.getLong("prize");
            slotModel.result = document.getInteger("result");
            slotModel.time_log = document.getString("time_log");
            slotModel.create_time = document.getDate("create_time");
            results.add(slotModel);
        });
        return results;
    }

    public Map<String, Long> calculatorTotalCuocThang(String nickName, String transId, String bet_value,
                                                      String timeStart, String timeEnd, int gameType){
        Map<String, Long> map = new HashMap<>();
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();

        List<Long> num = new ArrayList<Long>();
        num.add(0, 0L);//number success
        num.add(1, 0L);//total bet

        HashMap<String, Object> conditions = this.getCondition(nickName, transId, bet_value, timeStart, timeEnd);
        FindIterable iterable = db.getCollection(Games.findTableNameById(gameType)).
                find(new Document(conditions));
        Set<String> set = new HashSet<>();
        iterable.forEach((Block<Document>) document -> {
            LogSlotModel slotModel = new LogSlotModel();
            slotModel.bet_value = document.getLong("bet_value");
            slotModel.line_betting = document.getString("lines_betting");
            slotModel.prize = document.getLong("prize");
            String line[] = slotModel.line_betting.trim().split(",");
            long cuoc = line.length * slotModel.bet_value;
            long thang = slotModel.prize;
            num.set(0, num.get(0) + cuoc);
            num.set(1, num.get(1) + thang);
            set.add(document.getString("user_name"));
        });
        map.put("tong_cuoc", num.get(0));
        map.put("tong_thang", num.get(1));
        map.put("tong_player", (long) set.size());
        return map;
    }

    public long countListSlot(String nickName, String transId, String bet_value,
                                          String timeStart, String timeEnd, int gameType) {
        MongoDatabase db = MongoDBConnectionFactory.getDBSlave();
        HashMap<String, Object> conditions = getCondition(nickName, transId, bet_value, timeStart, timeEnd);
        long count_iterable = db.getCollection(Games.findTableNameById(gameType)).
                count(new Document(conditions));

        return count_iterable;
    }

    private HashMap<String, Object> getCondition(String nickName, String transId, String bet_value, String timeStart, String timeEnd){
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        if (nickName != null && !nickName.equals("")) {
            conditions.put("user_name", nickName);
//            String pattern = ".*" + nickName + ".*";
//            conditions.put("user_name", new BasicDBObject().append("$regex", pattern).append("$options", "i"));
        }
        if (transId != null && !transId.equals("")) {
            try {
                Long reference_id = Long.parseLong(transId);
                conditions.put("reference_id", reference_id);
            }catch (NumberFormatException e){
            }
        }
        if (bet_value != null && !bet_value.equals("")) {
            try {
                Long betValue = Long.parseLong(bet_value);
                conditions.put("bet_value", betValue);
            }catch (NumberFormatException e){
            }
        }
        if (timeStart != null && !timeStart.equals("") && timeEnd != null && !timeEnd.equals("")) {
            try {
                SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss" );
                Date ts = sim.parse(timeStart);
                Date te = sim.parse(timeEnd);
                BasicDBObject object = new BasicDBObject();
                object.put("$gte", ts);
                object.put("$lte", te);
                conditions.put("create_time",  object);
            } catch (ParseException e) {
                try {
                    SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd" );
                    Date ts = sim.parse(timeStart);
                    Date te = sim.parse(timeEnd);
                    BasicDBObject object = new BasicDBObject();
                    object.put("$gte", ts);
                    object.put("$lte", te);
                    conditions.put("create_time",  object);
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
            }
        }
        return conditions;
    }
}
