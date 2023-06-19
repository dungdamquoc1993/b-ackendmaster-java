/**
 * Archie
 */
package com.vinplay.dto.esport;

import java.io.Serializable;

/**
 * @author Archie
 *
 */
public class EsportTransferDto implements Serializable {
	
	private String member;// test22,
	private String operator_id;// 2,
	private Double amount;// 2000,
	private String reference_no;// 1234,
	private String currency;// RMB,
	private String transaction_type;// deposit,
	private Double balance_amount;// 35101.74

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
	}

	public String getOperator_id() {
		return operator_id;
	}

	public void setOperator_id(String operator_id) {
		this.operator_id = operator_id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getReference_no() {
		return reference_no;
	}

	public void setReference_no(String reference_no) {
		this.reference_no = reference_no;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getTransaction_type() {
		return transaction_type;
	}

	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}

	public Double getBalance_amount() {
		return balance_amount;
	}

	public void setBalance_amount(Double balance_amount) {
		this.balance_amount = balance_amount;
	}

	public EsportTransferDto(String member, String operator_id, Double amount, String reference_no, String currency,
			String transaction_type, Double balance_amount) {
		super();
		this.member = member;
		this.operator_id = operator_id;
		this.amount = amount;
		this.reference_no = reference_no;
		this.currency = currency;
		this.transaction_type = transaction_type;
		this.balance_amount = balance_amount;
	}

	public EsportTransferDto() {
		super();
	}

	@Override
	public String toString() {
		return "EsportTransferDto [member=" + member + ", operator_id=" + operator_id + ", amount=" + amount
				+ ", reference_no=" + reference_no + ", currency=" + currency + ", transaction_type=" + transaction_type
				+ ", balance_amount=" + balance_amount + "]";
	}

}
