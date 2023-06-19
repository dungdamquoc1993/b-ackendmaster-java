package com.vinplay.api.backend.processors.jackpotAndFund.jackpot;

import com.vinplay.vbee.common.response.BaseResponseModel;

import java.util.List;

public class ResultGetJackpotResponse extends BaseResponseModel {
    public ResultGetJackpotResponse(boolean success, String errorCode) {
        super(success, errorCode);
    }
    public List<JackpotUserData> listUserJackpot;

}
