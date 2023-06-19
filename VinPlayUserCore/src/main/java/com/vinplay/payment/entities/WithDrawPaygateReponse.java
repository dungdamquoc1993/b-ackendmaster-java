package com.vinplay.payment.entities;

import com.vinplay.vbee.common.response.BaseResponseModel;

import java.util.List;

public class WithDrawPaygateReponse extends BaseResponseModel {
    public long TotalTrans;
    public long TotalMoney;
    public long TotalSuccess;
    public List<WithDrawPaygateModel> ListTrans;

    public WithDrawPaygateReponse(boolean success, String errorCode) {
        super(success, errorCode);
    }
    public WithDrawPaygateReponse(int totalTrans, long totalMoney, List<WithDrawPaygateModel> listTrans) {
        super(false, "1001");
        TotalTrans = totalTrans;
        TotalMoney = totalMoney;
        ListTrans = listTrans;
    }

    public WithDrawPaygateReponse(long totalTrans, long totalMoney, long totalSuccess, List<WithDrawPaygateModel> listTrans) {
        super(true, "0");
        TotalTrans = totalTrans;
        TotalMoney = totalMoney;
        TotalSuccess = totalSuccess;
        ListTrans = listTrans;
    }
}
