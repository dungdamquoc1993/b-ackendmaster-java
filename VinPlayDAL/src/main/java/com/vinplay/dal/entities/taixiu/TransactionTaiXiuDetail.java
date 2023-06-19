/*
 * Decompiled with CFR 0.144.
 */
package com.vinplay.dal.entities.taixiu;

import java.sql.Date;

public class TransactionTaiXiuDetail {
    public long referenceId;
    public String transactionCode;
    public int transactionId;
    public int userId;
    public String username;
    public long betValue = 0L;
    public int betSide;
    public long prize = 0L;
    public long refund = 0L;
    public int inputTime;
    public int moneyType;
    public Date timestamp;
    public long jpAmount;

    public TransactionTaiXiuDetail(long referenceId, int transactionId, int userId, String username, long betValue, int betSide, 
    		long prize, long refund, int inputTime, Date timestamp, long jpAmount) {
        this.referenceId = referenceId;
        this.transactionId = transactionId;
        this.userId = userId;
        this.username = username;
        this.betValue = betValue;
        this.betSide = betSide;
        this.prize = prize;
        this.refund = refund;
        this.inputTime = inputTime;
        this.timestamp = timestamp;
		this.jpAmount = jpAmount;
    }

    public TransactionTaiXiuDetail(long referenceId, int userId, String username, long betValue, int betSide, int inputTime, int moneyType) {
        this.referenceId = referenceId;
        this.userId = userId;
        this.username = username;
        this.betValue = betValue;
        this.betSide = betSide;
        this.prize = 0L;
        this.refund = 0L;
        this.inputTime = inputTime;
        this.moneyType = moneyType;
    }

    public TransactionTaiXiuDetail() {
    }

    public void genTransactionCode() {
        this.transactionCode = this.username + "_" + this.inputTime + System.currentTimeMillis();
    }
}

