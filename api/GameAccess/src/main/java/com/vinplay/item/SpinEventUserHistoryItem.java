package com.vinplay.item;

import java.io.Serializable;

public class SpinEventUserHistoryItem implements Serializable {
    private static final long serialVersionUID = -3584205805553046895L;
    private String id;
    private int eventid;
    private String loginname;
    private String timespin;
    private int status;
    private int eventdetailwinid;
    private Integer moneyprize = 0;
    private String description;
    private String idadjust;
    private int prizetype;
    private String updateby;
    private String timefinish;

    public SpinEventUserHistoryItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getTimespin() {
        return timespin;
    }

    public void setTimespin(String timespin) {
        this.timespin = timespin;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getEventdetailwinid() {
        return eventdetailwinid;
    }

    public void setEventdetailwinid(int eventdetailwinid) {
        this.eventdetailwinid = eventdetailwinid;
    }

    public Integer getMoneyprize() {
        return moneyprize;
    }

    public void setMoneyprize(Integer moneyprize) {
        this.moneyprize = moneyprize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdadjust() {
        return idadjust;
    }

    public void setIdadjust(String idadjust) {
        this.idadjust = idadjust;
    }

    public int getPrizetype() {
        return prizetype;
    }

    public void setPrizetype(int prizetype) {
        this.prizetype = prizetype;
    }

    public String getUpdateby() {
        return updateby;
    }

    public void setUpdateby(String updateby) {
        this.updateby = updateby;
    }

    public String getTimefinish() {
        return timefinish;
    }

    public void setTimefinish(String timefinish) {
        this.timefinish = timefinish;
    }
}
