/*
 * Decompiled with CFR 0.144.
 *
 * Could not load the following classes:
 *  com.vinplay.dal.service.impl.TaiXiuServiceImpl
 *  com.vinplay.vbee.common.cp.BaseProcessor
 *  com.vinplay.vbee.common.cp.Param
 *  com.vinplay.vbee.common.models.minigame.TopWin
 *  javax.servlet.http.HttpServletRequest
 */
package com.vinplay.api.processors.minigame;

import com.vinplay.api.processors.minigame.response.TopWinTXResponse;
import com.vinplay.dal.service.TaiXiuService;
import com.vinplay.dal.service.impl.OverUnderServiceImpl;
import com.vinplay.dal.service.impl.TaiXiuMD5ServiceImpl;
import com.vinplay.dal.service.impl.TaiXiuServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.models.minigame.TopWin;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class TopWinTXProcessor
        implements BaseProcessor<HttpServletRequest, String> {

    private static final Logger logger = Logger.getLogger((String)"vbee");

    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = (HttpServletRequest) param.get();
        int moneyType = Integer.parseInt(request.getParameter("mt"));
        String txType = request.getParameter("txType");
        String md5 = request.getParameter("md5");
        TopWinTXResponse response = new TopWinTXResponse(false, "1001");
        logger.debug("TopWinTXProcessor1 : md5: " + md5);
        if (txType == null || txType.equals("1")) {
            TaiXiuService service = new TaiXiuServiceImpl();
            logger.debug("TopWinTXProcessor2 : md5: " + md5);
            if (!TextUtils.isEmpty(md5) && md5.equals("1")) {
                service = new TaiXiuMD5ServiceImpl();
                logger.debug("TopWinTXProcessor3 : md5: " + md5);
            }
            try {
                List result = service.getTopWin(moneyType);
                response.setTopTX(result);
                response.setSuccess(true);
                response.setErrorCode("0");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            logger.debug("TopWinTXProcessor4 : md5: " + md5);
            OverUnderServiceImpl service = new OverUnderServiceImpl();
            try {
                List result = service.getTopWin(moneyType);
                response.setTopTX(result);
                response.setSuccess(true);
                response.setErrorCode("0");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return response.toJson();
    }
}

