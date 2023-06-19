package com.vinplay.safebox.core.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.safebox.core.SafeBoxService;
import com.vinplay.safebox.dao.impl.SafeBoxDaoImpl;
import com.vinplay.safebox.response.SafeBoxResponse;
import com.vinplay.usercore.service.OtpService;
import com.vinplay.usercore.service.impl.OtpServiceImpl;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.messages.LogMoneyUserMessage;
import com.vinplay.vbee.common.messages.MoneyMessageInMinigame;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.rmq.RMQApi;
import com.vinplay.vbee.common.statics.Consts;
import com.vinplay.vbee.common.utils.VinPlayUtils;

public class SafeBoxImpl implements SafeBoxService {
    @Override
    public SafeBoxResponse depositSafeBox(String userName, double amount) {
        SafeBoxResponse safeBoxResponse = new SafeBoxResponse(1, "");
        HazelcastInstance client = HazelcastClientFactory.getInstance();
        if (client == null) {
            safeBoxResponse.message = "Không kết nối được";
            return safeBoxResponse;
        }
        IMap<String, UserModel> userMap = client.getMap("users");
        if (!userMap.containsKey(userName))
            return safeBoxResponse;

        try {
            userMap.lock(userName);
            UserCacheModel user = (UserCacheModel) userMap.get(userName);
            long moneyUser = user.getVin();
            long currentMoney = user.getVinTotal();
            long rechargeMoney = user.getRechargeMoney();

            if (currentMoney < amount) {
                safeBoxResponse.message = "Số tiền không đủ";
                return safeBoxResponse;
            }

            user.setVin(moneyUser -= amount);
            user.setVinTotal(currentMoney -= amount);
            user.setRechargeMoney(rechargeMoney -= amount);
            String desc = "Nạp tiền két sắt";
            MoneyMessageInMinigame messageMoney = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), user.getId(),
                    userName, Consts.RECHARGE_SAFE_BOX, moneyUser, currentMoney, (long) amount, "vin", 0L, 0, 0);
            LogMoneyUserMessage messageLog = new LogMoneyUserMessage(user.getId(), userName, Consts.RECHARGE_SAFE_BOX,
                    "SAFE BOX", currentMoney, (long) amount, "vin", desc, 0L, false, user.isBot());
            RMQApi.publishMessagePayment(messageMoney, 16);
            RMQApi.publishMessageLogMoney(messageLog);
            SafeBoxDaoImpl safeBoxDao = new SafeBoxDaoImpl();
            safeBoxDao.depositSafeBox(userName, amount);
            userMap.put(userName, user);
            safeBoxResponse.status = 0;
            safeBoxResponse.message = "Nạp tiền vào két sắt thành công";
            safeBoxResponse.amount = safeBoxDao.getSafeBox(userName);
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            userMap.unlock(userName);
        }

        return safeBoxResponse;
    }

    @Override
    public SafeBoxResponse getSafeBox(String userName) {
        SafeBoxResponse safeBoxResponse = new SafeBoxResponse(0, "");
        SafeBoxDaoImpl safeBoxDao = new SafeBoxDaoImpl();
        safeBoxResponse.amount = safeBoxDao.getSafeBox(userName);
        return safeBoxResponse;
    }

    @Override
    public SafeBoxResponse withDraw(String userName, double amount, String otp) {
        SafeBoxResponse safeBoxResponse = new SafeBoxResponse(1, "");
        SafeBoxDaoImpl safeBoxDao = new SafeBoxDaoImpl();
        double amountDB = safeBoxDao.getSafeBox(userName);
        if (amountDB < amount) {
            safeBoxResponse.message = "Số tiền rút lớn hơn";
            return safeBoxResponse;
        } else {
            OtpService otpService = new OtpServiceImpl();
            try {
                int code = otpService.checkOtp(otp, userName, "0", null);
                if (code == 0) {
                    HazelcastInstance client = HazelcastClientFactory.getInstance();
                    if (client == null) {
                        safeBoxResponse.message = "Không kết nối được";
                        return safeBoxResponse;
                    }
                    IMap<String, UserModel> userMap = client.getMap("users");
                    if (!userMap.containsKey(userName))
                        return safeBoxResponse;

                    try {
                        userMap.lock(userName);
                        UserCacheModel user = (UserCacheModel) userMap.get(userName);
                        long moneyUser = user.getVin();
                        long currentMoney = user.getVinTotal();
                        long rechargeMoney = user.getRechargeMoney();

                        user.setVin(moneyUser += amount);
                        user.setVinTotal(currentMoney += amount);
                        user.setRechargeMoney(rechargeMoney += amount);
                        String desc = "Nạp tiền két sắt";
                        MoneyMessageInMinigame messageMoney = new MoneyMessageInMinigame(VinPlayUtils.genMessageId(), user.getId(),
                                userName, Consts.RECHARGE_SAFE_BOX, moneyUser, currentMoney, (long) amount, "vin", 0L, 0, 0);
                        LogMoneyUserMessage messageLog = new LogMoneyUserMessage(user.getId(), userName, Consts.RECHARGE_SAFE_BOX,
                                "SAFE BOX", currentMoney, (long) amount, "vin", desc, 0L, false, user.isBot());
                        RMQApi.publishMessagePayment(messageMoney, 16);
                        RMQApi.publishMessageLogMoney(messageLog);
                        safeBoxDao.withDraw(userName, amount);
                        userMap.put(userName, user);
                        safeBoxResponse.status = 0;
                        safeBoxResponse.message = "Rút tiền két sắt thành công";
                        safeBoxResponse.amount = safeBoxDao.getSafeBox(userName);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    } finally {
                        userMap.unlock(userName);
                    }
                } else {
                    safeBoxResponse.message = "Có lỗi OTP";
                    return safeBoxResponse;
                }
            } catch (Exception e) {
                safeBoxResponse.message = "Có lỗi OTP";
                return safeBoxResponse;
            }
        }

        return safeBoxResponse;
    }
}
