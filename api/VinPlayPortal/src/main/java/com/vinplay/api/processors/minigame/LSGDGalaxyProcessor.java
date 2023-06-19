package com.vinplay.api.processors.minigame;

import com.vinplay.api.processors.minigame.response.LSGDGalaxyResponse;
import com.vinplay.dal.service.impl.GalaxyServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class LSGDGalaxyProcessor implements BaseProcessor<HttpServletRequest, String> {
    private Logger logger = Logger.getLogger((String)"api");

    public String execute(Param<HttpServletRequest> param) {
        LSGDGalaxyResponse response = new LSGDGalaxyResponse(false, "1001");
        HttpServletRequest request = (HttpServletRequest)param.get();
        String username = request.getParameter("un");
        int page = Integer.parseInt(request.getParameter("p"));
        int moneyType = Integer.parseInt(request.getParameter("mt"));
        GalaxyServiceImpl service = new GalaxyServiceImpl();
        try {
            List results = service.getLSGD(username, page, moneyType);
            response.setTotalPages(100);
            response.setResults(results);
            response.setSuccess(true);
            response.setErrorCode("0");
        }
        catch (Exception e) {
            this.logger.error((Object)"LSGD PokeGo Error: ", (Throwable)e);
            return e.getMessage();
        }
        return response.toJson();
    }
}
