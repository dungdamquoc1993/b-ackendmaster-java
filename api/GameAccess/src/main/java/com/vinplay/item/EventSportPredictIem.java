package com.vinplay.item;

import java.io.Serializable;

public class EventSportPredictIem implements Serializable {
    private static final long serialVersionUID = 8043806865697449829L;
    private int id;
    private int eventsportid;
    private int eventsportroundid;
    private String loginname;

    private Integer predict = 0;
    private Integer predictstatus = 0;
    private String inserttime;
    private String updatetime;
    
    private String updateby;
    private String timeby;

    private Integer gametype = 0;//old
    private String gametypes;//2 ibc2, 3 cmd, 4 sbo

    private Double ibc2betmoney = 0d;
    private String ibc2transids;
    private String cmdtransids;
    private Double cmdbetmoney = 0d;
    private String sbotransids;
    private Double sbobetmoney = 0d;

    private double dp;
    private double ibc2validbet;
    private double cmdvalidbet;
    private double sbovalidbet;
    private String starttimetc = "";
    private String endtimetc = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventsportid() {
        return eventsportid;
    }

    public void setEventsportid(int eventsportid) {
        this.eventsportid = eventsportid;
    }

    public int getEventsportroundid() {
        return eventsportroundid;
    }

    public void setEventsportroundid(int eventsportroundid) {
        this.eventsportroundid = eventsportroundid;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public Integer getPredict() {
        return predict;
    }

    public void setPredict(Integer predict) {
        this.predict = predict;
    }

    public Integer getPredictstatus() {
        return predictstatus;
    }

    public void setPredictstatus(Integer predictstatus) {
        this.predictstatus = predictstatus;
    }

    public String getInserttime() {
        return inserttime;
    }

    public void setInserttime(String inserttime) {
        this.inserttime = inserttime;
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

    public String getTimeby() {
        return timeby;
    }

    public void setTimeby(String timeby) {
        this.timeby = timeby;
    }

    public Integer getGametype() {
        return gametype;
    }

    public void setGametype(Integer gametype) {
        this.gametype = gametype;
    }

    public Double getIbc2betmoney() {
        return ibc2betmoney;
    }

    public void setIbc2betmoney(Double ibc2betmoney) {
        this.ibc2betmoney = ibc2betmoney;
    }

    public String getIbc2transids() {
        return ibc2transids;
    }

    public void setIbc2transids(String ibc2transids) {
        this.ibc2transids = ibc2transids;
    }

    public String getCmdtransids() {
        return cmdtransids;
    }

    public void setCmdtransids(String cmdtransids) {
        this.cmdtransids = cmdtransids;
    }

    public Double getCmdbetmoney() {
        return cmdbetmoney;
    }

    public void setCmdbetmoney(Double cmdbetmoney) {
        this.cmdbetmoney = cmdbetmoney;
    }

    public double getDp() {
        return dp;
    }

    public void setDp(double dp) {
        this.dp = dp;
    }

    public double getIbc2validbet() {
        return ibc2validbet;
    }

    public void setIbc2validbet(double ibc2validbet) {
        this.ibc2validbet = ibc2validbet;
    }

    public double getCmdvalidbet() {
        return cmdvalidbet;
    }

    public void setCmdvalidbet(double cmdvalidbet) {
        this.cmdvalidbet = cmdvalidbet;
    }

    public double getSbovalidbet() {
        return sbovalidbet;
    }

    public void setSbovalidbet(double sbovalidbet) {
        this.sbovalidbet = sbovalidbet;
    }

    public String getStarttimetc() {
        return starttimetc;
    }

    public void setStarttimetc(String starttimetc) {
        this.starttimetc = starttimetc;
    }

    public String getEndtimetc() {
        return endtimetc;
    }

    public void setEndtimetc(String endtimetc) {
        this.endtimetc = endtimetc;
    }

    public String getGametypes() {
        return gametypes;
    }

    public void setGametypes(String gametypes) {
        this.gametypes = gametypes;
    }

    public String getSbotransids() {
        return sbotransids;
    }

    public void setSbotransids(String sbotransids) {
        this.sbotransids = sbotransids;
    }

    public Double getSbobetmoney() {
        return sbobetmoney;
    }

    public void setSbobetmoney(Double sbobetmoney) {
        this.sbobetmoney = sbobetmoney;
    }
}
