package com.vinplay.item;

import java.io.Serializable;

public class AdvItem implements Serializable{
	private static final long serialVersionUID = -1143021960180964563L;
	private BuyItem bitem;
	private String loginname;
	private Integer lotteryid;
	private String issue;
	private Integer status=0;
	private String addtime;
	private String gameid = "";
	private String traceId = "";
	private String traceStop = "";
	
	public BuyItem getBitem() {
		return bitem;
	}
	public void setBitem(BuyItem bitem) {
		this.bitem = bitem;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public Integer getLotteryid() {
		return lotteryid;
	}
	public void setLotteryid(Integer lotteryid) {
		this.lotteryid = lotteryid;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getGameid() {
		return gameid;
	}
	public void setGameid(String gameid) {
		this.gameid = gameid;
	}
	public String getTraceId() {
		return traceId;
	}
	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}
	public String getTraceStop() {
		return traceStop;
	}
	public void setTraceStop(String traceStop) {
		this.traceStop = traceStop;
	}
}
