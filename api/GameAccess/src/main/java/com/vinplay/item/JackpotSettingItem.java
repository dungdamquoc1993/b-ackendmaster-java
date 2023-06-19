package com.vinplay.item;

import java.io.Serializable;

public class JackpotSettingItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String starttime;
	private String endtime;
	private int status;
	private int currentamount;
	private String games;
	private int jackpottype;
	private int totalbudget;
	private int dailybudget;
	private int betamtreq;
	private int logintimereq;
	private double profitreq;
	private int wdreq;
	private int minbonusreal;
	private int maxbonusreal;
	private int maxbonusvirtual;
	private String hottimestart;
	private String hottimeend;
	private int hottimemaxincrease;
	private int normaltimemaxincrease;
	private String createdate;
	private String updatedate;
	private String updateuser;
	private int currentjp; //-- 1: BOT; 0: REAL
	private int nextjp; //-- 1: BOT; 0: REAL
	private int percentrealnext; //-- percent next is real 
	private String param1;
	private String param2;
	private String param3;
	
	public JackpotSettingItem() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public int getCurrentamount() {
		return currentamount;
	}
	public void setCurrentamount(int currentamount) {
		this.currentamount = currentamount;
	}
	public String getGames() {
		return games;
	}
	public void setGames(String games) {
		this.games = games;
	}
	public int getJackpottype() {
		return jackpottype;
	}
	public void setJackpottype(int jackpottype) {
		this.jackpottype = jackpottype;
	}
	public int getTotalbudget() {
		return totalbudget;
	}
	public void setTotalbudget(int totalbudget) {
		this.totalbudget = totalbudget;
	}
	public int getDailybudget() {
		return dailybudget;
	}
	public void setDailybudget(int dailybudget) {
		this.dailybudget = dailybudget;
	}
	public int getBetamtreq() {
		return betamtreq;
	}
	public void setBetamtreq(int betamtreq) {
		this.betamtreq = betamtreq;
	}
	public int getLogintimereq() {
		return logintimereq;
	}
	public void setLogintimereq(int logintimereq) {
		this.logintimereq = logintimereq;
	}
	public double getProfitreq() {
		return profitreq;
	}
	public void setProfitreq(double profitreq) {
		this.profitreq = profitreq;
	}
	public int getWdreq() {
		return wdreq;
	}
	public void setWdreq(int wdreq) {
		this.wdreq = wdreq;
	}
	public int getMinbonusreal() {
		return minbonusreal;
	}
	public void setMinbonusreal(int minbonusreal) {
		this.minbonusreal = minbonusreal;
	}
	public int getMaxbonusreal() {
		return maxbonusreal;
	}
	public void setMaxbonusreal(int maxbonusreal) {
		this.maxbonusreal = maxbonusreal;
	}
	public int getMaxbonusvirtual() {
		return maxbonusvirtual;
	}
	public void setMaxbonusvirtual(int maxbonusvirtual) {
		this.maxbonusvirtual = maxbonusvirtual;
	}
	public String getHottimestart() {
		return hottimestart;
	}
	public void setHottimestart(String hottimestart) {
		this.hottimestart = hottimestart;
	}
	public String getHottimeend() {
		return hottimeend;
	}
	public void setHottimeend(String hottimeend) {
		this.hottimeend = hottimeend;
	}
	public int getHottimemaxincrease() {
		return hottimemaxincrease;
	}
	public void setHottimemaxincrease(int hottimemaxincrease) {
		this.hottimemaxincrease = hottimemaxincrease;
	}
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	public String getUpdatedate() {
		return updatedate;
	}
	public void setUpdatedate(String updatedate) {
		this.updatedate = updatedate;
	}
	public String getUpdateuser() {
		return updateuser;
	}
	public void setUpdateuser(String updateuser) {
		this.updateuser = updateuser;
	}
	public String getParam1() {
		return param1;
	}
	public void setParam1(String param1) {
		this.param1 = param1;
	}
	public String getParam2() {
		return param2;
	}
	public void setParam2(String param2) {
		this.param2 = param2;
	}
	public String getParam3() {
		return param3;
	}
	public void setParam3(String param3) {
		this.param3 = param3;
	}
	public int getCurrentjp() {
		return currentjp;
	}
	public void setCurrentjp(int currentjp) {
		this.currentjp = currentjp;
	}
	public int getNextjp() {
		return nextjp;
	}
	public void setNextjp(int nextjp) {
		this.nextjp = nextjp;
	}
	public int getPercentrealnext() {
		return percentrealnext;
	}
	public void setPercentrealnext(int percentrealnext) {
		this.percentrealnext = percentrealnext;
	}
	public int getNormaltimemaxincrease() {
		return normaltimemaxincrease;
	}
	public void setNormaltimemaxincrease(int normaltimemaxincrease) {
		this.normaltimemaxincrease = normaltimemaxincrease;
	}
	
	
	
}
