/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.vinplay.dal.service.impl.LogGameServiceImpl
 *  com.vinplay.vbee.common.cp.BaseProcessor
 *  com.vinplay.vbee.common.cp.Param
 *  com.vinplay.vbee.common.response.LogGameResponse
 *  javax.servlet.http.HttpServletRequest
 */
package com.vinplay.api.backend.processors;

import com.vinplay.dal.service.impl.LogGameServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.messages.LogGameMessage;
import com.vinplay.vbee.common.response.BaseResponseModel;
import com.vinplay.vbee.common.response.LogGameResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class SearchLogGameByNickNameProcessor
implements BaseProcessor<HttpServletRequest, String> {
    public String execute(Param<HttpServletRequest> param) {
        LogGameResponse response = new LogGameResponse(false, "1001");
        HttpServletRequest request = (HttpServletRequest)param.get();
        String sessionId = request.getParameter("sid");
        String nickName = request.getParameter("nn");
        String gameName = request.getParameter("gn");
        String timeStart = request.getParameter("ts");
        String timeEnd = request.getParameter("te");
        String moneyType = request.getParameter("mt");
        int page;
        try {
            page = Integer.parseInt(request.getParameter("p"));
        }catch (NumberFormatException e){
            page =1;
        }

        if(gameName == null || gameName.trim().isEmpty()) {
            response.setMessage("Thiếu tên game bài");
            return response.toJson();
        }

        LogGameServiceImpl service = new LogGameServiceImpl();
        try {
//        ------------ code old -----------
//        List trans = service.searchLogGameByNickName(sessionId, nickName, gameName, timeStart, timeEnd, moneyType, page);
//        int totalRecord = service.countSearchLogGameByNickName(sessionId, nickName, gameName, timeStart, timeEnd, moneyType);
//        long totalPages = totalRecord % 50 == 0 ? (long)(totalRecord / 50) : (long)(totalRecord / 50 + 1);
//        long totalPlayer = service.countPlayerLogGameByNickName(sessionId, nickName, gameName, timeStart, timeEnd, moneyType);
//        response.setTotalPlayer(totalPlayer);
//        response.setTotal(totalPages);
//        response.setTotalRecord((long)totalRecord);
//        response.setTransactions(trans);
//        response.setSuccess(true);
//        response.setErrorCode("0");
//        return response.toJson();
        
        List<Map<String, Object>> data = new ArrayList<>();
        data = service.searchLogGameByNickNameNEW(sessionId, nickName, gameName, timeStart, timeEnd, moneyType, page);
        if(data.size() == 0 || data == null) {
        	return response.toJson();
        }
        
        int totalRecord = Integer.parseInt(data.get(1).get("totalRecord").toString());
        int totalPlayer = Integer.parseInt(data.get(2).get("totalPlayer").toString());
        long totalPages = totalRecord % 50 == 0 ? (long)(totalRecord / 50) : (long)(totalRecord / 50 + 1);
        response.setTotalPlayer(totalPlayer);
        response.setTotal(totalPages);
        response.setTotalRecord((long)totalRecord);
        response.setTransactions(data.get(0).get("data"));
        response.setSuccess(true);
        response.setErrorCode("0");
        return response.toJson();
        }catch (Exception e) {
			e.printStackTrace();
			response.setData(e.getMessage());
			return response.toJson();
		}
    }
}

