/*
 * Decompiled with CFR 0.144.
 */
package com.vinplay.vbee.common.response;

//------------ code old -----------
//import com.vinplay.vbee.common.messages.LogGameMessage;
//import com.vinplay.vbee.common.response.BaseResponseModel;
//import java.util.ArrayList;
//import java.util.List;

//------------ code old -----------
//public class LogGameResponse
//extends BaseResponseModel {
//    private long total;
//    private long totalRecord;
//    private long totalPlayer;
//
//    public LogGameResponse(boolean success, String errorCode, long total, long totalRecord, long totalPlayer, List<LogGameMessage> transactions) {
//        super(success, errorCode);
//        this.total = total;
//        this.totalRecord = totalRecord;
//        this.totalPlayer = totalPlayer;
//        this.transactions = transactions;
//    }
//
//    public LogGameResponse(boolean success, String errorCode, Object data, long total, long totalRecord, long totalPlayer, List<LogGameMessage> transactions) {
//        super(success, errorCode, data);
//        this.total = total;
//        this.totalRecord = totalRecord;
//        this.totalPlayer = totalPlayer;
//        this.transactions = transactions;
//    }
//
//    public long getTotalPlayer() {
//        return totalPlayer;
//    }
//
//    public void setTotalPlayer(long totalPlayer) {
//        this.totalPlayer = totalPlayer;
//    }
//
//    private List<LogGameMessage> transactions = new ArrayList<LogGameMessage>();
//
//    public LogGameResponse(boolean success, String errorCode) {
//        super(success, errorCode);
//    }
//
//    public List<LogGameMessage> getTransactions() {
//        return this.transactions;
//    }
//
//    public void setTransactions(List<LogGameMessage> transactions) {
//        this.transactions = transactions;
//    }
//
//    public long getTotal() {
//        return this.total;
//    }
//
//    public void setTotal(long total) {
//        this.total = total;
//    }
//
//    public long getTotalRecord() {
//        return this.totalRecord;
//    }
//
//    public void setTotalRecord(long totalRecord) {
//        this.totalRecord = totalRecord;
//    }
//}

public class LogGameResponse<T>
extends BaseResponseModel {
    private long total;
    private long totalRecord;
    private long totalPlayer;
    
    public LogGameResponse(boolean success, String errorCode, long total, long totalRecord, long totalPlayer, T transactions) {
        super(success, errorCode);
        this.total = total;
        this.totalRecord = totalRecord;
        this.totalPlayer = totalPlayer;
        this.transactions = transactions;
    }
    
    public LogGameResponse(boolean success, String errorCode, Object data, long total, long totalRecord, long totalPlayer, T transactions) {
        super(success, errorCode, data);
        this.total = total;
        this.totalRecord = totalRecord;
        this.totalPlayer = totalPlayer;
        this.transactions = transactions;
    }

    public long getTotalPlayer() {
        return totalPlayer;
    }

    public void setTotalPlayer(long totalPlayer) {
        this.totalPlayer = totalPlayer;
    }

//    private List<LogGameMessage> transactions = new ArrayList<LogGameMessage>();
    private T transactions;

    public LogGameResponse(boolean success, String errorCode) {
        super(success, errorCode);
    }

    public T getTransactions() {
        return this.transactions;
    }

    public void setTransactions(T transactions) {
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

