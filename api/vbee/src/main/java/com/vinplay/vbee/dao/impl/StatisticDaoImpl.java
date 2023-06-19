/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.mongodb.client.MongoCollection
 *  com.mongodb.client.MongoDatabase
 *  com.vinplay.vbee.common.messages.statistic.LoginPortalInfoMsg
 *  com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory
 *  com.vinplay.vbee.common.utils.VinPlayUtils
 *  org.bson.Document
 */
package com.vinplay.vbee.dao.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.vinplay.vbee.common.messages.VippointMessage;
import com.vinplay.vbee.common.messages.statistic.LoginPortalInfoMsg;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.common.pools.ConnectionPool;
import com.vinplay.vbee.common.utils.VinPlayUtils;
import com.vinplay.vbee.dao.StatisticDao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bson.Document;

public class StatisticDaoImpl
implements StatisticDao {
    @Override
    public boolean saveLoginPortalInfo(LoginPortalInfoMsg msg) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection col = db.getCollection("user_login_info");
        Document doc = new Document();
        doc.append("user_id", (Object)msg.getUserId());
        doc.append("user_name", (Object)msg.getUsername());
        doc.append("nick_name", (Object)msg.getNickname());
        doc.append("ip", (Object)msg.getIp());
        doc.append("agent", (Object)msg.getAgent());
        doc.append("type", (Object)msg.getType());
        doc.append("time_log", (Object)VinPlayUtils.getCurrentDateTime());
        col.insertOne((Object)doc);
        return true;
    }
    
	@Override
	public boolean updateLastLogin(LoginPortalInfoMsg message) {
		boolean success = false;
		String sql = "update users set last_login = SYSDATE()  where nick_name =?";
		try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
				PreparedStatement stm = conn.prepareStatement(sql);) {
			stm.setString(1, message.getNickname());
			return stm.executeUpdate() == 1;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return success;
	}
}

