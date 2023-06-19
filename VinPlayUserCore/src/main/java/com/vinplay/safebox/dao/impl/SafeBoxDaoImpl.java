package com.vinplay.safebox.dao.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.vinplay.safebox.dao.SafeBoxDaoService;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import org.bson.Document;

public class SafeBoxDaoImpl implements SafeBoxDaoService {
    @Override
    public boolean depositSafeBox(String userName, double amount) {
        Document conditions = new Document();
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        conditions.put("user_name", userName);
        MongoCollection<Document> col = db.getCollection("safe_box");
        Document dc = col.find(conditions).first();
        if (dc != null) {
            double amountEnd = dc.getDouble("amount") + amount;
            Document doc = new Document();
            doc.append("user_name", userName);
            doc.append("amount", amountEnd);
            col.updateOne(conditions, new Document("$set", doc));
        } else {
            Document doc = new Document();
            doc.append("user_name", userName);
            doc.append("amount", amount);
            col.insertOne(doc);
        }
        return true;
    }

    @Override
    public double getSafeBox(String userName) {
        Document conditions = new Document();
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        conditions.put("user_name", userName);
        MongoCollection<Document> col = db.getCollection("safe_box");
        Document dc = col.find(conditions).first();
        if (dc != null)
            return dc.getDouble("amount");

        return 0;
    }

    @Override
    public boolean withDraw(String userName, double amount) {
        Document conditions = new Document();
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        conditions.put("user_name", userName);
        MongoCollection<Document> col = db.getCollection("safe_box");
        Document dc = col.find(conditions).first();
        if (dc != null) {
            double amountEnd = dc.getDouble("amount") - amount;
            if (amountEnd < 0)
                return false;
            Document doc = new Document();
            doc.append("user_name", userName);
            doc.append("amount", amountEnd);
            col.updateOne(conditions, new Document("$set", doc));
        }
        return false;
    }
}
