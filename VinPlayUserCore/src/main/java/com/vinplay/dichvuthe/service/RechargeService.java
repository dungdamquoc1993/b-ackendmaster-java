package com.vinplay.dichvuthe.service;

import com.vinplay.dichvuthe.response.RechargeResponse;
import com.vinplay.vbee.common.enums.ProviderType;

import java.util.Map;

public interface RechargeService {

//    public byte checkRechargeIAP(String var1, int var2);

//    public RechargeIAPResponse rechargeIAP(String var1, String var2, String var3);

//    public String smsPlusRequest(Map<String, String[]> var1);

//    public RechargeApiOTPResponse sendRequestChargingOTP(String var1, String var2, int var3);
//
//    public RechargeApiOTPResponse sendConfirmChargingOTP(String var1, String var2, String var3);
//
//    public String receiveConfirmChargingOTP(Map<String, String[]> var1);

//    public RechargeResponse rechargeByBankManual(String nickname, long amount, String bankAccountNumber);

    public RechargeResponse rechargeByGachThe(String nickname, ProviderType provider, String serial, String pin, String amount, String platform, int UserId) throws Exception;

    public RechargeResponse rechargeByGachTheHaDongPho(String nickname, ProviderType provider, String serial, String pin, String amount, String platform, int UserId) throws Exception;

    public RechargeResponse rechargeByMomoManual(String nickname);

    public RechargeResponse rechargeByMomoManualHaDongPho(String nickname);

    public RechargeResponse rechargeByBankManualHaDongPho(String nickname);

    public RechargeResponse rechargeByBankManual(String nickname,String amount, String code);

    public String rechargeByMomoIwin99(Map<String, String[]> var1);

    public String rechargeByBankHaDongPho(Map<String, String[]> var1);

    public String rechargeByBank(Map<String, String[]> var1);

    public String rechargeByMomoHaDongPho(String body);
}

