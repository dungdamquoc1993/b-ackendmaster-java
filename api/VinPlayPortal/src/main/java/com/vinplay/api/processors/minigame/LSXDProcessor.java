package com.vinplay.api.processors.minigame;

import com.vinplay.api.processors.minigame.response.LSXDResponse;
import com.vinplay.dal.service.GameBaiService;
import com.vinplay.dal.service.impl.GameBaiServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.LogMoneyUserResponse;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class LSXDProcessor implements BaseProcessor<HttpServletRequest, String> {
    private Logger logger = Logger.getLogger((String)"api");

    public String execute(Param<HttpServletRequest> param) {
        LSXDResponse response = new LSXDResponse(false, "1001");
        HttpServletRequest request = (HttpServletRequest)param.get();
        String username = request.getParameter("un");
        int page = Integer.parseInt(request.getParameter("p"));

        GameBaiService service = new GameBaiServiceImpl();
        try {
            List<LogMoneyUserResponse> results = service.getLSXD(username, page);
            response.setTotalPages(5);
            response.setTransactions(results);
            response.setSuccess(true);
            response.setErrorCode("0");
        }
        catch (Exception e) {
            this.logger.error((Object)"LS XD Error: ", (Throwable)e);
            return e.getMessage();
        }
        return response.toJson();
    }
}
