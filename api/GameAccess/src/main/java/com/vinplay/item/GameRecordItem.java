package com.vinplay.item;

public class GameRecordItem implements java.io.Serializable{
	private static final long serialVersionUID = 9028885892661481596L;
	private String id;
	private String loginname;
	private String bettime;
	private int lotteryid;
	private int methodid;
	private String issue;
	private String betcontent;
	private int betnumber;
	private int betmodle;
	private int multiple;
	private Double amount;
	private Double bonus;
	private String winnumber;
	private LotteryWinItem lotteryWinItem;
	private int status;
	private String inserttime;
	private String codes;
	private double point;
	private int fatherflag;
	private int source;
	private String traceid;
	
	private String bettype;
	private String remark;
	private int wintimes;
	private int model;
	private Double pointssc;
	private Double point115;
	private Double pointdp;
	
	private String lotteryname=null;
	private String methodname=null;
	private String statusname=null;
	
	private String betmodelname;
	private String finishtime;
	
	private int usertype;
	private String ip;
	private String fatherstr;
	private String position;
	private String usertime;
	private Double damount;
	private String hopen;
	
	private Integer betfrom;
	private Double odd;
	private Double rebate;
	private int rebatestatus;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getBettime() {
		return bettime;
	}
	public void setBettime(String bettime) {
		this.bettime = bettime;
	}
	public int getLotteryid() {
		return lotteryid;
	}
	public void setLotteryid(int lotteryid) {
		this.lotteryid = lotteryid;
	}
	public int getMethodid() {
		return methodid;
	}
	public void setMethodid(int methodid) {
		this.methodid = methodid;
	}
	public String getBetcontent() {
		return betcontent;
	}
	public void setBetcontent(String betcontent) {
		this.betcontent = betcontent;
	}
	/**
	 * 注单数
	 * @return
	 */
	public int getBetnumber() {
		return betnumber;
	}
	public void setBetnumber(int betnumber) {
		this.betnumber = betnumber;
	}
	public int getBetmodle() {
		return betmodle;
	}
	public void setBetmodle(int betmodle) {
		this.betmodle = betmodle;
	}
	public int getMultiple() {
		return multiple;
	}
	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}
	/**
	 * 总金额
	 */
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	/**
	 * 开奖号码
	 * @return
	 */
	public String getWinnumber() {
		return winnumber;
	}
	public void setWinnumber(String winnumber) {
		this.winnumber = winnumber;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getInserttime() {
		return inserttime;
	}
	public void setInserttime(String inserttime) {
		this.inserttime = inserttime;
	}
	/**
	 * 投注内容
	 * @return
	 */
	public String getCodes() {
		return codes;
	}
	public void setCodes(String codes) {
		this.codes = codes;
	}
	public double getPoint() {
		return point;
	}
	public void setPoint(double point) {
		this.point = point;
	}
	public int getFatherflag() {
		return fatherflag;
	}
	public void setFatherflag(int fatherflag) {
		this.fatherflag = fatherflag;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public String getTraceid() {
		return traceid;
	}
	public void setTraceid(String traceid) {
		this.traceid = traceid;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public String getLotteryname() {
		return lotteryname;
	}
	public void setLotteryname(String lotteryname) {
		this.lotteryname = lotteryname;
	}
	public String getMethodname() {
		return methodname;
	}
	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}
	public String getStatusname() {
		return statusname;
	}
	public void setStatusname(String statusname) {
		this.statusname = statusname;
	}

	public int getWintimes() {
		return wintimes;
	}
	public void setWintimes(int wintimes) {
		this.wintimes = wintimes;
	}
	public String getBettype() {
		return bettype;
	}
	public void setBettype(String bettype) {
		this.bettype = bettype;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getModel() {
		return model;
	}
	public void setModel(int model) {
		this.model = model;
	}
	public Double getPointssc() {
		return pointssc;
	}
	public void setPointssc(Double pointssc) {
		this.pointssc = pointssc;
	}
	public Double getPoint115() {
		return point115;
	}
	public void setPoint115(Double point115) {
		this.point115 = point115;
	}
	public Double getPointdp() {
		return pointdp;
	}
	public void setPointdp(Double pointdp) {
		this.pointdp = pointdp;
	}
	public String getBetmodelname() {
		return betmodelname;
	}
	public void setBetmodelname(String betmodelname) {
		this.betmodelname = betmodelname;
	}
	public String getFinishtime() {
		return finishtime;
	}
	public void setFinishtime(String finishtime) {
		this.finishtime = finishtime;
	}
	public int getUsertype() {
		return usertype;
	}
	public void setUsertype(int usertype) {
		this.usertype = usertype;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getFatherstr() {
		return fatherstr;
	}
	public void setFatherstr(String fatherstr) {
		this.fatherstr = fatherstr;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getHopen() {
		return hopen;
	}
	public void setHopen(String hopen) {
		this.hopen = hopen;
	}
	public String getUsertime() {
		return usertime;
	}
	public void setUsertime(String usertime) {
		this.usertime = usertime;
	}
	public Double getDamount() {
		return damount;
	}
	public void setDamount(Double damount) {
		this.damount = damount;
	}
	public Integer getBetfrom() {
		return betfrom;
	}
	public void setBetfrom(Integer betfrom) {
		this.betfrom = betfrom;
	}
	/**
	 * @return the lotteryWinItem
	 */
	public LotteryWinItem getLotteryWinItem() {
		return lotteryWinItem;
	}
	/**
	 * @param lotteryWinItem the lotteryWinItem to set
	 */
	public void setLotteryWinItem(LotteryWinItem lotteryWinItem) {
		this.lotteryWinItem = lotteryWinItem;
	}
	public Double getBonus() {
		return bonus;
	}
	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}

	public Double getOdd() {
		return odd;
	}
	public void setOdd(Double odd) {
		this.odd = odd;
	}
	public Double getRebate() {
		return rebate;
	}
	public void setRebate(Double rebate) {
		this.rebate = rebate;
	}
	public int getRebatestatus() {
		return rebatestatus;
	}
	public void setRebatestatus(int rebatestatus) {
		this.rebatestatus = rebatestatus;
	}
}
