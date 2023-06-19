/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.vinplay.dal.service.impl.LogTaiXiuServiceImpl
 *  com.vinplay.vbee.common.cp.BaseProcessor
 *  com.vinplay.vbee.common.cp.Param
 *  com.vinplay.vbee.common.response.TaiXiuResultReponse
 *  javax.servlet.http.HttpServletRequest
 *  org.apache.log4j.Logger
 */
package com.vinplay.api.backend.processors;

import com.vinplay.dal.service.impl.LogTaiXiuServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.TaiXiuResultReponse;
import com.vinplay.vbee.common.response.TaiXiuResultResponse;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class SearchLogTaiXiuResultProcessor
implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger((String)"backend");

    public String execute(Param<HttpServletRequest> param) {
        TaiXiuResultReponse response = new TaiXiuResultReponse(false, "1001");
        HttpServletRequest request = (HttpServletRequest)param.get();
        String referent_id = request.getParameter("rid");
        String timeStart = request.getParameter("ts");
        String timeEnd = request.getParameter("te");
        String moneyType = request.getParameter("mt");
        int page = 1;
        try{
            page = Integer.parseInt(request.getParameter("p"));
        }catch (NumberFormatException e){

        }
        LogTaiXiuServiceImpl service = new LogTaiXiuServiceImpl();
        try {
            List<TaiXiuResultResponse> trans = service.listLogTaiXiuResult(referent_id, moneyType, timeStart, timeEnd, page);
            long totalRecord = service.countLogTaiXiuResult(referent_id, moneyType, timeStart, timeEnd);
            long totalPages = (long) Math.ceil((double) totalRecord/50);
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

