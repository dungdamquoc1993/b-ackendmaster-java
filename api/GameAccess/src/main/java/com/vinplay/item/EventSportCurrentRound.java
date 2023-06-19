package com.vinplay.item;

import java.io.Serializable;

public class EventSportCurrentRound implements Serializable {
    private static final long serialVersionUID = -6262098533780847944L;
    ////round
    private int eventsportroundid;///
    private int eventsportid;///
    private String eventsportname;//new
    private int roundno;
    private String roundname;
    private String hometeam;
    private String homeurlwap;
    private String homeurlweb;
    private String awayteam;
    private String awayurlwap;
    private String awayurlweb;
    private int homescore;
    private int awayscore;
    private int roundresult;
    private int status;
    private String starttime;
    private String endtime;
    private String remark;
    
    
    /////prdict
    private int predictid=0;///
    private String loginname;
    
    private Integer predict;
    private Integer predictstatus;
    private String inserttime;
    private String updatetime;
    
    public int getEventsportroundid() {
        return eventsportroundid;
    }
    
    public void setEventsportroundid(int eventsportroundid) {
        this.eventsportroundid = eventsportroundid;
    }
    
    public int getEventsportid() {
        return eventsportid;
    }
    
    public void setEventsportid(int eventsportid) {
        this.eventsportid = eventsportid;
    }
    
    public String getEventsportname() {
        return eventsportname;
    }
    
    public void setEventsportname(String eventsportname) {
        this.eventsportname = eventsportname;
    }
    
    public int getRoundno() {
        return roundno;
    }
    
    public void setRoundno(int roundno) {
        this.roundno = roundno;
    }
    
    public String getRoundname() {
        return roundname;
    }
    
    public void setRoundname(String roundname) {
        this.roundname = roundname;
    }
    
    public String getHometeam() {
        return hometeam;
    }
    
    public void setHometeam(String hometeam) {
        this.hometeam = hometeam;
    }
    
    public String getHomeurlwap() {
        return homeurlwap;
    }
    
    public void setHomeurlwap(String homeurlwap) {
        this.homeurlwap = homeurlwap;
    }
    
    public String getHomeurlweb() {
        return homeurlweb;
    }
    
    public void setHomeurlweb(String homeurlweb) {
        this.homeurlweb = homeurlweb;
    }
    
    public String getAwayteam() {
        return awayteam;
    }
    
    public void setAwayteam(String awayteam) {
        this.awayteam = awayteam;
    }
    
    public String getAwayurlwap() {
        return awayurlwap;
    }
    
    public void setAwayurlwap(String awayurlwap) {
        this.awayurlwap = awayurlwap;
    }
    
    public String getAwayurlweb() {
        return awayurlweb;
    }
    
    public void setAwayurlweb(String awayurlweb) {
        this.awayurlweb = awayurlweb;
    }
    
    public int getHomescore() {
        return homescore;
    }
    
    public void setHomescore(int homescore) {
        this.homescore = homescore;
    }
    
    public int getAwayscore() {
        return awayscore;
    }
    
    public void setAwayscore(int awayscore) {
        this.awayscore = awayscore;
    }
    
    public int getRoundresult() {
        return roundresult;
    }
    
    public void setRoundresult(int roundresult) {
        this.roundresult = roundresult;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getStarttime() {
        return starttime;
    }
    
    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }
    
    public String getEndtime() {
        return endtime;
    }
    
    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public int getPredictid() {
        return predictid;
    }
    
    public void setPredictid(int predictid) {
        this.predictid = predictid;
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
}
