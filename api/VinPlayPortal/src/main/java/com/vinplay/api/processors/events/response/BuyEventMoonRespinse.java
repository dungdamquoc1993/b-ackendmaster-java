package com.vinplay.api.processors.events.response;

import com.vinplay.vbee.common.response.BaseResponseModel;

public class BuyEventMoonRespinse extends BaseResponseModel {
    private long money = 0;

    public BuyEventMoonRespinse(boolean success, String errorCode) {
        super(success, errorCode);
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }
}