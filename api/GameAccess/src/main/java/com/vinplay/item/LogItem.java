package com.vinplay.item;

public class LogItem implements java.io.Serializable{
	private static final long serialVersionUID = 7954027235246081144L;
	private Integer fatherid;
	private String userid;
	private String msg;
	private String inserttime;
	private String logtype;
	private String logtypename;
	private String ip;
	private String optby;
	private String domain;
	private String agent;
	
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}	
	public Integer getFatherid() {
		return fatherid;
	}
	public void setFatherid(Integer fatherid) {
		this.fatherid = fatherid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getInserttime() {
		return inserttime;
	}
	public void setInserttime(String inserttime) {
		this.inserttime = inserttime;
	}
	public String getLogtype() {
		return logtype;
	}
	public void setLogtype(String logtype) {
		this.logtype = logtype;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getLogtypename() {
		return logtypename;
	}
	public void setLogtypename(String logtypename) {
		this.logtypename = logtypename;
	}
	public String getOptby() {
		return optby;
	}
	public void setOptby(String optby) {
		this.optby = optby;
	}
	
}
