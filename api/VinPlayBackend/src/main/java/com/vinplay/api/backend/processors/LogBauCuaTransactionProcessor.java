/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.vinplay.dal.service.impl.LogBauCuaServiceImpl
 *  com.vinplay.vbee.common.cp.BaseProcessor
 *  com.vinplay.vbee.common.cp.Param
 *  com.vinplay.vbee.common.response.LogBauCuaResponse
 *  javax.servlet.http.HttpServletRequest
 *  org.apache.log4j.Logger
 */
package com.vinplay.api.backend.processors;

import com.vinplay.dal.service.impl.LogBauCuaServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.LogBauCuaResponse;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class LogBauCuaTransactionProcessor
implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger((String)"backend");

    public String execute(Param<HttpServletRequest> param) {
        LogBauCuaResponse response = new LogBauCuaResponse(false, "1001");
        HttpServletRequest request = (HttpServletRequest)param.get();
        String referent_id = request.getParameter("rid");
        String nickName = request.getParameter("nn");
        String room = request.getParameter("r");
        String timeStart = request.getParameter("ts");
        String timeEnd = request.getParameter("te");
        String moneyType = request.getParameter("mt");
        int page = Integer.parseInt(request.getParameter("p"));
        if (page < 0) {
            return response.toJson();
        }
        LogBauCuaServiceImpl service = new LogBauCuaServiceImpl();
        try {
            List trans = service.listLogBauCua(referent_id, nickName, room, timeStart, timeEnd, moneyType, page);
            Map map = service.countMapLogBauCua(referent_id, nickName, room, timeStart, timeEnd, moneyType);
            long totalRecord = (long) map.get("totalRecord");
            long totalPages = (long) Math.ceil((double) totalRecord/50);
            long totalPlayer = (long) map.get("totalPlayer");
            response.setTotal(totalPages);
            response.setTotalRecord(totalRecord);
            response.setTotalPlayer(totalPlayer);
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

