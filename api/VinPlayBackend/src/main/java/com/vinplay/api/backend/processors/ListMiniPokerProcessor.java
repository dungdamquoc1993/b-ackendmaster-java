/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.vinplay.dal.service.impl.LogMiniPokerServiceImpl
 *  com.vinplay.vbee.common.cp.BaseProcessor
 *  com.vinplay.vbee.common.cp.Param
 *  com.vinplay.vbee.common.response.ResultMiniPokerResponse
 *  javax.servlet.http.HttpServletRequest
 *  org.apache.log4j.Logger
 */
package com.vinplay.api.backend.processors;

import com.vinplay.dal.service.impl.LogMiniPokerServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.ResultMiniPokerResponse;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class ListMiniPokerProcessor
implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger((String)"backend");

    public String execute(Param<HttpServletRequest> param) {
        ResultMiniPokerResponse response = new ResultMiniPokerResponse(false, "1001");
        HttpServletRequest request = (HttpServletRequest)param.get();
        String nickName = request.getParameter("nn");
        String bet_value = request.getParameter("r");
        String timeStart = request.getParameter("ts");
        String timeEnd = request.getParameter("te");
        String moneyType = request.getParameter("mt");
        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("p"));
        }catch (NumberFormatException e){

        }
        if (page < 0) {
            page = 1;
        }
        LogMiniPokerServiceImpl service = new LogMiniPokerServiceImpl();
        try {
            List trans = service.listMiniPoker(nickName, bet_value, timeStart, timeEnd, moneyType, page);
            long totalRecord = service.countMiniPoker(nickName, bet_value, timeStart, timeEnd, moneyType);
            long totalPages = (long) Math.ceil((double) totalRecord/50);
            long totalPlayer = service.countPlayerMiniPoker(nickName, bet_value, timeStart, timeEnd, moneyType);
            response.setTotal_player(totalPlayer);
            response.setTotal(totalPages);
            response.setTotalRecord(totalRecord);
            response.setTransactions(trans);
            response.setSuccess(true);
            response.setErrorCode("0");
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.debug((Object)e);
        }
        return response.toJson();
    }
}

