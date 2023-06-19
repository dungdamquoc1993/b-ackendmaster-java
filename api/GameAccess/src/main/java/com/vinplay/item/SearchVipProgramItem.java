package com.vinplay.item;

import java.io.Serializable;

public class SearchVipProgramItem implements Serializable {
	private static final long serialVersionUID = 7355859470615958196L;
	
	private String agent;
	private String username;
	private long predepositamount = 0;
	private long validbetamount = 0;
	
	private long etdepositamount = 0;
	private long etvalidbetamount = 0;
	
	private String currentviplevel;
	private String etviplevel;
	
	
	
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public long getPredepositamount() {
		return predepositamount;
	}
	public void setPredepositamount(long predepositamount) {
		this.predepositamount = predepositamount;
	}
	public long getValidbetamount() {
		return validbetamount;
	}
	public void setValidbetamount(long validbetamount) {
		this.validbetamount = validbetamount;
	}
	public long getEtdepositamount() {
		return etdepositamount;
	}
	public void setEtdepositamount(long etdepositamount) {
		this.etdepositamount = etdepositamount;
	}
	public long getEtvalidbetamount() {
		return etvalidbetamount;
	}
	public void setEtvalidbetamount(long etvalidbetamount) {
		this.etvalidbetamount = etvalidbetamount;
	}
	public String getCurrentviplevel() {
		return currentviplevel;
	}
	public void setCurrentviplevel(String currentviplevel) {
		this.currentviplevel = currentviplevel;
	}
	public String getEtviplevel() {
		return etviplevel;
	}
	public void setEtviplevel(String etviplevel) {
		this.etviplevel = etviplevel;
	}
	

   
}
