package com.vinplay.api.backend.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinplay.vbee.common.response.BaseResponseModel;

public class BaseResponse extends BaseResponseModel {

    private long totalData;



    public BaseResponse(boolean success, String errorCode) {
        super(success, errorCode);
    }

    public BaseResponse(boolean success, String errorCode, Object data) {
        super(success, errorCode, data);
    }

    public BaseResponse(boolean success, String errorCode, int totalData) {
        super(success, errorCode);
        this.totalData = totalData;
    }

    public BaseResponse(boolean success, String errorCode, Object data, int totalData) {
        super(success, errorCode, data);
        this.totalData = totalData;
    }

    public long getTotalData() {
        return totalData;
    }

    public void setTotalData(long totalData) {
        this.totalData = totalData;
    }

    @Override
    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString((Object)this);
        }
        catch (JsonProcessingException mapper) {
            return "{\"success\":false,\"errorCode\":\"1001\",\"totalData\":\"0\"}";
        }
    }
}
