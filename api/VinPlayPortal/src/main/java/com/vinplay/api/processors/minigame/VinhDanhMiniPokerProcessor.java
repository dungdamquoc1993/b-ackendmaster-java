/*
 * Decompiled with CFR 0.144.
 *
 * Could not load the following classes:
 *  com.vinplay.dal.entities.minipoker.VinhDanhMiniPoker
 *  com.vinplay.dal.service.impl.MiniPokerServiceImpl
 *  com.vinplay.vbee.common.cp.BaseProcessor
 *  com.vinplay.vbee.common.cp.Param
 *  javax.servlet.http.HttpServletRequest
 */
package com.vinplay.api.processors.minigame;

import com.vinplay.api.processors.minigame.response.VinhDanhMiniPokerResponse;
import com.vinplay.api.processors.slot.response.LSNoHuResponse;
import com.vinplay.dal.entities.minipoker.VinhDanhMiniPoker;
import com.vinplay.dal.service.SlotMachineService;
import com.vinplay.dal.service.impl.MiniPokerServiceImpl;
import com.vinplay.dal.service.impl.SlotMachineServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.enums.Games;
import com.vinplay.vbee.common.models.minigame.pokego.TopPokeGo;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class VinhDanhMiniPokerProcessor
implements BaseProcessor<HttpServletRequest, String> {
    public String execute(Param<HttpServletRequest> param) {
//        VinhDanhMiniPokerResponse response = new VinhDanhMiniPokerResponse(false, "1001");
//        HttpServletRequest request = (HttpServletRequest)param.get();
//        int moneyType = Integer.parseInt(request.getParameter("mt"));
//        int page = 1;
//        if (request.getParameter("p") != null) {
//            page = Integer.parseInt(request.getParameter("p"));
//        }

        LSNoHuResponse response = new LSNoHuResponse(false, "1001");
        HttpServletRequest request = (HttpServletRequest) param.get();
        String gameName = Games.MINI_POKER.getName();
        int page = Integer.parseInt(request.getParameter("p"));
        SlotMachineService service = new SlotMachineServiceImpl();
        List<TopPokeGo> results = service.getLogNoHu(gameName, page);
        response.setTotalPages(10);
        response.setResults(results);
        response.setSuccess(true);
        response.setErrorCode("0");
//        MiniPokerServiceImpl service = new MiniPokerServiceImpl();
//        try {
//            List results = service.getBangVinhDanh(moneyType, page);
//            response.setTotalPages(100);
//            response.setResults(results);
//            response.setSuccess(true);
//            response.setErrorCode("0");
//        }
//        catch (Exception e) {
//            return e.getMessage();
//        }
        return response.toJson();
    }
}

