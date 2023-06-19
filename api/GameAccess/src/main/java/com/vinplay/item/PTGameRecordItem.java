package com.vinplay.item;

import java.io.Serializable;

public class PTGameRecordItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9055991385161329807L;
	
	private String reference_no;
	private String game_token;
	private String entity_name;
	private String kiosk_name;
	private String game_server;
	private String gamzo_player_name;
	private String game_name;
	private String game_code;
	private String game_type;
	private String currency;
	private Integer is_win;
	private Double bet;
	private Double win;
	private Double progressive_bet;
	private	Double progressive_win;
	private String game_server_reference;
	private String timestamp;
	private String datetime;
	private String game_snapshot_url;
	private String login_name;
	
	public String getReference_no() {
		return reference_no;
	}
	public void setReference_no(String reference_no) {
		this.reference_no = reference_no;
	}
	public String getGame_token() {
		return game_token;
	}
	public void setGame_token(String game_token) {
		this.game_token = game_token;
	}
	public String getEntity_name() {
		return entity_name;
	}
	public void setEntity_name(String entity_name) {
		this.entity_name = entity_name;
	}
	public String getKiosk_name() {
		return kiosk_name;
	}
	public void setKiosk_name(String kiosk_name) {
		this.kiosk_name = kiosk_name;
	}
	public String getGame_server() {
		return game_server;
	}
	public void setGame_server(String game_server) {
		this.game_server = game_server;
	}
	public String getGamzo_player_name() {
		return gamzo_player_name;
	}
	public void setGamzo_player_name(String gamzo_player_name) {
		this.gamzo_player_name = gamzo_player_name;
	}
	public String getGame_name() {
		return game_name;
	}
	public void setGame_name(String game_name) {
		this.game_name = game_name;
	}
	public String getGame_code() {
		return game_code;
	}
	public void setGame_code(String game_code) {
		this.game_code = game_code;
	}
	public String getGame_type() {
		return game_type;
	}
	public void setGame_type(String game_type) {
		this.game_type = game_type;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Integer getIs_win() {
		return is_win;
	}
	public void setIs_win(Integer is_win) {
		this.is_win = is_win;
	}
	public Double getBet() {
		return bet;
	}
	public void setBet(Double bet) {
		this.bet = bet;
	}
	public Double getWin() {
		return win;
	}
	public void setWin(Double win) {
		this.win = win;
	}
	public Double getProgressive_bet() {
		return progressive_bet;
	}
	public void setProgressive_bet(Double progressive_bet) {
		this.progressive_bet = progressive_bet;
	}
	public Double getProgressive_win() {
		return progressive_win;
	}
	public void setProgressive_win(Double progressive_win) {
		this.progressive_win = progressive_win;
	}
	public String getGame_server_reference() {
		return game_server_reference;
	}
	public void setGame_server_reference(String game_server_reference) {
		this.game_server_reference = game_server_reference;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public String getGame_snapshot_url() {
		return game_snapshot_url;
	}
	public void setGame_snapshot_url(String game_snapshot_url) {
		this.game_snapshot_url = game_snapshot_url;
	}
	public String getLogin_name() {
		return login_name;
	}
	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}
}
