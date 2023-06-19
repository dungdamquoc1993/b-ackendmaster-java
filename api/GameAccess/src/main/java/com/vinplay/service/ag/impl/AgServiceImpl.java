package com.vinplay.service.ag.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.vinplay.dao.ag.AgDao;
import com.vinplay.dao.impl.ag.AgDaoImpl;
import com.vinplay.dto.ResultFormat;
import com.vinplay.dto.ag.BaseAGResponseDto;
import com.vinplay.interfaces.ag.MemberAGService;
import com.vinplay.item.AGUserItem;
import com.vinplay.logic.CommonMethod;
import com.vinplay.service.ag.AgService;
import com.vinplay.utils.BaseResponse;

public class AgServiceImpl implements AgService {

	public static final Logger logger = Logger.getLogger(AgServiceImpl.class);

	private static Gson gson = new Gson();
	private AgDao agDao = new AgDaoImpl();

	public ResultFormat forwardGame(String nickName) throws Exception {
		ResultFormat rf = new ResultFormat();
		List<Object> listObj = new ArrayList<Object>();
		
		AGUserItem agUserItem = agDao.mappingUser(nickName);
		if (agUserItem == null) {
			rf.setRes(1);
			rf.setList(listObj);
			rf.setMsg("Lỗi : Liên hệ chăm sóc khách hàng!");
		}
		MemberAGService service = new MemberAGService();
		
		String queryplayerDto = service.forwardGame(nickName, nickName);
		if (queryplayerDto != null) {
			listObj.add(queryplayerDto);
			rf.setRes(0);
			rf.setList(listObj);
			rf.setMsg("");
		} else {
			listObj.add("");
			rf.setRes(1);
			rf.setList(listObj);
			rf.setMsg("Lỗi : Liên hệ chăm sóc khách hàng!");
		}
		return rf;
	}

	@Override
	public ResultFormat queryPlayer(String userName) throws Exception {
		ResultFormat rf = new ResultFormat();

		List<Object> listObj = new ArrayList<Object>();
		logger.info("AG_GetBalance request: " + "login name -" + userName);
		// call api
		MemberAGService service = new MemberAGService();
		BaseAGResponseDto queryplayerDto = service.checkBalance(userName);
		logger.info("AG_GetBalance response: " + gson.toJson(queryplayerDto));

		if (queryplayerDto != null) {
			listObj.add(queryplayerDto);
			rf.setRes(0);
			rf.setList(listObj);
			rf.setMsg("");
		} else {
			listObj.add("");
			rf.setRes(1);
			rf.setList(listObj);
			rf.setMsg("Error: Contact  Customer Service!!");
		}

		return rf;
	}
	
	/**
	 * PT Deposit
	 * 
	 * @param objInStream
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResultFormat depositPlayerMoney(String userName, Double amount, String ip) throws Exception {
		ResultFormat rf = new ResultFormat();
		if (!CommonMethod.ValidateRequest(userName)) {
			rf.setRes(1);
			rf.setMsg("Vui lòng đợi 10s cho giao dịch tiếp theo !");
			return rf;
		}
		if (amount == null || amount <= 0)
			throw new Exception("Deposit AG amount <= 0");

		List<Object> listObj = new ArrayList<Object>();
		logger.info("AG_Deposit Player Money request: " + "Amount-" + amount + " | " + "Username-" + userName + " | Ip-"
				+ ip);
		//check money format
		if (amount % 1 != 0) {
			return new ResultFormat(2, "Quý khách vui lòng nhập số tiền chẵn (là hệ số của 1000 vnđ) ");
		}
		// call api
		MemberAGService service = new MemberAGService();
		BaseAGResponseDto depositDTO = service.depositTransfer(userName, amount, ip);
		if (depositDTO != null && StringUtils.isEmpty(depositDTO.getMsg())) {
			listObj.add(depositDTO);
			rf.setRes(0);
			rf.setList(listObj);
		} else {
			rf.setRes(1);
			listObj.add("");
			rf.setList(listObj);
			rf.setMsg("Lỗi : Liên hệ chăm sóc khách hàng!");
		}
		return rf;
	}

	/**
	 * WithDraw
	 * 
	 * @param objInStream
	 * @return
	 * @throws Exception
	 */
	@Override
	public ResultFormat withdrawPlayerMoney(String userName, Double amount, String ip) throws Exception {
		ResultFormat rf = new ResultFormat();
		if (!CommonMethod.ValidateRequest(userName)) {
			rf.setRes(1);
			rf.setMsg("Vui lòng đợi 10s cho giao dịch tiếp theo !");
			return rf;
		}

		if (amount == null || amount <= 0)
			throw new Exception("Deposit AG amount <= 0");

		List<Object> listObj = new ArrayList<Object>();
		logger.info("AG_Withdraw Player Money request: " + "Amount-" + amount + " | " + "Username-" + userName
				+ " | Ip-" + ip);
		// cal API
		MemberAGService service = new MemberAGService();
		BaseAGResponseDto response = service.withdrawTransfer(userName, amount, ip);
		//code ngu ddeos muon sua
		logger.info("AG_Withdraw Player Money response: " + gson.toJson(response));
		if (response != null && StringUtils.isEmpty(response.getMsg())) {
			listObj.add(response);
			rf.setRes(0);
			rf.setList(listObj);
		}else if (response != null && "99".equals(response.getMsg())) {
			listObj.add(response);
			rf.setRes(1);
			rf.setList(listObj);
			rf.setMsg("Số dư của quý khách không đủ");
		}else if (response != null && "100".equals(response.getMsg())) {
			listObj.add(response);
			rf.setRes(1);
			rf.setList(listObj);
			rf.setMsg("Sảnh game Live casino AG đang bảo trì! ");
		} else {
			rf.setRes(1);
			listObj.add("");
			rf.setList(listObj);
			rf.setMsg("Lỗi : Liên hệ chăm sóc khách hàng!");
		}
		return rf;
	}

//	@Override
//	public ResultFormat getUserInfo(String userName) throws Exception {
//		ResultFormat rf = new ResultFormat();
//
//		List<Object> listObj = new ArrayList<Object>();
//		// access DB
//		AGUserItem agUserItem = agDao.mappingUser(userName);
//
//		if (agUserItem != null) {
//			return queryPlayer(userName);
//		} else {
//			rf.setRes(1);
//			listObj.add("");
//			rf.setList(listObj);
//			rf.setMsg("Lỗi : Liên hệ chăm sóc khách hàng!");
//		}
//		return rf;
//	}

}
