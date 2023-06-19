package com.vinplay.usercore.service;

import com.vinplay.payment.entities.Response;
import com.vinplay.payment.entities.UserBank;

public interface UserBankService {
	boolean isAddBank(String nickName);
	/**
	 * Add bank info of user
	 * @param userBank
	 * @return Response
	 */
	Response add(UserBank userBank);
	
	/**
	 * Update bank info of user
	 * @param userBank
	 * @return Response
	 */
	Response update(UserBank userBank);
	
	/**
	 * Delete bank info of user by id
	 * @param id
	 * @return Response
	 */
	Response delete(long id);
	
	/**
	 * Get bank info of user by id
	 * @param id
	 * @return Response
	 */
	Response getById(long id);
	
	/**
	 * Get bank info of user by bank number
	 * @param nickname
	 * @param bankNumber
	 * @return Response
	 */
	Response getByBankNumber(String nickname, String bankNumber);

	public UserBank getByDetail(String nickname, String bankNumber);
	
	/**
	 * Search bank info of user by nick name and bank name
	 * @param nickName
	 * @param bankName
	 * @param bankNumber
	 * @param isAdmin : 0-Play user;1-Admin
	 * @param pageNumber : page index
	 * @param limit : record number per page
	 * @return Response
	 */
	Response search(String nickName, String bankName, String bankNumber, int isAdmin, int pageNumber, int limit);

	/**
	 * Search bank info of user by nick name and bank name
	 * @param nickName
	 * @param bankName
	 * @param bankNumber
	 * @param isAdmin : 0-Play user;1-Admin
	 * @param pageNumber : page index
	 * @param limit : record number per page
	 * @return Response
	 */
	Response search(String nickName, String customerName, String bankName, String bankNumber, int isAdmin, int pageNumber, int limit);
}
