/*
 * Decompiled with CFR 0.144.
 */
package com.vinplay.vbee.common.response;

import com.vinplay.vbee.common.response.BaseResponseModel;
import com.vinplay.vbee.common.response.MiniPokerResponse;
import java.util.ArrayList;
import java.util.List;

public class ResultMiniPokerResponse
extends BaseResponseModel {
    private long total;
    private long totalRecord;
    private long total_player;

    public ResultMiniPokerResponse(boolean success, String errorCode, long total, long totalRecord, long total_player, List<MiniPokerResponse> transactions) {
        super(success, errorCode);
        this.total = total;
        this.totalRecord = totalRecord;
        this.total_player = total_player;
        this.transactions = transactions;
    }

    public ResultMiniPokerResponse(boolean success, String errorCode, Object data, long total, long totalRecord, long total_player, List<MiniPokerResponse> transactions) {
        super(success, errorCode, data);
        this.total = total;
        this.totalRecord = totalRecord;
        this.total_player = total_player;
        this.transactions = transactions;
    }

    public long getTotal_player() {
        return total_player;
    }

    public void setTotal_player(long total_player) {
        this.total_player = total_player;
    }

    private List<MiniPokerResponse> transactions = new ArrayList<MiniPokerResponse>();

    public ResultMiniPokerResponse(boolean success, String errorCode) {
        super(success, errorCode);
    }

    public List<MiniPokerResponse> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(List<MiniPokerResponse> transactions) {
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

