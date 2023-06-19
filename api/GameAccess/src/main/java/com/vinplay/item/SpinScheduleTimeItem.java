package com.vinplay.item;

import java.io.Serializable;

public class SpinScheduleTimeItem implements Serializable {
    private static final long serialVersionUID = -1042077032463754448L;
    private int id;
    private String starttimeofday;
    private String endtimeofday;
    private String dayname;
    private int spinscheduledayid;
    private long eventid;
    private int status; //1 is on, 0 is off
    /**
     * 38.000
     */
    private int prizeone;//id of prize, value of money,number of prizes ex: 1, 38k, 200
    /**
     * 38.000
     */
    private int prizeoneid;
    /**
     * 38.000
     */
    private int prizeonenumber;
    /**
     * 38.000
     */
    private int prizeonewin;
    private String prizeonetime;

    /**
     * 88.000
     */
    private int prizetwo;
    /**
     * 88.000
     */
    private int prizetwoid;
    /**
     * 88.000
     */
    private int prizetwonumber;
    /**
     * 88.000
     */
    private int prizetwowin;
    private String prizetwotime;

    /**
     * 188.000
     */
    private int prizethree;
    /**
     * 188.000
     */
    private int prizethreeid;
    /**
     * 188.000
     */
    private int prizethreenumber;
    /**
     * 188.000
     */
    private int prizethreewin;
    private String prizethreetime;

    private String updatetime;
    private String updateby;
    private int orderby;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStarttimeofday() {
        return starttimeofday;
    }

    public void setStarttimeofday(String starttimeofday) {
        this.starttimeofday = starttimeofday;
    }

    public String getEndtimeofday() {
        return endtimeofday;
    }

    public void setEndtimeofday(String endtimeofday) {
        this.endtimeofday = endtimeofday;
    }

    public String getDayname() {
        return dayname;
    }

    public void setDayname(String dayname) {
        this.dayname = dayname;
    }

    public int getSpinscheduledayid() {
        return spinscheduledayid;
    }

    public void setSpinscheduledayid(int spinscheduledayid) {
        this.spinscheduledayid = spinscheduledayid;
    }

    public long getEventid() {
        return eventid;
    }

    public void setEventid(long eventid) {
        this.eventid = eventid;
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
    public int getPrizeone() {
        return prizeone;
    }

    public void setPrizeone(int prizeone) {
        this.prizeone = prizeone;
    }
    /**
     * 38.000
     * @return
     */
    public int getPrizeoneid() {
        return prizeoneid;
    }

    public void setPrizeoneid(int prizeoneid) {
        this.prizeoneid = prizeoneid;
    }
    /**
     * 38.000
     * @return
     */
    public int getPrizeonenumber() {
        return prizeonenumber;
    }

    public void setPrizeonenumber(int prizeonenumber) {
        this.prizeonenumber = prizeonenumber;
    }
    /**
     * 38.000
     * @return
     */
    public int getPrizeonewin() {
        return prizeonewin;
    }

    public void setPrizeonewin(int prizeonewin) {
        this.prizeonewin = prizeonewin;
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
     * 88.000
     * @return
     */
    public int getPrizetwoid() {
        return prizetwoid;
    }

    public void setPrizetwoid(int prizetwoid) {
        this.prizetwoid = prizetwoid;
    }
    /**
     * 88.000
     * @return
     */
    public int getPrizetwonumber() {
        return prizetwonumber;
    }

    public void setPrizetwonumber(int prizetwonumber) {
        this.prizetwonumber = prizetwonumber;
    }
    /**
     * 88.000
     * @return
     */
    public int getPrizetwowin() {
        return prizetwowin;
    }

    public void setPrizetwowin(int prizetwowin) {
        this.prizetwowin = prizetwowin;
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
    /**
     * 188.000
     * @return
     */
    public int getPrizethreeid() {
        return prizethreeid;
    }

    public void setPrizethreeid(int prizethreeid) {
        this.prizethreeid = prizethreeid;
    }
    /**
     * 188.000
     * @return
     */
    public int getPrizethreenumber() {
        return prizethreenumber;
    }

    public void setPrizethreenumber(int prizethreenumber) {
        this.prizethreenumber = prizethreenumber;
    }
    /**
     * 188.000
     * @return
     */
    public int getPrizethreewin() {
        return prizethreewin;
    }
    public void setPrizethreewin(int prizethreewin) {
        this.prizethreewin = prizethreewin;
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
