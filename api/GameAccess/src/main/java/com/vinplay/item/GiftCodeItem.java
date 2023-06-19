package com.vinplay.item;

import java.io.Serializable;

public class GiftCodeItem implements Serializable {
    private static final long serialVersionUID = -1278256628774851974L;
    private long id;
    private String prefix = "";
	private String code = "";
	private long money;
	private long withdraw = 0;//=0 is not required
	private String starttime;
	private String endtime;
	private int status=0;
	private int quanlit=1;
	private String source;
	private String event;
	private String updateby;
	private String createdtime;
	private String updatedtime;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public long getMoney() {
		return money;
	}
	public void setMoney(long money) {
		this.money = money;
	}
	public long getWithdraw() {
		return withdraw;
	}
	public void setWithdraw(long withdraw) {
		this.withdraw = withdraw;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getUpdateby() {
		return updateby;
	}
	public void setUpdateby(String updateby) {
		this.updateby = updateby;
	}
	public String getCreatedtime() {
		return createdtime;
	}
	public void setCreatedtime(String createdtime) {
		this.createdtime = createdtime;
	}
	public String getUpdatedtime() {
		return updatedtime;
	}
	public void setUpdatedtime(String updatedtime) {
		this.updatedtime = updatedtime;
	}
	public int getQuanlit() {
		return quanlit;
	}
	public void setQuanlit(int quanlit) {
		this.quanlit = quanlit;
	}
	
	
    
}
