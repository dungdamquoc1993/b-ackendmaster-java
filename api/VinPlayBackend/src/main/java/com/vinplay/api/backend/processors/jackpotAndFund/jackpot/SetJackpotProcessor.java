package com.vinplay.api.backend.processors.jackpotAndFund.jackpot;

import com.vinplay.dal.service.SlotMachineService;
import com.vinplay.dal.service.impl.SlotMachineServiceImpl;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.utils.TelegramUtil;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.enums.Games;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class SetJackpotProcessor implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger("backend");

    public String execute(Param<HttpServletRequest> param) {
        ResultSetJackpotResponse response = new ResultSetJackpotResponse(false, "1001");
        HttpServletRequest request = param.get();
        String nickName = request.getParameter("nickName");
        String IDgame = request.getParameter("gameID");
        String betValue = request.getParameter("betValue");
        String actionInfo = request.getParameter("action");
        if(nickName == null || IDgame == null || betValue == null || actionInfo == null){
            return response.toJson();
        }
        try{
            int bet = Integer.parseInt(betValue);
            int gameID = Integer.parseInt(IDgame);
            int action = Integer.parseInt(actionInfo);
			// notification
			if ("pro".equals(GameThirdPartyInit.enviroment)) {
				TelegramUtil.warningCheat(
						"Setting config , action = " + action + " , betValue = " + bet + ", gameID=" + gameID);
			}
            if(action == ActionJackpot.ACTION_INSERT ){
                SlotMachineService slotMachineService = new SlotMachineServiceImpl();
                slotMachineService.setUserJackpotForGame(Games.getGameNameById(gameID),nickName,bet,gameID);
                response.jackpotUserData = new JackpotUserData(nickName, gameID, bet);
            }
            if(action == ActionJackpot.ACTION_DELETE ){
                SlotMachineService slotMachineService = new SlotMachineServiceImpl();
                slotMachineService.deleteUserJackpotForGame(Games.getGameNameById(gameID),nickName,bet);
                response.jackpotUserData = new JackpotUserData(nickName, gameID, bet);
            }
            response.setSuccess(true);
            response.setErrorCode("0");
        }catch (Exception e){
            e.printStackTrace();
            logger.debug(e);
        }
        return response.toJson();
    }
}
