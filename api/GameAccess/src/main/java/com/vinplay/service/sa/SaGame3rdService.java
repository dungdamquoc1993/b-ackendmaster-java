package com.vinplay.service.sa;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import com.vinplay.dto.ResultFormat;
import com.vinplay.dto.sa.SAUserInfoResp;
import com.vinplay.interfaces.sa.SAUtils;
import com.vinplay.logic.InitData;
import com.vinplay.service.GamesCommonService;
import com.vinplay.utils.BaseResponse;
/**
 * @author Archie
 *
 */
public class SaGame3rdService {
    /**
     * @param Username:  username
     * @throws Exception
     */
    public static ResultFormat QueryPlayer(ObjectInputStream objInStream)throws Exception{
        ResultFormat rf = new ResultFormat();
        String userName = (String) objInStream.readObject();
        List<Object> listObj = new ArrayList<Object>();
        if(userName==null || "".equals(userName)) throw new Exception("QueryPlayer SA userName is null or empty");
        
		SAUserInfoResp userinf = SAUtils.getSAInfo(userName);
		if (userinf != null) {
			userinf.setUserName(userinf.getSaid());
			listObj.add(userinf);
			rf.setRes(0);
			rf.setList(listObj);
			rf.setMsg("");
		} else {
			listObj.add("");
			rf.setRes(1);
			rf.setList(listObj);
			rf.setMsg("Thất bại! Liên hệ CSKH์");
		}
        return rf;
    }
    
    public static ResultFormat getUserInfo(String userName , String gameType)throws Exception{
        ResultFormat rf = new ResultFormat();
        List<Object> listObj = new ArrayList<Object>();
        //validation
        if (!InitData.isSADown()) {
            BaseResponse<String> resp = GamesCommonService.checkPlayerExist(userName, gameType);
            if(resp != null){
                if(0 == resp.getCode()) {
                    SAUserInfoResp userinf = SAUtils.getSAInfo(resp.getData());
                    if(userinf != null){
                        userinf.setUserName(userName);
                        userinf.setSaid(resp.getData());
                        listObj.add(userinf);
                        rf.setRes(0);
                        rf.setList(listObj);
                        rf.setMsg("");
                    }else{
                        listObj.add("");
                        rf.setRes(1);
                        rf.setList(listObj);
                        rf.setMsg("Thất bại! Liên hệ CSKH");
                    }
                }else {
                    rf.setRes(0);
                    rf.setList(listObj);
                    rf.setMsg(resp.getMessage());
                }
            }else{
                rf.setRes(1);
                rf.setList(listObj);
                rf.setMsg("Thất bại! Liên hệ CSKH");
            }
        }else {
            rf.setRes(2);
            listObj.add("");
            rf.setList(listObj);
            rf.setMsg("Đang bảo trì!");
        }

        return rf;
    }
}
