package com.vinplay.api.processors.minigame;

import com.vinplay.api.processors.minigame.response.TopGalaxyResponse;
import com.vinplay.dal.service.impl.GalaxyServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class TopGalaxyProcessor implements BaseProcessor<HttpServletRequest, String> {
    public String execute(Param<HttpServletRequest> param) {
        TopGalaxyResponse response = new TopGalaxyResponse(false, "1001");
        HttpServletRequest request = (HttpServletRequest)param.get();
        int moneyType = Integer.parseInt(request.getParameter("mt"));
        int page = Integer.parseInt(request.getParameter("p"));
        GalaxyServiceImpl service = new GalaxyServiceImpl();
        List results = service.getTopGalaxy(moneyType, page);
        response.setTotalPages(100);
        response.setResults(results);
        response.setSuccess(true);
        response.setErrorCode("0");
        return response.toJson();
    }
}
