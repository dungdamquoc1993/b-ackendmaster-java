package com.vinplay.item;


public class UserItem implements java.io.Serializable{
	private static final long serialVersionUID = -3695079124913220592L;
	private int id;
	private String loginname=null;
	private String name;
	private String pwd;
	private String checkpwd;
	private String balance;
	private String lastlogintime;
	private String lastloginip;
	private int usertype;
	private int subusertype;
	private int deposittimes;
	private int withdrawaltimes;
	private int logintimes;
	private String email;
	private String phone;
	private int fatherid = -1;
	private int model;
	private double pointssc;
	private double point115;
	private double pointdp;
	private String nickname;
	private String fatherflag;
	private double deposit;
	private double withdrawal;
	private String questionkey;
	private String answer;
	private String linkavatar;
	private String qq;
	private int verifiedphone;
	/**
	 * 是否绑定银行卡
	 */
	private int bankcheck;
	/**
	 * 来源(手动添加\推广注册)
	 */
	private String param1;
	/**
	 * 上级用户名
	 */
	private String param2;
	/**
	 * 族谱名
	 */
	private String param3;
	/**
	 * 银行卡分组
	 */
	private int bankgroup;
	private int status;
	private String addtime;
	/**
	 * 注册IP
	 */
	private String regip;
	
	private String param4;
	/**
	 * 备注
	 */
	private String param5;
	/**
	 * 上级是否有返点
	 */
	private int fd=1;
	/**
	 * 预留（信誉等级）
	 */
	private String param6;
	/**
	 * 预留(每日最高提款次数)
	 */
	private int param7=0;
	/**
	 * 总流水
	 */
	private double totalbet;
	/**
	 * 总转入【NEW】
	 */
	private int param8=0;
	private long twdagent=0;
	private long tdpagent=0;
	/**
	 * 总分红
	 */
	private double param10;
	/**
	 * 万国钱包
	 */
	private String wggame;
	private Double wgwallet;
	/**
	 * 日工资
	 */
	private double slrday = 0.0;
	/**
	 * 总赠送礼金
	 */
	private double tbonus;
	//以下都是废弃字段。可以再次使用
	private String ticket;
	/**
	 * 下线数量
	 */
	private int sonnum;
	/**
	 * 自动分红文字说明【现在废弃字段】
	 */
	private String param9;
	/**
	 * 以下都是预留字段
	 */
	private double param11;
	private String param12;
	private double param13;
	private double param14;
	private double param15;
	/**
	 * tien dong bang do gian lan
	 */
	private double dj;

	
	// Vip user
	private int currentlevelvip=0;
	private int monthlevelvip=0;
	private int currentpoint=0;
	private double totalvipbet=0d;
	
	
	public long getTwdagent() {
		return twdagent;
	}
	public void setTwdagent(long twdagent) {
		this.twdagent = twdagent;
	}
	public long getTdpagent() {
		return tdpagent;
	}
	public void setTdpagent(long tdpagent) {
		this.tdpagent = tdpagent;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getCheckpwd() {
		return checkpwd;
	}
	public void setCheckpwd(String checkpwd) {
		this.checkpwd = checkpwd;
	}
	public String getLastlogintime() {
		return lastlogintime;
	}
	public void setLastlogintime(String lastlogintime) {
		this.lastlogintime = lastlogintime;
	}
	public String getLastloginip() {
		return lastloginip;
	}
	public void setLastloginip(String lastloginip) {
		this.lastloginip = lastloginip;
	}
	public int getUsertype() {
		return usertype;
	}
	public void setUsertype(int usertype) {
		this.usertype = usertype;
	}
	public int getSubusertype() {
		return subusertype;
	}
	public void setSubusertype(int subusertype) {
		this.subusertype = subusertype;
	}
	public int getDeposittimes() {
		return deposittimes;
	}
	public void setDeposittimes(int deposittimes) {
		this.deposittimes = deposittimes;
	}
	public int getWithdrawaltimes() {
		return withdrawaltimes;
	}
	public void setWithdrawaltimes(int withdrawaltimes) {
		this.withdrawaltimes = withdrawaltimes;
	}
	public int getLogintimes() {
		return logintimes;
	}
	public void setLogintimes(int logintimes) {
		this.logintimes = logintimes;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getFatherid() {
		return fatherid;
	}
	public void setFatherid(int fatherid) {
		this.fatherid = fatherid;
	}
	public int getModel() {
		return model;
	}
	public void setModel(int model) {
		this.model = model;
	}
	public double getPointssc() {
		return pointssc;
	}
	public void setPointssc(double pointssc) {
		this.pointssc = pointssc;
	}
	public double getPoint115() {
		return point115;
	}
	public void setPoint115(double point115) {
		this.point115 = point115;
	}
	public double getPointdp() {
		return pointdp;
	}
	public void setPointdp(double pointdp) {
		this.pointdp = pointdp;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getFatherflag() {
		return fatherflag;
	}
	public void setFatherflag(String fatherflag) {
		this.fatherflag = fatherflag;
	}
	public double getDeposit() {
		return deposit;
	}
	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}
	public double getWithdrawal() {
		return withdrawal;
	}
	public void setWithdrawal(double withdrawal) {
		this.withdrawal = withdrawal;
	}
	public int getSonnum() {
		return sonnum;
	}
	public void setSonnum(int sonnum) {
		this.sonnum = sonnum;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public int getBankcheck() {
		return bankcheck;
	}
	public void setBankcheck(int bankcheck) {
		this.bankcheck = bankcheck;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public int getBankgroup() {
		return bankgroup;
	}
	public void setBankgroup(int bankgroup) {
		this.bankgroup = bankgroup;
	}
	public String getRegip() {
		return regip;
	}
	public void setRegip(String regip) {
		this.regip = regip;
	}
	public String getParam4() {
		return param4;
	}
	public void setParam4(String param4) {
		this.param4 = param4;
	}
	public String getParam5() {
		return param5;
	}
	public void setParam5(String param5) {
		this.param5 = param5;
	}
	public int getFd() {
		return fd;
	}
	public void setFd(int fd) {
		this.fd = fd;
	}
	public String getParam6() {
		return param6;
	}
	public void setParam6(String param6) {
		this.param6 = param6;
	}
	public int getParam7() {
		return param7;
	}
	public void setParam7(int param7) {
		this.param7 = param7;
	}
	public double getTotalbet() {
		return totalbet;
	}
	public void setTotalbet(double totalbet) {
		this.totalbet = totalbet;
	}

	public String getParam9() {
		return param9;
	}
	public void setParam9(String param9) {
		this.param9 = param9;
	}
	public double getParam10() {
		return param10;
	}
	public void setParam10(double param10) {
		this.param10 = param10;
	}
	
    public String getWggame(){
       return this.wggame;
    }
    
    public void setWggame(String wggame){
    	 this.wggame=wggame;
    }
    
    public Double getWgwallet(){
    	return this.wgwallet;
    }
    
    public void setWgwallet(Double wgwallet){
    	this.wgwallet=wgwallet;
    }
	public int getParam8() {
		return param8;
	}
	public void setParam8(int param8) {
		this.param8 = param8;
	}
	public double getSlrday() {
		return slrday;
	}
	public void setSlrday(double slrday) {
		this.slrday = slrday;
	}
	public double getTbonus() {
		return tbonus;
	}
	public void setTbonus(double tbonus) {
		this.tbonus = tbonus;
	}
	public double getParam11() {
		return param11;
	}
	public void setParam11(double param11) {
		this.param11 = param11;
	}
	public String getParam12() {
		return param12;
	}
	public void setParam12(String param12) {
		this.param12 = param12;
	}
	public double getParam13() {
		return param13;
	}
	public void setParam13(double param13) {
		this.param13 = param13;
	}
	public double getParam14() {
		return param14;
	}
	public void setParam14(double param14) {
		this.param14 = param14;
	}
	public double getParam15() {
		return param15;
	}
	public void setParam15(double param15) {
		this.param15 = param15;
	}

	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getQuestionkey() {
		return questionkey;
	}
	public void setQuestionkey(String questionkey) {
		this.questionkey = questionkey;
	}
	public int getVerifiedphone() {
		return verifiedphone;
	}
	public void setVerifiedphone(int verifiedphone) {
		this.verifiedphone = verifiedphone;
	}
	public String getLinkavatar() {
		return linkavatar;
	}
	public void setLinkavatar(String linkavatar) {
		this.linkavatar = linkavatar;
	}
	public double getDj() {
		return dj;
	}
	public void setDj(double dj) {
		this.dj = dj;
	}
	public int getCurrentlevelvip() {
		return currentlevelvip;
	}
	public void setCurrentlevelvip(int currentlevelvip) {
		this.currentlevelvip = currentlevelvip;
	}
	public int getMonthlevelvip() {
		return monthlevelvip;
	}
	public void setMonthlevelvip(int monthlevelvip) {
		this.monthlevelvip = monthlevelvip;
	}
	public int getCurrentpoint() {
		return currentpoint;
	}
	public void setCurrentpoint(int currentpoint) {
		this.currentpoint = currentpoint;
	}
	public double getTotalvipbet() {
		return totalvipbet;
	}
	public void setTotalvipbet(double totalvipbet) {
		this.totalvipbet = totalvipbet;
	}

	

}
