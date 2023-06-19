package com.vinplay.item;

public class ReportWLNewItem implements java.io.Serializable{
	private static final long serialVersionUID = 2801422641335942511L;
	//用户名
	private String loginname;
	//存款
	private Double deposit=0.0;
	//提款
	private Double withdrawl=0.0;
	//投注
	private Double bet=0.0;
	//返点
	private Double bonus=0.0;
	
	private Double jackpot=0.0;
	//中奖
	private Double win=0.0;
	//优惠活动
	private Double active=0.0;
	//佣金
	private Double yj=0.0;
	//净盈利
	private Double total=0.0;
	
	//给下级充值
	private Double xjdeposit = 0.0;
	
	//接受上级的充值
	private Double jsdeposit=0.0;
	
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public Double getDeposit() {
		return deposit;
	}
	public void setDeposit(Double deposit) {
		this.deposit = deposit;
	}
	public Double getWithdrawl() {
		return withdrawl;
	}
	public void setWithdrawl(Double withdrawl) {
		this.withdrawl = withdrawl;
	}
	public Double getBet() {
		return bet;
	}
	public void setBet(Double bet) {
		this.bet = bet;
	}
	public Double getBonus() {
		return bonus;
	}
	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}
	public Double getWin() {
		return win;
	}
	public void setWin(Double win) {
		this.win = win;
	}
	public Double getActive() {
		return active;
	}
	public void setActive(Double active) {
		this.active = active;
	}
	public Double getYj() {
		return yj;
	}
	public void setYj(Double yj) {
		this.yj = yj;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Double getXjdeposit() {
		return xjdeposit;
	}
	public void setXjdeposit(Double xjdeposit) {
		this.xjdeposit = xjdeposit;
	}
	public Double getJsdeposit() {
		return jsdeposit;
	}
	public void setJsdeposit(Double jsdeposit) {
		this.jsdeposit = jsdeposit;
	}
	public Double getJackpot() {
		return jackpot;
	}
	public void setJackpot(Double jackpot) {
		this.jackpot = jackpot;
	}
}
