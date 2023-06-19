/**
 * Archie
 */
package com.vinplay.dto.sbo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Archie
 *
 */
public class SboRecord implements Serializable {

	private String player_name;
	private String vendor_code;
	private String game_code;
	private String parent_bet_id;
	private String bet_id;
	private String trans_type;
	private String currency;
	private String wallet_code;
	private Double bet_amount;
	private Double win_amount;
	private String traceId;
	private Timestamp created_at;

	public SboRecord() {
		super();
	}

	public SboRecord(String player_name, String vendor_code, String game_code, String parent_bet_id, String bet_id,
			String trans_type, String currency, String wallet_code, Double bet_amount, Double win_amount,
			String traceId, Timestamp created_at) {
		super();
		this.player_name = player_name;
		this.vendor_code = vendor_code;
		this.game_code = game_code;
		this.parent_bet_id = parent_bet_id;
		this.bet_id = bet_id;
		this.trans_type = trans_type;
		this.currency = currency;
		this.wallet_code = wallet_code;
		this.bet_amount = bet_amount;
		this.win_amount = win_amount;
		this.traceId = traceId;
		this.created_at = created_at;
	}

	public void setBet_amount(Double bet_amount) {
		this.bet_amount = bet_amount;
	}

	public void setWin_amount(Double win_amount) {
		this.win_amount = win_amount;
	}

	public String getPlayer_name() {
		return player_name;
	}

	public void setPlayer_name(String player_name) {
		this.player_name = player_name;
	}

	public String getVendor_code() {
		return vendor_code;
	}

	public void setVendor_code(String vendor_code) {
		this.vendor_code = vendor_code;
	}

	public String getGame_code() {
		return game_code;
	}

	public void setGame_code(String game_code) {
		this.game_code = game_code;
	}

	public String getParent_bet_id() {
		return parent_bet_id;
	}

	public void setParent_bet_id(String parent_bet_id) {
		this.parent_bet_id = parent_bet_id;
	}

	public String getBet_id() {
		return bet_id;
	}

	public void setBet_id(String bet_id) {
		this.bet_id = bet_id;
	}

	public String getTrans_type() {
		return trans_type;
	}

	public void setTrans_type(String trans_type) {
		this.trans_type = trans_type;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getWallet_code() {
		return wallet_code;
	}

	public void setWallet_code(String wallet_code) {
		this.wallet_code = wallet_code;
	}

	public double getBet_amount() {
		return bet_amount;
	}

	public void setBet_amount(double bet_amount) {
		this.bet_amount = bet_amount;
	}

	public double getWin_amount() {
		return win_amount;
	}

	public void setWin_amount(double win_amount) {
		this.win_amount = win_amount;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public Timestamp getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}

	@Override
	public String toString() {
		return "SboRecord [player_name=" + player_name + ", vendor_code=" + vendor_code + ", game_code=" + game_code
				+ ", parent_bet_id=" + parent_bet_id + ", bet_id=" + bet_id + ", trans_type=" + trans_type
				+ ", currency=" + currency + ", wallet_code=" + wallet_code + ", bet_amount=" + bet_amount
				+ ", win_amount=" + win_amount + ", traceId=" + traceId + ", created_at=" + created_at + "]";
	}

}
