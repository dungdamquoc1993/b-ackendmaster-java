package com.vinplay.item;

import java.io.Serializable;

public class EventSportItem implements Serializable {
    private static final long serialVersionUID = 8926422185863986324L;
    private Integer id=0;
    private String sessionname;
    private int round;
    private Integer currentround=0;
    private String starttime;
    private String endtime;
    private int type;
    private String urlweb;
    private String urlwap;
    private int status;
    private String updatetime;
    private String updateby;
    private int orderby;
    private int ibc2leagueid;
    private int cmdleagueid;
    
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getSessionname() {
        return sessionname;
    }
    
    public void setSessionname(String sessionname) {
        this.sessionname = sessionname;
    }
    
    public int getRound() {
        return round;
    }
    
    public void setRound(int round) {
        this.round = round;
    }
    
    public Integer getCurrentround() {
        return currentround;
    }
    
    public void setCurrentround(Integer currentround) {
        this.currentround = currentround;
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
    
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public String getUrlweb() {
        return urlweb;
    }
    
    public void setUrlweb(String urlweb) {
        this.urlweb = urlweb;
    }
    
    public String getUrlwap() {
        return urlwap;
    }
    
    public void setUrlwap(String urlwap) {
        this.urlwap = urlwap;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
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

	public int getIbc2leagueid() {
		return ibc2leagueid;
	}

	public void setIbc2leagueid(int ibc2leagueid) {
		this.ibc2leagueid = ibc2leagueid;
	}

	public int getCmdleagueid() {
		return cmdleagueid;
	}

	public void setCmdleagueid(int cmdleagueid) {
		this.cmdleagueid = cmdleagueid;
	}
}
