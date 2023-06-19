package com.vinplay.vbee.rmq.payment.processor;

import com.hazelcast.core.IMap;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.hazelcast.HazelcastUtils;
import com.vinplay.vbee.common.messages.BaseMessage;
import com.vinplay.vbee.common.messages.MoneyMessageInGame;
import com.vinplay.vbee.common.models.cache.UserActiveModel;
import com.vinplay.vbee.dao.impl.MoneyInGameDaoImpl;
import java.util.Date;
import org.apache.log4j.Logger;

public class UpdateMoneyInGameProcessor
implements BaseProcessor<byte[], Boolean> {
    private static final Logger logger = Logger.getLogger("vbee");

    public Boolean execute(Param<byte[]> param) {
        byte[] body = param.get();
        MoneyMessageInGame message = (MoneyMessageInGame)BaseMessage.fromBytes(body);
        try {
            block8 : {
                IMap<String, UserActiveModel> userMap = HazelcastUtils.getActiveMap(message.getNickname());
                long processId = 0L;
                if (userMap.containsKey(message.getNickname())) {
                    try {
                        userMap.lock(message.getNickname());
                        UserActiveModel user = (UserActiveModel)userMap.get(message.getNickname());
                        processId = user.getLastMessageId();
                        if (Long.parseLong(message.getId()) < processId) {
                            return false;
                        }
                        user.setLastActive(new Date().getTime());
                        user.setLastMessageId(Long.parseLong(message.getId()));
                        userMap.put(message.getNickname(), user);
                    }
                    catch (Exception e) {
                        logger.error(e);
                        e.printStackTrace();
                        break block8;
                    }finally {
                    	userMap.unlock(message.getNickname());
					}
                }
            }
            MoneyInGameDaoImpl dao = new MoneyInGameDaoImpl();
            dao.updateMoneyInGame(message);
            return true;
        }
        catch (Exception e2) {
            e2.printStackTrace();
            logger.error(e2);
            return false;
        }
    }
}

