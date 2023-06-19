/**
 * Archie
 */
package com.vinplay.item;

import java.io.Serializable;

/**
 * @author Archie
 *
 */
public class SynchronizeReport implements Serializable {

	private String action_date;
	private String gamename;
	private Integer id;
	private String loginname;
	private String rtime;
	private String usera;

	public String getAction_date() {
		return action_date;
	}

	public void setAction_date(String action_date) {
		this.action_date = action_date;
	}

	public String getGamename() {
		return gamename;
	}

	public void setGamename(String gamename) {
		this.gamename = gamename;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getRtime() {
		return rtime;
	}

	public void setRtime(String rtime) {
		this.rtime = rtime;
	}

	public String getUsera() {
		return usera;
	}

	public void setUsera(String usera) {
		this.usera = usera;
	}

	@Override
	public String toString() {
		return "SynchronizeReport [action_date=" + action_date + ", gamename=" + gamename + ", id=" + id
				+ ", loginname=" + loginname + ", rtime=" + rtime + ", usera=" + usera + "]";
	}

	public SynchronizeReport(String action_date, String gamename, Integer id, String loginname, String rtime,
			String usera) {
		super();
		this.action_date = action_date;
		this.gamename = gamename;
		this.id = id;
		this.loginname = loginname;
		this.rtime = rtime;
		this.usera = usera;
	}

	public SynchronizeReport() {
		super();
	}

}
