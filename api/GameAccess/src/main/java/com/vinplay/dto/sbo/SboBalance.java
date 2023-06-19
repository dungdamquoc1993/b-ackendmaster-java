/**
 * Archie
 */
package com.vinplay.dto.sbo;

import java.io.Serializable;


/**
 * @author Archie
 *
 */
public class SboBalance extends AbsSboBaseResponse<String, SboError> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3340006897538555451L;
	
	private String username;
	private String currency;
	private Double outstanding;
	private Double balance;
	
	public SboBalance(String username, String currency, Double outstanding, Double balance) {
		super();
		this.username = username;
		this.currency = currency;
		this.outstanding = outstanding;
		this.balance = balance;
	}
	public SboBalance() {
		super();
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Double getOutstanding() {
		return outstanding;
	}
	public void setOutstanding(Double outstanding) {
		this.outstanding = outstanding;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	@Override
	public String toString() {
		return "SboBalance [username=" + username + ", currency=" + currency + ", outstanding=" + outstanding
				+ ", balance=" + balance + "]";
	}
	
}
