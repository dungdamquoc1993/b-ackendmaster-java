package com.vinplay.item;
/**
 * �?��中奖记录
 * @author BRIAN
 *
 */
public class NewWinListItem implements Cloneable,java.io.Serializable{

	private static final long serialVersionUID = 8105466788697642449L;
	private String loginname;	//中奖人昵�?
	private Double bonus;		//中奖金额
	private int lotteryid;
	private String lotteryname;	//彩种
	private String issue;		//期号
	
	public String getLotteryname() {
		return lotteryname;
	}
	public void setLotteryname(String lotteryname) {
		this.lotteryname = lotteryname;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}

	public int getLotteryid() {
		return lotteryid;
	}
	public void setLotteryid(int lotteryid) {
		this.lotteryid = lotteryid;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public Double getBonus() {
		return bonus;
	}
	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}
}
