/*
 * Decompiled with CFR 0.144.
 */
package com.vinplay.vbee.common.response;

import com.vinplay.vbee.common.response.BaseResponseModel;
import com.vinplay.vbee.common.response.TaiXiuResponse;
import java.util.ArrayList;
import java.util.List;

public class ResultTaiXiuResponse
extends BaseResponseModel {
    private long total;
    private long totalRecord;
    private long totalPlayer;

    public long getTotalPlayer() {
        return totalPlayer;
    }

    public void setTotalPlayer(long totalPlayer) {
        this.totalPlayer = totalPlayer;
    }

    private List<TaiXiuResponse> transactions = new ArrayList<TaiXiuResponse>();

    public ResultTaiXiuResponse(boolean success, String errorCode) {
        super(success, errorCode);
    }

    public List<TaiXiuResponse> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(List<TaiXiuResponse> transactions) {
        this.transactions = transactions;
    }

    public long getTotal() {
        return this.total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotalRecord() {
        return this.totalRecord;
    }

    public void setTotalRecord(long totalRecord) {
        this.totalRecord = totalRecord;
    }
}

