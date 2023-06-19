/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.vinplay.dal.service.impl.AgentServiceImpl
 *  com.vinplay.vbee.common.cp.BaseProcessor
 *  com.vinplay.vbee.common.cp.Param
 *  com.vinplay.vbee.common.response.ResultAgentTranferResponse
 *  javax.servlet.http.HttpServletRequest
 */
package com.vinplay.api.backend.processors;

import com.vinplay.dal.service.impl.AgentServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.ResultAgentTranferResponse;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class SeachAgentTranferMoneyByNickNameProcessor
implements BaseProcessor<HttpServletRequest, String> {
    public String execute(Param<HttpServletRequest> param) {
        ResultAgentTranferResponse response = new ResultAgentTranferResponse(false, "1001");
        HttpServletRequest request = (HttpServletRequest)param.get();
        String nickName = request.getParameter("nn");
        String status = request.getParameter("st");
        String timeStart = request.getParameter("ts");
        String timeEnd = request.getParameter("te");
        String topDS = request.getParameter("tds");
        int page = Integer.parseInt(request.getParameter("p"));
        AgentServiceImpl service = new AgentServiceImpl();
        List trans = service.searchAgentTranferMoney(nickName, status, timeStart, timeEnd, topDS, page);
        long totalRecord = service.countsearchAgentTranferMoney(nickName, status, timeStart, timeEnd, topDS);
        long totalVinReceive = service.totalMoneyVinReceiveFromAgent(nickName, status, timeStart, timeEnd, topDS);
        long totalVinSend = service.totalMoneyVinSendFromAgent(nickName, status, timeStart, timeEnd, topDS);
        long totalVinFee = service.totalMoneyVinFeeFromAgent(nickName, status, timeStart, timeEnd, topDS);
        long totalPages = 0L;
        totalPages = totalRecord % 50L == 0L ? totalRecord / 50L : totalRecord / 50L + 1L;
        response.setTotal(totalPages);
        response.setTotalRecord(totalRecord);
        response.setTransactions(trans);
        response.setTotalVinReceive(totalVinReceive);
        response.setTotalVinSend(totalVinSend);
        response.setTotalFee(totalVinFee);
        response.setSuccess(true);
        response.setErrorCode("0");
        return response.toJson();
    }
}

