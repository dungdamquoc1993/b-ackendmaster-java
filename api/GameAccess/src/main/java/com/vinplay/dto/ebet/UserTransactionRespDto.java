package com.vinplay.dto.ebet;

import java.io.Serializable;

public class UserTransactionRespDto extends BaseRespDto implements Serializable{
	private static final long serialVersionUID = -5429449990699848174L;
	private String count;
	private Integer remainingVisits;
	private TransactionsDto transactions;
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public Integer getRemainingVisits() {
		return remainingVisits;
	}
	public void setRemainingVisits(Integer remainingVisits) {
		this.remainingVisits = remainingVisits;
	}
	public TransactionsDto getTransactions() {
		return transactions;
	}
	public void setTransactions(TransactionsDto transactions) {
		this.transactions = transactions;
	}
	
}
