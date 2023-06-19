/*
 * Decompiled with CFR 0.144.
 *
 * Could not load the following classes:
 *  com.vinplay.dal.service.impl.TaiXiuServiceImpl
 *  com.vinplay.usercore.service.impl.UserExtraServiceImpl
 *  com.vinplay.vbee.common.cp.BaseProcessor
 *  com.vinplay.vbee.common.cp.Param
 *  com.vinplay.vbee.common.models.cache.ThanhDuTXModel
 *  javax.servlet.http.HttpServletRequest
 */
package com.vinplay.api.processors.minigame;

import com.vinplay.api.processors.minigame.response.TopThanhDuResponse;
import com.vinplay.dal.service.impl.OverUnderServiceImpl;
import com.vinplay.dal.service.impl.TaiXiuServiceImpl;
import com.vinplay.usercore.service.impl.UserExtraServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.models.cache.ThanhDuTXModel;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class TopThanhDuProcessor
        implements BaseProcessor<HttpServletRequest, String> {
    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = (HttpServletRequest) param.get();
        TopThanhDuResponse response = new TopThanhDuResponse(false, "1001");
        int type = Integer.parseInt(request.getParameter("type"));
        String txType = request.getParameter("txType");
        if (txType == null || txType.equals("1")) {
            TaiXiuServiceImpl service = new TaiXiuServiceImpl();
            try {
                if (request.getParameterMap().containsKey("date")) {
                    String dateStr = request.getParameter("date");
                    List results = service.getTopThanhDuDaily(dateStr, type);
                    if (type == 1) {
                        response.setTopThanhDu(results, TopThanhDuResponse.winPrizesDaily);
                    } else {
                        response.setTopThanhDu(results, TopThanhDuResponse.lossPrizesDaily);
                    }
                } else if (request.getParameterMap().containsKey("month")) {
                    String dateStr = request.getParameter("month");
                    List results = service.getTopThanhDuMonthly(dateStr, type);
                    UserExtraServiceImpl ser = new UserExtraServiceImpl();
                    String platform = ser.getPlatformFromToken(request.getParameter("at"));
                    if (type == 1) {
                        response.setTopThanhDu(results, TopThanhDuResponse.getWinPrizesMonthly(platform));
                    } else {
                        response.setTopThanhDu(results, TopThanhDuResponse.getLossPrizesMonthlyVin(platform));
                    }
                }
                response.setSuccess(true);
                response.setErrorCode("0");
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
        else{
            OverUnderServiceImpl service = new OverUnderServiceImpl();
            try {
                if (request.getParameterMap().containsKey("date")) {
                    String dateStr = request.getParameter("date");
                    List results = service.getTopThanhDuDaily(dateStr, type);
                    if (type == 1) {
                        response.setTopThanhDu(results, TopThanhDuResponse.winPrizesDaily);
                    } else {
                        response.setTopThanhDu(results, TopThanhDuResponse.lossPrizesDaily);
                    }
                } else if (request.getParameterMap().containsKey("month")) {
                    String dateStr = request.getParameter("month");
                    List results = service.getTopThanhDuMonthly(dateStr, type);
                    UserExtraServiceImpl ser = new UserExtraServiceImpl();
                    String platform = ser.getPlatformFromToken(request.getParameter("at"));
                    if (type == 1) {
                        response.setTopThanhDu(results, TopThanhDuResponse.getWinPrizesMonthly(platform));
                    } else {
                        response.setTopThanhDu(results, TopThanhDuResponse.getLossPrizesMonthlyVin(platform));
                    }
                }
                response.setSuccess(true);
                response.setErrorCode("0");
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }

        return response.toJson();
    }
}

