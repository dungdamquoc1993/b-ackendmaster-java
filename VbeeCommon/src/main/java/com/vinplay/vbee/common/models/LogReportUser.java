package com.vinplay.vbee.common.models;

public class LogReportUser {

	private long id;
	private java.sql.Date timeReport;
	private String nickName;
	private long wm;
	private long wmWin;
	private long ibc;
	private long ibcWin;
	private long ag;
	private long agWin;
	private long tlmn;
	private long bacay;
	private long bacayWin;
	private long xocdia;
	private long xocdiaWin;
	private long minipoker;
	private long minipokerWin;
	private long slotPokemon;
	private long slotPokemonWin;
	private long baucua;
	private long baucuaWin;
	private long taixiu;
	private long taixiuWin;
	private long caothap;
	private long caothapWin;
	private long slotBitcoin;
	private long slotBitcoinWin;
	private long slotTaydu;
	private long slotTayduWin;
	private long slotAngrybird;
	private long slotAngrybirdWin;
	private long slotThantai;
	private long slotThantaiWin;
	private long slotThethao;
	private long slotThethaoWin;
	private long deposit;
	private long withdraw;

	private long cmd;
	private long cmdWin;
	
	private long taixiu_st;
	private long taixiu_st_win;
	
	private long slotThanbai;
	private long slotThanbaiWin;
	
	private long ebet=0;
	private long ebetWin=0;
	
	private long sbo=0;
	private long sboWin=0;
	
	public long totalBonus;// tổng bonus

	public long totalRefund;// tổng hoàn trả

	public long getCmd() {
		return cmd;
	}

	public void setCmd(long cmd) {
		this.cmd = cmd;
	}

	public long getCmdWin() {
		return cmdWin;
	}

	public void setCmdWin(long cmdWin) {
		this.cmdWin = cmdWin;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTaixiu_st() {
		return taixiu_st;
	}

	public void setTaixiu_st(long taixiu_st) {
		this.taixiu_st = taixiu_st;
	}

	public long getTaixiu_st_win() {
		return taixiu_st_win;
	}

	public void setTaixiu_st_win(long taixiu_st_win) {
		this.taixiu_st_win = taixiu_st_win;
	}

	public java.sql.Date getTimeReport() {
		return timeReport;
	}

	public void setTimeReport(java.sql.Date timeReport) {
		this.timeReport = timeReport;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public long getWm() {
		return wm;
	}

	public void setWm(long wm) {
		this.wm = wm;
	}

	public long getWmWin() {
		return wmWin;
	}

	public void setWmWin(long wmWin) {
		this.wmWin = wmWin;
	}

	public long getIbc() {
		return ibc;
	}

	public void setIbc(long ibc) {
		this.ibc = ibc;
	}

	public long getIbcWin() {
		return ibcWin;
	}

	public void setIbcWin(long ibcWin) {
		this.ibcWin = ibcWin;
	}

	public long getAg() {
		return ag;
	}

	public void setAg(long ag) {
		this.ag = ag;
	}

	public long getAgWin() {
		return agWin;
	}

	public void setAgWin(long agWin) {
		this.agWin = agWin;
	}

	public long getTlmn() {
		return tlmn;
	}

	public void setTlmn(long tlmn) {
		this.tlmn = tlmn;
	}

	public long getBacay() {
		return bacay;
	}

	public void setBacay(long bacay) {
		this.bacay = bacay;
	}

	public long getBacayWin() {
		return bacayWin;
	}

	public void setBacayWin(long bacayWin) {
		this.bacayWin = bacayWin;
	}

	public long getXocdia() {
		return xocdia;
	}

	public void setXocdia(long xocdia) {
		this.xocdia = xocdia;
	}

	public long getXocdiaWin() {
		return xocdiaWin;
	}

	public void setXocdiaWin(long xocdiaWin) {
		this.xocdiaWin = xocdiaWin;
	}

	public long getMinipoker() {
		return minipoker;
	}

	public void setMinipoker(long minipoker) {
		this.minipoker = minipoker;
	}

	public long getMinipokerWin() {
		return minipokerWin;
	}

	public void setMinipokerWin(long minipokerWin) {
		this.minipokerWin = minipokerWin;
	}

	public long getSlotPokemon() {
		return slotPokemon;
	}

	public void setSlotPokemon(long slotPokemon) {
		this.slotPokemon = slotPokemon;
	}

	public long getSlotPokemonWin() {
		return slotPokemonWin;
	}

	public void setSlotPokemonWin(long slotPokemonWin) {
		this.slotPokemonWin = slotPokemonWin;
	}

	public long getBaucua() {
		return baucua;
	}

	public void setBaucua(long baucua) {
		this.baucua = baucua;
	}

	public long getBaucuaWin() {
		return baucuaWin;
	}

	public void setBaucuaWin(long baucuaWin) {
		this.baucuaWin = baucuaWin;
	}

	public long getTaixiu() {
		return taixiu;
	}

	public void setTaixiu(long taixiu) {
		this.taixiu = taixiu;
	}

	public long getTaixiuWin() {
		return taixiuWin;
	}

	public void setTaixiuWin(long taixiuWin) {
		this.taixiuWin = taixiuWin;
	}

	public long getCaothap() {
		return caothap;
	}

	public void setCaothap(long caothap) {
		this.caothap = caothap;
	}

	public long getCaothapWin() {
		return caothapWin;
	}

	public void setCaothapWin(long caothapWin) {
		this.caothapWin = caothapWin;
	}

	public long getSlotBitcoin() {
		return slotBitcoin;
	}

	public void setSlotBitcoin(long slotBitcoin) {
		this.slotBitcoin = slotBitcoin;
	}

	public long getSlotBitcoinWin() {
		return slotBitcoinWin;
	}

	public void setSlotBitcoinWin(long slotBitcoinWin) {
		this.slotBitcoinWin = slotBitcoinWin;
	}

	public long getSlotTaydu() {
		return slotTaydu;
	}

	public void setSlotTaydu(long slotTaydu) {
		this.slotTaydu = slotTaydu;
	}

	public long getSlotTayduWin() {
		return slotTayduWin;
	}

	public void setSlotTayduWin(long slotTayduWin) {
		this.slotTayduWin = slotTayduWin;
	}

	public long getSlotAngrybird() {
		return slotAngrybird;
	}

	public void setSlotAngrybird(long slotAngrybird) {
		this.slotAngrybird = slotAngrybird;
	}

	public long getSlotAngrybirdWin() {
		return slotAngrybirdWin;
	}

	public void setSlotAngrybirdWin(long slotAngrybirdWin) {
		this.slotAngrybirdWin = slotAngrybirdWin;
	}

	public long getSlotThantai() {
		return slotThantai;
	}

	public void setSlotThantai(long slotThantai) {
		this.slotThantai = slotThantai;
	}

	public long getSlotThantaiWin() {
		return slotThantaiWin;
	}

	public void setSlotThantaiWin(long slotThantaiWin) {
		this.slotThantaiWin = slotThantaiWin;
	}

	public long getSlotThethao() {
		return slotThethao;
	}

	public void setSlotThethao(long slotThethao) {
		this.slotThethao = slotThethao;
	}

	public long getSlotThethaoWin() {
		return slotThethaoWin;
	}

	public void setSlotThethaoWin(long slotThethaoWin) {
		this.slotThethaoWin = slotThethaoWin;
	}

	public long getDeposit() {
		return deposit;
	}

	public void setDeposit(long deposit) {
		this.deposit = deposit;
	}

	public long getWithdraw() {
		return withdraw;
	}

	public void setWithdraw(long withdraw) {
		this.withdraw = withdraw;
	}

	public long getSlotThanbai() {
		return slotThanbai;
	}

	public void setSlotThanbai(long slotThanbai) {
		this.slotThanbai = slotThanbai;
	}

	public long getSlotThanbaiWin() {
		return slotThanbaiWin;
	}

	public void setSlotThanbaiWin(long slotThanbaiWin) {
		this.slotThanbaiWin = slotThanbaiWin;
	}

	public long getEbet() {
		return ebet;
	}

	public void setEbet(long ebet) {
		this.ebet = ebet;
	}

	public long getEbetWin() {
		return ebetWin;
	}

	public void setEbetWin(long ebetWin) {
		this.ebetWin = ebetWin;
	}

	public long getSbo() {
		return sbo;
	}

	public void setSbo(long sbo) {
		this.sbo = sbo;
	}

	public long getSboWin() {
		return sboWin;
	}

	public void setSboWin(long sboWin) {
		this.sboWin = sboWin;
	}

	public long getTotalBonus() {
		return totalBonus;
	}

	public void setTotalBonus(long totalBonus) {
		this.totalBonus = totalBonus;
	}

	public long getTotalRefund() {
		return totalRefund;
	}

	public void setTotalRefund(long totalRefund) {
		this.totalRefund = totalRefund;
	}
	
}
