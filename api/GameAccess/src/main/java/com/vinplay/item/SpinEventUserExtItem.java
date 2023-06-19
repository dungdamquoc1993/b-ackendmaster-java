package com.vinplay.item;

import java.io.Serializable;

public class SpinEventUserExtItem implements Serializable {

    private static final long serialVersionUID = 7634927766631028640L;

    private int eventid;
    private String loginname;
    private double totaldeposit;
    private int spintimes;
    private double totaldeposittoday;
    private int spintimesinday;
    private int totalusedspintimes;
    private int usedspintimestoday;
    private int register;
    private double totaldepositintime;
    private int totalspins;
    private int luckynext;
    private int freebet;
    private int gift;

    public int getEventid() {
        return eventid;
    }

    public void setEventid(int eventid) {
        this.eventid = eventid;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public double getTotaldeposit() {
        return totaldeposit;
    }

    public void setTotaldeposit(double totaldeposit) {
        this.totaldeposit = totaldeposit;
    }

    public int getSpintimes() {
        return spintimes;
    }

    public void setSpintimes(int spintimes) {
        this.spintimes = spintimes;
    }

    public double getTotaldeposittoday() {
        return totaldeposittoday;
    }

    public void setTotaldeposittoday(double totaldeposittoday) {
        this.totaldeposittoday = totaldeposittoday;
    }

    public int getSpintimesinday() {
        return spintimesinday;
    }

    public void setSpintimesinday(int spintimesinday) {
        this.spintimesinday = spintimesinday;
    }

    public int getTotalusedspintimes() {
        return totalusedspintimes;
    }

    public void setTotalusedspintimes(int totalusedspintimes) {
        this.totalusedspintimes = totalusedspintimes;
    }

    public int getUsedspintimestoday() {
        return usedspintimestoday;
    }

    public void setUsedspintimestoday(int usedspintimestoday) {
        this.usedspintimestoday = usedspintimestoday;
    }

    public int getRegister() {
        return register;
    }

    public void setRegister(int register) {
        this.register = register;
    }

    public double getTotaldepositintime() {
        return totaldepositintime;
    }

    public void setTotaldepositintime(double totaldepositintime) {
        this.totaldepositintime = totaldepositintime;
    }

    public int getTotalspins() {
        return totalspins;
    }

    public void setTotalspins(int totalspins) {
        this.totalspins = totalspins;
    }

    public int getLuckynext() {
        return luckynext;
    }

    public void setLuckynext(int luckynext) {
        this.luckynext = luckynext;
    }

    public int getFreebet() {
        return freebet;
    }

    public void setFreebet(int freebet) {
        this.freebet = freebet;
    }

    public int getGift() {
        return gift;
    }

    public void setGift(int gift) {
        this.gift = gift;
    }
}
