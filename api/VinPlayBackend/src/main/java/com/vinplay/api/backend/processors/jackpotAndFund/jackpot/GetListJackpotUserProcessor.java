package com.vinplay.api.backend.processors.jackpotAndFund.jackpot;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.utils.TelegramUtil;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;

public class GetListJackpotUserProcessor implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger("backend");

    public String execute(Param<HttpServletRequest> param) {
        ResultGetJackpotResponse response = new ResultGetJackpotResponse(false, "1001");
        HttpServletRequest request = param.get();
        String nickName = request.getParameter("nickName");
		// notification
		if ("pro".equals(GameThirdPartyInit.enviroment)) {
			TelegramUtil.warningCheat("Setting nổ hũ , nickName = " + nickName);
		}
        try {
            boolean isCheckNickName = nickName != null && nickName.length() > 0;
            List<JackpotUserData> jackpotUserDataList = new ArrayList<>();
            HazelcastInstance client = HazelcastClientFactory.getInstance();
            IMap map = client.getMap("cacheSetUserJackpot");
            Set key = map.keySet();
            for (Object o : key) {
                String data = (String) o;
                String[] datas = data.split("_");
                if(datas.length <3) {
                    logger.debug(data);
                    continue;
                }
                int gameID = (int)map.get(data);
                int betValue = Integer.parseInt(datas[1]);
                String iNickName = datas[2];
                if(isCheckNickName){
                    if(iNickName.equals(nickName)){
                        jackpotUserDataList.add(new JackpotUserData(iNickName,gameID,betValue));
                    }
                }else{
                    jackpotUserDataList.add(new JackpotUserData(iNickName,gameID,betValue));
                }
            }
            response.listUserJackpot = jackpotUserDataList;
            response.setSuccess(true);
            response.setErrorCode("0");
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug(e);
        }

        return response.toJson();
    }
}
