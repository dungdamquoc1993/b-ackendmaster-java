package com.vinplay.api.backend.processors.jackpotAndFund.resultGame;

import com.vinplay.api.backend.processors.jackpotAndFund.jackpot.ResultSetJackpotResponse;
import com.vinplay.dal.service.BauCuaService;
import com.vinplay.dal.service.TaiXiuService;
import com.vinplay.dal.service.impl.BauCuaServiceImpl;
import com.vinplay.dal.service.impl.TaiXiuMD5ServiceImpl;
import com.vinplay.dal.service.impl.TaiXiuServiceImpl;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.utils.TelegramUtil;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class DeleteSetDataResultProcessor implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger("backend");

    public String execute(Param<HttpServletRequest> param) {
        ResultGetSetResultResponse response = new ResultGetSetResultResponse(false, "1001");
        HttpServletRequest request = param.get();
        String action = request.getParameter("action");
		// notification
		if ("pro".equals(GameThirdPartyInit.enviroment)) {
			TelegramUtil.warningCheat("Setting config , action =  DeleteS kết quả tài xỉu /bầu cua");
		}
        try {
            int validateAction = Integer.parseInt(action);
            if(validateAction == ActionGetData.ACTION_TAI_XIU){
                TaiXiuService taiXiuService = new TaiXiuServiceImpl();
                taiXiuService.suaKetQuaTaiXiu();
            }
            if(validateAction == ActionGetData.ACTION_BAU_CUA){
                BauCuaService bauCuaService = new BauCuaServiceImpl();
                bauCuaService.layKetQuaBauCua();
            }
            if(validateAction == ActionGetData.ACTION_TAI_XIU_MD5){
                TaiXiuService taiXiuService = new TaiXiuMD5ServiceImpl();
                taiXiuService.suaKetQuaTaiXiu();
            }
            response.setSuccess(true);
            response.setErrorCode("0");
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug(e);
        }

        return response.toJson();
    }
}
