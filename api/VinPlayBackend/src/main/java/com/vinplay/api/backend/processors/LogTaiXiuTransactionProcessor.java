/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.vinplay.dal.service.impl.LogTaiXiuServiceImpl
 *  com.vinplay.vbee.common.cp.BaseProcessor
 *  com.vinplay.vbee.common.cp.Param
 *  com.vinplay.vbee.common.response.ResultTaiXiuResponse
 *  javax.servlet.http.HttpServletRequest
 *  org.apache.log4j.Logger
 */
package com.vinplay.api.backend.processors;

import com.vinplay.dal.service.impl.LogTaiXiuServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.ResultTaiXiuResponse;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class LogTaiXiuTransactionProcessor
implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger((String)"backend");

    public String execute(Param<HttpServletRequest> param) {
        ResultTaiXiuResponse response = new ResultTaiXiuResponse(false, "1001");
        HttpServletRequest request = (HttpServletRequest)param.get();
        String referent_id = request.getParameter("rid");
        String nickName = request.getParameter("nn");
        String betSide = request.getParameter("bs");
        String timeStart = request.getParameter("ts");
        String timeEnd = request.getParameter("te");
        String moneyType = request.getParameter("mt");
        String isBot = request.getParameter("ib");

        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("p"));
        } catch (NumberFormatException e) {
            page = 1;
        }
        LogTaiXiuServiceImpl service = new LogTaiXiuServiceImpl();
        try {
            List trans = service.listLogTaiXiu(referent_id, nickName, betSide, moneyType, timeStart, timeEnd, isBot, page);
            Map<String, Integer> map = service.countPlayerLogTaiXiu(referent_id, nickName, betSide, moneyType, timeStart, timeEnd, isBot);
            long totalRecord = (long) map.get("totalRecord");
            long totalPages = (long) Math.ceil((double) totalRecord/50);
            long totalPlayer = (long) map.get("totalPlayer");
            response.setTotalRecord(totalRecord);
            response.setTotal(totalPages);
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

