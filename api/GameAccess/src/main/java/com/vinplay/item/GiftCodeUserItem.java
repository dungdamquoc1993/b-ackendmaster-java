package com.vinplay.item;

import java.io.Serializable;

public class GiftCodeUserItem implements Serializable {
    private static final long serialVersionUID = 7325961897836480104L;
    
    private long id;
    private long codeid;
    private String codename;
    private String loginname;
    private String agentname;
    private String inserttime;
    private String event;
    private String source;
    private long money;
    private String ip;
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }

    
    public long getCodeid() {
		return codeid;
	}

	public void setCodeid(long codeid) {
		this.codeid = codeid;
	}



	public String getCodename() {
		return codename;
	}

	public void setCodename(String codename) {
		this.codename = codename;
	}

	public String getLoginname() {
        return loginname;
    }
    
    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }
    
    public String getAgentname() {
        return agentname;
    }
    
    public void setAgentname(String agentname) {
        this.agentname = agentname;
    }
    
    public String getInserttime() {
        return inserttime;
    }
    
    public void setInserttime(String inserttime) {
        this.inserttime = inserttime;
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

	public long getMoney() {
		return money;
	}

	public void setMoney(long money) {
		this.money = money;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	

    
}
