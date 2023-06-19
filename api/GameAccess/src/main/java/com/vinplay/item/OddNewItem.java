package com.vinplay.item;

import java.io.Serializable;

public class OddNewItem implements Serializable {
    private static final long serialVersionUID = -5694311113149727266L;

    private int lotteryid;
    private int methodid;
    private Double odd=0d;
    private Double rebate=0d;
    private int active;
    private String remark;
    private String param1;

    public int getLotteryid() {
        return lotteryid;
    }

    public void setLotteryid(int lotteryid) {
        this.lotteryid = lotteryid;
    }

    public int getMethodid() {
        return methodid;
    }

    public void setMethodid(int methodid) {
        this.methodid = methodid;
    }

    public Double getOdd() {
        return odd;
    }

    public void setOdd(Double odd) {
        this.odd = odd;
    }

    public Double getRebate() {
        return rebate;
    }

    public void setRebate(Double rebate) {
        this.rebate = rebate;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }
}
