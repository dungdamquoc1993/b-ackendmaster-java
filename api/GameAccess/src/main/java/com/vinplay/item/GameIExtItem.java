package com.vinplay.item;

public class GameIExtItem implements java.io.Serializable{
	private static final long serialVersionUID = 3429646253006202496L;
	private String rtime;
	private String loginname;
	private Integer fatherid=-1;
	private String fatherstr;
	private Double win=0.0;
	
	private Double dp=0.0;
	private Double wd=0.0;
	private Double tz=0.0;
	private Double rf=0.0;
	private Double saletz=0.0;
	private Double ibcsport=0.0;
	private Double ibcsportwin=0.0;
	private Double ibc2sport=0.0;
	private Double ibc2sportvb=0.0;
	private Double ibc2vb=0.0;
	private Double ibc2sportwin=0.0;
	
	private Double haba=0.0;
	private Double habawin=0.0;
	private Double habadp=0.0;
	private Double habawd=0.0;
	private Double habarf=0.0;
	private Double habavb=0.0;
	
	private Double ebet=0.0;
	private Double ebetwin=0.0;
	private Double ebetdp=0.0;
	private Double ebetwd=0.0;
	private Double ebetrf=0.0;
	private Double ebetvb=0.0;
	
	private Double gg=0.0;
	private Double ggwin=0.0;
	private Double ggdp=0.0;
	private Double ggwd=0.0;
	private Double ggrf=0.0;
	private Double ggvb=0.0;

	private Double dg=0.0;
	private Double dgwin=0.0;
	private Double dgdp=0.0;
	private Double dgwd=0.0;
	private Double dgrf=0.0;
	private Double dgvb=0.0;
	
	private Double pt = 0.0;
	private Double ptwin = 0.0;
	private Double ptdp = 0.0;
	private Double ptwd = 0.0;
	private Double ptrf = 0.0;
	private Double ptvb=0.0;
	
	private Double td=0.0;
	private Double tdwin=0.0;
	private Double tddp=0.0;
	private Double tdwd=0.0;
	private Double tdrf=0.0;
	private Double tdvb=0.0;
	
	private Double sa=0.0;
	private Double sawin=0.0;
	private Double sadp=0.0;
	private Double sawd=0.0;
	private Double sarf=0.0;
	private Double savb=0.0;
	
	private Double ag=0.0;
	private Double agwin=0.0;
	private Double agdp=0.0;
	private Double agwd=0.0;
	private Double agrf=0.0;
	private Double agvb=0.0;
	
	private Double cmd=0.0;
	private Double cmdwin=0.0;
	private Double cmddp=0.0;
	private Double cmdwd=0.0;
	private Double cmdrf=0.0;
	private Double cmdvb=0.0;
	
	private Double sbo=0.0;
	private Double sbowin=0.0;
	private Double sbodp=0.0;
	private Double sbowd=0.0;
	private Double sborf=0.0;
	private Double sbovb=0.0;
	
	private Double wm=0.0;
	private Double wmwin=0.0;
	private Double wmdp=0.0;
	private Double wmwd=0.0;
	private Double wmrf=0.0;
	private Double wmvb=0.0;
	
	private Double esport=0.0;
	private Double esportwin=0.0;
	private Double esportdp=0.0;
	private Double esportwd=0.0;
	private Double esportrf=0.0;
	private Double esportvb=0.0;
	
	private Double sgdp = 0.0;
	private Double sgwd = 0.0;
	private Double sg = 0.0;
	private Double sgwin = 0.0;
	private Double sgrf=0.0;
	private Double sgvb=0.0;
	
	private Double bbindp = 0.0;
	private Double bbinwd = 0.0;
	
	private Double bbinlive = 0.0;
	private Double bbinlivewin = 0.0;
	private Double bbinlivevb = 0.0;
	private Double bbinslot = 0.0;
	private Double bbinslotwin = 0.0;
	private Double bbinslotvb = 0.0;
	private Double bbinrf = 0.0;//bbin only refund
	
	
	public Double getSgdp() {
		return sgdp;
	}
	public void setSgdp(Double sgdp) {
		this.sgdp = sgdp;
	}
	public Double getSgwd() {
		return sgwd;
	}
	public void setSgwd(Double sgwd) {
		this.sgwd = sgwd;
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
	public Double getSgrf() {
		return sgrf;
	}
	public void setSgrf(Double sgrf) {
		this.sgrf = sgrf;
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
	public Double getEsportdp() {
		return esportdp;
	}
	public void setEsportdp(Double esportdp) {
		this.esportdp = esportdp;
	}
	public Double getEsportwd() {
		return esportwd;
	}
	public void setEsportwd(Double esportwd) {
		this.esportwd = esportwd;
	}
	public Double getEsportrf() {
		return esportrf;
	}
	public void setEsportrf(Double esportrf) {
		this.esportrf = esportrf;
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
	public Double getWmdp() {
		return wmdp;
	}
	public void setWmdp(Double wmdp) {
		this.wmdp = wmdp;
	}
	public Double getWmwd() {
		return wmwd;
	}
	public void setWmwd(Double wmwd) {
		this.wmwd = wmwd;
	}
	public Double getWmrf() {
		return wmrf;
	}
	public void setWmrf(Double wmrf) {
		this.wmrf = wmrf;
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
	public Double getCmddp() {
		return cmddp;
	}
	public void setCmddp(Double cmddp) {
		this.cmddp = cmddp;
	}
	public Double getCmdwd() {
		return cmdwd;
	}
	public void setCmdwd(Double cmdwd) {
		this.cmdwd = cmdwd;
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
	public Double getSbodp() {
		return sbodp;
	}
	public void setSbodp(Double sbodp) {
		this.sbodp = sbodp;
	}
	public Double getSbowd() {
		return sbowd;
	}
	public void setSbowd(Double sbowd) {
		this.sbowd = sbowd;
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
	public Double getSadp() {
		return sadp;
	}
	public void setSadp(Double sadp) {
		this.sadp = sadp;
	}
	public Double getSawd() {
		return sawd;
	}
	public void setSawd(Double sawd) {
		this.sawd = sawd;
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
	public Double getAgdp() {
		return agdp;
	}
	public void setAgdp(Double agdp) {
		this.agdp = agdp;
	}
	public Double getAgwd() {
		return agwd;
	}
	public void setAgwd(Double agwd) {
		this.agwd = agwd;
	}
	public String getRtime() {
		return rtime;
	}
	public void setRtime(String rtime) {
		this.rtime = rtime;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public Integer getFatherid() {
		return fatherid;
	}
	public void setFatherid(Integer fatherid) {
		this.fatherid = fatherid;
	}
	public String getFatherstr() {
		return fatherstr;
	}
	public void setFatherstr(String fatherstr) {
		this.fatherstr = fatherstr;
	}
	public Double getWin() {
		return win;
	}
	public void setWin(Double win) {
		this.win = win;
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
	public Double getTz() {
		return tz;
	}
	public void setTz(Double tz) {
		this.tz = tz;
	}
	public Double getSaletz() {
		return saletz;
	}
	public void setSaletz(Double saletz) {
		this.saletz = saletz;
	}
	public Double getIbcsport() {
		return ibcsport;
	}
	public void setIbcsport(Double ibcsport) {
		this.ibcsport = ibcsport;
	}
	public Double getIbcsportwin() {
		return ibcsportwin;
	}
	public void setIbcsportwin(Double ibcsportwin) {
		this.ibcsportwin = ibcsportwin;
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
	public Double getHabadp() {
		return habadp;
	}
	public void setHabadp(Double habadp) {
		this.habadp = habadp;
	}
	public Double getHabawd() {
		return habawd;
	}
	public void setHabawd(Double habawd) {
		this.habawd = habawd;
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
	public Double getEbetdp() {
		return ebetdp;
	}
	public void setEbetdp(Double ebetdp) {
		this.ebetdp = ebetdp;
	}
	public Double getEbetwd() {
		return ebetwd;
	}
	public void setEbetwd(Double ebetwd) {
		this.ebetwd = ebetwd;
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
	public Double getGgdp() {
		return ggdp;
	}
	public void setGgdp(Double ggdp) {
		this.ggdp = ggdp;
	}
	public Double getGgwd() {
		return ggwd;
	}
	public void setGgwd(Double ggwd) {
		this.ggwd = ggwd;
	}


	public Double getIbc2sport() {
		return ibc2sport;
	}

	public void setIbc2sport(Double ibc2sport) {
		this.ibc2sport = ibc2sport;
	}

	public Double getIbc2sportwin() {
		return ibc2sportwin;
	}

	public void setIbc2sportwin(Double ibc2sportwin) {
		this.ibc2sportwin = ibc2sportwin;
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
	public Double getDgdp() {
		return dgdp;
	}
	public void setDgdp(Double dgdp) {
		this.dgdp = dgdp;
	}
	public Double getDgwd() {
		return dgwd;
	}
	public void setDgwd(Double dgwd) {
		this.dgwd = dgwd;
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
	public Double getPtdp() {
		return ptdp;
	}
	public void setPtdp(Double ptdp) {
		this.ptdp = ptdp;
	}
	public Double getPtwd() {
		return ptwd;
	}
	public void setPtwd(Double ptwd) {
		this.ptwd = ptwd;
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
	public Double getTddp() {
		return tddp;
	}
	public void setTddp(Double tddp) {
		this.tddp = tddp;
	}
	public Double getTdwd() {
		return tdwd;
	}
	public void setTdwd(Double tdwd) {
		this.tdwd = tdwd;
	}
	
	public Double getRf() {
		return rf;
	}
	
	public void setRf(Double rf) {
		this.rf = rf;
	}
	
	public Double getHabarf() {
		return habarf;
	}
	
	public void setHabarf(Double habarf) {
		this.habarf = habarf;
	}
	
	public Double getEbetrf() {
		return ebetrf;
	}
	
	public void setEbetrf(Double ebetrf) {
		this.ebetrf = ebetrf;
	}
	
	public Double getGgrf() {
		return ggrf;
	}
	
	public void setGgrf(Double ggrf) {
		this.ggrf = ggrf;
	}
	
	public Double getDgrf() {
		return dgrf;
	}
	
	public void setDgrf(Double dgrf) {
		this.dgrf = dgrf;
	}
	
	public Double getPtrf() {
		return ptrf;
	}
	
	public void setPtrf(Double ptrf) {
		this.ptrf = ptrf;
	}
	
	public Double getTdrf() {
		return tdrf;
	}
	
	public void setTdrf(Double tdrf) {
		this.tdrf = tdrf;
	}
	
	public Double getSarf() {
		return sarf;
	}
	
	public void setSarf(Double sarf) {
		this.sarf = sarf;
	}
	
	public Double getAgrf() {
		return agrf;
	}
	
	public void setAgrf(Double agrf) {
		this.agrf = agrf;
	}
	
	public Double getCmdrf() {
		return cmdrf;
	}
	
	public void setCmdrf(Double cmdrf) {
		this.cmdrf = cmdrf;
	}
	
	public Double getSborf() {
		return sborf;
	}
	
	public void setSborf(Double sborf) {
		this.sborf = sborf;
	}
	public Double getBbindp() {
		return bbindp;
	}
	public void setBbindp(Double bbindp) {
		this.bbindp = bbindp;
	}
	public Double getBbinwd() {
		return bbinwd;
	}
	public void setBbinwd(Double bbinwd) {
		this.bbinwd = bbinwd;
	}
	public Double getBbinlive() {
		return bbinlive;
	}
	public void setBbinlive(Double bbinlive) {
		this.bbinlive = bbinlive;
	}
	public Double getBbinlivewin() {
		return bbinlivewin;
	}
	public void setBbinlivewin(Double bbinlivewin) {
		this.bbinlivewin = bbinlivewin;
	}
	public Double getBbinslot() {
		return bbinslot;
	}
	public void setBbinslot(Double bbinslot) {
		this.bbinslot = bbinslot;
	}
	public Double getBbinslotwin() {
		return bbinslotwin;
	}
	public void setBbinslotwin(Double bbinslotwin) {
		this.bbinslotwin = bbinslotwin;
	}
	public Double getBbinrf() {
		return bbinrf;
	}
	public void setBbinrf(Double bbinrf) {
		this.bbinrf = bbinrf;
	}

	public Double getIbc2sportvb() {
		return ibc2sportvb;
	}

	public void setIbc2sportvb(Double ibc2sportvb) {
		this.ibc2sportvb = ibc2sportvb;
	}

	public Double getIbc2vb() {
		return ibc2vb;
	}

	public void setIbc2vb(Double ibc2vb) {
		this.ibc2vb = ibc2vb;
	}

	public Double getHabavb() {
		return habavb;
	}

	public void setHabavb(Double habavb) {
		this.habavb = habavb;
	}

	public Double getEbetvb() {
		return ebetvb;
	}

	public void setEbetvb(Double ebetvb) {
		this.ebetvb = ebetvb;
	}

	public Double getGgvb() {
		return ggvb;
	}

	public void setGgvb(Double ggvb) {
		this.ggvb = ggvb;
	}

	public Double getDgvb() {
		return dgvb;
	}

	public void setDgvb(Double dgvb) {
		this.dgvb = dgvb;
	}

	public Double getPtvb() {
		return ptvb;
	}

	public void setPtvb(Double ptvb) {
		this.ptvb = ptvb;
	}

	public Double getTdvb() {
		return tdvb;
	}

	public void setTdvb(Double tdvb) {
		this.tdvb = tdvb;
	}

	public Double getSavb() {
		return savb;
	}

	public void setSavb(Double savb) {
		this.savb = savb;
	}

	public Double getAgvb() {
		return agvb;
	}

	public void setAgvb(Double agvb) {
		this.agvb = agvb;
	}

	public Double getCmdvb() {
		return cmdvb;
	}

	public void setCmdvb(Double cmdvb) {
		this.cmdvb = cmdvb;
	}

	public Double getSbovb() {
		return sbovb;
	}

	public void setSbovb(Double sbovb) {
		this.sbovb = sbovb;
	}

	public Double getWmvb() {
		return wmvb;
	}

	public void setWmvb(Double wmvb) {
		this.wmvb = wmvb;
	}

	public Double getEsportvb() {
		return esportvb;
	}

	public void setEsportvb(Double esportvb) {
		this.esportvb = esportvb;
	}

	public Double getSgvb() {
		return sgvb;
	}

	public void setSgvb(Double sgvb) {
		this.sgvb = sgvb;
	}

	public Double getBbinlivevb() {
		return bbinlivevb;
	}

	public void setBbinlivevb(Double bbinlivevb) {
		this.bbinlivevb = bbinlivevb;
	}
	public Double getBbinslotvb() {
		return bbinslotvb;
	}
	public void setBbinslotvb(Double bbinslotvb) {
		this.bbinslotvb = bbinslotvb;
	}
}
