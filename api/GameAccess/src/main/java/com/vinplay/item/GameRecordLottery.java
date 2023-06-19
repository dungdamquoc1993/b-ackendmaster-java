package com.vinplay.item;

import java.io.Serializable;

public class GameRecordLottery implements Serializable {
    private static final long serialVersionUID = -4858432383051640638L;

    private String loginname="";
    private int lotteryid;
    private double amount = 0d;
    private double bonus = 0d;
    private double rebate = 0d;

    public int getLotteryid() {
        return lotteryid;
    }

    public void setLotteryid(int lotteryid) {
        this.lotteryid = lotteryid;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public double getRebate() {
        return rebate;
    }

    public void setRebate(double rebate) {
        this.rebate = rebate;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }
}
