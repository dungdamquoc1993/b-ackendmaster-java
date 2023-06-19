package com.vinplay.item;

import java.io.Serializable;

public class DailyReportItem implements Serializable {
	private static final long serialVersionUID = 2015324887574056470L;
	private String rtime;
	private Double winamount = 0.0D;
	private Double betamount = 0.0D;
	private Double rebate = 0.0D;
	private Double profit = 0.0D;
	private Double membercomm = 0.0D;
	private Double agencomm = 0.0D;
	private Double membercashback = 0.0D;
	private Double promotions = 0.0D;
	private Double netprofit1 = 0.0D;
	private Double totaldeposit = 0.0D;
	private Double totalwithdraw = 0.0D;
	private Double netprofit2 = 0.0D;
	private Double dj = 0.0D;
	private Double adjdp = 0.0D;
	private Double other = 0.0D;
	private Integer countuserbet = 0;
	private Integer count3rduserbet = 0;
	private Integer countlotteryuserbet = 0;
	private Integer countuserdeposit = 0;
	private Integer countcommanddeposit = 0;
	private Integer countuserwithdraw = 0;
	private Integer countcommandwithdraw = 0;
	private Integer countuserregister = 0;
	private Integer countusernewdeposit = 0;
	private Double totalbetamtpc = 0.0D;
	private Double totalbetamtwap = 0.0D;
	private Double totalbetamtios = 0.0D;
	private Double totalbetamtandroid = 0.0D;
	private Double totalbetuser = 0.0D;
	private Double ibc2 = 0.0D;
	private Double ibc2win = 0.0D;
	private Integer ibc2countuserbet = 0;
	private Double haba = 0.0D;
	private Double habawin = 0.0D;
	private Integer habacountuserbet = 0;
	private Double ebet = 0.0D;
	private Double ebetwin = 0.0D;
	private Integer ebetcountuserbet = 0;
	private Double gg = 0.0D;
	private Double ggwin = 0.0D;
	private Integer ggcountuserbet = 0;
	private Double dg = 0.0D;
	private Double dgwin = 0.0D;
	private Integer dgcountuserbet = 0;
	private Double pt = 0.0D;
	private Double ptwin = 0.0D;
	private Integer ptcountuserbet = 0;
	private Double ag = 0.0D;
	private Double agwin = 0.0D;
	private Integer agcountuserbet = 0;
	private Double td = 0.0D;
	private Double tdwin = 0.0D;
	private Integer tdcountuserbet = 0;
	private Double wm = 0.0D;
	private Double wmwin = 0.0D;
	private Integer wmcountuserbet = 0;
	private Double cmd = 0.0D;
	private Double cmdwin = 0.0D;
	private Integer cmdcountuserbet = 0;
	private Double sa = 0.0D;
	private Double sawin = 0.0D;
	private Integer sacountuserbet = 0;
	private Double esport = 0.0D;
	private Double esportwin = 0.0D;
	private Integer esportcountuserbet = 0;
	private Double sg = 0.0D;
	private Double sgwin = 0.0D;
	private Integer sgcountuserbet = 0;
	private Double sbo = 0.0D;
	private Double sbowin = 0.0D;
	private Integer sbocountuserbet = 0;
	private Double dangian = 0.0D;
	private Double dangianwin = 0.0D;
	private Integer dangiancountuserbet = 0;
	private Double dangianpc = 0.0D;
	private Double dangianwap = 0.0D;
	private Double dangianios = 0.0D;
	private Double dangianandroid = 0.0D;
	private Double fastlottery = 0.0D;
	private Double fastlotterywin = 0.0D;
	private Integer fastlotterycountuserbet = 0;
	private Double fastlotterypc = 0.0D;
	private Double fastlotterywap = 0.0D;
	private Double fastlotteryios = 0.0D;
	private Double fastlotteryandroid = 0.0D;
	private Double mienbac = 0.0D;
	private Double mienbacwin = 0.0D;
	private Integer mienbaccountuserbet = 0;
	private Double mienbacpc = 0.0D;
	private Double mienbacwap = 0.0D;
	private Double mienbacios = 0.0D;
	private Double mienbacandroid = 0.0D;
	private Double miennam = 0.0D;
	private Double miennamwin = 0.0D;
	private Integer miennamcountuserbet = 0;
	private Double miennampc = 0.0D;
	private Double miennamwap = 0.0D;
	private Double miennamios = 0.0D;
	private Double miennamandroid = 0.0D;
	private Double mientrung = 0.0D;
	private Double mientrungwin = 0.0D;
	private Integer mientrungcountuserbet = 0;
	private Double mientrungpc = 0.0D;
	private Double mientrungwap = 0.0D;
	private Double mientrungios = 0.0D;
	private Double mientrungandroid = 0.0D;
	private Double pp = 0.0D;
	private Double ppwin = 0.0D;
	private Double pp5p = 0.0D;
	private Double pp5pwin = 0.0D;
	private Double ppmb = 0.0D;
	private Double ppmbwin = 0.0D;
	private Integer ppcountuserbet = 0;
	private Double xsvip = 0.0D;
	private Double xsvipwin = 0.0D;
	private Integer xsvipcountuserbet = 0;
	private Double xsvippc = 0.0D;
	private Double xsvipwap = 0.0D;
	private Double xsvipios = 0.0D;
	private Double xsvipandroid = 0.0D;

	private Double bbin = 0.0D;
	private Double bbinwin = 0.0D;
	private Integer bbincountuserbet = 0;

    private Double minigame = 0.0D;
    private Double minigamewin = 0.0D;
    private Integer minigamecountuserbet = 0;
    private Double minigamepc = 0.0D;
    private Double minigamewap = 0.0D;
    private Double minigameios = 0.0D;
    private Double minigameandroid = 0.0D;


	public Double getBbin() {
		return bbin;
	}

	public void setBbin(Double bbin) {
		this.bbin = bbin;
	}

	public Double getBbinwin() {
		return bbinwin;
	}

	public void setBbinwin(Double bbinwin) {
		this.bbinwin = bbinwin;
	}

	public Integer getBbincountuserbet() {
		return bbincountuserbet;
	}

	public void setBbincountuserbet(Integer bbincountuserbet) {
		this.bbincountuserbet = bbincountuserbet;
	}

	public String getRtime() {
		return rtime;
	}

	public void setRtime(String rtime) {
		this.rtime = rtime;
	}

	public Double getWinamount() {
		return winamount;
	}

	public void setWinamount(Double winamount) {
		this.winamount = winamount;
	}

	public Double getBetamount() {
		return betamount;
	}

	public void setBetamount(Double betamount) {
		this.betamount = betamount;
	}

	public Double getRebate() {
		return rebate;
	}

	public void setRebate(Double rebate) {
		this.rebate = rebate;
	}

	public Double getProfit() {
		return profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
	}

	public Double getMembercomm() {
		return membercomm;
	}

	public void setMembercomm(Double membercomm) {
		this.membercomm = membercomm;
	}

	public Double getAgencomm() {
		return agencomm;
	}

	public void setAgencomm(Double agencomm) {
		this.agencomm = agencomm;
	}

	public Double getMembercashback() {
		return membercashback;
	}

	public void setMembercashback(Double membercashback) {
		this.membercashback = membercashback;
	}

	public Double getPromotions() {
		return promotions;
	}

	public void setPromotions(Double promotions) {
		this.promotions = promotions;
	}

	public Double getNetprofit1() {
		return netprofit1;
	}

	public void setNetprofit1(Double netprofit1) {
		this.netprofit1 = netprofit1;
	}

	public Double getTotaldeposit() {
		return totaldeposit;
	}

	public void setTotaldeposit(Double totaldeposit) {
		this.totaldeposit = totaldeposit;
	}

	public Double getTotalwithdraw() {
		return totalwithdraw;
	}

	public void setTotalwithdraw(Double totalwithdraw) {
		this.totalwithdraw = totalwithdraw;
	}

	public Double getNetprofit2() {
		return netprofit2;
	}

	public void setNetprofit2(Double netprofit2) {
		this.netprofit2 = netprofit2;
	}

	public Double getDj() {
		return dj;
	}

	public void setDj(Double dj) {
		this.dj = dj;
	}

	public Double getAdjdp() {
		return adjdp;
	}

	public void setAdjdp(Double adjdp) {
		this.adjdp = adjdp;
	}

	public Double getOther() {
		return other;
	}

	public void setOther(Double other) {
		this.other = other;
	}

	public Integer getCountuserbet() {
		return countuserbet;
	}

	public void setCountuserbet(Integer countuserbet) {
		this.countuserbet = countuserbet;
	}

	public Integer getCount3rduserbet() {
		return count3rduserbet;
	}

	public void setCount3rduserbet(Integer count3rduserbet) {
		this.count3rduserbet = count3rduserbet;
	}

	public Integer getCountlotteryuserbet() {
		return countlotteryuserbet;
	}

	public void setCountlotteryuserbet(Integer countlotteryuserbet) {
		this.countlotteryuserbet = countlotteryuserbet;
	}

	public Integer getCountuserdeposit() {
		return countuserdeposit;
	}

	public void setCountuserdeposit(Integer countuserdeposit) {
		this.countuserdeposit = countuserdeposit;
	}

	public Integer getCountcommanddeposit() {
		return countcommanddeposit;
	}

	public void setCountcommanddeposit(Integer countcommanddeposit) {
		this.countcommanddeposit = countcommanddeposit;
	}

	public Integer getCountuserwithdraw() {
		return countuserwithdraw;
	}

	public void setCountuserwithdraw(Integer countuserwithdraw) {
		this.countuserwithdraw = countuserwithdraw;
	}

	public Integer getCountcommandwithdraw() {
		return countcommandwithdraw;
	}

	public void setCountcommandwithdraw(Integer countcommandwithdraw) {
		this.countcommandwithdraw = countcommandwithdraw;
	}

	public Integer getCountuserregister() {
		return countuserregister;
	}

	public void setCountuserregister(Integer countuserregister) {
		this.countuserregister = countuserregister;
	}

	public Integer getCountusernewdeposit() {
		return countusernewdeposit;
	}

	public void setCountusernewdeposit(Integer countusernewdeposit) {
		this.countusernewdeposit = countusernewdeposit;
	}

	public Double getTotalbetamtpc() {
		return totalbetamtpc;
	}

	public void setTotalbetamtpc(Double totalbetamtpc) {
		this.totalbetamtpc = totalbetamtpc;
	}

	public Double getTotalbetamtwap() {
		return totalbetamtwap;
	}

	public void setTotalbetamtwap(Double totalbetamtwap) {
		this.totalbetamtwap = totalbetamtwap;
	}

	public Double getTotalbetamtios() {
		return totalbetamtios;
	}

	public void setTotalbetamtios(Double totalbetamtios) {
		this.totalbetamtios = totalbetamtios;
	}

	public Double getTotalbetamtandroid() {
		return totalbetamtandroid;
	}

	public void setTotalbetamtandroid(Double totalbetamtandroid) {
		this.totalbetamtandroid = totalbetamtandroid;
	}

	public Double getTotalbetuser() {
		return totalbetuser;
	}

	public void setTotalbetuser(Double totalbetuser) {
		this.totalbetuser = totalbetuser;
	}

	public Double getIbc2() {
		return ibc2;
	}

	public void setIbc2(Double ibc2) {
		this.ibc2 = ibc2;
	}

	public Double getIbc2win() {
		return ibc2win;
	}

	public void setIbc2win(Double ibc2win) {
		this.ibc2win = ibc2win;
	}

	public Integer getIbc2countuserbet() {
		return ibc2countuserbet;
	}

	public void setIbc2countuserbet(Integer ibc2countuserbet) {
		this.ibc2countuserbet = ibc2countuserbet;
	}

	public Double getHaba() {
		return haba;
	}

	public void setHaba(Double haba) {
		this.haba = haba;
	}

	public Double getHabawin() {
		return habawin;
	}

	public void setHabawin(Double habawin) {
		this.habawin = habawin;
	}

	public Integer getHabacountuserbet() {
		return habacountuserbet;
	}

	public void setHabacountuserbet(Integer habacountuserbet) {
		this.habacountuserbet = habacountuserbet;
	}

	public Double getEbet() {
		return ebet;
	}

	public void setEbet(Double ebet) {
		this.ebet = ebet;
	}

	public Double getEbetwin() {
		return ebetwin;
	}

	public void setEbetwin(Double ebetwin) {
		this.ebetwin = ebetwin;
	}

	public Integer getEbetcountuserbet() {
		return ebetcountuserbet;
	}

	public void setEbetcountuserbet(Integer ebetcountuserbet) {
		this.ebetcountuserbet = ebetcountuserbet;
	}

	public Double getGg() {
		return gg;
	}

	public void setGg(Double gg) {
		this.gg = gg;
	}

	public Double getGgwin() {
		return ggwin;
	}

	public void setGgwin(Double ggwin) {
		this.ggwin = ggwin;
	}

	public Integer getGgcountuserbet() {
		return ggcountuserbet;
	}

	public void setGgcountuserbet(Integer ggcountuserbet) {
		this.ggcountuserbet = ggcountuserbet;
	}

	public Double getDg() {
		return dg;
	}

	public void setDg(Double dg) {
		this.dg = dg;
	}

	public Double getDgwin() {
		return dgwin;
	}

	public void setDgwin(Double dgwin) {
		this.dgwin = dgwin;
	}

	public Integer getDgcountuserbet() {
		return dgcountuserbet;
	}

	public void setDgcountuserbet(Integer dgcountuserbet) {
		this.dgcountuserbet = dgcountuserbet;
	}

	public Double getPt() {
		return pt;
	}

	public void setPt(Double pt) {
		this.pt = pt;
	}

	public Double getPtwin() {
		return ptwin;
	}

	public void setPtwin(Double ptwin) {
		this.ptwin = ptwin;
	}

	public Integer getPtcountuserbet() {
		return ptcountuserbet;
	}

	public void setPtcountuserbet(Integer ptcountuserbet) {
		this.ptcountuserbet = ptcountuserbet;
	}

	public Double getAg() {
		return ag;
	}

	public void setAg(Double ag) {
		this.ag = ag;
	}

	public Double getAgwin() {
		return agwin;
	}

	public void setAgwin(Double agwin) {
		this.agwin = agwin;
	}

	public Integer getAgcountuserbet() {
		return agcountuserbet;
	}

	public void setAgcountuserbet(Integer agcountuserbet) {
		this.agcountuserbet = agcountuserbet;
	}

	public Double getTd() {
		return td;
	}

	public void setTd(Double td) {
		this.td = td;
	}

	public Double getTdwin() {
		return tdwin;
	}

	public void setTdwin(Double tdwin) {
		this.tdwin = tdwin;
	}

	public Integer getTdcountuserbet() {
		return tdcountuserbet;
	}

	public void setTdcountuserbet(Integer tdcountuserbet) {
		this.tdcountuserbet = tdcountuserbet;
	}

	public Double getWm() {
		return wm;
	}

	public void setWm(Double wm) {
		this.wm = wm;
	}

	public Double getWmwin() {
		return wmwin;
	}

	public void setWmwin(Double wmwin) {
		this.wmwin = wmwin;
	}

	public Integer getWmcountuserbet() {
		return wmcountuserbet;
	}

	public void setWmcountuserbet(Integer wmcountuserbet) {
		this.wmcountuserbet = wmcountuserbet;
	}

	public Double getCmd() {
		return cmd;
	}

	public void setCmd(Double cmd) {
		this.cmd = cmd;
	}

	public Double getCmdwin() {
		return cmdwin;
	}

	public void setCmdwin(Double cmdwin) {
		this.cmdwin = cmdwin;
	}

	public Integer getCmdcountuserbet() {
		return cmdcountuserbet;
	}

	public void setCmdcountuserbet(Integer cmdcountuserbet) {
		this.cmdcountuserbet = cmdcountuserbet;
	}

	public Double getSa() {
		return sa;
	}

	public void setSa(Double sa) {
		this.sa = sa;
	}

	public Double getSawin() {
		return sawin;
	}

	public void setSawin(Double sawin) {
		this.sawin = sawin;
	}

	public Integer getSacountuserbet() {
		return sacountuserbet;
	}

	public void setSacountuserbet(Integer sacountuserbet) {
		this.sacountuserbet = sacountuserbet;
	}

	public Double getDangian() {
		return dangian;
	}

	public void setDangian(Double dangian) {
		this.dangian = dangian;
	}

	public Double getDangianwin() {
		return dangianwin;
	}

	public void setDangianwin(Double dangianwin) {
		this.dangianwin = dangianwin;
	}

	public Integer getDangiancountuserbet() {
		return dangiancountuserbet;
	}

	public void setDangiancountuserbet(Integer dangiancountuserbet) {
		this.dangiancountuserbet = dangiancountuserbet;
	}

	public Double getDangianpc() {
		return dangianpc;
	}

	public void setDangianpc(Double dangianpc) {
		this.dangianpc = dangianpc;
	}

	public Double getDangianwap() {
		return dangianwap;
	}

	public void setDangianwap(Double dangianwap) {
		this.dangianwap = dangianwap;
	}

	public Double getDangianios() {
		return dangianios;
	}

	public void setDangianios(Double dangianios) {
		this.dangianios = dangianios;
	}

	public Double getDangianandroid() {
		return dangianandroid;
	}

	public void setDangianandroid(Double dangianandroid) {
		this.dangianandroid = dangianandroid;
	}

	public Double getFastlottery() {
		return fastlottery;
	}

	public void setFastlottery(Double fastlottery) {
		this.fastlottery = fastlottery;
	}

	public Double getFastlotterywin() {
		return fastlotterywin;
	}

	public void setFastlotterywin(Double fastlotterywin) {
		this.fastlotterywin = fastlotterywin;
	}

	public Integer getFastlotterycountuserbet() {
		return fastlotterycountuserbet;
	}

	public void setFastlotterycountuserbet(Integer fastlotterycountuserbet) {
		this.fastlotterycountuserbet = fastlotterycountuserbet;
	}

	public Double getFastlotterypc() {
		return fastlotterypc;
	}

	public void setFastlotterypc(Double fastlotterypc) {
		this.fastlotterypc = fastlotterypc;
	}

	public Double getFastlotterywap() {
		return fastlotterywap;
	}

	public void setFastlotterywap(Double fastlotterywap) {
		this.fastlotterywap = fastlotterywap;
	}

	public Double getFastlotteryios() {
		return fastlotteryios;
	}

	public void setFastlotteryios(Double fastlotteryios) {
		this.fastlotteryios = fastlotteryios;
	}

	public Double getFastlotteryandroid() {
		return fastlotteryandroid;
	}

	public void setFastlotteryandroid(Double fastlotteryandroid) {
		this.fastlotteryandroid = fastlotteryandroid;
	}

	public Double getMienbac() {
		return mienbac;
	}

	public void setMienbac(Double mienbac) {
		this.mienbac = mienbac;
	}

	public Double getMienbacwin() {
		return mienbacwin;
	}

	public void setMienbacwin(Double mienbacwin) {
		this.mienbacwin = mienbacwin;
	}

	public Integer getMienbaccountuserbet() {
		return mienbaccountuserbet;
	}

	public void setMienbaccountuserbet(Integer mienbaccountuserbet) {
		this.mienbaccountuserbet = mienbaccountuserbet;
	}

	public Double getMienbacpc() {
		return mienbacpc;
	}

	public void setMienbacpc(Double mienbacpc) {
		this.mienbacpc = mienbacpc;
	}

	public Double getMienbacwap() {
		return mienbacwap;
	}

	public void setMienbacwap(Double mienbacwap) {
		this.mienbacwap = mienbacwap;
	}

	public Double getMienbacios() {
		return mienbacios;
	}

	public void setMienbacios(Double mienbacios) {
		this.mienbacios = mienbacios;
	}

	public Double getMienbacandroid() {
		return mienbacandroid;
	}

	public void setMienbacandroid(Double mienbacandroid) {
		this.mienbacandroid = mienbacandroid;
	}

	public Double getMiennam() {
		return miennam;
	}

	public void setMiennam(Double miennam) {
		this.miennam = miennam;
	}

	public Double getMiennamwin() {
		return miennamwin;
	}

	public void setMiennamwin(Double miennamwin) {
		this.miennamwin = miennamwin;
	}

	public Integer getMiennamcountuserbet() {
		return miennamcountuserbet;
	}

	public void setMiennamcountuserbet(Integer miennamcountuserbet) {
		this.miennamcountuserbet = miennamcountuserbet;
	}

	public Double getMiennampc() {
		return miennampc;
	}

	public void setMiennampc(Double miennampc) {
		this.miennampc = miennampc;
	}

	public Double getMiennamwap() {
		return miennamwap;
	}

	public void setMiennamwap(Double miennamwap) {
		this.miennamwap = miennamwap;
	}

	public Double getMiennamios() {
		return miennamios;
	}

	public void setMiennamios(Double miennamios) {
		this.miennamios = miennamios;
	}

	public Double getMiennamandroid() {
		return miennamandroid;
	}

	public void setMiennamandroid(Double miennamandroid) {
		this.miennamandroid = miennamandroid;
	}

	public Double getMientrung() {
		return mientrung;
	}

	public void setMientrung(Double mientrung) {
		this.mientrung = mientrung;
	}

	public Double getMientrungwin() {
		return mientrungwin;
	}

	public void setMientrungwin(Double mientrungwin) {
		this.mientrungwin = mientrungwin;
	}

	public Integer getMientrungcountuserbet() {
		return mientrungcountuserbet;
	}

	public void setMientrungcountuserbet(Integer mientrungcountuserbet) {
		this.mientrungcountuserbet = mientrungcountuserbet;
	}

	public Double getMientrungpc() {
		return mientrungpc;
	}

	public void setMientrungpc(Double mientrungpc) {
		this.mientrungpc = mientrungpc;
	}

	public Double getMientrungwap() {
		return mientrungwap;
	}

	public void setMientrungwap(Double mientrungwap) {
		this.mientrungwap = mientrungwap;
	}

	public Double getMientrungios() {
		return mientrungios;
	}

	public void setMientrungios(Double mientrungios) {
		this.mientrungios = mientrungios;
	}

	public Double getMientrungandroid() {
		return mientrungandroid;
	}

	public void setMientrungandroid(Double mientrungandroid) {
		this.mientrungandroid = mientrungandroid;
	}

	public Double getXsvip() {
		return xsvip;
	}

	public void setXsvip(Double xsvip) {
		this.xsvip = xsvip;
	}

	public Double getXsvipwin() {
		return xsvipwin;
	}

	public void setXsvipwin(Double xsvipwin) {
		this.xsvipwin = xsvipwin;
	}

	public Integer getXsvipcountuserbet() {
		return xsvipcountuserbet;
	}

	public void setXsvipcountuserbet(Integer xsvipcountuserbet) {
		this.xsvipcountuserbet = xsvipcountuserbet;
	}

	public Double getXsvippc() {
		return xsvippc;
	}

	public void setXsvippc(Double xsvippc) {
		this.xsvippc = xsvippc;
	}

	public Double getXsvipwap() {
		return xsvipwap;
	}

	public void setXsvipwap(Double xsvipwap) {
		this.xsvipwap = xsvipwap;
	}

	public Double getXsvipios() {
		return xsvipios;
	}

	public void setXsvipios(Double xsvipios) {
		this.xsvipios = xsvipios;
	}

	public Double getXsvipandroid() {
		return xsvipandroid;
	}

	public void setXsvipandroid(Double xsvipandroid) {
		this.xsvipandroid = xsvipandroid;
	}

	public Double getSg() {
		return sg;
	}

	public void setSg(Double sg) {
		this.sg = sg;
	}

	public Double getSgwin() {
		return sgwin;
	}

	public void setSgwin(Double sgwin) {
		this.sgwin = sgwin;
	}

	public Integer getSgcountuserbet() {
		return sgcountuserbet;
	}

	public void setSgcountuserbet(Integer sgcountuserbet) {
		this.sgcountuserbet = sgcountuserbet;
	}

	public Double getEsport() {
		return esport;
	}

	public void setEsport(Double esport) {
		this.esport = esport;
	}

	public Double getEsportwin() {
		return esportwin;
	}

	public void setEsportwin(Double esportwin) {
		this.esportwin = esportwin;
	}

	public Integer getEsportcountuserbet() {
		return esportcountuserbet;
	}

	public void setEsportcountuserbet(Integer esportcountuserbet) {
		this.esportcountuserbet = esportcountuserbet;
	}

	public Double getSbo() {
		return sbo;
	}

	public void setSbo(Double sbo) {
		this.sbo = sbo;
	}

	public Double getSbowin() {
		return sbowin;
	}

	public void setSbowin(Double sbowin) {
		this.sbowin = sbowin;
	}

	public Integer getSbocountuserbet() {
		return sbocountuserbet;
	}

	public void setSbocountuserbet(Integer sbocountuserbet) {
		this.sbocountuserbet = sbocountuserbet;
	}

	public Double getPp() {
		return pp;
	}

	public void setPp(Double pp) {
		this.pp = pp;
	}

	public Double getPpwin() {
		return ppwin;
	}

	public void setPpwin(Double ppwin) {
		this.ppwin = ppwin;
	}

	public Integer getPpcountuserbet() {
		return ppcountuserbet;
	}

	public void setPpcountuserbet(Integer ppcountuserbet) {
		this.ppcountuserbet = ppcountuserbet;
	}

	public Double getPp5p() {
		return pp5p;
	}

	public void setPp5p(Double pp5p) {
		this.pp5p = pp5p;
	}

	public Double getPp5pwin() {
		return pp5pwin;
	}

	public void setPp5pwin(Double pp5pwin) {
		this.pp5pwin = pp5pwin;
	}

	public Double getPpmb() {
		return ppmb;
	}

	public void setPpmb(Double ppmb) {
		this.ppmb = ppmb;
	}

	public Double getPpmbwin() {
		return ppmbwin;
	}

	public void setPpmbwin(Double ppmbwin) {
		this.ppmbwin = ppmbwin;
	}

    public Double getMinigame() {
        return minigame;
    }

    public void setMinigame(Double minigame) {
        this.minigame = minigame;
    }

    public Double getMinigamewin() {
        return minigamewin;
    }

    public void setMinigamewin(Double minigamewin) {
        this.minigamewin = minigamewin;
    }

    public Integer getMinigamecountuserbet() {
        return minigamecountuserbet;
    }

    public void setMinigamecountuserbet(Integer minigamecountuserbet) {
        this.minigamecountuserbet = minigamecountuserbet;
    }

    public Double getMinigamepc() {
        return minigamepc;
    }

    public void setMinigamepc(Double minigamepc) {
        this.minigamepc = minigamepc;
    }

    public Double getMinigamewap() {
        return minigamewap;
    }

    public void setMinigamewap(Double minigamewap) {
        this.minigamewap = minigamewap;
    }

    public Double getMinigameios() {
        return minigameios;
    }

    public void setMinigameios(Double minigameios) {
        this.minigameios = minigameios;
    }

    public Double getMinigameandroid() {
        return minigameandroid;
    }

    public void setMinigameandroid(Double minigameandroid) {
        this.minigameandroid = minigameandroid;
    }
}
