package com.vinplay.usercore.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.vinplay.payment.entities.Response;
import com.vinplay.payment.entities.UserBank;
import com.vinplay.usercore.dao.UserBankDao;
import com.vinplay.usercore.dao.impl.UserBankDaoImpl;
import com.vinplay.usercore.service.UserBankService;
import com.vinplay.utils.EnumConstants;


public class UserBankServiceImpl implements UserBankService {
	
	public static final int maxBank = 5;
	public static final Logger logger = Logger.getLogger(UserBankServiceImpl.class);
	
	/**
	 * Validate data before submit to db
	 * @param userBank
	 * @param action : 0-Add; 1-Update
	 * @return Response
	 */
	private Response validateAdd(UserBank userBank, Integer action) throws Exception{
		Response res = new Response(1, "");
		UserBankDao dao = new UserBankDaoImpl();
		
		if(userBank.getStatus() < 0 || userBank.getStatus() > 1) {
			res.setMessage("Status is invalid");
			return res;
		}
		
		int countBank = dao.getCountByNickName(userBank.getNickName());
		//TODO: Validate data when insert
		if(action == 0) {
			if(countBank >= maxBank) {
				return Response.error(EnumConstants.ErrorBank.ERR_OVER_BANK_NUMBER.getKey(),
						EnumConstants.ErrorBank.ERR_OVER_BANK_NUMBER.getValue());
			}
			
			if(dao.getByCustomerName(userBank.getNickName(), userBank.getCustomerName()).size() < 1 && countBank > 0) {
				return Response.error(EnumConstants.ErrorBank.ERR_CUSTOMER_NAME.getKey(),
						EnumConstants.ErrorBank.ERR_CUSTOMER_NAME.getValue());
			}
			if(dao.isExistBankNumber(userBank.getBankNumber())) {
				return Response.error(EnumConstants.ErrorBank.ERR_EXISTED_BANK_NUMBER.getKey(),
						EnumConstants.ErrorBank.ERR_EXISTED_BANK_NUMBER.getValue());
			}
			
		}
		
		//TODO: Validate data when update
		if(action == 1) {
			UserBank old = dao.getById(userBank.getId());
			if(old == null) {
				return Response.error(EnumConstants.ErrorBank.ERR_NOT_EXIST_ID.getKey(),
						EnumConstants.ErrorBank.ERR_NOT_EXIST_ID.getValue());
			}
			if(!old.getBankNumber().equals(userBank.getBankNumber())) {
				return Response.error(EnumConstants.ErrorBank.ERR_CHANGE_BANKNUMBER.getKey(),
						EnumConstants.ErrorBank.ERR_CHANGE_BANKNUMBER.getValue());
			}
//			if(!old.getCustomerName().equalsIgnoreCase(userBank.getCustomerName())) {
//				return Response.error(EnumConstants.ErrorBank.ERR_CHANGE_CUSTOMERNAME.getKey(),
//						EnumConstants.ErrorBank.ERR_CHANGE_CUSTOMERNAME.getValue());
//			}
//			if(dao.getByCustomerName(userBank.getNickName(), userBank.getCustomerName()).size() < 1 && countBank > 1) {
//				return Response.error(EnumConstants.ErrorBank.ERR_CUSTOMER_NAME.getKey(),
//						EnumConstants.ErrorBank.ERR_CUSTOMER_NAME.getValue());
//			}
		}
		res.setCode(0);
		return res;
	}
	
	/**
	 * Add bank info of user
	 * @param userBank
	 * @return Response
	 */
	@Override
    public Response add(UserBank userBank) {
		Response res = new Response(1, "");
		try {
			UserBankDao dao = new UserBankDaoImpl();
			userBank.setId(0l);
			userBank.setStatus(1);
			res = validateAdd(userBank, 0);
			if(res.getCode() != 0)
				return res;
			
	        long id = dao.add(userBank);
	        if(id < 1) {
	        	return Response.error(EnumConstants.ErrorBank.ERR_TRANSACTION.getKey(),
						EnumConstants.ErrorBank.ERR_TRANSACTION.getValue());
	        }
	        
	        userBank = dao.getById(id);
	        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String data = ow.writeValueAsString(userBank);
	        res.setCode(0);
	        res.setMessage(data);
	        return res;
		}
		catch (Exception e) {
			logger.error(e);
			res.setCode(1);
        	res.setMessage("Add data fail");
        	return res;
		}
        
    }

	/**
	 * Update bank info of user
	 * @param userBank
	 * @return Response
	 */
	@Override
    public Response update(UserBank userBank) {
		Response res = new Response(1, "");
		try {
			UserBankDao dao = new UserBankDaoImpl();
			long id = userBank.getId();
			res = validateAdd(userBank, 1);
			if(res.getCode() != 0)
				return res;
			
	        boolean result = dao.update(userBank);
	        if(!result) {
	        	return Response.error(EnumConstants.ErrorBank.ERR_TRANSACTION.getKey(),
						EnumConstants.ErrorBank.ERR_TRANSACTION.getValue());
	        }
	        
	        userBank = dao.getById(id);
	        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String data = ow.writeValueAsString(userBank);
	        res.setCode(0);
	        res.setMessage(data);
	        return res;
		}
		catch (Exception e) {
			logger.error(e);
			res.setCode(1);
        	res.setMessage("Update data fail");
        	return res;
		}
        
    }
	
	/**
	 * Delete bank info of user by id
	 * @param id
	 * @return Response
	 */
	@Override
    public Response delete(long id) {
		Response res = new Response(1, "");
		try {
			UserBankDao dao = new UserBankDaoImpl();
			if(dao.getById(id) == null) {
				return Response.error(EnumConstants.ErrorBank.ERR_TRANSACTION.getKey(),
						EnumConstants.ErrorBank.ERR_TRANSACTION.getValue());
			}
			
	        boolean result = dao.delete(id);
	        if(!result) {
	        	return Response.error(EnumConstants.ErrorBank.ERR_TRANSACTION.getKey(),
						EnumConstants.ErrorBank.ERR_TRANSACTION.getValue());
	        }
	        res.setCode(0);
	        return res;
		}
		catch (Exception e) {
			logger.error(e);
			res.setCode(1);
        	res.setMessage("Delete data fail");
        	return res;
		}
        
    }
	
	/**
	 * Get bank info of user by id
	 * @param id
	 * @return Response
	 */
	@Override
    public Response getById(long id) {
		Response res = new Response(1, "");
		try {
			UserBankDao dao = new UserBankDaoImpl();
			if(dao.getById(id) == null) {
				return Response.error(EnumConstants.ErrorBank.ERR_NOT_EXIST_ID.getKey(),
						EnumConstants.ErrorBank.ERR_NOT_EXIST_ID.getValue());
			}
			
			UserBank userBank = dao.getById(id);
	        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String data = ow.writeValueAsString(userBank);
	        res.setCode(0);
	        res.setMessage(data);
	        return res;
		}
		catch (Exception e) {
			logger.error(e);
        	return res;
		}
        
    }
	
	/**
	 * Search user bank
	 * Get bank info of user by bank number
	 * @param nickname
	 * @param bankNumber
	 * @return Response
	 */
	@Override
    public Response getByBankNumber(String nickname, String bankNumber) {
		Response res = new Response(1, "");
		try {
			UserBankDao dao = new UserBankDaoImpl();
			
			UserBank userBank = dao.getByBankNumber(nickname, bankNumber);
	        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String data = ow.writeValueAsString(userBank);
	        res.setCode(0);
	        res.setData(data);
	        return res;
		}
		catch (Exception e) {
			logger.error(e);
        	return res;
		}
        
    }

	@Override
	public UserBank getByDetail(String nickname, String bankNumber) {
		try {
			UserBankDao dao = new UserBankDaoImpl();
			return dao.getByBankNumber(nickname, bankNumber);
		}
		catch (Exception e) {
			logger.error(e);
			return null;
		}

	}

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
	@Override
    public Response search(String nickName, String bankName, String bankNumber, int isAdmin, int pageNumber, int limit) {
		Response res = new Response(1, "");
		List<UserBank> userBanks = new ArrayList<UserBank>();
		try {
			UserBankDao dao = new UserBankDaoImpl();
			userBanks = dao.search(nickName, bankName, bankNumber, isAdmin, pageNumber, limit);
	        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String data = ow.writeValueAsString(userBanks);
			Integer total = 0;
			try	{
				total = dao.search_count(nickName, bankName, bankNumber, isAdmin);
			}catch (Exception e) { }
			String result = "{\"total\":" + total + ",\"data\":" + data+ "}";
	        res.setCode(0); 
	        res.setData(result);
	        return res;
		}
		catch (Exception e) {
			logger.error(e);
			res.setMessage(e.getMessage());
        	return res;
		}
    }

	@Override
	public Response search(String nickName, String customerName, String bankName, String bankNumber, int isAdmin, int pageNumber, int limit) {
		Response res = new Response(1, "");
		List<UserBank> userBanks = new ArrayList<UserBank>();
		try {
			UserBankDao dao = new UserBankDaoImpl();
			userBanks = dao.search(nickName, customerName, bankName, bankNumber, isAdmin, pageNumber, limit);
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String data = ow.writeValueAsString(userBanks);
			Integer total = 0;
			try	{
				total = dao.search_count(nickName, customerName, bankName, bankNumber, isAdmin);
			}catch (Exception e) { }
			String result = "{\"total\":" + total + ",\"data\":" + data+ "}";
			res.setCode(0);
			res.setData(result);
			return res;
		}
		catch (Exception e) {
			logger.error(e);
			res.setMessage(e.getMessage());
			return res;
		}
	}

	@Override
	public boolean isAddBank(String nickName) {
		UserBankDao dao = new UserBankDaoImpl();
		return dao.isAddedBank(nickName);
	}
}
