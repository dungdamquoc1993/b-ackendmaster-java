package com.vinplay.dto.ibc2;

import java.io.Serializable;

public class BetDetailResult implements Serializable {

    private static final long serialVersionUID = -6186049157040361179L;
    private int error_code;
    private String message;
    private BetDetailData Data;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BetDetailData getData() {
        return Data;
    }

    public void setData(BetDetailData data) {
        Data = data;
    }
}
