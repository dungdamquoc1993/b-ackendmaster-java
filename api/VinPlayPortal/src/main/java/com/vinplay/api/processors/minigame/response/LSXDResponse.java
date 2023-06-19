package com.vinplay.api.processors.minigame.response;

import com.vinplay.vbee.common.response.BaseResponseModel;
import com.vinplay.vbee.common.response.LogMoneyUserResponse;

import java.util.ArrayList;
import java.util.List;

public class LSXDResponse  extends BaseResponseModel {
    private int totalPages;
    private List<LogMoneyUserResponse> transactions = new ArrayList<LogMoneyUserResponse>();

    public LSXDResponse(boolean success, String errorCode) {
        super(success, errorCode);
    }

    public List<LogMoneyUserResponse> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(List<LogMoneyUserResponse> transactions) {
        this.transactions = transactions;
    }

    public int getTotalPages() {
        return this.totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
