package com.vinplay.item;

import java.io.Serializable;

public class SpinEventItem implements Serializable {
    private static final long serialVersionUID = -3378541365717256198L;
    private int eventid;
    private String eventname;
    private String starttime;
    private String endtime;
    private String createtime;
    private String createby;
    private int status;
    private String lastupdateby;
    private String lasttimeupdate;
    private Double depositmin;
    private Integer defaulwinorder;
    private Integer maxspins;
    private String scheduletime;

    public SpinEventItem() {
    }

    public int getEventid() {
        return this.eventid;
    }

    public void setEventid(int eventid) {
        this.eventid = eventid;
    }

    public String getEventname() {
        return this.eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getStarttime() {
        return this.starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return this.endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getCreatetime() {
        return this.createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCreateby() {
        return this.createby;
    }

    public void setCreateby(String createby) {
        this.createby = createby;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLastupdateby() {
        return this.lastupdateby;
    }

    public void setLastupdateby(String lastupdateby) {
        this.lastupdateby = lastupdateby;
    }

    public String getLasttimeupdate() {
        return this.lasttimeupdate;
    }

    public void setLasttimeupdate(String lasttimeupdate) {
        this.lasttimeupdate = lasttimeupdate;
    }

    public Double getDepositmin() {
        return depositmin;
    }

    public void setDepositmin(Double depositmin) {
        this.depositmin = depositmin;
    }

    public Integer getDefaulwinorder() {
        return defaulwinorder;
    }

    public void setDefaulwinorder(Integer defaulwinorder) {
        this.defaulwinorder = defaulwinorder;
    }

    public Integer getMaxspins() {
        return maxspins;
    }

    public void setMaxspins(Integer maxspins) {
        this.maxspins = maxspins;
    }

    public String getScheduletime() {
        return scheduletime;
    }

    public void setScheduletime(String scheduletime) {
        this.scheduletime = scheduletime;
    }
}
