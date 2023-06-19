package com.vinplay.api.backend.processors.giftcode;

import com.vinplay.dal.dao.GiftCodeDAO;
import com.vinplay.dal.dao.impl.GiftCodeDAOImpl;
import com.vinplay.dal.entities.giftcode.GiftCodeModel;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowGiftCodeProcessor implements BaseProcessor<HttpServletRequest, String> {
//    private static final Logger logger = Logger.getLogger("backend");
    @Override
    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = param.get();
//        String log =  String.format(("%s - %s - %s"), request.getRemoteAddr(), this.getClass().getName(), request.getQueryString());
//        logger.info(log);

        String giftcode = request.getParameter("gc");
        String user_name = request.getParameter("un");
        String created_by = request.getParameter("cb");
        String startTime = request.getParameter("st");
        String endTime = request.getParameter("et");

        int page, maxItem;
        Integer event = null;

        try {
            event = Integer.parseInt(request.getParameter("ev"));
        } catch (NumberFormatException e) {

        }
        try {
            page = Integer.parseInt(request.getParameter("pg"));
        } catch (NumberFormatException e) {
            page =1;
        }
        try {
            maxItem = Integer.parseInt(request.getParameter("mi"));
        } catch (NumberFormatException e) {
            maxItem = 10;
        }

        GiftCodeDAO dao = new GiftCodeDAOImpl();
        try {
            long total = dao.countGiftCode(giftcode, user_name, created_by, event, startTime, endTime);
            long totalValue = dao.countValueGiftCode(giftcode, user_name, created_by, event, startTime, endTime);
            Map<String, Object> map = new HashMap<>();
            map.put("totalValue", totalValue);
            List<GiftCodeModel> list = dao.showListGiftCode(giftcode, user_name, created_by, event, startTime, endTime, page, maxItem);
            return BaseResponse.success(list, total, map);
        } catch (SQLException e) {
            return BaseResponse.error("-1", e.getMessage());
        }
    }
}
