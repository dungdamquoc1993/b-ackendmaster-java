/*
 * Decompiled with CFR 0.144.
 *
 * Could not load the following classes:
 *  com.vinplay.vbee.common.exceptions.KeyNotFoundException
 *  com.vinplay.vbee.common.messages.dvt.RechargeByBankMessage
 *  com.vinplay.vbee.common.messages.dvt.RechargeByCardMessage
 */
package com.vinplay.dichvuthe.dao;

import com.vinplay.dichvuthe.entities.DepositBankModel;
import com.vinplay.dichvuthe.entities.DepositMomoModel;
import com.vinplay.iap.lib.Purchase;
import com.vinplay.usercore.response.LogRechargeBankNLResponse;
import com.vinplay.usercore.response.LogRechargeBankNapasResponse;
import com.vinplay.vbee.common.exceptions.KeyNotFoundException;
import com.vinplay.vbee.common.messages.dvt.RechargeByBankMessage;
import com.vinplay.vbee.common.messages.dvt.RechargeByCardMessage;
import org.bson.Document;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

public interface RechargeDao {
    public List<RechargeByCardMessage> getListCardPending(String var1, String var2) throws NumberFormatException, KeyNotFoundException;

    public List<RechargeByCardMessage> getListCardPending() throws NumberFormatException, KeyNotFoundException;

    public boolean updateCard(String var1, int var2, int var3, String var4, int var5);

    public RechargeByBankMessage getRechargeByBank(String var1);

    public boolean logRechargeByBank(RechargeByBankMessage var1) throws Exception;

    public boolean updateRechargeByBank(String var1, String var2, String var3, String var4, String var5, String var6) throws Exception;

    public boolean insertLogRechargeByBankError(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13, String var14) throws Exception;

    public boolean logRechargeByNL(String var1, String var2, String var3, String var4, String var5, int var6, int var7, int var8, int var9, String var10, String var11, String var12, String var13, String var14, String var15, String var16, String var17);

    public boolean updateRechargeByNL(String var1, String var2, String var3);

    public boolean logRechargeByNLError(String var1, String var2, String var3);

    public LogRechargeBankNapasResponse getLogNapas(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, int var9);

    public LogRechargeBankNLResponse getLogNL(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, int var9);

    public boolean saveLogIAP(Purchase var1, String var2, int var3, int var4, String var5);

    public boolean checkOrderId(String var1);

    public long getTotalRechargeIapInday(String var1, Calendar var2) throws ParseException;

    public boolean checkRequestIdSMS(String var1);

    public boolean checkRequestIdSMSPlus(String var1);

    public boolean saveLogRechargeBySMS(String var1, String var2, String var3, int var4, String var5, String var6, String var7, int var8, String var9, int var10);

    public boolean saveLogRechargeBySMSPlus(String var1, String var2, String var3, int var4, String var5, String var6, String var7, String var8, String var9, int var10, String var11, int var12);

    public boolean saveLogRechargeBySMSPlusCheckMO(String var1, String var2, int var3, String var4, int var5, String var6);

    public boolean saveLogRequestApiOTP(String var1, String var2, int var3, String var4, String var5, String var6, String var7, int var8, String var9);

    public boolean saveLogConfirmApiOTP(String var1, String var2, int var3, String var4, String var5, String var6, String var7, String var8, int var9, String var10, int var11);

    public RechargeByCardMessage getPendingCardByReferenceId(String var1);

    public boolean insertLogUpdateCardPending(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12) throws Exception;

    public boolean isAgent(String var1) throws SQLException;

    public List<String> getListSmsIdNearly();

    public List<String> getListSmsPlusIdNearly();

    public boolean updateSMS(String var1, int var2, String var3, int var4);

    public Document getRechargeByGachthe(String transId);

    public Document getRechargeByGachthe(String transId, String amount);

    public Document getRechargeByGachthe(String nickname, String serial, String pin);

    public List<Document> getRechargeByGachtheRecently();

    public boolean saveLogRechargeByGachThe(String nickname, String serial, String pin, long amount, String requestId, String requestTime, int code, String des, long money, String provider, String platform, long currentMoney, long addMoney, int userId, String username, String partner, String client);

    public boolean UpdateGachtheTransctions(String transId, int code, String message, long amount);

    public boolean UpdateGachtheTransctionsSent(String transId);

    public Document getRechargeByNapTienGa(String transId);

    public Document getRechargeByNapTienGa(String nickname, String serial, String pin);

    public List<Document> getRechargeByNapTienGaRecently();

    public boolean saveLogRechargeByNapTienGa(String nickname, String serial, String pin, long amount, String requestId, String requestTime, int code, String des, long money, String provider, String platform, long currentMoney, long addMoney, int userId, String username, int napTienGaId);

    public boolean UpdateNapTienGaTransctions(String transId, int code, String message);

    public boolean UpdateNapTienGaTransctionsSent(String transId);

    //Deposit bank region
    public boolean UpdateDepositBankManualStatus(String transId, int status, String desc, String userApprove, long amount);

    public boolean isPendingTransDepositBank(String nickname);

    // deposit Momo region

    public boolean UpdateDepositMomoManualStatus(String transId, int status, String desc, String userApprove, long amount);

    public boolean isPendingTransDepositMomo(String nickname);

    public String InsertDepositMomoManual(DepositMomoModel depositBankModel);

    public String InsertDepositMomoManualId(DepositMomoModel depositBankModel);

    public String InsertDepositBankManualId(DepositBankModel depositBankModel);

    public DepositMomoModel FindDepositMomoById(String Id);

    public DepositBankModel FindDepositBankById(String Id);
}

