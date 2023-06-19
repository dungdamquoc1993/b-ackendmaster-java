package com.vinplay.item;

import java.io.Serializable;

public class EventSportRoundItem implements Serializable {
    private static final long serialVersionUID = -6262098533780847944L;
    private int id;
    private int eventsportid;
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
    private String updatetime;
    private String updateby;
    private int ib2homeid;
    private int ib2awayid;
    private int cmdhomeid;
    private int cmdawayid;
    
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

	public int getIb2homeid() {
		return ib2homeid;
	}

	public void setIb2homeid(int ib2homeid) {
		this.ib2homeid = ib2homeid;
	}

	public int getIb2awayid() {
		return ib2awayid;
	}

	public void setIb2awayid(int ib2awayid) {
		this.ib2awayid = ib2awayid;
	}

	public int getCmdhomeid() {
		return cmdhomeid;
	}

	public void setCmdhomeid(int cmdhomeid) {
		this.cmdhomeid = cmdhomeid;
	}

	public int getCmdawayid() {
		return cmdawayid;
	}

	public void setCmdawayid(int cmdawayid) {
		this.cmdawayid = cmdawayid;
	}
}
