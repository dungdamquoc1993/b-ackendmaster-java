package com.vinplay.dichvuthe.response;

import com.google.gson.Gson;

public class Iwin99CallbackResponse {
    private int errorCode;
    private String errorDescription;

    public Iwin99CallbackResponse(int errorCode, String errorDescription) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
