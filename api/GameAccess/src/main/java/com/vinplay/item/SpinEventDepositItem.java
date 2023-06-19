package com.vinplay.item;

import java.io.Serializable;

public class SpinEventDepositItem implements Serializable {

    private static final long serialVersionUID = -1611632183577405823L;
    private int eventid;
    private String loginname;
    private double deposittoday;
    private double depositmin;
    private int spinstoday;
    private String timedeposit;
    private String timeupdate;
    private String scheduletime;

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

    public double getDeposittoday() {
        return deposittoday;
    }

    public void setDeposittoday(double deposittoday) {
        this.deposittoday = deposittoday;
    }

    public double getDepositmin() {
        return depositmin;
    }

    public void setDepositmin(double depositmin) {
        this.depositmin = depositmin;
    }

    public int getSpinstoday() {
        return spinstoday;
    }

    public void setSpinstoday(int spinstoday) {
        this.spinstoday = spinstoday;
    }

    public String getTimedeposit() {
        return timedeposit;
    }

    public void setTimedeposit(String timedeposit) {
        this.timedeposit = timedeposit;
    }

    public String getTimeupdate() {
        return timeupdate;
    }

    public void setTimeupdate(String timeupdate) {
        this.timeupdate = timeupdate;
    }

    public String getScheduletime() {
        return scheduletime;
    }

    public void setScheduletime(String scheduletime) {
        this.scheduletime = scheduletime;
    }
}
