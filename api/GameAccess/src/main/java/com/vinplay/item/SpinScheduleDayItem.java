package com.vinplay.item;

import java.io.Serializable;

public class SpinScheduleDayItem implements Serializable {
    private static final long serialVersionUID = 501093746545498010L;
    private long id;
    private int dayofweek;
    private String dayname;
    private int numberprizes;
    private long eventid;
    private double budget;
    private int status;//1 on, 0 off

    /**
     * 38.000
     */
    private String prizeone;//id of prize, value of money,number of prizes ex: 1, 38k, 200
    /**
     * 88.000
     */
    private String prizetwo;
    /**
     * 188.000
     */
    private String prizethree;

    private String updatetime;
    private String updateby;
    private int orderby;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDayofweek() {
        return dayofweek;
    }

    public void setDayofweek(int dayofweek) {
        this.dayofweek = dayofweek;
    }

    public String getDayname() {
        return dayname;
    }

    public void setDayname(String dayname) {
        this.dayname = dayname;
    }

    public int getNumberprizes() {
        return numberprizes;
    }

    public void setNumberprizes(int numberprizes) {
        this.numberprizes = numberprizes;
    }

    public long getEventid() {
        return eventid;
    }

    public void setEventid(long eventid) {
        this.eventid = eventid;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    /**
     * 38.000
     * @return
     */
    public String getPrizeone() {
        return prizeone;
    }

    public void setPrizeone(String prizeone) {
        this.prizeone = prizeone;
    }
    /**
     * 88.000
     * @return
     */
    public String getPrizetwo() {
        return prizetwo;
    }

    public void setPrizetwo(String prizetwo) {
        this.prizetwo = prizetwo;
    }
    /**
     * 188.000
     * @return
     */
    public String getPrizethree() {
        return prizethree;
    }

    public void setPrizethree(String prizethree) {
        this.prizethree = prizethree;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getUpdateby() {
        return updateby;
    }

    public void setUpdateby(String updateby) {
        this.updateby = updateby;
    }

    public int getOrderby() {
        return orderby;
    }

    public void setOrderby(int orderby) {
        this.orderby = orderby;
    }
}
