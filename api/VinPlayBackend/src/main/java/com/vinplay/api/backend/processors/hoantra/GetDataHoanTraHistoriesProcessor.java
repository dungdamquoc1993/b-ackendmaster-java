package com.vinplay.api.backend.processors.hoantra;

import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.models.HoanTraModel;
import com.vinplay.vbee.common.response.BaseResponse;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class GetDataHoanTraHistoriesProcessor implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger((String) "backend");

    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = param.get();
        String time = request.getParameter("tm");
        String nick_name = request.getParameter("nn");
        int page, limit;
        try {
            page = Integer.parseInt( request.getParameter("pg"));
        } catch (NumberFormatException e) {
            page = 1;
        }

        try {
            limit = Integer.parseInt( request.getParameter("mi"));
        } catch (NumberFormatException e) {
            limit = 10;
        }

        try {
            Date date = time == null || time.isEmpty() ? null : new Date(Long.parseLong(time));
            Map<String, Object> result = new ReturnMoney().searchListHoanTraHistories(date, nick_name, page, limit);
            long count = Long.parseLong(result.get("totalrecords").toString());
            result.remove("totalrecords");
            return BaseResponse.success(result, count);
        }catch (NumberFormatException e){
            return BaseResponse.error("-1", "tm: định dạng trả về cần long");
        }catch (SQLException e){
            logger.info(e);
            return BaseResponse.error("-1", e.getMessage());
        }
    }

}
