package com.vinplay.livecasino.api.wsclient;

import com.vinplay.livecasino.api.core.obj.BalanceResponse;
import com.vinplay.livecasino.api.core.obj.LaunchGameResponse;
import com.vinplay.livecasino.api.core.obj.TCGBaseResponse;
import com.vinplay.livecasino.api.wsclient.exception.ProcessException;
import com.vinplay.livecasino.api.wsclient.exception.RemoteException;
import com.vinplay.livecasino.api.wsclient.exception.TransportException;

/**
 * <CODE> TCGamingAPICommon </CODE>
 * 天成共用接口介面
 * @author PHOENIX WU
 * @Version 1.0
 * @Date 2016年9月30日
 **/
public interface TCGamingAPICommon {

	/**
	 * 2.1.	Create / Register Player API
	 * 2.1.	创建/确认玩家接口
	 * @param username 玩家帳號
	 * @param password 玩家密碼
	 * @param currency 玩家幣別
	 * @return
	 * @throws RemoteException
	 * @throws ProcessException
	 * @throws TransportException
	 */
	public TCGBaseResponse registerMember(String username, String password, String currency) throws RemoteException, ProcessException, TransportException;

	public BalanceResponse getBalanceMember(String username, int productType) throws RemoteException, ProcessException, TransportException;

	public TCGBaseResponse fundTransferIn(String username, int productType, double amount, String transactionId) throws RemoteException, ProcessException, TransportException;

	public TCGBaseResponse fundTransferOutAll(String username, int productType, String transactionId) throws RemoteException, ProcessException, TransportException;

	public LaunchGameResponse launchGame(String username, int productType, String platform, String game_mode, String game_code) throws RemoteException, ProcessException, TransportException;

}
