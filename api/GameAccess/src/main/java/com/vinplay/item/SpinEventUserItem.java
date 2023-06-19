package com.vinplay.item;

import java.io.Serializable;

public class SpinEventUserItem implements Serializable {
    private static final long serialVersionUID = 5247299086412913327L;
    private int eventid;
    private String loginname;
    private double totaldeposit;
    private int spintimes;
    private double totaldeposittoday;
    private int spintimesinday;
    private int totalusedspintimes;
    private int usedspintimestoday;
    private int register;
    /**
     * 38.000
     */
    private int prizeone;//38k, so luong giai thuong per one day
    private String prizeonetime;
    /**
     * 88.000
     */
    private int prizetwo;//88k
    private String prizetwotime;
    /**
     * 188.000
     */
    private int prizethree;//188k
    private String prizethreetime;

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

    /**
     * 38.000
     * @return
     */
    public int getPrizeone() {
        return prizeone;
    }

    public void setPrizeone(int prizeone) {
        this.prizeone = prizeone;
    }

    /**
     * 88.000
     * @return
     */
    public int getPrizetwo() {
        return prizetwo;
    }

    public void setPrizetwo(int prizetwo) {
        this.prizetwo = prizetwo;
    }

    /**
     * 188.000
     * @return
     */
    public int getPrizethree() {
        return prizethree;
    }

    public void setPrizethree(int prizethree) {
        this.prizethree = prizethree;
    }

    public String getPrizeonetime() {
        return prizeonetime;
    }

    public void setPrizeonetime(String prizeonetime) {
        this.prizeonetime = prizeonetime;
    }

    public String getPrizetwotime() {
        return prizetwotime;
    }

    public void setPrizetwotime(String prizetwotime) {
        this.prizetwotime = prizetwotime;
    }

    public String getPrizethreetime() {
        return prizethreetime;
    }

    public void setPrizethreetime(String prizethreetime) {
        this.prizethreetime = prizethreetime;
    }
}
