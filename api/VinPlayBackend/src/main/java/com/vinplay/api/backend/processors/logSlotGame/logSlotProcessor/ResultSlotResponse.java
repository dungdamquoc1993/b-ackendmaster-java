package com.vinplay.api.backend.processors.logSlotGame.logSlotProcessor;

import com.vinplay.api.backend.processors.logSlotGame.logSlotModel.LogSlotModel;
import com.vinplay.vbee.common.response.BaseResponseModel;

import java.util.ArrayList;
import java.util.List;

public class ResultSlotResponse extends BaseResponseModel {
    private long total;
    private long totalRecord;
    private String message;
    private long tong_cuoc = 0;
    private long tong_thang = 0;
    private long tong_player = 0;

    public ResultSlotResponse(boolean success, String errorCode) {
        super(success, errorCode);
    }

    public ResultSlotResponse(boolean success, String errorCode, String message) {
        super(success, errorCode);
        this.message= message;
    }

    public ResultSlotResponse(boolean success, String errorCode, long total, long totalRecord, String message, long tong_cuoc, long tong_thang, List<LogSlotModel> transactions) {
        super(success, errorCode);
        this.total = total;
        this.totalRecord = totalRecord;
        this.message = message;
        this.tong_cuoc = tong_cuoc;
        this.tong_thang = tong_thang;
        this.transactions = transactions;
    }

    public ResultSlotResponse(boolean success, String errorCode, Object data, long total, long totalRecord, String message, long tong_cuoc, long tong_thang, List<LogSlotModel> transactions) {
        super(success, errorCode, data);
        this.total = total;
        this.totalRecord = totalRecord;
        this.message = message;
        this.tong_cuoc = tong_cuoc;
        this.tong_thang = tong_thang;
        this.transactions = transactions;
    }

    public ResultSlotResponse(boolean success, String errorCode, long total, long totalRecord, String message, long tong_cuoc, long tong_thang, long tong_player, List<LogSlotModel> transactions) {
        super(success, errorCode);
        this.total = total;
        this.totalRecord = totalRecord;
        this.message = message;
        this.tong_cuoc = tong_cuoc;
        this.tong_thang = tong_thang;
        this.tong_player = tong_player;
        this.transactions = transactions;
    }

    public ResultSlotResponse(boolean success, String errorCode, Object data, long total, long totalRecord, String message, long tong_cuoc, long tong_thang, long tong_player, List<LogSlotModel> transactions) {
        super(success, errorCode, data);
        this.total = total;
        this.totalRecord = totalRecord;
        this.message = message;
        this.tong_cuoc = tong_cuoc;
        this.tong_thang = tong_thang;
        this.tong_player = tong_player;
        this.transactions = transactions;
    }

    public long getTong_player() {
        return tong_player;
    }

    public void setTong_player(long tong_player) {
        this.tong_player = tong_player;
    }

    public long getTong_cuoc() {
        return tong_cuoc;
    }

    public void setTong_cuoc(long tong_cuoc) {
        this.tong_cuoc = tong_cuoc;
    }

    public long getTong_thang() {
        return tong_thang;
    }

    public void setTong_thang(long tong_thang) {
        this.tong_thang = tong_thang;
    }

    private List<LogSlotModel> transactions = new ArrayList<>();

    public List<LogSlotModel> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(List<LogSlotModel> transactions) {
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
