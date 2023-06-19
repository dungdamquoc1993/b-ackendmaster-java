package com.vinplay.api.backend.processors.jackpotAndFund.resultGame;

import com.vinplay.api.backend.processors.jackpotAndFund.jackpot.JackpotUserData;
import com.vinplay.vbee.common.response.BaseResponseModel;

import java.util.List;

public class ResultGetSetResultResponse extends BaseResponseModel {
    public ResultGetSetResultResponse(boolean success, String errorCode) {
        super(success, errorCode);
    }
    public short[] data;

}
