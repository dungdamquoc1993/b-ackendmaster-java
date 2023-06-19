package com.vinplay.dto.sg;

import java.util.List;

@SuppressWarnings("serial")
public class GetTransactionRespDto extends BaseRespDto{
	private List<TransactionsDetailRespDto> transactions;

	public List<TransactionsDetailRespDto> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<TransactionsDetailRespDto> transactions) {
		this.transactions = transactions;
	}

	
	
}
