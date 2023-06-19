package com.vinplay.api.backend.processors.hoantra;

import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.models.HoanTraModel;
import com.vinplay.vbee.common.response.BaseResponse;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GetDataHoanTraProcessor implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger((String) "backend");

    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = param.get();
        String time = request.getParameter("tm");
        String nick_name = request.getParameter("nn");
        int page =1 , maxItem = 10;
        try {
            page = Integer.parseInt(request.getParameter("pg"));
        } catch (NumberFormatException e){
            page = 1;
        }
        try {
            maxItem = Integer.parseInt(request.getParameter("mi"));
        } catch (NumberFormatException e){
            maxItem = 10;
        }

        try {
            Date datetime = new SimpleDateFormat("yyyy-MM-dd").parse(time);
            long count = new ReturnMoney().countListHoanTra(datetime, nick_name);
            List<HoanTraModel> data = new ReturnMoney().getListHoanTra(datetime, nick_name, page, maxItem);
            return BaseResponse.success(data, count);

        }catch (NumberFormatException e){
            return BaseResponse.error("-1", "tm: định dạng trả về cần long");
        }catch (SQLException e){
            logger.info(e);
            return BaseResponse.error("-1", e.getMessage());
        } catch (ParseException e) {
            return BaseResponse.error("-1", e.getMessage());
        }
    }
}
