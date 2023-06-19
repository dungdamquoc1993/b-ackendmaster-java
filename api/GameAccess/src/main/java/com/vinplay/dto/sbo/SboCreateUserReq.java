/**
 * Archie
 */
package com.vinplay.dto.sbo;

/**
 * @author Archie
 *
 */

public class SboCreateUserReq extends AbsSboBaseRequest {
	private String player_name;
	private String currency;

	public String getPlayer_name() {
		return player_name;
	}

	public void setPlayer_name(String player_name) {
		this.player_name = player_name;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public SboCreateUserReq(String secret_key, String operator_token, String player_name, String currency) {
		super(secret_key, operator_token);
		this.player_name = player_name;
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "SboCreateUserReq [player_name=" + player_name + ", currency=" + currency + "]";
	}

}
