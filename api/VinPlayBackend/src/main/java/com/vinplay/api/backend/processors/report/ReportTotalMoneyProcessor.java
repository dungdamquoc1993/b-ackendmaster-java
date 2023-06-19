/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.vinplay.dal.dao.impl.ReportDaoImpl
 *  com.vinplay.dal.entities.report.ReportTotalMoneyModel
 *  com.vinplay.vbee.common.cp.BaseProcessor
 *  com.vinplay.vbee.common.cp.Param
 *  javax.servlet.http.HttpServletRequest
 *  org.apache.log4j.Logger
 */
package com.vinplay.api.backend.processors.report;

import com.vinplay.api.backend.response.ReportTotalMoneyResponse;
import com.vinplay.dal.dao.impl.ReportDaoImpl;
import com.vinplay.dal.entities.report.ReportTotalMoneyModel;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class ReportTotalMoneyProcessor
implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger((String)"report");

    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = (HttpServletRequest)param.get();
        String startTime = request.getParameter("ts");
        String endTime = request.getParameter("te");
        String page = request.getParameter("p");
        ReportTotalMoneyResponse res = new ReportTotalMoneyResponse(false, "1001");
        try {
            if (page != null && startTime != null && endTime != null) {
                int pageNumber = Integer.parseInt(page);
                ReportDaoImpl dao = new ReportDaoImpl();
                res.totals = dao.getReportTotalMoney(pageNumber, startTime, endTime);
                res.setErrorCode("0");
                res.setSuccess(true);
            }
        }
        catch (Exception e) {
            logger.debug((Object)e);
        }
        return res.toJson();
    }
}

