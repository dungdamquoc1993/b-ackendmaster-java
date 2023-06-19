/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.vinplay.vbee.common.exceptions.KeyNotFoundException
 *  com.vinplay.vbee.common.messages.dvt.RechargeByBankMessage
 *  com.vinplay.vbee.common.messages.dvt.RechargeByCardMessage
 */
package com.vinplay.payment.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vinplay.payment.entities.WithDrawPaygateModel;
import com.vinplay.payment.entities.WithDrawPaygateReponse;

public interface WithDrawPaygateDao {
	
	public long countNumberWithdrawSuccessInDay(String nickname);
	/**
	 * Check exist transaction is pending by nickname and provider name
	 * @param nickname
	 * @param providerName
	 * @return true: exist; false: not exist
	 */
	public boolean CheckPending(String nickname, String providerName);
	
	/**
	 * Get detail transaction by id
	 * @param Id: Id of transaction
	 * @return DepositPaygateModel
	 */
	public WithDrawPaygateModel GetById(String Id);
	
	/**
	 * Add transaction
	 * @param withDrawPaygateModel
	 * @return orderId
	 */
	public long Add(WithDrawPaygateModel withDrawPaygateModel);
	
	/**
	 * Update info transaction
	 * @param OrderId: Order id of transaction (not Id)
	 * @param MerchantCode: Id reference
	 * @param paymentType: Payment type
	 * @param providerName: Provider name
	 * @param userApprove: User name change status
	 * @param bankCode
	 * @return true: success; false: failed
	 */
	public Boolean UpdateInfo(String orderId, String merchantCode, String paymentType, String providerName, String bankCode, String userApprove);
	
	/**
	 * Update info transaction
	 * @param OrderId: Order id of transaction (not Id)
	 * @param MerchantCode: Id reference
	 * @param paymentType: Payment type
	 * @param providerName: Provider name
	 * @param userApprove: User name change status
	 * @return true: success; false: failed
	 */
	public Boolean UpdateInfo(String orderId, String merchantCode, String paymentType, String providerName, String userApprove);
	
	/**
	 * Update status transaction
	 * @param OrderId: Order id of transaction (not Id)
	 * @param cartId: Id reference
	 * @param status: Status of transaction will be change
	 * @param userApprove: User name change status
	 * @param reason
	 * @return true: success; false: failed
	 */
	public Boolean UpdateStatus(String orderId, String cartId, int status, String userApprove, String reason);
	/**
	 * Update status transaction
	 * @param OrderId: Order id of transaction (not Id)
	 * @param cartId: Id reference
	 * @param status: Status of transaction will be change
	 * @param userApprove: User name change status
	 * @return true: success; false: failed
	 */
	public Boolean UpdateStatus(String orderId, String cartId, int status, String userApprove);
	
	/**
	 * Update status transaction
	 * @param OrderId: Order id of transaction (not Id)
	 * @param status: Status of transaction will be change
	 * @param userApprove: User name change status
	 * @return true: success; false: failed
	 */
	public Boolean UpdateStatus(String orderId, int status, String userApprove);
	
	/**
	 * Update request time of transaction
	 * @param OrderId: Order id of transaction (not Id)
	 * @param reRequestTime: Time request
	 * @param userApprove: User name change status
	 * @param userApprove: User name change status
	 * @return true: success; false: failed
	 */
	public Boolean UpdateRequestTime(String orderId, String reRequestTime, String userApprove);
	
	/**
	 * Update amount, amount fee of transaction
	 * @param OrderId: Order id of transaction (not Id)
	 * @param amount: Amount of transaction
	 * @param amountFee: Amount fee of transaction
	 * @param userApprove: User name change status
	 * @return true: success; false: failed
	 */
	public Boolean UpdateAmount(String orderId, long amount, long amountFee, String userApprove);
	
	/**
	 * Get detail transaction by reference Id
	 * @param cartId: Reference Id of transaction
	 * @return WithDrawPaygateModel
	 */
	public WithDrawPaygateModel GetByReferenceId(String cartId);
	
	/**
	 * Get detail transaction by reference Id
	 * @param orderId: Order id of transaction (not Id)
	 * @return WithDrawPaygateModel
	 */
	public WithDrawPaygateModel GetByOrderId(String orderId);
	
	/**
	 * Search transaction
	 * @param withDrawPaygateModel: Contain value of fields want to search
	 * @param status
	 * @param page
	 * @param maxItem
	 * @param fromTime
	 * @param endTime
	 * @return DepositPaygateModel
	 */
	public WithDrawPaygateReponse Find(WithDrawPaygateModel withDrawPaygateModel, int page, int maxItem, String fromTime, String endTime);
	
	/**
	 * Search transaction
	 * @param nickname
	 * @param status
	 * @param page
	 * @param maxItem
	 * @param fromTime
	 * @param endTime
	 * @param providerName (If want get all, set value is empty)
	 * @return WithDrawPaygateReponse
	 */
	public WithDrawPaygateReponse Find(String nickname, int status, int page, int maxItem, String fromTime, String endTime, String providerName);
	
	/**
	 * Search transaction
	 * @param nickname
	 * @param status
	 * @param page
	 * @param maxItem
	 * @param fromTime
	 * @param endTime
	 * @param providerName (If want get all, set value is empty)
	 * @param orderId
	 * @param transactionId
	 * @param bankCode
	 * @param bankAccountNumber
	 * @param bankAccountName
	 * @return WithDrawPaygateReponse
	 */
	public WithDrawPaygateReponse Find(String nickname, Integer status, int page, int maxItem, String fromTime, String endTime,
			String providerName, String orderId, String transactionId, String bankCode, String bankAccountNumber, String bankAccountName,Double fromAmount,Double toAmount);

	public ArrayList<Object> find(HashMap<String, Object> condition, int page, int maxItem) throws Exception;
	public long count(HashMap<String, Object> condition);
	public Long[] statistic(HashMap<String, Object> condition);
	 /** Get all records have status is RECEIVED
	 * @param minutes
	 * @see ArrayList<WithDrawPaygateModel>
	 */
	public ArrayList<WithDrawPaygateModel> GetRecevied(Integer minutes);

	public Boolean UpdateStatus(String orderId, int status, String userApprove ,String reason);

	/**
	 * Search transaction only display for user play
	 * @param nickname
	 * @param status
	 * @param page
	 * @param maxItem
	 * @param fromTime
	 * @param endTime
	 * @param providerName (If want get all, set value is empty)
	 * @return DepositPaygateModel
	 */
	public Map<String, Object> FindTransaction(String nickname, int status, int page, int maxItem, String fromTime,
													 String endTime, String providerName);
	public Map<String, Object> FindTransaction(String nickname, String agentCode, int status, int page, int maxItem,
			String fromTime, String endTime, String providerName);

	public Boolean delete(String id);

	public Boolean Update(WithDrawPaygateModel withDrawPaygateModel);
}

