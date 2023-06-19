package com.vinplay.api.backend.processors.jackpotAndFund.resultGame;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.api.backend.processors.jackpotAndFund.jackpot.JackpotUserData;
import com.vinplay.api.backend.processors.jackpotAndFund.jackpot.ResultGetJackpotResponse;
import com.vinplay.dal.service.BauCuaService;
import com.vinplay.dal.service.TaiXiuService;
import com.vinplay.dal.service.impl.BauCuaServiceImpl;
import com.vinplay.dal.service.impl.TaiXiuMD5ServiceImpl;
import com.vinplay.dal.service.impl.TaiXiuServiceImpl;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.utils.TelegramUtil;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.response.BaseResponseModel;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GetSetDataResultProcessor implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger("backend");

    public String execute(Param<HttpServletRequest> param) {
        BaseResponseModel response = new BaseResponseModel(false, "1001");
        HttpServletRequest request = param.get();
        String action = request.getParameter("action");
		// notification
		if ("pro".equals(GameThirdPartyInit.enviroment)) {
			TelegramUtil.warningCheat("Setting config , action = get kết quả tài xỉu /bầu cua");
		}
        try {
            int validateAction = Integer.parseInt(action);
            if(validateAction == ActionGetData.ACTION_TAI_XIU){
                logger.debug("get tai xiu ");
                TaiXiuService taiXiuService = new TaiXiuServiceImpl();
                response.setData(taiXiuService.getKetQuaTaiXiu());
            }

            if(validateAction == ActionGetData.ACTION_TAI_XIU_MD5){
                logger.debug("get tai xiu md5");
                TaiXiuService taiXiuService = new TaiXiuMD5ServiceImpl();
                response.setData(taiXiuService.getKetQuaTaiXiu());
            }
            if(validateAction == ActionGetData.ACTION_BAU_CUA){
                logger.debug("get bau cua ");
                BauCuaService bauCuaService = new BauCuaServiceImpl();
                response.setData(bauCuaService.getKetQuaBauCua());
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
