package com.vinplay.service.sexygame;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.vinplay.dto.ResultFormat;
import com.vinplay.dto.sg.GetBalanceReqDto;
import com.vinplay.dto.sg.GetBalanceRespDto;
import com.vinplay.dto.sg.SGFundTransferRespDto;
import com.vinplay.dto.sg.SGLoginResp;
import com.vinplay.interfaces.sexygame.DepositWithDrawSGService;
import com.vinplay.interfaces.sexygame.MemberSGService;
import com.vinplay.item.SGUserItem;
import com.vinplay.logic.CommonMethod;
import com.vinplay.logic.InitData;
import com.google.gson.Gson;

public class SGService {
	private static Logger logger = Logger.getLogger(SGService.class);
	private static Gson gson  = new Gson();
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		
	}
	public static ResultFormat loginOrCreatePlayer(ObjectInputStream objInStream)
			throws ClassNotFoundException, IOException {
		ResultFormat rf = new ResultFormat();
		String loginname = (String) objInStream.readObject();
		String isMobile = (String) objInStream.readObject();
		List<Object> listObj = new ArrayList<Object>();
		if (!InitData.isSGDown()) {
			MemberSGService memberService = new MemberSGService();
			SGLoginResp response = memberService.login(loginname, isMobile);
            logger.info("SG_LoginOrCreatePlayer response: " + gson.toJson(response));
			if (response != null) {
				listObj.add(response);
				rf.setRes(0);
				rf.setList(listObj);
			} else {
				rf.setRes(1);
				listObj.add("");
				rf.setList(listObj);
				rf.setMsg("Lỗi: Liên hệ CSKH!!");
			}
		} else {
			rf.setRes(2);
			listObj.add("");
			rf.setList(listObj);
			rf.setMsg("Đang bảo trì!");
		}
		return rf;
	}
	public static ResultFormat QueryPlayer(String userName) throws Exception {
		ResultFormat rf = new ResultFormat();
		List<Object> listObj = new ArrayList<Object>();
		SGUserItem user = QuerySGService.getSGId(userName);
		if (user == null || user.getSgid() == null) {
			listObj.add("");
			rf.setRes(3);
			rf.setList(listObj);
			rf.setMsg("chưa liên kết");
			return rf;
		}
		if(!InitData.isSGDown()) {
			MemberSGService memberService = new MemberSGService();
			GetBalanceReqDto reqDto = new GetBalanceReqDto();
			GetBalanceRespDto response = new GetBalanceRespDto();
			
			reqDto.setUserIds(user.getSgid());
			String data = memberService.callAPI("getBalance", reqDto);
			response = gson.fromJson(data, GetBalanceRespDto.class);
			if (response != null) {
				listObj.add(response);
				rf.setRes(0);
				rf.setList(listObj);
			} else {
				rf.setRes(1);
				listObj.add("");
				rf.setList(listObj);
				rf.setMsg("Error");
			}
		}else {
			rf.setRes(2);
			listObj.add("");
			rf.setList(listObj);
			rf.setMsg("Đang bảo trì !");
		}
		return rf;
	}

	public static ResultFormat getBalanceByLoginName(ObjectInputStream objInStream)
			throws ClassNotFoundException, IOException {
		ResultFormat rf = new ResultFormat();
		String loginname = (String) objInStream.readObject();
		List<Object> listObj = new ArrayList<Object>();
		if(!InitData.isSGDown()) {
			MemberSGService memberService = new MemberSGService();
			GetBalanceReqDto reqDto = new GetBalanceReqDto();
			GetBalanceRespDto response = new GetBalanceRespDto();
			SGUserItem sguser = memberService.mappingUserSG(loginname);
			if(sguser == null) {
				response.setStatus("1");
				response.setDesc("User not found!");
			}else {
				reqDto.setUserIds(sguser.getSgid());
				String data = memberService.callAPI("getBalance", reqDto);
				response = gson.fromJson(data, GetBalanceRespDto.class);
			}
			if (response != null) {
				listObj.add(response);
				rf.setRes(0);
				rf.setList(listObj);
			} else {
				rf.setRes(1);
				listObj.add("");
				rf.setList(listObj);
				rf.setMsg("Lỗi: Liên hệ CSKH!!");
			}
		}else {
			rf.setRes(2);
			listObj.add("");
			rf.setList(listObj);
			rf.setMsg("Đang bảo trì !");
		}
		return rf;
	}

	public static ResultFormat depositPlayerMoney(ObjectInputStream objInStream)
			throws ClassNotFoundException, IOException {
		ResultFormat rf = new ResultFormat();
		String loginname = (String) objInStream.readObject();
		if (!CommonMethod.ValidateRequest(loginname)) {
			rf.setRes(1);
			rf.setMsg("Vui lòng đợi 20s cho giao dịch tiếp theo!");
			return rf;
		}
		Double amount = Double.parseDouble(objInStream.readObject().toString());
		String ip = (String) objInStream.readObject();
		List<Object> listObj = new ArrayList<Object>();
		if(!InitData.isSGDown()) {
			DepositWithDrawSGService service = new DepositWithDrawSGService();
			SGFundTransferRespDto response;
			try {
				response = service.depositTransfer(amount, loginname, ip);
				if (response != null) {
					if(response.getStatus().equals("0000")) {
						listObj.add(response);
						rf.setRes(0);
						rf.setList(listObj);
					}else {
						rf.setRes(1);
						listObj.add("");
						rf.setList(listObj);
						rf.setMsg(response.getDesc());
					}
				}else {
					rf.setRes(1);
					listObj.add("");
					rf.setList(listObj);
					rf.setMsg("Lỗi: Liên hệ CSKH!!");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			rf.setRes(2);
			listObj.add("");
			rf.setList(listObj);
			rf.setMsg("Đang bảo trì !");
		}
		return rf;
	}

	public static ResultFormat withdrawPlayerMoney(ObjectInputStream objInStream)
			throws ClassNotFoundException, IOException {
		ResultFormat rf = new ResultFormat();
		String loginname = (String) objInStream.readObject();
		if (!CommonMethod.ValidateRequest(loginname)) {
			rf.setRes(1);
			rf.setMsg("Vui lòng đợi 20s cho giao dịch tiếp theo!");
			return rf;
		}
		Double amount = Double.parseDouble(objInStream.readObject().toString());
		String ip = (String) objInStream.readObject();
		List<Object> listObj = new ArrayList<Object>();
		if(!InitData.isSGDown()) {
			DepositWithDrawSGService service = new DepositWithDrawSGService();
			SGFundTransferRespDto response;
			try {
				response = service.withdrawTransfer(amount, loginname, ip);
				if (response != null) {
					if(response.getStatus().equals("0000")) {
						listObj.add(response);
						rf.setRes(0);
						rf.setList(listObj);
					}else if(response.getStatus().equals("1018")){
						rf.setRes(3);
						listObj.add("");
						rf.setList(listObj);
						rf.setMsg(response.getDesc());
					}else {
						rf.setRes(1);
						listObj.add("");
						rf.setList(listObj);
						rf.setMsg(response.getDesc());
					}
				}else {
					rf.setRes(1);
					listObj.add("");
					rf.setList(listObj);
					rf.setMsg("Lỗi: Liên hệ CSKH!!");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else {
			rf.setRes(2);
			listObj.add("");
			rf.setList(listObj);
			rf.setMsg("Đang bảo trì !");
		}
		return rf;
		
	}
//	
//	public static String setMaintenanceMode(Boolean enable)
//			throws ClassNotFoundException, IOException {
//		Map<String, String> param = new HashMap<>();
//		param.put("Enabled",enable.toString());
//		String msg = getData("SG_SetMaintenanceMode", param);
//		return msg;
//	}
}
