package com.vinplay.api.backend.processors.jackpotAndFund.resultGame;

import javax.servlet.http.HttpServletRequest;

import com.vinplay.dal.service.impl.TaiXiuMD5ServiceImpl;
import org.apache.log4j.Logger;

import com.vinplay.dal.service.BauCuaService;
import com.vinplay.dal.service.TaiXiuService;
import com.vinplay.dal.service.impl.BauCuaServiceImpl;
import com.vinplay.dal.service.impl.TaiXiuServiceImpl;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.utils.TelegramUtil;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponseModel;

public class SetResultGameProcessor implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger("backend");

    public String execute(Param<HttpServletRequest> param) {
        BaseResponseModel response = new BaseResponseModel(false, "1001");
        HttpServletRequest request = param.get();
        String data = request.getParameter("result");
        String action = request.getParameter("action");

        String[] sDice = data.split(",");

        short[] dice = new short[sDice.length];

        // notification
        if ("pro".equals(GameThirdPartyInit.enviroment)) {
            TelegramUtil.warningCheat("Setting config , action = set kết quả tài xỉu /bầu cua");
        }
        logger.debug("sDice  " + sDice.length + " data   " + data + " dice " + dice.length);
        try {
            int validateAction = Integer.parseInt(action);
            for (int i = 0; i < sDice.length; i++) {
                dice[i] = Short.parseShort(sDice[i]);
            }

            if (validateAction == ActionGetData.ACTION_TAI_XIU) {
                logger.debug("set tai xiu " + data);
                TaiXiuService taiXiuService = new TaiXiuServiceImpl();
                taiXiuService.setKetQuaTaiXiu(dice);
            }

            if (validateAction == ActionGetData.ACTION_TAI_XIU_MD5) {
                logger.debug("set tai xiu md5" + data);
                TaiXiuService taiXiuService = new TaiXiuMD5ServiceImpl();
                taiXiuService.setKetQuaTaiXiu(dice);
            }

            if (validateAction == ActionGetData.ACTION_BAU_CUA) {
                logger.debug("set bau cua " + data);
                BauCuaService bauCuaService = new BauCuaServiceImpl();
                bauCuaService.setKetQuaBauCua(dice);
            }
            response.setSuccess(true);
            response.setData(dice);
            response.setErrorCode("0");
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug(e);
        }
        return response.toJson();
    }
}
