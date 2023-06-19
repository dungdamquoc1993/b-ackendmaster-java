package com.vinplay.service.sa;

import com.vinplay.dto.ResultFormat;
import com.vinplay.dto.sa.SADepositResp;
import com.vinplay.dto.sa.SAWithdrawalResp;
import com.vinplay.interfaces.sa.SAFundTransferService;
import com.vinplay.logic.CommonMethod;
import com.vinplay.utils.Constants;
import com.google.gson.Gson;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
/**
 * @author Archie
 *
 */
public class SACommonService {
	
	private static final Logger logger = Logger.getLogger(SACommonService.class);
	
    public static ResultFormat SADepositPlayerMoney(ObjectInputStream objInStream)throws Exception{
        ResultFormat rf = new ResultFormat();
        String userName = (String) objInStream.readObject();
        if (!CommonMethod.ValidateRequest(userName)) {
            return new ResultFormat(Constants.ERROR_MESSAGE_20S);
        }
        Double amount = Double.parseDouble(objInStream.readObject().toString());
        String playerCode =((String) objInStream.readObject());
		//validation
        if(amount == null || amount <= 0) throw new Exception("SADepositPlayerMoney amount<=0");
		if (playerCode == null || "".equals(playerCode)) throw new Exception("playerCode is null or empty");
		
		logger.info("SA_DepositPlayerMoney request: " + "Amount=" + amount + " | " + "Username=" + userName + " | playerCode=" + playerCode);
		
		List<Object> listObj = new ArrayList<Object>();
		//call API
		SAFundTransferService service = new SAFundTransferService();
		SADepositResp depositDTO = service.DepositTransfer(amount, userName, playerCode);
		
		Gson gson =new Gson();
		logger.info("SA_LoginOrCreatePlayer response: " + gson.toJson(depositDTO));
		
		if (depositDTO != null) {
			listObj.add(depositDTO);
			rf.setRes(0);
			rf.setList(listObj);
		} else {
			rf.setRes(1);
			listObj.add("");
			rf.setList(listObj);
			rf.setMsg("Thất bại! Liên hệ CSKH");
		}
        return rf;
    }

    public static ResultFormat WithdrawPlayerMoney(ObjectInputStream objInStream)throws Exception{
        ResultFormat rf = new ResultFormat();
        String userName = (String) objInStream.readObject();
        if (!CommonMethod.ValidateRequest(userName)) {
            return new ResultFormat(Constants.ERROR_MESSAGE_20S);
        }
        Double amount = Double.parseDouble(objInStream.readObject().toString());
        String playerCode = ((String) objInStream.readObject());
        //validation
        if(amount == null || amount <= 0) throw new Exception("SA_WithdrawPlayerMoney amount<=0");
		if (playerCode == null || "".equals(playerCode)) throw new Exception("SA_WithdrawPlayerMoney playerCode is null or empty");
		
		logger.info("SA_WithdrawPlayerMoney request: " + "Amount-" + amount + " | " + "Username-" + userName + " | PlayerCode-" + playerCode);
		
		List<Object> listObj = new ArrayList<Object>();
		
		SAFundTransferService service = new SAFundTransferService();
		SAWithdrawalResp withdrawDTO = service.WithdrawTransfer(amount, userName, playerCode);
		Gson gson = new Gson();
		logger.info("SA_WithdrawPlayerMoney response: " + gson.toJson(withdrawDTO));
		
		if (withdrawDTO != null) {
			listObj.add(withdrawDTO);
			rf.setRes(0);
			rf.setList(listObj);
		} else {
			rf.setRes(1);
			listObj.add("");
			rf.setList(listObj);
			rf.setMsg("Thất bại! Liên hệ CSKH์");
		}
        return rf;
    }
}
