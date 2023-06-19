package com.vinplay.item;

public class ReportDayItem implements java.io.Serializable{
	private static final long serialVersionUID = 3992022948659880981L;
	private String days;
	private Double sale=0d;
	private Double win=0d;
	private Double fastsale=0d;
	private Double fastwin=0d;
	private Double minigamesale=0d;
	private Double minigamewin=0d;
	private Double officesale=0d;
	private Double officewin=0d;
	private Double vipsale=0d;
	private Double vipwin=0d;
	private Double thirdsale=0d;
	private Double thirdwin=0d;
	private Double rewin=0d;
	private Double ml=0d;
	private Double bonus=0d;
	private Double jl1=0d;
	private Double dp=0d;
	private Double wd=0d;
	private Double jl2=0d;
	private Double other=0d;
	private Double jackpot=0d;

	private Integer nregnum=0;
	private Integer ndpnum=0;
	private Integer tdpnum=0;
	private Integer betnum=0;
	private Integer toponline=0;
	private String topremark;

	private Integer wdtimes=0;
	private Integer wdpeople=0;
	private Integer dptimes=0;

	private Double iosbet=0d;
	private Double androidbet=0d;
	private Double pcbet=0d;
	private Double mobilebet=0d;
	private Double dj=0.0;

	private Integer numag=0;
	private Integer numebet=0;
	private Integer numgg=0;
	private Integer numhaba=0;
	private Integer numibc2=0;
	private Integer numpt=0;
	private Integer numsa=0;
	private Integer numtd=0;

	private Integer numcmd=0;
	private Integer numsbo=0;

	private Integer numxsn=0;
	private Integer numminigame=0;
	private Integer numxstt=0;
	private Integer numxsvip=0;
	private Integer numdg=0;
	private Integer numwm=0;
	private Integer numesport=0;
	private Integer numbbin=0;

	private Integer numsg=0;

	public Integer getNumbbin() {
		return numbbin;
	}
	public void setNumbbin(Integer numbbin) {
		this.numbbin = numbbin;
	}
	public Integer getNumesport() {
		return numesport;
	}
	public void setNumesport(Integer numesport) {
		this.numesport = numesport;
	}
	public Integer getNumwm() {
		return numwm;
	}
	public void setNumwm(Integer numwm) {
		this.numwm = numwm;
	}
	public Integer getNumxsn() {
		return numxsn;
	}
	public void setNumxsn(Integer numxsn) {
		this.numxsn = numxsn;
	}
	public Integer getNumxstt() {
		return numxstt;
	}
	public void setNumxstt(Integer numxstt) {
		this.numxstt = numxstt;
	}
	public Integer getNumdg() {
		return numdg;
	}
	public void setNumdg(Integer numdg) {
		this.numdg = numdg;
	}
	public Integer getNumag() {
		return numag;
	}
	public void setNumag(Integer numag) {
		this.numag = numag;
	}
	public Integer getNumebet() {
		return numebet;
	}
	public void setNumebet(Integer numebet) {
		this.numebet = numebet;
	}
	public Integer getNumgg() {
		return numgg;
	}
	public void setNumgg(Integer numgg) {
		this.numgg = numgg;
	}
	public Integer getNumhaba() {
		return numhaba;
	}
	public void setNumhaba(Integer numhaba) {
		this.numhaba = numhaba;
	}
	public Integer getNumibc2() {
		return numibc2;
	}
	public void setNumibc2(Integer numibc2) {
		this.numibc2 = numibc2;
	}
	public Integer getNumpt() {
		return numpt;
	}
	public void setNumpt(Integer numpt) {
		this.numpt = numpt;
	}
	public Integer getNumsa() {
		return numsa;
	}
	public void setNumsa(Integer numsa) {
		this.numsa = numsa;
	}
	public Integer getNumtd() {
		return numtd;
	}
	public void setNumtd(Integer numtd) {
		this.numtd = numtd;
	}
	public Double getIosbet() {
		return iosbet;
	}
	public void setIosbet(Double iosbet) {
		this.iosbet = iosbet;
	}
	public Double getAndroidbet() {
		return androidbet;
	}
	public void setAndroidbet(Double androidbet) {
		this.androidbet = androidbet;
	}
	public Double getPcbet() {
		return pcbet;
	}
	public void setPcbet(Double pcbet) {
		this.pcbet = pcbet;
	}
	public Double getMobilebet() {
		return mobilebet;
	}
	public void setMobilebet(Double mobilebet) {
		this.mobilebet = mobilebet;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public Double getSale() {
		return sale;
	}
	public void setSale(Double sale) {
		this.sale = sale;
	}
	public Double getWin() {
		return win;
	}
	public void setWin(Double win) {
		this.win = win;
	}

	public Double getFastsale() {
		return fastsale;
	}

	public void setFastsale(Double fastsale) {
		this.fastsale = fastsale;
	}

	public Double getFastwin() {
		return fastwin;
	}

	public void setFastwin(Double fastwin) {
		this.fastwin = fastwin;
	}

	public Double getOfficesale() {
		return officesale;
	}

	public void setOfficesale(Double officesale) {
		this.officesale = officesale;
	}

	public Double getOfficewin() {
		return officewin;
	}

	public void setOfficewin(Double officewin) {
		this.officewin = officewin;
	}

	public Double getVipsale() {
		return vipsale;
	}

	public void setVipsale(Double vipsale) {
		this.vipsale = vipsale;
	}

	public Double getVipwin() {
		return vipwin;
	}

	public void setVipwin(Double vipwin) {
		this.vipwin = vipwin;
	}

	public Double getThirdsale() {
		return thirdsale;
	}

	public void setThirdsale(Double thirdsale) {
		this.thirdsale = thirdsale;
	}

	public Double getThirdwin() {
		return thirdwin;
	}

	public void setThirdwin(Double thirdwin) {
		this.thirdwin = thirdwin;
	}

	public Double getRewin() {
		return rewin;
	}
	public void setRewin(Double rewin) {
		this.rewin = rewin;
	}
	public Double getMl() {
		return ml;
	}
	public void setMl(Double ml) {
		this.ml = ml;
	}
	public Double getBonus() {
		return bonus;
	}
	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}
	public Double getJl1() {
		return jl1;
	}
	public void setJl1(Double jl1) {
		this.jl1 = jl1;
	}
	public Double getDp() {
		return dp;
	}
	public void setDp(Double dp) {
		this.dp = dp;
	}
	public Double getWd() {
		return wd;
	}
	public void setWd(Double wd) {
		this.wd = wd;
	}
	public Double getJl2() {
		return jl2;
	}
	public void setJl2(Double jl2) {
		this.jl2 = jl2;
	}
	public Integer getNregnum() {
		return nregnum;
	}
	public void setNregnum(Integer nregnum) {
		this.nregnum = nregnum;
	}
	public Integer getNdpnum() {
		return ndpnum;
	}
	public void setNdpnum(Integer ndpnum) {
		this.ndpnum = ndpnum;
	}
	public Integer getTdpnum() {
		return tdpnum;
	}
	public void setTdpnum(Integer tdpnum) {
		this.tdpnum = tdpnum;
	}
	public Integer getBetnum() {
		return betnum;
	}
	public void setBetnum(Integer betnum) {
		this.betnum = betnum;
	}
	public Integer getToponline() {
		return toponline;
	}
	public void setToponline(Integer toponline) {
		this.toponline = toponline;
	}
	public String getTopremark() {
		return topremark;
	}
	public void setTopremark(String topremark) {
		this.topremark = topremark;
	}
	public Double getOther() {
		return other;
	}
	public void setOther(Double other) {
		this.other = other;
	}
	public Integer getWdtimes() {
		return wdtimes;
	}
	public void setWdtimes(Integer wdtimes) {
		this.wdtimes = wdtimes;
	}
	public Integer getWdpeople() {
		return wdpeople;
	}
	public void setWdpeople(Integer wdpeople) {
		this.wdpeople = wdpeople;
	}
	public Integer getDptimes() {
		return dptimes;
	}
	public void setDptimes(Integer dptimes) {
		this.dptimes = dptimes;
	}
	public Integer getNumcmd() {
		return numcmd;
	}
	public void setNumcmd(Integer numcmd) {
		this.numcmd = numcmd;
	}
	public Integer getNumsbo() {
		return numsbo;
	}
	public void setNumsbo(Integer numsbo) {
		this.numsbo = numsbo;
	}
	public Integer getNumxsvip() {
		return numxsvip;
	}
	public void setNumxsvip(Integer numxsvip) {
		this.numxsvip = numxsvip;
	}
	public Double getDj() {
		return dj;
	}
	public void setDj(Double dj) {
		this.dj = dj;
	}
	public Integer getNumsg() {
		return numsg;
	}
	public void setNumsg(Integer numsg) {
		this.numsg = numsg;
	}
	public Double getJackpot() {
		return jackpot;
	}
	public void setJackpot(Double jackpot) {
		this.jackpot = jackpot;
	}

	public Double getMinigamesale() {
		return minigamesale;
	}

	public void setMinigamesale(Double minigamesale) {
		this.minigamesale = minigamesale;
	}

	public Double getMinigamewin() {
		return minigamewin;
	}

	public void setMinigamewin(Double minigamewin) {
		this.minigamewin = minigamewin;
	}

	public Integer getNumminigame() {
		return numminigame;
	}

	public void setNumminigame(Integer numminigame) {
		this.numminigame = numminigame;
	}
}
