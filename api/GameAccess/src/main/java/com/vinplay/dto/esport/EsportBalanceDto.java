/**
 * Archie
 */
package com.vinplay.dto.esport;

import java.io.Serializable;

/**
 * @author Archie
 *
 */
public class EsportBalanceDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8961285823679649739L;
	private Long id;// 14392,
	private String operator_id;// 2,
	private String member_id;// test22,
	private String currency;// RMB,
	private Double balance = 0.0;// 33101.74,
	private String last_bet_datetime;// 2020-03-03T01;//24;//14.662008Z,
	private Double last_bet_amount;// 20

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOperator_id() {
		return operator_id;
	}

	public void setOperator_id(String operator_id) {
		this.operator_id = operator_id;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getLast_bet_datetime() {
		return last_bet_datetime;
	}

	public void setLast_bet_datetime(String last_bet_datetime) {
		this.last_bet_datetime = last_bet_datetime;
	}

	public Double getLast_bet_amount() {
		return last_bet_amount;
	}

	public void setLast_bet_amount(Double last_bet_amount) {
		this.last_bet_amount = last_bet_amount;
	}

	public EsportBalanceDto(Long id, String operator_id, String member_id, String currency, Double balance,
			String last_bet_datetime, Double last_bet_amount) {
		super();
		this.id = id;
		this.operator_id = operator_id;
		this.member_id = member_id;
		this.currency = currency;
		this.balance = balance;
		this.last_bet_datetime = last_bet_datetime;
		this.last_bet_amount = last_bet_amount;
	}

	public EsportBalanceDto() {
		super();
	}

	@Override
	public String toString() {
		return "EsportBalanceDto [id=" + id + ", operator_id=" + operator_id + ", member_id=" + member_id
				+ ", currency=" + currency + ", balance=" + balance + ", last_bet_datetime=" + last_bet_datetime
				+ ", last_bet_amount=" + last_bet_amount + "]";
	}

}
