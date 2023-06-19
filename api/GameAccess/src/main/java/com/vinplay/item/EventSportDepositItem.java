package com.vinplay.item;

import java.io.Serializable;

public class EventSportDepositItem implements Serializable {
    private static final long serialVersionUID = 3072498752262184709L;

    private String loginname;
    private double totaldeposit = 0;
    private double cmdvalidbet = 0;
    private double ibc2validbet = 0;
    private double sbovalidbet = 0;
    private String starttimeTC = "";
    private String endtimeTC = "";

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

    public double getCmdvalidbet() {
        return cmdvalidbet;
    }

    public void setCmdvalidbet(double cmdvalidbet) {
        this.cmdvalidbet = cmdvalidbet;
    }

    public double getIbc2validbet() {
        return ibc2validbet;
    }

    public void setIbc2validbet(double ibc2validbet) {
        this.ibc2validbet = ibc2validbet;
    }

    public double getSbovalidbet() {
        return sbovalidbet;
    }

    public void setSbovalidbet(double sbovalidbet) {
        this.sbovalidbet = sbovalidbet;
    }

    public String getStarttimeTC() {
        return starttimeTC;
    }

    public void setStarttimeTC(String starttimeTC) {
        this.starttimeTC = starttimeTC;
    }

    public String getEndtimeTC() {
        return endtimeTC;
    }

    public void setEndtimeTC(String endtimeTC) {
        this.endtimeTC = endtimeTC;
    }
}
