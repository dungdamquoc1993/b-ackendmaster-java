package com.vinplay.api.backend.processors.report;

import com.vinplay.api.backend.response.ReportTransactionDetailResponse;
import com.vinplay.dal.dao.impl.ReportDaoImpl;
import com.vinplay.dal.entities.report.ReportTransactionDetailModel;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportTransactionDetailProcessor implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger((String)"report");

    @Override
    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = param.get();
        String action_name = request.getParameter("an");
        String nick_name = request.getParameter("nn");
        String fromTime = request.getParameter("ft");
        String endTime = request.getParameter("et");

        int page = 1, maxItem = 10,flagTime = 0;
        Long fee = null,money_lost = null,money_other = null,money_win = null;

        try {
            fee = Long.parseLong(request.getParameter("fe"));
        } catch (NumberFormatException e) {
            // TODO: handle exception
        }
        try {
            money_lost = Long.parseLong(request.getParameter("ml"));
        } catch (NumberFormatException e) {
            // TODO: handle exception
        }
        try {
            money_other = Long.parseLong(request.getParameter("mo"));
        } catch (NumberFormatException e) {
            // TODO: handle exception
        }
        try {
            money_win = Long.parseLong(request.getParameter("mw"));
        } catch (NumberFormatException e) {
            // TODO: handle exception
        }
        try {
            flagTime = Integer.parseInt(request.getParameter("fgt"));
        } catch (NumberFormatException e) {
            // TODO: handle exception
        }
        try {
            page = Integer.parseInt(request.getParameter("pg"));
        } catch (NumberFormatException e) {
            // TODO: handle exception
        }
        try {
            maxItem = Integer.parseInt(request.getParameter("mi"));
        } catch (NumberFormatException e) {
            // TODO: handle exception
        }
        ReportDaoImpl dao = new ReportDaoImpl();
        List<ReportTransactionDetailModel> data = new ArrayList<>();
        ReportTransactionDetailResponse res = new ReportTransactionDetailResponse(false, "1001");
        try {

            Map<String, Object> map = dao.getReportTransactionDetail(action_name, null, nick_name, null, fee, money_lost, money_other, money_win,flagTime, fromTime, endTime, page, maxItem);
            data = (List<ReportTransactionDetailModel>) map.get("listData");
            res.setData(data);
            res.setErrorCode("0");
            res.setSuccess(true);
            res.setTotalData((Long) map.get("totalData"));
        }
        catch (Exception e) {
            logger.debug((Object)e);
        }
        return res.toJson();
    }
}
