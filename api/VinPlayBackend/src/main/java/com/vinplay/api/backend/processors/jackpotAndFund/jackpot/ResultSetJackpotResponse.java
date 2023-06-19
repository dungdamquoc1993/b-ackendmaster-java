package com.vinplay.api.backend.processors.jackpotAndFund.jackpot;

import com.vinplay.vbee.common.response.BaseResponseModel;


public class ResultSetJackpotResponse extends BaseResponseModel {
    public ResultSetJackpotResponse(boolean success, String errorCode) {
        super(success, errorCode);
    }
    public JackpotUserData jackpotUserData;

}
