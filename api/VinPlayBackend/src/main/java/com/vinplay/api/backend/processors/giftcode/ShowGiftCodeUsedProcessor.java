package com.vinplay.api.backend.processors.giftcode;

import com.vinplay.dal.dao.GiftCodeUsedDAO;
import com.vinplay.dal.dao.impl.GiftCodeUsedDAOImpl;
import com.vinplay.dal.entities.giftcode.GiftCodeUsedModel;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowGiftCodeUsedProcessor implements BaseProcessor<HttpServletRequest, String> {
//    private static final Logger logger = Logger.getLogger("backend");

    @Override
    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = param.get();
//        String log =  String.format(("%s - %s - %s"), request.getRemoteAddr(), this.getClass().getName(), request.getQueryString());
//        logger.info(log);

        int page, maxItem, flagtime = 1;
        Integer type = null,event = null;
        // username in DB change to nickname for private data
        String nickname = request.getParameter("nn");
        String gid = request.getParameter("gid");
        String startTime = request.getParameter("st");
        String endTime = request.getParameter("et");

        try {
            event = Integer.parseInt(request.getParameter("ev"));
        } catch (NumberFormatException e) {

        }
        try {
            type = Integer.parseInt(request.getParameter("type"));
        } catch (NumberFormatException e) {

        }
        try {
            flagtime = Integer.parseInt(request.getParameter("ft"));
        } catch (NumberFormatException e) {

        }
        try {
            page = Integer.parseInt(request.getParameter("pg"));
        } catch (NumberFormatException e) {
            page = 1;
        }
        try {
            maxItem = Integer.parseInt(request.getParameter("mi"));
        } catch (NumberFormatException e) {
            maxItem = 10;
        }


        GiftCodeUsedDAO dao = new GiftCodeUsedDAOImpl();
        try {
            long total = dao.countGiftCodeUsed(gid, nickname, type, event, flagtime, startTime, endTime);
            long totalValue = dao.countValueGiftCodeUsed(gid, nickname, type, event, flagtime, startTime, endTime);
            Map<String, Object> map = new HashMap<>();
            map.put("totalValue", totalValue);
            List<GiftCodeUsedModel> list = dao.showListGiftCodeUsed(gid, nickname, type, event, flagtime, startTime, endTime, page, maxItem);
            return BaseResponse.success(list, total, map);
        } catch (SQLException e) {
            return BaseResponse.error("-1", e.toString());
        }
    }
}