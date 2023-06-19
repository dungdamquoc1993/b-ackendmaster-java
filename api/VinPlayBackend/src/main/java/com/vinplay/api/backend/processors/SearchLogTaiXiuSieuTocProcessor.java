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

import com.vinplay.dal.dao.LogTaiXiuSieuTocDAO;
import com.vinplay.dal.dao.impl.LogTaiXiuSieuTocDAOImpl;
import com.vinplay.dal.entities.taixiu.LogTaiXiuSieuToc;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class SearchLogTaiXiuSieuTocProcessor
implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger((String)"backend");

    public String execute(Param<HttpServletRequest> param) {
        BaseResponse response = new BaseResponse("1001", null);
        HttpServletRequest request = (HttpServletRequest)param.get();
        String fromTime = request.getParameter("ft");
        String endTime = request.getParameter("et");
        int page, maxItem, status;
        try {
            page = Integer.parseInt(request.getParameter("pg"));
        } catch (NumberFormatException e) {
            page = -1;
        }

        try {
            maxItem = Integer.parseInt(request.getParameter("mi"));
        } catch (NumberFormatException e) {
            maxItem = -1;
        }

        try {
            status = Integer.parseInt(request.getParameter("st"));
        } catch (NumberFormatException e) {
            status = -1;
        }

        fromTime = (fromTime == null || fromTime.trim().isEmpty()) ? new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(Calendar.getInstance().getTime()) : fromTime + " 00:00:00";
        endTime = (endTime == null || endTime.trim().isEmpty()) ? new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(Calendar.getInstance().getTime()) : endTime + " 23:59:59";
        LogTaiXiuSieuTocDAO service = new LogTaiXiuSieuTocDAOImpl();
        try {
            List<LogTaiXiuSieuToc> data = service.search(fromTime, endTime, status, page, maxItem);
            long totalRecord = data.get(data.size() - 1).status;
            long totalPages = maxItem == -1 ? (long) Math.ceil((double) totalRecord/50) : (long) Math.ceil((double) totalRecord/maxItem);
            response.setTotalRecords(totalRecord);
            data.remove(data.size() - 1);
            response.setData(data);
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

