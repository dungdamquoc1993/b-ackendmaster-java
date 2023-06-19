package com.vinplay.usercore.dao;

import java.sql.SQLException;
import java.util.List;

import com.vinplay.payment.entities.Bank;

public interface BankDao {
	public boolean addBank(Bank bank);

	public boolean editBank(Bank bank);

	public boolean deleteBank(long id);

	public List<Bank> search(String bankName, String bankCode, int pageNumber, int limit)  throws SQLException ;
	
	public List<Bank> findAll();
}
