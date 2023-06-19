package com.vinplay.item;

import java.io.Serializable;

public class HTCKInfoItem extends IBCFHItem implements Serializable {
    private static final long serialVersionUID = 6399080719352454076L;
    private int currentlevelvip=0;
    private int currentpoint=0;
    private double totalvipbet=0d;
    private int monthlevelvip=0;


    public int getCurrentlevelvip() {
        return currentlevelvip;
    }

    public void setCurrentlevelvip(int currentlevelvip) {
        this.currentlevelvip = currentlevelvip;
    }

    public int getCurrentpoint() {
        return currentpoint;
    }

    public void setCurrentpoint(int currentpoint) {
        this.currentpoint = currentpoint;
    }

    public double getTotalvipbet() {
        return totalvipbet;
    }

    public void setTotalvipbet(double totalvipbet) {
        this.totalvipbet = totalvipbet;
    }

    public int getMonthlevelvip() {
        return monthlevelvip;
    }

    public void setMonthlevelvip(int monthlevelvip) {
        this.monthlevelvip = monthlevelvip;
    }
}
