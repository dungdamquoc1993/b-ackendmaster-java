package com.vinplay.dal.entities.agent;

import java.io.Serializable;
import java.util.Date;

public class UserDetailAgentModel implements Serializable {

	private Long id;
	private Date time;
	private String nick_name;
	private Long wm;
	private Long wm_win;
	private Long ibc;
	private Long ibc_win;
	private Long ag;
	private Long ag_win;
	private Long tlmn;
	private Long tlmn_win;
	private Long bacay;
	private Long bacay_win;
	private Long xocdia;
	private Long xocdia_win;
	private Long minipoker;
	private Long minipoker_win;
	private Long slot_pokemon;
	private Long slot_pokemon_win;
	private Long baucua;
	private Long baucua_win;
	private Long taixiu;
	private Long taixiu_win;
	private Long caothap;
	private Long caothap_win;
	private Long slot_bitcoin;
	private Long slot_bitcoin_win;
	private Long slot_taydu;
	private Long slot_taydu_win;
	private Long slot_angrybird;
	private Long slot_angrybird_win;
	private Long slot_thantai;
	private Long slot_thantai_win;
	private Long slot_thethao;
	private Long slot_thethao_win;
	private Long slot_chiemtinh;
	private Long slot_chiemtinh_win;
	private Long taixiu_st;
	private Long taixiu_st_win;
	private Long fish;
	private Long fish_win;
	private Long deposit;
	private Long withdraw;
	private Long slot_bikini;
	private Long slot_bikini_win;
	private Long slot_galaxy;
	private Long slot_galaxy_win;
	private Long ebet;
	private Long ebet_win;

	public UserDetailAgentModel() {
	}

	public UserDetailAgentModel(Date time, String nick_name, Integer user_id, Long wm, Long wm_win, Long ibc,
			Long ibc_win, Long ag, Long ag_win, Long tlmn, Long bacay, Long bacay_win, Long xocdia, Long xocdia_win,
			Long minipoker, Long minipoker_win, Long slot_pokemon, Long slot_pokemon_win, Long baucua, Long baucua_win,
			Long taixiu, Long taixiu_win, Long caothap, Long caothap_win, Long slot_bitcoin, Long slot_bitcoin_win,
			Long slot_taydu, Long slot_taydu_win, Long slot_angrybird, Long slot_angrybird_win, Long slot_thantai,
			Long slot_thantai_win, Long slot_thethao, Long slot_thethao_win, Long slot_chiemtinh,
			Long slot_chiemtinh_win, Long taixiu_st, Long taixiu_st_win, Long fish, Long fish_win, Long deposit,
			Long withdraw, Long slot_bikini, Long slot_bikini_win, Long slot_galaxy, Long slot_galaxy_win,
			Long ebet, Long ebet_win) {
		this.time = time;
		this.nick_name = nick_name;
		this.wm = wm;
		this.wm_win = wm_win;
		this.ibc = ibc;
		this.ibc_win = ibc_win;
		this.ag = ag;
		this.ag_win = ag_win;
		this.tlmn = tlmn;
		this.bacay = bacay;
		this.bacay_win = bacay_win;
		this.xocdia = xocdia;
		this.xocdia_win = xocdia_win;
		this.minipoker = minipoker;
		this.minipoker_win = minipoker_win;
		this.slot_pokemon = slot_pokemon;
		this.slot_pokemon_win = slot_pokemon_win;
		this.baucua = baucua;
		this.baucua_win = baucua_win;
		this.taixiu = taixiu;
		this.taixiu_win = taixiu_win;
		this.caothap = caothap;
		this.caothap_win = caothap_win;
		this.slot_bitcoin = slot_bitcoin;
		this.slot_bitcoin_win = slot_bitcoin_win;
		this.slot_taydu = slot_taydu;
		this.slot_taydu_win = slot_taydu_win;
		this.slot_angrybird = slot_angrybird;
		this.slot_angrybird_win = slot_angrybird_win;
		this.slot_thantai = slot_thantai;
		this.slot_thantai_win = slot_thantai_win;
		this.slot_thethao = slot_thethao;
		this.slot_thethao_win = slot_thethao_win;
		this.slot_chiemtinh = slot_chiemtinh;
		this.slot_chiemtinh_win = slot_chiemtinh_win;
		this.taixiu_st = taixiu_st;
		this.taixiu_st_win = taixiu_st_win;
		this.fish = fish;
		this.fish_win = fish_win;
		this.deposit = deposit;
		this.withdraw = withdraw;
		this.slot_bikini = slot_bikini;
		this.slot_bikini_win = slot_bikini_win;
		this.slot_galaxy = slot_galaxy;
		this.slot_galaxy_win = slot_galaxy_win;
		this.ebet = ebet;
		this.ebet_win = ebet_win;
	}

	public UserDetailAgentModel(Long id, Date time, String nick_name, Long wm, Long wm_win, Long ibc, Long ibc_win,
			Long ag, Long ag_win, Long tlmn, Long tlmn_win, Long bacay, Long bacay_win, Long xocdia, Long xocdia_win,
			Long minipoker, Long minipoker_win, Long slot_pokemon, Long slot_pokemon_win, Long baucua, Long baucua_win,
			Long taixiu, Long taixiu_win, Long caothap, Long caothap_win, Long slot_bitcoin, Long slot_bitcoin_win,
			Long slot_taydu, Long slot_taydu_win, Long slot_angrybird, Long slot_angrybird_win, Long slot_thantai,
			Long slot_thantai_win, Long slot_thethao, Long slot_thethao_win, Long slot_chiemtinh,
			Long slot_chiemtinh_win, Long taixiu_st, Long taixiu_st_win, Long fish, Long fish_win,
			Long deposit, Long withdraw, Long slot_bikini, Long slot_bikini_win, Long slot_galaxy, Long slot_galaxy_win,
			Long ebet, Long ebet_win) {
		this.id = id;
		this.time = time;
		this.nick_name = nick_name;
		this.wm = wm;
		this.wm_win = wm_win;
		this.ibc = ibc;
		this.ibc_win = ibc_win;
		this.ag = ag;
		this.ag_win = ag_win;
		this.tlmn = tlmn;
		this.tlmn_win = tlmn_win;
		this.bacay = bacay;
		this.bacay_win = bacay_win;
		this.xocdia = xocdia;
		this.xocdia_win = xocdia_win;
		this.minipoker = minipoker;
		this.minipoker_win = minipoker_win;
		this.slot_pokemon = slot_pokemon;
		this.slot_pokemon_win = slot_pokemon_win;
		this.baucua = baucua;
		this.baucua_win = baucua_win;
		this.taixiu = taixiu;
		this.taixiu_win = taixiu_win;
		this.caothap = caothap;
		this.caothap_win = caothap_win;
		this.slot_bitcoin = slot_bitcoin;
		this.slot_bitcoin_win = slot_bitcoin_win;
		this.slot_taydu = slot_taydu;
		this.slot_taydu_win = slot_taydu_win;
		this.slot_angrybird = slot_angrybird;
		this.slot_angrybird_win = slot_angrybird_win;
		this.slot_thantai = slot_thantai;
		this.slot_thantai_win = slot_thantai_win;
		this.slot_thethao = slot_thethao;
		this.slot_thethao_win = slot_thethao_win;
		this.slot_chiemtinh = slot_chiemtinh;
		this.slot_chiemtinh_win = slot_chiemtinh_win;
		this.taixiu_st = taixiu_st;
		this.taixiu_st_win = taixiu_st_win;
		this.fish = fish;
		this.fish_win = fish_win;
		this.deposit = deposit;
		this.withdraw = withdraw;
		this.slot_bikini = slot_bikini;
		this.slot_bikini_win = slot_bikini_win;
		this.slot_galaxy = slot_galaxy;
		this.slot_galaxy_win = slot_galaxy_win;
		this.ebet = ebet;
		this.ebet_win = ebet_win;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTlmn_win() {
		return tlmn_win;
	}

	public void setTlmn_win(Long tlmn_win) {
		this.tlmn_win = tlmn_win;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public Long getWm() {
		return wm;
	}

	public void setWm(Long wm) {
		this.wm = wm;
	}

	public Long getWm_win() {
		return wm_win;
	}

	public void setWm_win(Long wm_win) {
		this.wm_win = wm_win;
	}

	public Long getIbc() {
		return ibc;
	}

	public void setIbc(Long ibc) {
		this.ibc = ibc;
	}

	public Long getIbc_win() {
		return ibc_win;
	}

	public void setIbc_win(Long ibc_win) {
		this.ibc_win = ibc_win;
	}

	public Long getAg() {
		return ag;
	}

	public void setAg(Long ag) {
		this.ag = ag;
	}

	public Long getAg_win() {
		return ag_win;
	}

	public void setAg_win(Long ag_win) {
		this.ag_win = ag_win;
	}

	public Long getTlmn() {
		return tlmn;
	}

	public void setTlmn(Long tlmn) {
		this.tlmn = tlmn;
	}

	public Long getBacay() {
		return bacay;
	}

	public void setBacay(Long bacay) {
		this.bacay = bacay;
	}

	public Long getBacay_win() {
		return bacay_win;
	}

	public void setBacay_win(Long bacay_win) {
		this.bacay_win = bacay_win;
	}

	public Long getXocdia() {
		return xocdia;
	}

	public void setXocdia(Long xocdia) {
		this.xocdia = xocdia;
	}

	public Long getXocdia_win() {
		return xocdia_win;
	}

	public void setXocdia_win(Long xocdia_win) {
		this.xocdia_win = xocdia_win;
	}

	public Long getMinipoker() {
		return minipoker;
	}

	public void setMinipoker(Long minipoker) {
		this.minipoker = minipoker;
	}

	public Long getMinipoker_win() {
		return minipoker_win;
	}

	public void setMinipoker_win(Long minipoker_win) {
		this.minipoker_win = minipoker_win;
	}

	public Long getSlot_pokemon() {
		return slot_pokemon;
	}

	public void setSlot_pokemon(Long slot_pokemon) {
		this.slot_pokemon = slot_pokemon;
	}

	public Long getSlot_pokemon_win() {
		return slot_pokemon_win;
	}

	public void setSlot_pokemon_win(Long slot_pokemon_win) {
		this.slot_pokemon_win = slot_pokemon_win;
	}

	public Long getBaucua() {
		return baucua;
	}

	public void setBaucua(Long baucua) {
		this.baucua = baucua;
	}

	public Long getBaucua_win() {
		return baucua_win;
	}

	public void setBaucua_win(Long baucua_win) {
		this.baucua_win = baucua_win;
	}

	public Long getTaixiu() {
		return taixiu;
	}

	public void setTaixiu(Long taixiu) {
		this.taixiu = taixiu;
	}

	public Long getTaixiu_win() {
		return taixiu_win;
	}

	public void setTaixiu_win(Long taixiu_win) {
		this.taixiu_win = taixiu_win;
	}

	public Long getCaothap() {
		return caothap;
	}

	public void setCaothap(Long caothap) {
		this.caothap = caothap;
	}

	public Long getCaothap_win() {
		return caothap_win;
	}

	public void setCaothap_win(Long caothap_win) {
		this.caothap_win = caothap_win;
	}

	public Long getSlot_bitcoin() {
		return slot_bitcoin;
	}

	public void setSlot_bitcoin(Long slot_bitcoin) {
		this.slot_bitcoin = slot_bitcoin;
	}

	public Long getSlot_bitcoin_win() {
		return slot_bitcoin_win;
	}

	public void setSlot_bitcoin_win(Long slot_bitcoin_win) {
		this.slot_bitcoin_win = slot_bitcoin_win;
	}

	public Long getSlot_taydu() {
		return slot_taydu;
	}

	public void setSlot_taydu(Long slot_taydu) {
		this.slot_taydu = slot_taydu;
	}

	public Long getSlot_taydu_win() {
		return slot_taydu_win;
	}

	public void setSlot_taydu_win(Long slot_taydu_win) {
		this.slot_taydu_win = slot_taydu_win;
	}

	public Long getSlot_angrybird() {
		return slot_angrybird;
	}

	public void setSlot_angrybird(Long slot_angrybird) {
		this.slot_angrybird = slot_angrybird;
	}

	public Long getSlot_angrybird_win() {
		return slot_angrybird_win;
	}

	public void setSlot_angrybird_win(Long slot_angrybird_win) {
		this.slot_angrybird_win = slot_angrybird_win;
	}

	public Long getSlot_thantai() {
		return slot_thantai;
	}

	public void setSlot_thantai(Long slot_thantai) {
		this.slot_thantai = slot_thantai;
	}

	public Long getSlot_thantai_win() {
		return slot_thantai_win;
	}

	public void setSlot_thantai_win(Long slot_thantai_win) {
		this.slot_thantai_win = slot_thantai_win;
	}

	public Long getSlot_thethao() {
		return slot_thethao;
	}

	public void setSlot_thethao(Long slot_thethao) {
		this.slot_thethao = slot_thethao;
	}

	public Long getSlot_thethao_win() {
		return slot_thethao_win;
	}

	public void setSlot_thethao_win(Long slot_thethao_win) {
		this.slot_thethao_win = slot_thethao_win;
	}

	public Long getSlot_chiemtinh() {
		return slot_chiemtinh;
	}

	public void setSlot_chiemtinh(Long slot_chiemtinh) {
		this.slot_chiemtinh = slot_chiemtinh;
	}

	public Long getSlot_chiemtinh_win() {
		return slot_chiemtinh_win;
	}

	public void setSlot_chiemtinh_win(Long slot_chiemtinh_win) {
		this.slot_chiemtinh = slot_chiemtinh_win;
	}

	public Long getTaixiu_st() {
		return taixiu_st;
	}

	public void setTaixiu_st(Long taixiu_st) {
		this.taixiu_st = taixiu_st;
	}

	public Long getTaixiu_st_win() {
		return taixiu_st_win;
	}

	public void setTaixiu_st_win(Long taixiu_st_win) {
		this.taixiu_st_win = taixiu_st_win;
	}
	
	public Long getFish() {
		return fish;
	}

	public void setFish(Long fish) {
		this.fish = fish;
	}
	
	public Long getFish_win() {
		return fish_win;
	}

	public void setFish_win(Long fish_win) {
		this.fish_win = fish_win;
	}

	public Long getDeposit() {
		return deposit;
	}

	public void setDeposit(Long deposit) {
		this.deposit = deposit;
	}

	public Long getWithdraw() {
		return withdraw;
	}

	public void setWithdraw(Long withdraw) {
		this.withdraw = withdraw;
	}

	public Long getSlot_bikini() {
		return slot_bikini;
	}

	public void setSlot_bikini(Long slot_bikini) {
		this.slot_bikini = slot_bikini;
	}

	public Long getSlot_bikini_win() {
		return slot_bikini_win;
	}

	public void setSlot_bikini_win(Long slot_bikini_win) {
		this.slot_bikini_win = slot_bikini_win;
	}

	public Long getSlot_galaxy() {
		return slot_galaxy;
	}

	public void setSlot_galaxy(Long slot_galaxy) {
		this.slot_galaxy = slot_galaxy;
	}

	public Long getSlot_galaxy_win() {
		return slot_galaxy_win;
	}

	public void setSlot_galaxy_win(Long slot_galaxy_win) {
		this.slot_galaxy_win = slot_galaxy_win;
	}
	
	public Long getEbet() {
		return ebet;
	}

	public void setEbet(Long ebet) {
		this.ebet = ebet;
	}
	
	public Long getEbet_win() {
		return ebet_win;
	}

	public void setEbet_win(Long ebet_win) {
		this.ebet_win = ebet_win;
	}
}
