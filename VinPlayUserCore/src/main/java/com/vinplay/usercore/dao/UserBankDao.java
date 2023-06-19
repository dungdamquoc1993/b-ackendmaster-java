package com.vinplay.usercore.dao;

import java.sql.SQLException;
import java.util.List;

import com.vinplay.payment.entities.UserBank;

public interface UserBankDao {
	
	boolean isExistBankNumber(String bankNumber) throws SQLException;
	
	boolean isAddedBank(String nickName);
	
	long add(UserBank userBank) throws SQLException;

	boolean update(UserBank userBank) throws SQLException;

	public boolean delete(long id) throws SQLException;
	
	UserBank getById(Long id) throws SQLException;
	
	UserBank getByBankNumber(String nickname, String bankNumber) throws SQLException;
	
	int getCountByNickName(String nickName) throws SQLException;
	List<UserBank> getByCustomerName(String nickName, String customerName) throws SQLException;
	
	List<UserBank> search(String nickName, String bankName, String bankNumber, int isAdmin, int pageNumber, int limit) throws SQLException;
	
	int search_count(String nickName, String bankName, String bankNumber, int isAdmin) throws SQLException;

	List<UserBank> search(String nickName, String customerName, String bankName, String bankNumber, int isAdmin, int pageNumber, int limit) throws SQLException;

	int search_count(String nickName, String customerName, String bankName, String bankNumber, int isAdmin) throws SQLException;
}
