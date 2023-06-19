package com.vinplay.api.backend.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vinplay.vbee.common.response.BaseResponse;
import com.vinplay.vbee.common.statics.Consts;

import java.io.Serializable;

public class AgentResponse<T> implements Serializable {
    protected boolean success;
    protected String errorCode;
    protected String message;
    protected T data;

    public AgentResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public AgentResponse(boolean success, String errorCode, String message, T data) {
        this.success = success;
        this.errorCode = errorCode;
        this.message = message;
        this.data = data;
    }

    public static String success(String errorCode, String message, Object data) {
        AgentResponse base = new AgentResponse(true, errorCode, message, data);
        return base.toJson();
    }

    public static String error(String errorCode, String message) {
        AgentResponse base = new AgentResponse(false, errorCode, message, null);
        return base.toJson();
    }

    @Override
    public String toString() {
        return "AgentResponse{" +
                "success=" + success +
                ", errorCode='" + errorCode + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public String toJson() {
        ObjectWriter ow = new ObjectMapper().writer();
        ow.with(SerializationFeature.INDENT_OUTPUT);
        try {
            String json = ow.writeValueAsString(this);
            return json;
        } catch (Exception e) {
            return null;
        }
    }
}
