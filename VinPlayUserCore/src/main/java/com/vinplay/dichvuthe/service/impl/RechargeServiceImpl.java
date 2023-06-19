/*
 * Decompiled with CFR 0.144.
 *
 * Could not load the following classes:
 *  com.hazelcast.core.HazelcastInstance
 *  com.hazelcast.core.IMap
 *  com.vinplay.vbee.common.enums.I2BType
 *  com.vinplay.vbee.common.enums.PhoneCardType
 *  com.vinplay.vbee.common.enums.ProviderType
 *  com.vinplay.vbee.common.hazelcast.HazelcastClientFactory
 *  com.vinplay.vbee.common.messages.BaseMessage
 *  com.vinplay.vbee.common.messages.LogMoneyUserMessage
 *  com.vinplay.vbee.common.messages.MoneyMessageInMinigame
 *  com.vinplay.vbee.common.messages.dvt.RechargeByBankMessage
 *  com.vinplay.vbee.common.messages.dvt.RechargeByCardMessage
 *  com.vinplay.vbee.common.models.UserModel
 *  com.vinplay.vbee.common.models.cache.ApiOtpModel
 *  com.vinplay.vbee.common.models.cache.UserCacheModel
 *  com.vinplay.vbee.common.rmq.RMQApi
 *  com.vinplay.vbee.common.utils.UserValidaton
 *  com.vinplay.vbee.common.utils.VinPlayUtils
 *  org.apache.log4j.Logger
 *  org.json.JSONObject
 */
package com.vinplay.dichvuthe.service.impl;

//import bitzero.util.common.business.Debug;

import bitzero.util.common.business.Debug;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.dichvuthe.client.GachTheClient;
import com.vinplay.dichvuthe.client.HttpURLClient;
import com.vinplay.dichvuthe.dao.RechargeDao;
import com.vinplay.dichvuthe.dao.impl.RechargeDaoImpl;
import com.vinplay.dichvuthe.entities.DepositBankModel;
import com.vinplay.dichvuthe.entities.DepositMomoModel;
import com.vinplay.dichvuthe.response.Iwin99CallbackResponse;
import com.vinplay.dichvuthe.response.RechargeResponse;
import com.vinplay.dichvuthe.service.RechargeService;
import com.vinplay.dichvuthe.utils.DvtConst;
import com.vinplay.dichvuthe.utils.DvtUtils;
import com.vinplay.usercore.logger.MoneyLogger;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.usercore.utils.PartnerConfig;
import com.vinplay.utils.TelegramAlert;
import com.vinplay.vbee.common.enums.ProviderType;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.messages.BaseMessage;
import com.vinplay.vbee.common.messages.LogMoneyUserMessage;
import com.vinplay.vbee.common.messages.MoneyMessageInMinigame;
import com.vinplay.vbee.common.messages.dvt.RechargeByCardMessage;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.response.BaseResponseModel;
import com.vinplay.vbee.common.rmq.RMQApi;
import com.vinplay.vbee.common.statics.Consts;
import com.vinplay.vbee.common.utils.UserValidaton;
import com.vinplay.vbee.common.utils.VinPlayUtils;
import okhttp3.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RechargeServiceImpl implements RechargeService {
    public static final Logger logger = Logger.getLogger("vbee");


    @Override
    public RechargeResponse rechargeByMomoManual(String nickname) {
        try {
            int code = 1;
            RechargeResponse res = new RechargeResponse(code, 0L, 0, 0L);
            //HazelcastInstance client;
            //IMap<String, UserModel> userMap;
            if (nickname.isEmpty()) {
                res.setCode(DvtConst.RECHARGE_STATUS_DATA_ERROR);
                return res;
            }

            //get pending transaction
            RechargeDao rechargeDao = new RechargeDaoImpl();
//            if (rechargeDao.isPendingTransDepositMomo(nickname)) {
//                res.setCode(DvtConst.RECHARGE_STATUS_PENDING_TRANS);
//                return res;
//            }
            //validate bank infor
//            String momoInfo = DvtUtils.getMomoNumber();
//            if (momoInfo == null) {
//                res.setCode(DvtConst.RECHARGE_STATUS_DATA_ERROR);
//                return res;
//            }

            // insert to db
            DepositMomoModel model = new DepositMomoModel(nickname, 10000, "", "", "");
            String tranId = rechargeDao.InsertDepositMomoManual(model);
            if (TextUtils.isEmpty(tranId)) {
                return res;
            }

            String url = "http://api.3456789.pro/api?c=RegCharge&apiKey=5499ee8d-4fb6-429f-b62f-ef425bd3ede0&chargeType=momo&amount=10000&requestId=" + tranId + "&callback=https://iwspay.go88vin.live/api/momo/callback";
            Debug.trace((Object) ("depositMomoManual url" + url));
            String response = HttpURLClient.sendGET(url);
            Debug.trace((Object) ("depositMomoManual response" + response));
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.getInt("stt");
            if (status == 1) {
                res.setCode(DvtConst.RECHARGE_STATUS_SUCCESS);
                res.setMessage(jsonObject.getJSONObject("data").toString());
            } else {
                res.setCode(DvtConst.RECHARGE_STATUS_DATA_ERROR);
            }
//            TelegramAlert.SendMessageDepositMomo(model);
            return res;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public RechargeResponse rechargeByMomoManualHaDongPho(String nickname) {
        try {
            int code = 1;
            RechargeResponse res = new RechargeResponse(code, 0L, 0, 0L);
            //HazelcastInstance client;
            //IMap<String, UserModel> userMap;
            if (nickname.isEmpty()) {
                res.setCode(DvtConst.RECHARGE_STATUS_DATA_ERROR);
                return res;
            }

            //get pending transaction
            RechargeDao rechargeDao = new RechargeDaoImpl();

            String url = "http://hadongpho.com/api/transfer/get?merchant_id=12225012515147&transferType=MOMO";
            Debug.trace((Object) ("depositMomoManual url" + url));
            String response = HttpURLClient.sendGET(url);
            Debug.trace((Object) ("depositMomoManual response" + response));
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.getInt("status");
            if (status == 1) {

                // insert to db
                DepositMomoModel model = new DepositMomoModel(nickname, 10000, "", "", "");
                model.Id = jsonObject.getJSONObject("data").getString("bank_id");
                String tranId = rechargeDao.InsertDepositMomoManualId(model);
                if (TextUtils.isEmpty(tranId)) {
                    res.setCode(DvtConst.RECHARGE_STATUS_DATA_ERROR);
                    return res;
                }

                res.setCode(DvtConst.RECHARGE_STATUS_SUCCESS);
                res.setMessage(jsonObject.getJSONObject("data").toString());
            } else {
                res.setCode(DvtConst.RECHARGE_STATUS_DATA_ERROR);
            }
//            TelegramAlert.SendMessageDepositMomo(model);
            return res;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public RechargeResponse rechargeByBankManualHaDongPho(String nickname) {
        try {
            int code = 1;
            RechargeResponse res = new RechargeResponse(code, 0L, 0, 0L);
            //HazelcastInstance client;
            //IMap<String, UserModel> userMap;
            if (nickname.isEmpty()) {
                res.setCode(DvtConst.RECHARGE_STATUS_DATA_ERROR);
                return res;
            }

            //get pending transaction
            RechargeDao rechargeDao = new RechargeDaoImpl();
            String url = "http://hadongpho.com/api/transfer/get?merchant_id=12225012515147&transferType=BANK";
            Debug.trace((Object) ("depositBankManual url" + url));
            String response = HttpURLClient.sendGET(url);
            Debug.trace((Object) ("depositBankManual response" + response));
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.getInt("status");
            if (status == 1) {

                // insert to db
                String id = jsonObject.getJSONObject("data").getString("bank_id");
                DepositBankModel depositBankModel = new DepositBankModel(id, nickname, DvtConst.STATUS_PENDING, "10000");
                String tranId = rechargeDao.InsertDepositBankManualId(depositBankModel);
                if (TextUtils.isEmpty(tranId)) {
                    res.setCode(DvtConst.RECHARGE_STATUS_DATA_ERROR);
                    return res;
                }

                res.setCode(DvtConst.RECHARGE_STATUS_SUCCESS);
                res.setMessage(jsonObject.getJSONObject("data").toString());
            } else {
                res.setCode(DvtConst.RECHARGE_STATUS_DATA_ERROR);
            }
//            TelegramAlert.SendMessageDepositMomo(model);
            return res;
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public RechargeResponse rechargeByBankManual(String nickname, String amount, String code) {
        try {
            RechargeResponse res = new RechargeResponse(1, 0L, 0, 0L);
            HazelcastInstance client;
            IMap<String, UserModel> userMap;
            if ((client = HazelcastClientFactory.getInstance()) == null) {
                MoneyLogger.log(nickname, "RechargeByBank", 0L, 0L, "vin", "Nap vin qua bank", "1030", "can not connect hazelcast");
                return res;
            }
            userMap = client.getMap("users");
            if (nickname.isEmpty()) {
                res.setCode(DvtConst.RECHARGE_STATUS_DATA_ERROR);
                return res;
            }
            UserCacheModel user = (UserCacheModel) userMap.get(nickname);

            //get pending transaction
            RechargeDao rechargeDao = new RechargeDaoImpl();
            // insert to db
            String id = VinPlayUtils.genTransactionId(user.getId());
            DepositBankModel depositBankModel = new DepositBankModel(id, nickname, DvtConst.STATUS_PENDING, amount);
            String tranId = rechargeDao.InsertDepositBankManualId(depositBankModel);
            if (TextUtils.isEmpty(tranId)) {
                res.setCode(DvtConst.RECHARGE_STATUS_DATA_ERROR);
                return res;
            }
            OkHttpClient client1 = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("http://api.3456789.pro/api?c=RegCharge&apiKey=aa399aba-c9d5-4eb7-8002-140ca4b38b8d&chargeType=bank&amount=" + amount + "&requestId=" + tranId + "&callback=https://iwspay.go88live.vin/api/bank/callback&subType=" + code)
                    .method("GET", null)
                    .build();
            Response responseCall = client1.newCall(request).execute();
            String response = responseCall.body().string();
            Debug.trace((Object) ("depositBankManual response" + response));
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.getInt("stt");
            if (status == 1) {
                res.setCode(DvtConst.RECHARGE_STATUS_SUCCESS);
                res.setMessage(jsonObject.getJSONObject("data").toString());
            } else {
                res.setCode(DvtConst.RECHARGE_STATUS_DATA_ERROR);
            }
//            TelegramAlert.SendMessageDepositMomo(model);
            return res;
        } catch (Exception e) {
            return null;
        }
    }

    public RechargeResponse rechargeByGachThe(String nickname, ProviderType provider, String serial, String pin, String sAmount, String platform, int UserId) throws Exception {
        //RechargeServiceImpl.logger.debug((Object)("Start rechargeByCard Gachthe:  nickname: " + nickname + ", provider: " + provider.getName() + ", serial: " + serial + ", pin: " + pin));
        Debug.trace((Object) ("Start rechargeByCard Gachthe:  nickname: " + nickname + ", provider: " + provider.getName() + ", serial: " + serial + ", pin: " + pin));
        int code = 1;
        HazelcastInstance client;
        IMap<String, UserModel> userMap;
        String description;
        RechargeResponse res = new RechargeResponse(code, 0L, 0, 0L);
        if (GameCommon.getValueInt("IS_RECHARGE_CARD") == 1 || provider == null) {
            RechargeServiceImpl.logger.debug("rechargeByCard: param fail");
            return res;
        }
        if (pin != null) {
            pin = pin.trim();
        }
        if (serial != null) {
            serial = serial.trim();
        }
        Debug.trace("rechargeByGachThe  abccc");
        if ((client = HazelcastClientFactory.getInstance()) == null) {
            MoneyLogger.log(nickname, "RechargeByCard", 0L, 0L, "vin", "Nap vin qua the", "1030", "can not connect hazelcast");
            return res;
        }
        long amount = Long.parseLong(sAmount);
        userMap = client.getMap("users");
        RechargeByCardMessage message = null;
        if (userMap.containsKey(nickname)) {
            try {
                UserCacheModel user = (UserCacheModel) userMap.get(nickname);
                long time = DvtUtils.checkRechargeFail(user.getRechargeFail(), user.getRechargeFailTime());
                res.setCurrentMoney(user.getVinTotal());
                boolean cardFail = false;
                if (UserValidaton.validateSerialPin(serial)) {
                    if (UserValidaton.validateSerialPin(pin)) {
                        // check exists card pending
                        RechargeDaoImpl dao = new RechargeDaoImpl();
                        Document doc = dao.getRechargeByGachthe(nickname, serial, pin);
                        Debug.trace("rechargeByGachThe  22222");
                        if (doc != null) {
                            // pending card
                            code = 30;
                        } else {
                            String id = VinPlayUtils.genTransactionId(user.getId());
                            res.setTid(id);

                            Debug.trace("rechargeByGachThe  xxxxx " + id);
                            GachTheClient gachTheClient = new GachTheClient();
                            org.json.simple.JSONObject result = gachTheClient.doCharge(provider.getId(), provider.getValue().toUpperCase(), pin, serial, id, amount);
                            RechargeServiceImpl.logger.debug(result.toJSONString());
                            Debug.trace("rechargeByGachThe: " + result.toJSONString());
                            res.setMessage(result.get("message").toString());
                            if ("0".equals(result.get("status").toString())) {
                                code = 1;
                            } else if ("1".equals(result.get("status").toString())) {
                                try {
                                    code = 30;
                                    // thẻ được chấp nhận
                                    userMap = client.getMap("users");
                                    user = (UserCacheModel) userMap.get(nickname);
                                    long moneyUser = user.getVin();
                                    long currentMoney = user.getVinTotal();
                                    long rechargeMoney = user.getRechargeMoney();

                                    if (code == 30) {
                                        UserServiceImpl userService = new UserServiceImpl();

                                        long money = Math.round((double) amount * GameCommon.getValueDouble("RATIO_RECHARGE_CARD"));
                                        //String nickname, String serial, String pin, int amount, String requestId, String requestTime, int code, String des, int money
                                        dao.saveLogRechargeByGachThe(nickname, serial, pin, amount, id, System.currentTimeMillis() + "", 30, "pending_card", currentMoney, provider.getValue(), platform, currentMoney, money, UserId, user.getUsername(), "gachthe",
                                                userService.getUserByNickName(nickname).getClient());
                                        description = "Kết quả: Đang xử lý, Serial: " + serial;
                                        String description1 = "Thẻ: " + provider.getName() + " " + amount + ", Pin: " + pin;
                                        LogMoneyUserMessage messageLog2 = new LogMoneyUserMessage(user.getId(), nickname, "RechargeByCard", description1, currentMoney, 0L, "vin", description, 0L, false, user.isBot());
                                        RMQApi.publishMessageLogMoney((LogMoneyUserMessage) messageLog2);
                                    }
                                } catch (Exception e2) {
                                    code = 1;
                                    RechargeServiceImpl.logger.debug(e2);
                                    MoneyLogger.log(nickname, "RechargeByCard", 0L, 0L, "vin", "Nap vin qua the", "1001", e2.getMessage());
                                } finally {

                                }
                            } else {
                                code = 1;
                            }
                        }
                    } else {
                        code = 35;
                        cardFail = true;
                    }
                } else {
                    code = 36;
                    cardFail = true;
                }
                if (cardFail) {
                    try {
                        userMap = client.getMap("users");
                        userMap.lock(nickname);
                        user = (UserCacheModel) userMap.get(nickname);
                        user.setRechargeFail(user.getRechargeFail() + 1);
                        user.setRechargeFailTime(new Date());
                        userMap.put(nickname, user);
                        res.setTime(time);
                    } catch (Exception e3) {
                        code = 1;
                        RechargeServiceImpl.logger.debug(e3);
                        MoneyLogger.log(nickname, "RechargeByCard", 0L, 0L, "vin", "Nap vin qua the", "1001", e3.getMessage());
//                        obj = null;
//                        return obj;
                    } finally {
                        userMap.unlock(nickname);
                    }
                }
                res.setFail(user.getRechargeFail());
            } catch (Exception e4) {
                code = 1;
                RechargeServiceImpl.logger.debug(e4);
                MoneyLogger.log(nickname, "RechargeByCard", 0L, 0L, "vin", "Nap vin qua the", "1001", e4.getMessage());
            }
        }
        res.setCode(code);
        return res;
    }

    public RechargeResponse rechargeByGachTheHaDongPho(String nickname, ProviderType provider, String serial, String pin, String sAmount, String platform, int UserId) throws Exception {
        //RechargeServiceImpl.logger.debug((Object)("Start rechargeByCard Gachthe:  nickname: " + nickname + ", provider: " + provider.getName() + ", serial: " + serial + ", pin: " + pin));
        Debug.trace((Object) ("Start rechargeByCard Gachthe:  nickname: " + nickname + ", provider: " + provider.getName() + ", serial: " + serial + ", pin: " + pin));
        int code = 1;
        HazelcastInstance client;
        IMap<String, UserModel> userMap;
        String description;
        RechargeResponse res = new RechargeResponse(code, 0L, 0, 0L);
        if (GameCommon.getValueInt("IS_RECHARGE_CARD") == 1 || provider == null) {
            RechargeServiceImpl.logger.debug("rechargeByCard: param fail");
            return res;
        }
        if (pin != null) {
            pin = pin.trim();
        }
        if (serial != null) {
            serial = serial.trim();
        }
        Debug.trace("rechargeByGachThe  abccc");
        if ((client = HazelcastClientFactory.getInstance()) == null) {
            MoneyLogger.log(nickname, "RechargeByCard", 0L, 0L, "vin", "Nap vin qua the", "1030", "can not connect hazelcast");
            return res;
        }
        long amount = Long.parseLong(sAmount);
        userMap = client.getMap("users");
        RechargeByCardMessage message = null;
        if (userMap.containsKey(nickname)) {
            try {
                UserCacheModel user = (UserCacheModel) userMap.get(nickname);
                long time = DvtUtils.checkRechargeFail(user.getRechargeFail(), user.getRechargeFailTime());
                res.setCurrentMoney(user.getVinTotal());
                boolean cardFail = false;
                if (UserValidaton.validateSerialPin(serial)) {
                    if (UserValidaton.validateSerialPin(pin)) {
                        // check exists card pending
                        RechargeDaoImpl dao = new RechargeDaoImpl();
                        Document doc = dao.getRechargeByGachthe(nickname, serial, pin);
                        Debug.trace("rechargeByGachThe  22222");
                        if (doc != null) {
                            // pending card
                            code = 30;
                        } else {
                            String id = VinPlayUtils.genTransactionId(user.getId());
                            res.setTid(id);

                            Debug.trace("rechargeByGachThe  xxxxx " + id);
                            GachTheClient gachTheClient = new GachTheClient();
                            org.json.simple.JSONObject result = gachTheClient.doChargeHaDongPho(provider.getId(), provider.getValue().toUpperCase(), pin, serial, id, amount);
                            RechargeServiceImpl.logger.debug(result.toJSONString());
                            Debug.trace("rechargeByGachThe: " + result.toJSONString());
                            res.setMessage(result.get("message").toString());
                            if ("0".equals(result.get("status").toString())) {
                                code = 1;
                            } else if ("-101".equals(result.get("status").toString())) {
                                code = 35;
                            } else if ("1".equals(result.get("status").toString())) {
                                try {
                                    code = 30;
                                    // thẻ được chấp nhận
                                    userMap = client.getMap("users");
                                    user = (UserCacheModel) userMap.get(nickname);
                                    long moneyUser = user.getVin();
                                    long currentMoney = user.getVinTotal();
                                    long rechargeMoney = user.getRechargeMoney();

                                    if (code == 30) {
                                        UserServiceImpl userService = new UserServiceImpl();

                                        long money = Math.round((double) amount * GameCommon.getValueDouble("RATIO_RECHARGE_CARD"));
                                        //String nickname, String serial, String pin, int amount, String requestId, String requestTime, int code, String des, int money
                                        dao.saveLogRechargeByGachThe(nickname, serial, pin, amount, id, System.currentTimeMillis() + "", 30, "pending_card", currentMoney, provider.getValue(), platform, currentMoney, money, UserId, user.getUsername(), "gachthe",
                                                userService.getUserByNickName(nickname).getClient());
                                        description = "Kết quả: Đang xử lý, Serial: " + serial;
                                        String description1 = "Thẻ: " + provider.getName() + " " + amount + ", Pin: " + pin;
                                        LogMoneyUserMessage messageLog2 = new LogMoneyUserMessage(user.getId(), nickname, "RechargeByCard", description1, currentMoney, 0L, "vin", description, 0L, false, user.isBot());
                                        RMQApi.publishMessageLogMoney((LogMoneyUserMessage) messageLog2);
                                    }
                                } catch (Exception e2) {
                                    code = 1;
                                    RechargeServiceImpl.logger.debug(e2);
                                    MoneyLogger.log(nickname, "RechargeByCard", 0L, 0L, "vin", "Nap vin qua the", "1001", e2.getMessage());
                                } finally {

                                }
                            } else {
                                code = 1;
                            }
                        }
                    } else {
                        code = 35;
                        cardFail = true;
                    }
                } else {
                    code = 36;
                    cardFail = true;
                }
                if (cardFail) {
                    try {
                        userMap = client.getMap("users");
                        userMap.lock(nickname);
                        user = (UserCacheModel) userMap.get(nickname);
                        user.setRechargeFail(user.getRechargeFail() + 1);
                        user.setRechargeFailTime(new Date());
                        userMap.put(nickname, user);
                        res.setTime(time);
                    } catch (Exception e3) {
                        code = 1;
                        RechargeServiceImpl.logger.debug(e3);
                        MoneyLogger.log(nickname, "RechargeByCard", 0L, 0L, "vin", "Nap vin qua the", "1001", e3.getMessage());
//                        obj = null;
//                        return obj;
                    } finally {
                        userMap.unlock(nickname);
                    }
                }
                res.setFail(user.getRechargeFail());
            } catch (Exception e4) {
                code = 1;
                RechargeServiceImpl.logger.debug(e4);
                MoneyLogger.log(nickname, "RechargeByCard", 0L, 0L, "vin", "Nap vin qua the", "1001", e4.getMessage());
            }
        }
        res.setCode(code);
        return res;
    }

    // xử lý gach thẻ callback
    public String receiveResultFromIwin99BuyCard(Map<String, String[]> request) {
        {
            Iwin99CallbackResponse response = new Iwin99CallbackResponse(-1, "");
            try {
                logger.debug("Start receiveResultFromIwin99BuyCard: ");
                StringBuilder log = new StringBuilder();
                HashMap<String, String> fields = new HashMap<>();
                for (Map.Entry<String, String[]> entry : request.entrySet()) {
                    String fieldName = entry.getKey();
                    String fieldValue = entry.getValue()[0];
                    log.append(fieldName).append(":").append(fieldValue).append(", ");
                    if (fieldValue == null || fieldValue.length() <= 0) {
                        continue;
                    }
                    fields.put(fieldName, fieldValue);
                }
                String signature = fields.get("signature");
//                String encryptString = PartnerConfig.Iwin99BuyCardToken + fields.get("transaction_id");
                //logger.debug(encryptString);
                String md5Hex = DigestUtils.md5Hex(PartnerConfig.GachTheClientId + "|" + PartnerConfig.GachTheSecretKey + "|" + fields.get("transaction_id") + "|" + fields.get("status"));
//                String md5Hex = DigestUtils.md5Hex(encryptString);
                logger.debug("signature = : " + md5Hex);
                boolean isSignature = md5Hex.equals(signature);
                if (!isSignature) {
                    response.setErrorCode(1);
                    response.setErrorDescription("invalid signature");
                    return response.toJson();
                }
                logger.debug("receiveResultFromIwin99BuyCard ==>> Paramester: " + log.toString());
                RechargeByCardMessage message;
                HazelcastInstance client;
                client = HazelcastClientFactory.getInstance();
                // tìm theo id
                RechargeDaoImpl dao = new RechargeDaoImpl();
                Document trans = dao.getRechargeByGachthe(fields.get("transaction_id"));
                if (trans != null && trans.getInteger("code") == 30) {
//                    //Recheck:
//                    TlxGachTheClient tlxGachTheClient = new TlxGachTheClient(PartnerConfig.TlxGachThePrivateKey,
//                        PartnerConfig.TlxGachTheSecretKey, PartnerConfig.TlxGachTheDepositUrl,
//                        PartnerConfig.TlxGachTheRecheckUrl);
//                    TlxRecheckResponse tlxRecheckResponse = tlxGachTheClient.recheck(fields.get("tran_code").toString());
                    String nickname = trans.getString("nick_name");
//                    if(trans.getLong("amount") == Double.parseDouble(fields.get("amount"))){
//                        return "amount not same";
//                    }
                    if (StringUtils.equals(fields.get("status").toLowerCase(), "1")) {
                        IMap<String, UserModel> userMap;
                        //get user
                        userMap = client.getMap("users");
                        userMap.lock(nickname);
                        UserCacheModel user = (UserCacheModel) userMap.get(nickname);
                        try {
                            long cardAmount = Long.parseLong(fields.get("value"));
                            message = new RechargeByCardMessage(nickname, trans.getString("request_id"), trans.getString("provider"),
                                    trans.getString("serial"), trans.getString("pin"), (int) cardAmount, 1, "Thanh cong", 1, (int) cardAmount,
                                    null, null, "gachthe", trans.getString("platform"), null);
                            String description;
                            long moneyUser = user.getVin();
                            long currentMoney = user.getVinTotal();
                            long rechargeMoney = user.getRechargeMoney();
                            // Thẻ ok
                            long money = Math.round(cardAmount/* * GameCommon.getValueDouble("RATIO_RECHARGE_CARD")*/);
                            user.setVin(moneyUser += money);
                            user.setVinTotal(currentMoney += money);
                            user.setRechargeMoney(rechargeMoney += money);
                            user.setRechargeFail(0);
                            description = "K\u1ebft qu\u1ea3: Th\u00e0nh c\u00f4ng, M\u00e3 GD: " + trans.getString("request_id") + ", Th\u1ebb: " + trans.getString("provider") + ", M\u1ec7nh gi\u00e1: " + cardAmount + ", Serial: " + trans.getString("serial") + ", Pin: " + trans.getString("pin") + "";
                            MoneyMessageInMinigame messageMoney = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), user.getId(), nickname, "RechargeByCard", moneyUser, currentMoney, money, "vin", 0L, 0, 0);
                            LogMoneyUserMessage messageLog = new LogMoneyUserMessage(user.getId(), nickname, "RechargeByCard", "N\u1ea1p Win qua th\u1ebb", currentMoney, money, "vin", description, 0L, false, user.isBot());
                            RMQApi.publishMessagePayment(messageMoney, 16);
                            RMQApi.publishMessageLogMoney(messageLog);
                            RMQApi.publishMessage("queue_dvt", message, 301);
                            userMap.put(nickname, user);
                            // update trans success
                            dao.UpdateGachtheTransctions(fields.get("transaction_id"), 0, "success", money);
                            logger.debug("receiveResultFromIwin99BuyCard ==>>UpdateGachtheTransctions success ");
                            response.setErrorCode(0);
                            //RMQApi.publishMessage((String) "queue_dvt", (BaseMessage) message, (int) 301);
                        } catch (Exception ex) {
                            logger.debug(ex);
                            MoneyLogger.log("", "RechargeByGachThe", 0L, 0L, "vin", "Nap vin qua the cao", "1001", ex.getMessage());
                            return ex.getMessage();
                        } finally {
                            userMap.unlock(nickname);
                        }
                    } else {
                        // failed
                        logger.debug("receiveResultFromIwin99BuyCard ==>>UpdateGachtheTransctions false " + fields.get("transaction_id"));
                        message = new RechargeByCardMessage(nickname, trans.getString("request_id"), trans.getString("provider"),
                                trans.getString("serial"), trans.getString("pin"), 0, -1, "The loi", -1, 0,
                                null, null, "gachthe", trans.getString("platform"), null);
                        RMQApi.publishMessage("queue_dvt", message, 301);
                        dao.UpdateGachtheTransctions(fields.get("transaction_id"), -1, "failed", 0);
                        response.setErrorCode(3);
                    }
                    response.setErrorDescription("valid trans");
                    return response.toJson();
                } else {
                    logger.debug("receiveResultFromIwin99BuyCard ==>> Can't find transaction: ");
                    response.setErrorDescription("Can't find transactionRechargeByGachthe");
                    return response.toJson();
                }
            } catch (Exception e4) {
                logger.debug(e4);
                MoneyLogger.log("", "RechargeByGachThe", 0L, 0L, "vin", "Nap vin qua the cao", "1001", e4.getMessage());
                return response.toJson();
            }
        }
    }


    // xử lý gach thẻ callback
    public String receiveResultFromHaDongPhoBuyCard(Map<String, String[]> request) {
        {
            Iwin99CallbackResponse response = new Iwin99CallbackResponse(-1, "");
            try {
                logger.debug("Start receiveResultFromIwin99BuyCard: ");
                StringBuilder log = new StringBuilder();
                HashMap<String, String> fields = new HashMap<>();
                for (Map.Entry<String, String[]> entry : request.entrySet()) {
                    String fieldName = entry.getKey();
                    String fieldValue = entry.getValue()[0];
                    log.append(fieldName).append(":").append(fieldValue).append(", ");
                    if (fieldValue == null || fieldValue.length() <= 0) {
                        continue;
                    }
                    fields.put(fieldName, fieldValue);
                }

                logger.debug("receiveResultFromIwin99BuyCard ==>> Paramester: " + log.toString());
                RechargeByCardMessage message;
                HazelcastInstance client;
                client = HazelcastClientFactory.getInstance();
                // tìm theo id
                RechargeDaoImpl dao = new RechargeDaoImpl();
                Document trans = dao.getRechargeByGachthe(fields.get("refcode"));
                if (trans != null && trans.getInteger("code") == 30) {
//                    //Recheck:
//                    TlxGachTheClient tlxGachTheClient = new TlxGachTheClient(PartnerConfig.TlxGachThePrivateKey,
//                        PartnerConfig.TlxGachTheSecretKey, PartnerConfig.TlxGachTheDepositUrl,
//                        PartnerConfig.TlxGachTheRecheckUrl);
//                    TlxRecheckResponse tlxRecheckResponse = tlxGachTheClient.recheck(fields.get("tran_code").toString());
                    String nickname = trans.getString("nick_name");
//                    if(trans.getLong("amount") == Double.parseDouble(fields.get("amount"))){
//                        return "amount not same";
//                    }
                    if (StringUtils.equals(fields.get("status").toLowerCase(), "1")) {
                        IMap<String, UserModel> userMap;
                        //get user
                        userMap = client.getMap("users");
                        userMap.lock(nickname);
                        UserCacheModel user = (UserCacheModel) userMap.get(nickname);
                        try {
                            long cardAmount = Long.parseLong(fields.get("realvalue"));
                            message = new RechargeByCardMessage(nickname, trans.getString("request_id"), trans.getString("provider"),
                                    trans.getString("serial"), trans.getString("pin"), (int) cardAmount, 1, "Thanh cong", 1, (int) cardAmount,
                                    null, null, "gachthe", trans.getString("platform"), null);
                            String description;
                            long moneyUser = user.getVin();
                            long currentMoney = user.getVinTotal();
                            long rechargeMoney = user.getRechargeMoney();
                            // Thẻ ok
                            long money = Math.round(cardAmount/* * GameCommon.getValueDouble("RATIO_RECHARGE_CARD")*/);
                            user.setVin(moneyUser += money);
                            user.setVinTotal(currentMoney += money);
                            user.setRechargeMoney(rechargeMoney += money);
                            user.setRechargeFail(0);
                            description = "K\u1ebft qu\u1ea3: Th\u00e0nh c\u00f4ng, M\u00e3 GD: " + trans.getString("request_id") + ", Th\u1ebb: " + trans.getString("provider") + ", M\u1ec7nh gi\u00e1: " + cardAmount + ", Serial: " + trans.getString("serial") + ", Pin: " + trans.getString("pin") + "";
                            MoneyMessageInMinigame messageMoney = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), user.getId(), nickname, "RechargeByCard", moneyUser, currentMoney, money, "vin", 0L, 0, 0);
                            LogMoneyUserMessage messageLog = new LogMoneyUserMessage(user.getId(), nickname, "RechargeByCard", "N\u1ea1p Win qua th\u1ebb", currentMoney, money, "vin", description, 0L, false, user.isBot());
                            RMQApi.publishMessagePayment(messageMoney, 16);
                            RMQApi.publishMessageLogMoney(messageLog);
                            RMQApi.publishMessage("queue_dvt", message, 301);
                            userMap.put(nickname, user);
                            // update trans success
                            dao.UpdateGachtheTransctions(fields.get("refcode"), 0, "success", money);
                            logger.debug("receiveResultFromIwin99BuyCard ==>>UpdateGachtheTransctions success ");
                            response.setErrorCode(0);
                            //RMQApi.publishMessage((String) "queue_dvt", (BaseMessage) message, (int) 301);
                        } catch (Exception ex) {
                            logger.debug(ex);
                            MoneyLogger.log("", "RechargeByGachThe", 0L, 0L, "vin", "Nap vin qua the cao", "1001", ex.getMessage());
                            return ex.getMessage();
                        } finally {
                            userMap.unlock(nickname);
                        }
                    } else {
                        // failed
                        logger.debug("receiveResultFromIwin99BuyCard ==>>UpdateGachtheTransctions false " + fields.get("refcode"));
                        message = new RechargeByCardMessage(nickname, trans.getString("request_id"), trans.getString("provider"),
                                trans.getString("serial"), trans.getString("pin"), 0, -1, "The loi", -1, 0,
                                null, null, "gachthe", trans.getString("platform"), null);
                        RMQApi.publishMessage("queue_dvt", message, 301);
                        dao.UpdateGachtheTransctions(fields.get("refcode"), -1, "failed", 0);
                        response.setErrorCode(3);
                    }
                    response.setErrorDescription("valid trans");
                    return response.toJson();
                } else {
                    logger.debug("receiveResultFromIwin99BuyCard ==>> Can't find transaction: ");
                    response.setErrorDescription("Can't find transactionRechargeByGachthe");
                    return response.toJson();
                }
            } catch (Exception e4) {
                logger.debug(e4);
                MoneyLogger.log("", "RechargeByGachThe", 0L, 0L, "vin", "Nap vin qua the cao", "1001", e4.getMessage());
                return response.toJson();
            }
        }
    }


    @Override
    public String rechargeByMomoIwin99(Map<String, String[]> request) {
        Iwin99CallbackResponse response = new Iwin99CallbackResponse(-1, "");
        try {
//            Debug.trace("Start cashOutByMomoIwin99");
            StringBuilder parameter = new StringBuilder();
            HashMap<String, String> fields = new HashMap<>();
            for (Map.Entry<String, String[]> entry : request.entrySet()) {
                String fieldName = entry.getKey();
                String fieldValue = entry.getValue()[0];
                parameter.append(fieldName).append(":").append(fieldValue).append(", ");
                if (fieldValue == null || fieldValue.length() <= 0) {
                    continue;
                }
                fields.put(fieldName, fieldValue);
            }
//            Debug.trace("cashOutByMomoIwin99 ==>> parameter: " + parameter.toString());
            String requestId = fields.get("requestId");
            String momoTransId = fields.get("momoTransId");
//            "waiting" giao dịch đang chờ xử lý (đang chờ user nhắn tin, chờ tin nhắn đc gửi tới đầu số dịch vụ..)
//            "success" giao dịch đã thành công
//            "timeout" quá thời gian giao dịch
            String status = fields.get("status");
            String money = fields.get("chargeAmount");
            String chargeId = fields.get("chargeId");
            String chargeCode = fields.get("chargeCode");
            String signature = fields.get("signature");
            RechargeDao dao = new RechargeDaoImpl();
            if (StringUtils.isEmpty(chargeId) || StringUtils.isEmpty(chargeCode) || StringUtils.isEmpty(money)
                    || StringUtils.isEmpty(status) || StringUtils.isEmpty(momoTransId) || StringUtils.isEmpty(requestId)
                    || StringUtils.isEmpty(signature)) {
//                Debug.trace("cashOutByMomoIwin99 ==>> invalid param");
                response.setErrorDescription("Parameter null or empty");
                response.setErrorCode(1);
                dao.UpdateDepositMomoManualStatus(requestId, DvtConst.STATUS_REJECT, status, "Auto", Long.parseLong(money));
                return response.toJson();
            }


            //md5(chargeId + chargeType + chargeCode + chargeAmount + status + requestId + loginPW)
            String encryptString = chargeId + "momo" + chargeCode + money + status + requestId + "Kh!!xyz1";

            logger.debug("encryptString: " + encryptString);;
            String md5Hex = DigestUtils.md5Hex(encryptString);
            logger.debug("encryptString md5Hex: " + md5Hex);;
//            Debug.trace("cashOutByMomoIwin99 ==>> signature = : " + md5Hex);
            boolean isValidSignature = md5Hex.equals(signature);
            if (!isValidSignature) {
//                Debug.trace("cashOutByMomoIwin99 ==>> signature param");
                response.setErrorCode(2);
                response.setErrorDescription("signature invalid");
                dao.UpdateDepositMomoManualStatus(requestId, DvtConst.STATUS_REJECT, status, "Auto", Long.parseLong(money));
                return response.toJson();
            }


            // find transaction in db
            DepositMomoModel trans = dao.FindDepositMomoById(requestId);
            if (trans == null) {
//                Debug.trace("cashOutByMomoIwin99 ==>> Can't find transaction pending by nickname and amount = ");
                response.setErrorDescription("Can't find request id.");
                return response.toJson();
            }

            if (trans.Description.contains(momoTransId)) {
//                Debug.trace("cashOutByMomoIwin99 ==>> The transaction has been processed");
                response.setErrorDescription("The transaction by momoTransId has been processed.");
                dao.UpdateDepositMomoManualStatus(requestId, DvtConst.STATUS_REJECT, status, "Auto", Long.parseLong(money));
                return response.toJson();
            }

            if (!status.equals("success")) {
//                Debug.trace("cashOutByMomoIwin99 ==>> " + status);
                response.setErrorDescription(status);
                dao.UpdateDepositMomoManualStatus(requestId, DvtConst.STATUS_REJECT, status, "Auto", Long.parseLong(money));
                return response.toJson();
            }

            boolean resultUpdateTrans = dao.UpdateDepositMomoManualStatus(trans.Id, DvtConst.STATUS_APPROVE, "Approved by Momo iwin99: " + momoTransId, "Auto", Long.parseLong(money));
            if (!resultUpdateTrans) {
//                Debug.trace("cashOutByMomoIwin99 ==>> Can't update status transaction");
                response.setErrorDescription("Can't update status transaction");
                return response.toJson();
            }
            UserServiceImpl service = new UserServiceImpl();
            double fee = GameCommon.getValueDouble("RATIO_RECHARGE_MOMO");
            double amount = fee * Double.parseDouble(money);
            long totalFee = Math.round(Double.parseDouble(money) - amount);
            totalFee = totalFee > 0 ? totalFee : 0;
            BaseResponseModel response1 = service.updateMoneyFromAdmin(trans.Nickname, Long.parseLong(money), "vin", "RechargeByMomo", "Deposit momo", "Deposit momo", totalFee, false);
            if (response1.isSuccess()) {
                logger.debug("cashOutByMomoIwin99 ==>> Approval transaction success");
                response.setErrorDescription("Approval transaction success");
                response.setErrorCode(0);
            } else {
                logger.debug("cashOutByMomoIwin99 ==>> Update money to user fail");
                response.setErrorDescription("Update money to user fail");
            }
            return response.toJson();
        } catch (Exception e) {
            logger.debug("cashOutByMomoIwin99 ==>> error" + e.getMessage());
            response.setErrorDescription("Handle Error");
            return response.toJson();
        }
    }


    @Override
    public String rechargeByMomoHaDongPho(String body) {
        Iwin99CallbackResponse response = new Iwin99CallbackResponse(-1, "");
        try {
//            Debug.trace("Start cashOutByMomoIwin99");

            JSONObject jsonObject = new JSONObject(body);

            String requestId = jsonObject.getString("bank_id");
            String status = jsonObject.getString("status");
            String money = jsonObject.getString("amount");
            String msg = jsonObject.getString("msg");
            logger.debug("bank_id: " + requestId + " status: " + status + " money: " + money + " msg: " + msg);

            RechargeDao dao = new RechargeDaoImpl();
            if (StringUtils.isEmpty(money)
                    || StringUtils.isEmpty(status) || StringUtils.isEmpty(requestId)) {
                response.setErrorDescription("Parameter null or empty");
                response.setErrorCode(1);
                dao.UpdateDepositMomoManualStatus(requestId, DvtConst.STATUS_REJECT, status, "Auto", Long.parseLong(money));
                return response.toJson();
            }

            // find transaction in db
            DepositMomoModel trans = dao.FindDepositMomoById(requestId);
            if (trans == null) {
                response.setErrorDescription("Can't find request id.");
                return response.toJson();
            }


            if (!status.equals("1")) {
                response.setErrorDescription(status);
                dao.UpdateDepositMomoManualStatus(requestId, DvtConst.STATUS_REJECT, status, "Auto", Long.parseLong(money));
                return response.toJson();
            }

            boolean resultUpdateTrans = dao.UpdateDepositMomoManualStatus(trans.Id, DvtConst.STATUS_APPROVE, "Approved by Momo HaDongPho " + msg, "Auto", Long.parseLong(money));
            if (!resultUpdateTrans) {
                response.setErrorDescription("Can't update status transaction");
                return response.toJson();
            }
            UserServiceImpl service = new UserServiceImpl();
            double fee = GameCommon.getValueDouble("RATIO_RECHARGE_MOMO");
            double amount = fee * Double.parseDouble(money);
            long totalFee = Math.round(Double.parseDouble(money) - amount);
            totalFee = totalFee > 0 ? totalFee : 0;
            BaseResponseModel response1 = service.updateMoneyFromAdmin(trans.Nickname, Long.parseLong(money), "vin", "RechargeByMomo", "Deposit momo", "Deposit momo", totalFee, false);
            if (response1.isSuccess()) {
                response.setErrorDescription("Approval transaction success");
                response.setErrorCode(0);
            } else {
                response.setErrorDescription("Update money to user fail");
            }
            return response.toJson();
        } catch (Exception e) {
            logger.debug("cashOutByMomoIwin99 ==>> error" + e.getMessage());
            response.setErrorDescription("Handle Error");
            return response.toJson();
        }
    }

    @Override
    public String rechargeByBankHaDongPho(Map<String, String[]> request) {
        Iwin99CallbackResponse response = new Iwin99CallbackResponse(-1, "");
        try {
//            Debug.trace("Start cashOutByMomoIwin99");
            StringBuilder parameter = new StringBuilder();
            HashMap<String, String> fields = new HashMap<>();
            for (Map.Entry<String, String[]> entry : request.entrySet()) {
                String fieldName = entry.getKey();
                String fieldValue = entry.getValue()[0];
                parameter.append(fieldName).append(":").append(fieldValue).append(", ");
                if (fieldValue == null || fieldValue.length() <= 0) {
                    continue;
                }
                fields.put(fieldName, fieldValue);
            }

            String requestId = fields.get("bank_id");
            String status = fields.get("status");
            String money = fields.get("amount");
            String msg = fields.get("msg");
            logger.debug("bank_id: " + requestId + " status: " + status + " money: " + money + " msg: " + msg);

            RechargeDao dao = new RechargeDaoImpl();
            if (StringUtils.isEmpty(money)
                    || StringUtils.isEmpty(status) || StringUtils.isEmpty(requestId)) {
                response.setErrorDescription("Parameter null or empty");
                response.setErrorCode(1);
                dao.UpdateDepositBankManualStatus(requestId, DvtConst.STATUS_REJECT, status, "Auto", Long.parseLong(money));
                return response.toJson();
            }

            // find transaction in db
            DepositBankModel trans = dao.FindDepositBankById(requestId);
            if (trans == null) {
                response.setErrorDescription("Can't find request id.");
                return response.toJson();
            }


            if (!status.equals("1")) {
                response.setErrorDescription(status);
                dao.UpdateDepositBankManualStatus(requestId, DvtConst.STATUS_REJECT, status, "Auto", Long.parseLong(money));
                return response.toJson();
            }

            boolean resultUpdateTrans = dao.UpdateDepositBankManualStatus(trans.Id, DvtConst.STATUS_APPROVE, "Approved by bank HaDongPho " + msg, "Auto", Long.parseLong(money));
            if (!resultUpdateTrans) {
                response.setErrorDescription("Can't update status transaction");
                return response.toJson();
            }
            UserServiceImpl service = new UserServiceImpl();
            double fee = GameCommon.getValueDouble("RATIO_RECHARGE_MOMO");
            double amount = fee * Double.parseDouble(money);
            long totalFee = Math.round(Double.parseDouble(money) - amount);
            totalFee = totalFee > 0 ? totalFee : 0;
            BaseResponseModel response1 = service.updateMoneyFromAdmin(trans.Nickname, Long.parseLong(money), "vin", "RechargeByBank", "Deposit bank", "Deposit bank", totalFee, false);
            if (response1.isSuccess()) {
                response.setErrorDescription("Approval transaction success");
                response.setErrorCode(0);
            } else {
                response.setErrorDescription("Update money to user fail");
            }
            return response.toJson();
        } catch (Exception e) {
            logger.debug("cashOutByMomoIwin99 ==>> error" + e.getMessage());
            response.setErrorDescription("Handle Error");
            return response.toJson();
        }
    }


    @Override
    public String rechargeByBank(Map<String, String[]> request) {
        Iwin99CallbackResponse response = new Iwin99CallbackResponse(-1, "");
        try {
//            Debug.trace("Start cashOutByMomoIwin99");
            StringBuilder parameter = new StringBuilder();
            HashMap<String, String> fields = new HashMap<>();
            for (Map.Entry<String, String[]> entry : request.entrySet()) {
                String fieldName = entry.getKey();
                String fieldValue = entry.getValue()[0];
                parameter.append(fieldName).append(":").append(fieldValue).append(", ");
                if (fieldValue == null || fieldValue.length() <= 0) {
                    continue;
                }
                fields.put(fieldName, fieldValue);
            }

            String requestId = fields.get("requestId");
            String status = fields.get("status");
            String money = fields.get("chargeAmount");
            String chargeId = fields.get("chargeId");
            String chargeCode = fields.get("chargeCode");
            String signature = fields.get("signature");
            String msg = fields.get("result");
            logger.debug("bank_id: " + requestId + " status: " + status + " money: " + money + " signature: " + signature + " result: " + msg);

            RechargeDao dao = new RechargeDaoImpl();
            if (StringUtils.isEmpty(money)
                    || StringUtils.isEmpty(status) || StringUtils.isEmpty(requestId)) {
                response.setErrorDescription("Parameter null or empty");
                response.setErrorCode(1);
                dao.UpdateDepositBankManualStatus(requestId, DvtConst.STATUS_REJECT, status, "Auto", Long.parseLong(money));
                return response.toJson();
            }

            String encryptString = chargeId + "bank" + chargeCode + money + status + requestId + "ToToRo$$22";

            logger.debug("encryptString: " + encryptString);;
            String md5Hex = DigestUtils.md5Hex(encryptString);
            logger.debug("encryptString md5Hex: " + md5Hex);;
//            Debug.trace("cashOutByMomoIwin99 ==>> signature = : " + md5Hex);
            boolean isValidSignature = md5Hex.equals(signature);
            if (!isValidSignature) {
//                Debug.trace("cashOutByMomoIwin99 ==>> signature param");
                response.setErrorCode(2);
                response.setErrorDescription("signature invalid");
                dao.UpdateDepositMomoManualStatus(requestId, DvtConst.STATUS_REJECT, status, "Auto", Long.parseLong(money));
                return response.toJson();
            }

            // find transaction in db
            DepositBankModel trans = dao.FindDepositBankById(requestId);
            if (trans == null) {
                response.setErrorDescription("Can't find request id.");
                return response.toJson();
            }


            if (!status.equals("1")) {
                response.setErrorDescription(status);
                dao.UpdateDepositBankManualStatus(requestId, DvtConst.STATUS_REJECT, status, "Auto", Long.parseLong(money));
                return response.toJson();
            }

            boolean resultUpdateTrans = dao.UpdateDepositBankManualStatus(trans.Id, DvtConst.STATUS_APPROVE, "Approved by bank  " + msg, "Auto", Long.parseLong(money));
            if (!resultUpdateTrans) {
                response.setErrorDescription("Can't update status transaction");
                return response.toJson();
            }
            UserServiceImpl service = new UserServiceImpl();
            double fee = GameCommon.getValueDouble("RATIO_RECHARGE_MOMO");
            double amount = fee * Double.parseDouble(money);
            long totalFee = Math.round(Double.parseDouble(money) - amount);
            totalFee = totalFee > 0 ? totalFee : 0;
            BaseResponseModel response1 = service.updateMoneyFromAdmin(trans.Nickname, Long.parseLong(money), "vin", "RechargeByBank", "Deposit bank", "Deposit bank", totalFee, false);
            if (response1.isSuccess()) {
                response.setErrorDescription("Approval transaction success");
                response.setErrorCode(0);
            } else {
                response.setErrorDescription("Update money to user fail");
            }
            return response.toJson();
        } catch (Exception e) {
            logger.debug("cashOutByMomoIwin99 ==>> error" + e.getMessage());
            response.setErrorDescription("Handle Error");
            return response.toJson();
        }
    }

    private String mapVinplayLucky79Message(String lucky79Code) {
        String trim;
        switch (trim = lucky79Code.trim()) {
            case "waiting": {
                return "Th\u1ebb \u0111ang ch\u1edd \u0111\u1ec3 n\u1ea1p";
            }
            case "processing": {
                return "Th\u1ebb \u0111ang \u0111\u01b0\u1ee3c n\u1ea1p tr\u00ean thi\u1ebft b\u1ecb";
            }
            case "success": {
                return "N\u1ea1p th\u1ebb th\u00e0nh c\u00f4ng";
            }
            case "card_fail": {
                return "N\u1ea1p Kh\u00f4ng th\u1ebb th\u00e0nh c\u00f4ng";
            }
        }
        return "Tr\u1ea1ng th\u00e1i kh\u00f4ng x\u00e1c \u0111\u1ecbnh";
    }

    private int mapLucky79ToVinplayCode(String lucky79Code) {
        String trim;
        switch (trim = lucky79Code.trim()) {
            case "success": {
                return 0;
            }
            case "card_fail": {
                return 35;
            }
            case "processing": {
                return 30;
            }
            case "waiting": {
                return 30;
            }
        }
        return 30;
    }

    private String mapVinplayMessage(String maxpayCode) {
        switch (maxpayCode) {
            case "200": {
                return "G\u1ecdi api th\u00e0nh c\u00f4ng";
            }
            case "400": {
                return "D\u1eef li\u1ec7u g\u1eedi l\u00ean kh\u00f4ng ch\u00ednh x\u00e1c";
            }
            case "404": {
                return "Kh\u00f4ng t\u00ecm th\u1ea5y giao d\u1ecbch th\u1ebb";
            }
            case "1": {
                return "N\u1ea1p th\u1ebb th\u00e0nh c\u00f4ng";
            }
            case "2": {
                return "Th\u1ebb sai ho\u1eb7c \u0111\u00e3 s\u1eed d\u1ee5ng";
            }
            case "3": {
                return "Th\u1ebb b\u1ecb kh\u00f3a";
            }
            case "4": {
                return "S\u1ed1 l\u1ea7n n\u1ea1p th\u1ebb sai li\u00ean ti\u1ebfp v\u01b0\u1ee3t quy \u0111\u1ecbnh";
            }
            case "5": {
                return "Th\u00f4ng tin merchant sai";
            }
            case "6": {
                return "Ch\u01b0a truy\u1ec1n transaction id";
            }
            case "7": {
                return "Th\u1ebb sai \u0111\u1ecbnh d\u1ea1ng";
            }
            case "8": {
                return "Kh\u00f4ng t\u00ecm th\u1ea5y nh\u00e0 cung c\u1ea5p th\u1ebb";
            }
            case "9": {
                return "Th\u00f4ng tin Session sai";
            }
            case "10": {
                return "Session timeout";
            }
            case "11": {
                return "L\u1ed7i h\u1ec7 th\u1ed1ng nh\u00e0 cung c\u1ea5p";
            }
            case "12": {
                return "Merchant b\u1ecb kh\u00f3a";
            }
            case "13": {
                return "Ip kh\u00f4ng h\u1ee3p l\u1ec7";
            }
            case "14": {
                return "Transaction id b\u1ecb tr\u00f9ng";
            }
            case "15": {
                return "Th\u1ebb kh\u00f4ng h\u1ee3p l\u1ec7";
            }
            case "16": {
                return "Lo\u1ea1i th\u1ebb b\u1ecb kh\u00f3a";
            }
            case "17": {
                return "Th\u1ebb \u0111ang x\u1eed l\u00fd";
            }
            case "96": {
                return "N\u1ea1p th\u1ebb th\u1ea5t b\u1ea1i";
            }
            case "98": {
                return "Ch\u1edd x\u1eed l\u00fd";
            }
            case "99": {
                return "Tr\u1ea1ng th\u00e1i kh\u00f4ng x\u00e1c \u0111\u1ecbnh";
            }
        }
        return "Tr\u1ea1ng th\u00e1i kh\u00f4ng x\u00e1c \u0111\u1ecbnh";
    }

    private int mapVinplayCode(String maxpayCode) {
        switch (maxpayCode) {
            case "1": {
                return 0;
            }
            case "2": {
                return 31;
            }
            case "3": {
                return 32;
            }
            case "16": {
                return 32;
            }
            case "7": {
                return 35;
            }
            case "4": {
                return 1;
            }
            case "400": {
                return 1;
            }
            case "404": {
                return 1;
            }
            case "8": {
                return 1;
            }
            case "15": {
                return 1;
            }
            case "96": {
                return 1;
            }
            case "9": {
                return 1;
            }
            case "10": {
                return 1;
            }
            case "11": {
                return 1;
            }
            case "12": {
                return 1;
            }
            case "14": {
                return 1;
            }
            case "5": {
                return 1;
            }
            case "6": {
                return 1;
            }
            case "13": {
                return 1;
            }
            case "17": {
                return 30;
            }
            case "98": {
                return 30;
            }
            case "99": {
                return 30;
            }
        }
        return 30;
    }

    private void alert(String number, String content, boolean isCall) {
        AlertServiceImpl alertService = new AlertServiceImpl();
        if (number.contains(",")) {
            String[] arr = number.split(",");
            ArrayList<String> mList = new ArrayList<String>();
            for (String m : arr) {
                m = m.trim();
                mList.add(m);
            }
            alertService.alert2List(mList, content, isCall);
        } else {
            alertService.alert2One(number, content, isCall);
        }
    }

    private int mapPayVietToVinplayCode(String lucky79Code) {
        String trim;
        switch (trim = lucky79Code.trim()) {
            case "99": {
                return 30;
            }

        }
        return 35;
    }
}

