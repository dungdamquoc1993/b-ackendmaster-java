/*
 * Decompiled with CFR 0.144.
 *
 * Could not load the following classes:
 *  com.mongodb.BasicDBObject
 *  com.mongodb.Block
 *  com.mongodb.client.AggregateIterable
 *  com.mongodb.client.FindIterable
 *  com.mongodb.client.MongoCollection
 *  com.mongodb.client.MongoDatabase
 *  com.mongodb.client.result.UpdateResult
 *  com.vinplay.vbee.common.exceptions.KeyNotFoundException
 *  com.vinplay.vbee.common.messages.dvt.RechargeByBankMessage
 *  com.vinplay.vbee.common.messages.dvt.RechargeByCardMessage
 *  com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory
 *  com.vinplay.vbee.common.pools.ConnectionPool
 *  com.vinplay.vbee.common.utils.VinPlayUtils
 *  org.bson.Document
 *  org.bson.conversions.Bson
 */
package com.vinplay.dichvuthe.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import com.vinplay.dichvuthe.dao.RechargeDao;
import com.vinplay.dichvuthe.entities.DepositBankModel;
import com.vinplay.dichvuthe.entities.DepositMomoModel;
import com.vinplay.dichvuthe.utils.DvtConst;
import com.vinplay.iap.lib.Purchase;
import com.vinplay.usercore.entities.LogRechargeBankNL;
import com.vinplay.usercore.entities.LogRechargeBankNapas;
import com.vinplay.usercore.response.LogRechargeBankNLResponse;
import com.vinplay.usercore.response.LogRechargeBankNapasResponse;
import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.vbee.common.exceptions.KeyNotFoundException;
import com.vinplay.vbee.common.messages.dvt.RechargeByBankMessage;
import com.vinplay.vbee.common.messages.dvt.RechargeByCardMessage;
import com.vinplay.vbee.common.mongodb.MongoDBConnectionFactory;
import com.vinplay.vbee.common.pools.ConnectionPool;
import com.vinplay.vbee.common.utils.VinPlayUtils;
import org.apache.log4j.Logger;
import org.bson.Document;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

public class RechargeDaoImpl implements RechargeDao {

    public static final Logger logger = Logger.getLogger("rechargeDao");

    @Override
    public List<RechargeByCardMessage> getListCardPending(String startTime, String endTime) throws NumberFormatException {
        final ArrayList<RechargeByCardMessage> results = new ArrayList<>();
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        Document conditions = new Document();
        conditions.put("code", 30);
        if (!startTime.isEmpty() && !endTime.isEmpty()) {
            BasicDBObject obj = new BasicDBObject();
            obj.put("$gte", startTime);
            obj.put("$lte", endTime);
            conditions.put("time_log", obj);
        }
        FindIterable<Document> iterable = db.getCollection("dvt_recharge_by_card").find(conditions);
        iterable.forEach((Block<Document>) document -> {
            RechargeByCardMessage message = new RechargeByCardMessage(document.getString("nick_name"), document.getString("reference_id"), document.getString("provider"), document.getString("serial"), document.getString("pin"), document.getInteger("amount"), document.getInteger("status"), document.getString("message"), document.getInteger("code"), 0, document.getString("time_log"), null, document.getString("partner"), document.getString("platform"), document.getString("user_mega"));
            results.add(message);
        });
        return results;
    }

    @Override
    public List<RechargeByCardMessage> getListCardPending() throws NumberFormatException, KeyNotFoundException {
        final ArrayList<RechargeByCardMessage> results = new ArrayList<>();
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        Document conditions = new Document();
        conditions.put("code", 30);
        conditions.put("time_log", new BasicDBObject("$gte", VinPlayUtils.parseDateTimeToString(VinPlayUtils.subtractDay(new Date(), GameCommon.getValueInt("TIME_RECHECK_RECHARGE")))));
        FindIterable<Document> iterable = db.getCollection("dvt_recharge_by_card").find(conditions);
        iterable.forEach((Block<Document>) document -> {
            RechargeByCardMessage message = new RechargeByCardMessage(document.getString("nick_name"), document.getString("reference_id"), document.getString("provider"), document.getString("serial"), document.getString("pin"), document.getInteger("amount"), document.getInteger("status"), document.getString("message"), document.getInteger("code"), 0, document.getString("time_log"), null, document.getString("partner"), document.getString("platform"), document.getString("user_mega"));
            results.add(message);
        });
        return results;
    }

    @Override
    public boolean updateSMS(String requestId, int code, String des, int smsType) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection<Document> col;
        col = smsType == 2 ? db.getCollection("dvt_recharge_by_sms_plus") : db.getCollection("dvt_recharge_by_sms");
        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("code", code);
        updateFields.append("description", des);
        col.updateOne(new Document("request_id", requestId), new Document("$set", updateFields));
        return true;
    }

    @Override
    public RechargeByCardMessage getPendingCardByReferenceId(String referenceId) {
        RechargeByCardMessage result = null;
        Document conditions = new Document();
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        conditions.put("reference_id", referenceId);
        Document dc = db.getCollection("dvt_recharge_by_card").find(conditions).first();
        if (dc != null) {
            result = new RechargeByCardMessage(dc.getString("nick_name"), dc.getString("reference_id"), dc.getString("provider"), dc.getString("serial"), dc.getString("pin"), dc.getInteger("amount"), dc.getInteger("status"), dc.getString("message"), dc.getInteger("code"), 0, dc.getString("time_log"), null, dc.getString("partner"), dc.getString("platform"), dc.getString("user_mega"));
        }
        return result;
    }

    @Override
    public boolean insertLogUpdateCardPending(String reference_id, String nick_name, String provider, String serial, String pin, String amount, String status, String message, String code, String time_log, String money, String actor) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection<Document> col = db.getCollection("log_update_card_pending");
        Document doc = new Document();
        doc.append("reference_id", reference_id);
        doc.append("nick_name", nick_name);
        doc.append("provider", provider);
        doc.append("serial", serial);
        doc.append("pin", pin);
        doc.append("amount", amount);
        doc.append("status", status);
        doc.append("message", message);
        doc.append("code", code);
        doc.append("time_log", time_log);
        doc.append("plus_money_log", VinPlayUtils.getCurrentDateTime());
        doc.append("money", money);
        doc.append("actor", actor);
        col.insertOne(doc);
        return true;
    }

    @Override
    public boolean updateCard(String id, int amount, int status, String message, int code) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("amount", amount);
        updateFields.append("status", status);
        updateFields.append("message", message);
        updateFields.append("code", code);
        updateFields.append("update_time", VinPlayUtils.getCurrentDateTime());
        db.getCollection("dvt_recharge_by_card").updateOne(new Document("reference_id", id), new Document("$set", updateFields));
        return true;
    }

    @Override
    public RechargeByBankMessage getRechargeByBank(String transId) {
        RechargeByBankMessage result = null;
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        Document conditions = new Document();
        conditions.put("trans_id", transId);
        Document dc = db.getCollection("dvt_recharge_by_bank").find(conditions).first();
        if (dc != null) {
            result = new RechargeByBankMessage(dc.getString("nick_name"), dc.getLong("money"), dc.getString("bank"), transId, dc.getString("amount"), dc.getString("order_info"), dc.getString("ticket_no"));
        }
        return result;
    }

    @Override
    public boolean logRechargeByBank(RechargeByBankMessage message) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection<Document> col = db.getCollection("dvt_recharge_by_bank");
        Document doc = new Document();
        doc.append("nick_name", message.getNickname());
        doc.append("money", message.getMoney());
        doc.append("bank", message.getBank());
        doc.append("trans_id", message.getTransId());
        doc.append("amount", message.getAmount());
        doc.append("order_info", message.getOrderInfo());
        doc.append("ticket_no", message.getTicketNo());
        doc.append("trans_time", message.getCreateTime());
        doc.append("response_code", "");
        doc.append("description", "");
        doc.append("amount_receive", "");
        doc.append("transaction_no", "");
        doc.append("message", "");
        doc.append("update_time", "");
        col.insertOne(doc);
        return true;
    }

    @Override
    public boolean updateRechargeByBank(String transId, String responseCode, String description, String transactionNo, String message, String amountReceive) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("response_code", responseCode);
        updateFields.append("description", description);
        updateFields.append("transaction_no", transactionNo);
        updateFields.append("message", message);
        updateFields.append("amount_receive", amountReceive);
        updateFields.append("update_time", VinPlayUtils.getCurrentDateTime());
        db.getCollection("dvt_recharge_by_bank").updateOne(new Document("trans_id", transId), new Document("$set", updateFields));
        return true;
    }

    @Override
    public boolean insertLogRechargeByBankError(String txnResponseCode, String version, String command, String merchTxnRef, String merchantID, String orderInfo, String currency, String amount, String locale, String cardType, String transactionNo, String message, String secureHash, String description) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection<Document> col = db.getCollection("dvt_recharge_by_bank_error");
        Document doc = new Document();
        doc.append("trans_id", merchTxnRef);
        doc.append("response_code", txnResponseCode);
        doc.append("version", version);
        doc.append("command", command);
        doc.append("merchant_id", merchantID);
        doc.append("order_info", orderInfo);
        doc.append("currency_code", currency);
        doc.append("amount", amount);
        doc.append("locale", locale);
        doc.append("card_type", cardType);
        doc.append("transaction_no", transactionNo);
        doc.append("message", message);
        doc.append("secure_hash", secureHash);
        doc.append("description", description);
        doc.append("trans_time", VinPlayUtils.getCurrentDateTime());
        col.insertOne(doc);
        return true;
    }

    @Override
    public boolean logRechargeByNL(String nickname, String email, String mobile, String ip, String orderCode, int totalAmount, int taxAmount, int discountAmount, int feeShipping, String paymentMethod, String bank, String errorCodeSend, String descVP, String token, String checkoutUrl, String timeLimit, String descNL) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection<Document> col = db.getCollection("ngan_luong_transaction");
        Document doc = new Document();
        doc.append("nick_name", nickname);
        doc.append("email", email);
        doc.append("mobile", mobile);
        doc.append("ip", ip);
        doc.append("order_code", orderCode);
        doc.append("total_amount", totalAmount);
        doc.append("tax_amount", taxAmount);
        doc.append("discount_amount", discountAmount);
        doc.append("fee_shipping", feeShipping);
        doc.append("payment_method", paymentMethod);
        doc.append("bank", bank);
        doc.append("error_code_send", errorCodeSend);
        doc.append("desc_vp", descVP);
        doc.append("token", token);
        doc.append("checkout_url", checkoutUrl);
        doc.append("time_limit", timeLimit);
        doc.append("desc_nl", descNL);
        doc.append("trans_time", VinPlayUtils.getCurrentDateTime());
        doc.append("error_code_return", "");
        doc.append("desc_return", "");
        doc.append("update_time", "");
        col.insertOne(doc);
        return true;
    }

    @Override
    public boolean updateRechargeByNL(String token, String errorCodeReturn, String descReturn) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("error_code_return", errorCodeReturn);
        updateFields.append("desc_return", descReturn);
        updateFields.append("update_time", VinPlayUtils.getCurrentDateTime());
        UpdateResult result = db.getCollection("ngan_luong_transaction").updateOne(new Document("token", token), new Document("$set", updateFields));
        return result.isModifiedCountAvailable();
    }

    @Override
    public boolean logRechargeByNLError(String token, String errorCodeReturn, String descReturn) {
        return true;
    }

    @Override
    public LogRechargeBankNapasResponse getLogNapas(String nickname, String bank, String transId, String ip, String transNo, String status, String startTime, String endTime, int page) {
        String pattern;
        final ArrayList<LogRechargeBankNapas> records = new ArrayList<>();
        final ArrayList<Long> num = new ArrayList<>();
        num.add(0, 0L);
        num.add(1, 0L);
        num.add(2, 0L);
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        int numStart = (page - 1) * 50;
        BasicDBObject objSort = new BasicDBObject();
        objSort.put("_id", -1);
        HashMap<String, Object> conditions = new HashMap<>();
        if (!nickname.isEmpty()) {
            pattern = ".*" + nickname + ".*";
            conditions.put("nick_name", new BasicDBObject().append("$regex", pattern).append("$options", "i"));
        }
        if (!bank.isEmpty()) {
            conditions.put("bank", bank);
        }
        if (!transId.isEmpty()) {
            pattern = ".*" + transId + ".*";
            conditions.put("trans_id", new BasicDBObject().append("$regex", pattern).append("$options", "i"));
        }
        if (!ip.isEmpty()) {
            conditions.put("ticket_no", ip);
        }
        if (!transNo.isEmpty()) {
            pattern = ".*" + transNo + ".*";
            conditions.put("transaction_no", new BasicDBObject().append("$regex", pattern).append("$options", "i"));
        }
        if (!status.isEmpty()) {
            conditions.put("response_code", status);
        }
        if (!startTime.isEmpty() && !endTime.isEmpty()) {
            BasicDBObject obj = new BasicDBObject();
            obj.put("$gte", startTime);
            obj.put("$lte", endTime);
            conditions.put("trans_time", obj);
        }
        FindIterable<Document> iterable = db.getCollection("dvt_recharge_by_bank").find(new Document(conditions)).sort(objSort).skip(numStart).limit(50);
        iterable.forEach((Block<Document>) document -> {
            LogRechargeBankNapas model = new LogRechargeBankNapas();
            model.nickname = document.getString("nick_name");
            model.money = document.getLong("money");
            model.bank = document.getString("bank");
            model.transId = document.getString("trans_id");
            model.ticketNo = document.getString("ticket_no");
            model.transTime = document.getString("trans_time");
            model.responseCode = document.getString("response_code");
            model.description = document.getString("description");
            model.transactionNo = document.getString("transaction_no");
            model.updateTime = document.getString("update_time");
            records.add(model);
        });
        FindIterable<Document> iterable2 = db.getCollection("dvt_recharge_by_bank").find(new Document(conditions));
        iterable2.forEach((Block<Document>) document -> {
            LogRechargeBankNapas model = new LogRechargeBankNapas();
            model.money = document.getLong("money");
            model.responseCode = document.getString("response_code");
            long count = num.get(0) + 1L;
            num.set(0, count);
            if (model.responseCode.equals("0")) {
                long numSuccess = num.get(1) + 1L;
                num.set(1, numSuccess);
                long moneySuccess = num.get(2) + model.money;
                num.set(2, moneySuccess);
            }
        });
        return new LogRechargeBankNapasResponse(true, "0", num.get(0) / 50L + 1L, num.get(1), num.get(2), records);
    }

    @Override
    public LogRechargeBankNLResponse getLogNL(String nickname, String bank, String transId, String ip, String transNo, String status, String startTime, String endTime, int page) {
        String pattern;
        final ArrayList<LogRechargeBankNL> records = new ArrayList<>();
        final ArrayList<Long> num = new ArrayList<>();
        num.add(0, 0L);
        num.add(1, 0L);
        num.add(2, 0L);
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        int numStart = (page - 1) * 50;
        BasicDBObject objSort = new BasicDBObject();
        objSort.put("_id", -1);
        HashMap<String, Object> conditions = new HashMap<>();
        if (!nickname.isEmpty()) {
            pattern = ".*" + nickname + ".*";
            conditions.put("nick_name", new BasicDBObject().append("$regex", pattern).append("$options", "i"));
        }
        if (!bank.isEmpty()) {
            conditions.put("bank", bank);
        }
        if (!transId.isEmpty()) {
            pattern = ".*" + transId + ".*";
            conditions.put("order_code", new BasicDBObject().append("$regex", pattern).append("$options", "i"));
        }
        if (!ip.isEmpty()) {
            conditions.put("ip", ip);
        }
        if (!transNo.isEmpty()) {
            pattern = ".*" + transNo + ".*";
            conditions.put("token", new BasicDBObject().append("$regex", pattern).append("$options", "i"));
        }
        if (!status.isEmpty()) {
            conditions.put("error_code_return", status);
        }
        if (!startTime.isEmpty() && !endTime.isEmpty()) {
            BasicDBObject obj = new BasicDBObject();
            obj.put("$gte", startTime);
            obj.put("$lte", endTime);
            conditions.put("trans_time", obj);
        }
        FindIterable<Document> iterable = db.getCollection("ngan_luong_transaction").find(new Document(conditions)).sort(objSort).skip(numStart).limit(50);
        iterable.forEach((Block<Document>) document -> {
            LogRechargeBankNL model = new LogRechargeBankNL();
            model.nickname = document.getString("nick_name");
            model.email = document.getString("email");
            model.mobile = document.getString("mobile");
            model.ip = document.getString("ip");
            model.orderCode = document.getString("order_code");
            model.totalAmount = document.getInteger("total_amount");
            model.bank = document.getString("bank");
            model.token = document.getString("token");
            model.transTime = document.getString("trans_time");
            model.errorCodeReturn = document.getString("error_code_return");
            model.descReturn = document.getString("desc_return");
            model.updateTime = document.getString("update_time");
            records.add(model);
        });
        FindIterable<Document> iterable2 = db.getCollection("ngan_luong_transaction").find(new Document(conditions));
        iterable2.forEach((Block<Document>) document -> {
            LogRechargeBankNL model = new LogRechargeBankNL();
            model.totalAmount = document.getInteger("total_amount");
            model.errorCodeReturn = document.getString("error_code_return");
            long count = num.get(0) + 1L;
            num.set(0, count);
            if (model.errorCodeReturn.equals("00")) {
                long numSuccess = num.get(1) + 1L;
                num.set(1, numSuccess);
                long moneySuccess = num.get(2) + (long) model.totalAmount;
                num.set(2, moneySuccess);
            }
        });
        return new LogRechargeBankNLResponse(true, "0", num.get(0) / 50L + 1L, num.get(1), num.get(2), records);
    }

    @Override
    public boolean saveLogIAP(Purchase pc, String nickname, int amount, int code, String des) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection<Document> col = db.getCollection("dvt_recharge_by_iap");
        Document doc = new Document();
        doc.append("nick_name", nickname);
        doc.append("amount", amount);
        doc.append("code", code);
        doc.append("description", des);
        doc.append("trans_time", VinPlayUtils.getCurrentDateTime());
        doc.append("order_id", pc.getOrderId());
        doc.append("package_name", pc.getPackageName());
        doc.append("product_id", pc.getSku());
        doc.append("purchase_time", pc.getPurchaseTime());
        doc.append("purchase_state", pc.getPurchaseState());
        doc.append("developer_payload", pc.getDeveloperPayload());
        doc.append("token", pc.getToken());
        doc.append("signature", pc.getSignature());
        col.insertOne(doc);
        return true;
    }

    @Override
    public boolean checkOrderId(String orderId) {
        boolean res = false;
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        Document conditions = new Document();
        conditions.put("order_id", orderId);
        conditions.put("code", 0);
        Document dc = db.getCollection("dvt_recharge_by_iap").find(conditions).first();
        if (dc != null) {
            res = true;
        }
        return res;
    }

    @Override
    public long getTotalRechargeIapInday(String nickname, Calendar cal) throws ParseException {
        long res = 0L;
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        BasicDBObject obj = new BasicDBObject();
        Document conditions = new Document();
        if (nickname != null && !nickname.equals("")) {
            conditions.put("nick_name", nickname);
        }
        conditions.put("code", 0);
        String timeStart = VinPlayUtils.getDateTimeStr(VinPlayUtils.getDateTimeFromDate(VinPlayUtils.getDateFromDateTime(VinPlayUtils.getDateTimeStr(cal.getTime()))));
        cal.add(Calendar.DATE, 1);
        String timeEnd = VinPlayUtils.getDateTimeStr(VinPlayUtils.getDateTimeFromDate(VinPlayUtils.getDateFromDateTime(VinPlayUtils.getDateTimeStr(cal.getTime()))));
        obj.put("$gte", timeStart);
        obj.put("$lt", timeEnd);
        conditions.put("trans_time", obj);
        Document dc = db.getCollection("dvt_recharge_by_iap").aggregate(Arrays.asList(new Document("$match", conditions), new Document("$group", new Document("_id", null).append("money", new Document("$sum", "$amount"))))).first();
        if (dc != null) {
            res = dc.getInteger("money");
        }
        return res;
    }

    @Override
    public boolean checkRequestIdSMS(String requestId) {
        boolean res = false;
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        Document conditions = new Document();
        conditions.put("request_id", requestId);
        Document dc = db.getCollection("dvt_recharge_by_sms").find(conditions).first();
        if (dc != null) {
            res = true;
        }
        return res;
    }

    @Override
    public boolean checkRequestIdSMSPlus(String requestId) {
        boolean res = false;
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        Document conditions = new Document();
        conditions.put("request_id", requestId);
        Document dc = db.getCollection("dvt_recharge_by_sms_plus").find(conditions).first();
        if (dc != null) {
            res = true;
        }
        return res;
    }

    @Override
    public boolean saveLogRechargeBySMS(String nickname, String mobile, String moMessage, int amount, String shortCode, String requestId, String requestTime, int code, String des, int money) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection<Document> col = db.getCollection("dvt_recharge_by_sms");
        Document doc = new Document();
        doc.append("nick_name", nickname);
        doc.append("mobile", mobile);
        doc.append("message_MO", moMessage);
        doc.append("amount", amount);
        doc.append("money", money);
        doc.append("short_code", shortCode);
        doc.append("request_id", requestId);
        doc.append("time_request", requestTime);
        doc.append("code", code);
        doc.append("description", des);
        doc.append("time_log", VinPlayUtils.getCurrentDateTime());
        doc.append("create_time", new Date());
        col.insertOne(doc);
        return true;
    }

    @Override
    public boolean saveLogRechargeBySMSPlus(String nickname, String mobile, String moMessage, int amount, String shortCode, String errorCode, String errorMessage, String requestId, String requestTime, int code, String des, int money) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection<Document> col = db.getCollection("dvt_recharge_by_sms_plus");
        Document doc = new Document();
        doc.append("nick_name", nickname);
        doc.append("mobile", mobile);
        doc.append("message_MO", moMessage);
        doc.append("amount", amount);
        doc.append("money", money);
        doc.append("short_code", shortCode);
        doc.append("error_code", errorCode);
        doc.append("error_message", errorMessage);
        doc.append("request_id", requestId);
        doc.append("time_request", requestTime);
        doc.append("code", code);
        doc.append("description", des);
        doc.append("time_log", VinPlayUtils.getCurrentDateTime());
        doc.append("create_time", new Date());
        col.insertOne(doc);
        return true;
    }

    @Override
    public boolean saveLogRechargeBySMSPlusCheckMO(String mobile, String moMessage, int amount, String shortCode, int code, String des) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection<Document> col = db.getCollection("dvt_recharge_by_sms_plus_check_mo");
        Document doc = new Document();
        doc.append("mobile", mobile);
        doc.append("message_MO", moMessage);
        doc.append("amount", amount);
        doc.append("short_code", shortCode);
        doc.append("code", code);
        doc.append("description", des);
        doc.append("time_log", VinPlayUtils.getCurrentDateTime());
        doc.append("create_time", new Date());
        col.insertOne(doc);
        return true;
    }

    @Override
    public List<String> getListSmsIdNearly() {
        final ArrayList<String> res = new ArrayList<>();
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection<Document> col = db.getCollection("dvt_recharge_by_sms");
        BasicDBObject objSort = new BasicDBObject();
        objSort.put("_id", -1);
        FindIterable<Document> iterable = col.find().sort(objSort).skip(0).limit(1000);
        iterable.forEach((Block<Document>) document -> res.add(document.getString("request_id")));
        return res;
    }

    @Override
    public List<String> getListSmsPlusIdNearly() {
        final ArrayList<String> res = new ArrayList<>();
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection<Document> col = db.getCollection("dvt_recharge_by_sms_plus");
        BasicDBObject objSort = new BasicDBObject();
        objSort.put("_id", -1);
        FindIterable<Document> iterable = col.find().sort(objSort).skip(0).limit(1000);
        iterable.forEach((Block<Document>) document -> res.add(document.getString("request_id")));
        return res;
    }

    @Override
    public boolean saveLogRequestApiOTP(String nickname, String mobile, int amount, String errorCode, String errorMessage, String requestId, String transId, int code, String redirectURL) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection<Document> col = db.getCollection("dvt_recharge_by_otp_request");
        Document doc = new Document();
        doc.append("nick_name", nickname);
        doc.append("mobile", mobile);
        doc.append("amount", amount);
        doc.append("error_code", errorCode);
        doc.append("error_message", errorMessage);
        doc.append("request_id", requestId);
        doc.append("trans_id", transId);
        doc.append("code", code);
        doc.append("redirect_url", redirectURL);
        doc.append("time_log", VinPlayUtils.getCurrentDateTime());
        doc.append("create_time", new Date());
        col.insertOne(doc);
        return true;
    }

    @Override
    public boolean saveLogConfirmApiOTP(String nickname, String mobile, int amount, String otp, String errorCode, String errorMessage, String requestId, String transId, int code, String des, int money) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection<Document> col = db.getCollection("dvt_recharge_by_otp_confirm");
        Document doc = new Document();
        doc.append("nick_name", nickname);
        doc.append("mobile", mobile);
        doc.append("amount", amount);
        doc.append("otp", otp);
        doc.append("error_code", errorCode);
        doc.append("error_message", errorMessage);
        doc.append("request_id", requestId);
        doc.append("trans_id", transId);
        doc.append("code", code);
        doc.append("description", des);
        doc.append("money", money);
        doc.append("time_log", VinPlayUtils.getCurrentDateTime());
        doc.append("create_time", new Date());
        col.insertOne(doc);
        return true;
    }

    @Override
    public boolean isAgent(String nickName) throws SQLException {
        boolean result = false;
        String sql = " SELECT *  FROM vinplay_admin.useragent  WHERE nickname = ? ";
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpool_admin")) {
            PreparedStatement stmt;
            ResultSet rs;
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nickName);
            rs = stmt.executeQuery();
            while (rs.next()) {
                result = true;
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    @Override
    public Document getRechargeByGachthe(String transId) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        Document conditions = new Document();
        conditions.put("request_id", transId);
        return db.getCollection("dvt_recharge_by_gachthe").find(conditions).first();
    }

    @Override
    public Document getRechargeByGachthe(String transId, String amount) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        Document conditions = new Document();
        conditions.put("request_id", transId);
        conditions.put("amount", Integer.parseInt(amount));
        return db.getCollection("dvt_recharge_by_gachthe").find(conditions).first();
    }

    @Override
    public Document getRechargeByGachthe(String nickname, String serial, String pin) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        Document conditions = new Document();
        conditions.put("nick_name", nickname);
        conditions.put("serial", serial);
        conditions.put("pin", pin);
        conditions.put("code", 30);
        return db.getCollection("dvt_recharge_by_gachthe").find(conditions).first();
    }

    @Override
    public List<Document> getRechargeByGachtheRecently() {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        Document conditions = new Document();
        BasicDBObject codeCoditions = new BasicDBObject();
        codeCoditions.append("$ne", 30);
        conditions.put("code", codeCoditions);
        conditions.put("is_sent", false);
        return db.getCollection("dvt_recharge_by_gachthe").find(conditions).into(new ArrayList<>());
    }

    @Override
    public boolean saveLogRechargeByGachThe(String nickname, String serial, String pin, long amount, String requestId,
                                            String requestTime, int code, String des, long money, String provider, String platform,
                                            long currentMoney, long addMoney, int userId, String username, String partner, String client) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection<Document> col = db.getCollection("dvt_recharge_by_gachthe");
        Document doc = new Document();
        doc.append("nick_name", nickname);
        doc.append("serial", serial);
        doc.append("pin", pin);
        doc.append("amount", amount);
        doc.append("money", money);
        doc.append("provider", provider);
        doc.append("platform", platform);
        doc.append("request_id", requestId);
        doc.append("time_request", requestTime);
        doc.append("code", code);
        doc.append("description", des);
        doc.append("time_log", VinPlayUtils.getCurrentDateTime());
        doc.append("create_time", new Date());
        doc.append("is_sent", false);
        doc.append("current_money", currentMoney);
        doc.append("add_money", addMoney);
        doc.append("user_id", userId);
        doc.append("username", username);
        doc.append("partner", partner);
        doc.append("client", client);
        col.insertOne(doc);
        return true;
    }

    @Override
    public boolean UpdateDepositBankManualStatus(String transId, int status, String desc, String userApprove, long amount) {
        try {
            MongoDatabase db = MongoDBConnectionFactory.getDB();
            Document conditions = new Document();
            conditions.put("Id", transId);
            BasicDBObject updateFields = new BasicDBObject();
            updateFields.append("Status", status);
            updateFields.append("Description", desc);
            updateFields.append("UserApprove", userApprove);
            updateFields.append("Amount", amount);
            db.getCollection(DvtConst.DEPOSIT_BANK_COLLECTION).updateOne(conditions, new Document("$set", updateFields));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean UpdateDepositMomoManualStatus(String transId, int status, String desc, String userApprove, long amount) {
        try {
            MongoDatabase db = MongoDBConnectionFactory.getDB();
            Document conditions = new Document();
            conditions.put("Id", transId);
            BasicDBObject updateFields = new BasicDBObject();
            updateFields.append("Status", status);
            updateFields.append("Description", desc);
            updateFields.append("UserApprove", userApprove);
            updateFields.append("Amount", amount);
            db.getCollection(DvtConst.DEPOSIT_MOMO_COLLECTION).updateOne(conditions, new Document("$set", updateFields));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean UpdateGachtheTransctions(String transId, int code, String message, long amount) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        Document conditions = new Document();
        conditions.put("request_id", transId);
        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("code", code);
        updateFields.append("description", message);
        if (amount > 0)
            updateFields.append("amount", amount);
        db.getCollection("dvt_recharge_by_gachthe").updateOne(conditions, new Document("$set", updateFields));
        return true;
    }

    @Override
    public boolean UpdateGachtheTransctionsSent(String transId) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        Document conditions = new Document();
        conditions.put("request_id", transId);
        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("is_sent", true);
        db.getCollection("dvt_recharge_by_gachthe").updateOne(conditions, new Document("$set", updateFields));
        return true;
    }

    @Override
    public Document getRechargeByNapTienGa(String transId) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        Document conditions = new Document();
        conditions.put("request_id", transId);
        return db.getCollection("dvt_recharge_by_naptienga").find(conditions).first();
    }

    @Override
    public Document getRechargeByNapTienGa(String nickname, String serial, String pin) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        Document conditions = new Document();
        conditions.put("nick_name", nickname);
        conditions.put("serial", serial);
        conditions.put("pin", pin);
        conditions.put("code", 30);
        return db.getCollection("dvt_recharge_by_naptienga").find(conditions).first();
    }

    @Override
    public List<Document> getRechargeByNapTienGaRecently() {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        Document conditions = new Document();
        conditions.put("code", 0);
        conditions.put("is_sent", false);
        return db.getCollection("dvt_recharge_by_naptienga").find(conditions).into(new ArrayList<>());
    }

    @Override
    public boolean saveLogRechargeByNapTienGa(String nickname, String serial, String pin, long amount, String requestId, String requestTime, int code, String des, long money, String provider, String platform, long currentMoney, long addMoney, int userId, String username, int napTienGaId) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        MongoCollection<Document> col = db.getCollection("dvt_recharge_by_naptienga");
        Document doc = new Document();
        doc.append("nick_name", nickname);
        doc.append("serial", serial);
        doc.append("pin", pin);
        doc.append("amount", amount);
        doc.append("money", money);
        doc.append("provider", provider);
        doc.append("platform", platform);
        doc.append("request_id", requestId);
        doc.append("time_request", requestTime);
        doc.append("code", code);
        doc.append("description", des);
        doc.append("time_log", VinPlayUtils.getCurrentDateTime());
        doc.append("create_time", new Date());
        doc.append("is_sent", false);
        doc.append("current_money", currentMoney);
        doc.append("add_money", addMoney);
        doc.append("user_id", userId);
        doc.append("username", username);
        doc.append("nap_tien_ga_id", napTienGaId);
        col.insertOne(doc);
        return true;
    }

    @Override
    public boolean UpdateNapTienGaTransctions(String transId, int code, String message) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        Document conditions = new Document();
        conditions.put("request_id", transId);
        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("code", code);
        updateFields.append("description", message);
        db.getCollection("dvt_recharge_by_naptienga").updateOne(conditions, new Document("$set", updateFields));
        return true;
    }

    @Override
    public boolean UpdateNapTienGaTransctionsSent(String transId) {
        MongoDatabase db = MongoDBConnectionFactory.getDB();
        Document conditions = new Document();
        conditions.put("request_id", transId);
        BasicDBObject updateFields = new BasicDBObject();
        updateFields.append("is_sent", true);
        db.getCollection("dvt_recharge_by_naptienga").updateOne(conditions, new Document("$set", updateFields));
        return true;
    }

    @Override
    public boolean isPendingTransDepositBank(String nickname) {
        try {
            MongoDatabase db = MongoDBConnectionFactory.getDB();
            Document conditions = new Document();
            conditions.put("Nickname", nickname);

            conditions.put("Status", DvtConst.STATUS_PENDING);
            Document dc = db.getCollection(DvtConst.DEPOSIT_BANK_COLLECTION).find(conditions).first();
            return dc != null;
        } catch (Exception e) {
            RechargeDaoImpl.logger.error(e);
            return false;
        }
    }

    public boolean isPendingTransDepositMomo(String nickname) {
        try {
            MongoDatabase db = MongoDBConnectionFactory.getDB();
            Document conditions = new Document();
            conditions.put("Nickname", nickname);

            conditions.put("Status", DvtConst.STATUS_PENDING);
            Document dc = db.getCollection(DvtConst.DEPOSIT_MOMO_COLLECTION).find(conditions).first();
            return dc != null;
        } catch (Exception e) {
            RechargeDaoImpl.logger.error(e);
            return false;
        }
    }

    @Override
    public String InsertDepositMomoManual(DepositMomoModel depositBankModel) {
        try {
            MongoDatabase db = MongoDBConnectionFactory.getDB();
            MongoCollection<Document> col = db.getCollection(DvtConst.DEPOSIT_MOMO_COLLECTION);
            Document doc = new Document();
            String id = String.valueOf(VinPlayUtils.generateTransId());
            doc.append("Id", id);
            doc.append("Nickname", depositBankModel.Nickname);
            doc.append("CreatedAt", VinPlayUtils.getCurrentDateTime());
            doc.append("UpdatedAt", VinPlayUtils.getCurrentDateTime());
            doc.append("Amount", depositBankModel.Amount);
            doc.append("Status", DvtConst.STATUS_PENDING);
            doc.append("ReceivedPhoneNumber", depositBankModel.ReceivedPhoneNumber);
            doc.append("ReceivedName", depositBankModel.ReceivedName);
            doc.append("SendFromNumber", depositBankModel.SendFromNumber);
            doc.append("Description", "");
            doc.append("UserApprove", depositBankModel.UserApprove);
            col.insertOne(doc);
            return id;
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public String InsertDepositMomoManualId(DepositMomoModel depositBankModel) {
        try {
            MongoDatabase db = MongoDBConnectionFactory.getDB();
            MongoCollection<Document> col = db.getCollection(DvtConst.DEPOSIT_MOMO_COLLECTION);
            Document doc = new Document();
            doc.append("Id", depositBankModel.Id);
            doc.append("Nickname", depositBankModel.Nickname);
            doc.append("CreatedAt", VinPlayUtils.getCurrentDateTime());
            doc.append("UpdatedAt", VinPlayUtils.getCurrentDateTime());
            doc.append("Amount", depositBankModel.Amount);
            doc.append("Status", DvtConst.STATUS_PENDING);
            doc.append("ReceivedPhoneNumber", depositBankModel.ReceivedPhoneNumber);
            doc.append("ReceivedName", depositBankModel.ReceivedName);
            doc.append("SendFromNumber", depositBankModel.SendFromNumber);
            doc.append("Description", "");
            doc.append("UserApprove", depositBankModel.UserApprove);
            col.insertOne(doc);
            return depositBankModel.Id;
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public String InsertDepositBankManualId(DepositBankModel depositBankModel) {
        try {
            MongoDatabase db = MongoDBConnectionFactory.getDB();
            MongoCollection<Document> col = db.getCollection(DvtConst.DEPOSIT_BANK_COLLECTION);
            Document doc = new Document();
            doc.append("Id", depositBankModel.Id);
            doc.append("Nickname", depositBankModel.Nickname);
            doc.append("CreatedAt", VinPlayUtils.getCurrentDateTime());
            doc.append("UpdatedAt", VinPlayUtils.getCurrentDateTime());
            doc.append("Amount", depositBankModel.Amount);
            doc.append("Status", DvtConst.STATUS_PENDING);
            doc.append("Description", "");
            doc.append("UserApprove", depositBankModel.UserApprove);
            col.insertOne(doc);
            return depositBankModel.Id;
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public DepositMomoModel FindDepositMomoById(String Id) {
        try {
            MongoDatabase db = MongoDBConnectionFactory.getDB();
            Document conditions = new Document();
            conditions.put("Id", Id);
            Document document = db.getCollection(DvtConst.DEPOSIT_MOMO_COLLECTION).find(conditions).first();
            if (document == null)
                return null;
            return new DepositMomoModel(
                    document.getString("Id"),
                    document.getString("Nickname"),
                    document.getString("CreatedAt"),
                    document.getString("UpdatedAt"),
                    document.getLong("Amount"),
                    document.getInteger("Status"),
                    document.getString("ReceivedPhoneNumber"),
                    document.getString("ReceivedName"),
                    document.getString("SendFromNumber"),
                    document.getString("Description"),
                    document.getString("UserApprove")
            );
        } catch (Exception e) {
            RechargeDaoImpl.logger.error(e);
            return null;
        }
    }

    @Override
    public DepositBankModel FindDepositBankById(String Id) {
        try {
            MongoDatabase db = MongoDBConnectionFactory.getDB();
            Document conditions = new Document();
            conditions.put("Id", Id);
            Document document = db.getCollection(DvtConst.DEPOSIT_BANK_COLLECTION).find(conditions).first();
            if (document == null)
                return null;
            return new DepositBankModel(
                    document.getString("Id"),
                    document.getString("Nickname"),
                    document.getString("CreatedAt"),
                    document.getString("UpdatedAt"),
                    document.getString("Amount"),
                    document.getInteger("Status"),
                    document.getString("Description"),
                    document.getString("UserApprove")
            );
        } catch (Exception e) {
            RechargeDaoImpl.logger.error(e);
            return null;
        }
    }

}

