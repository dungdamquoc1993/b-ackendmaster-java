/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.hazelcast.core.IMap
 *  com.vinplay.vbee.common.cp.BaseProcessor
 *  com.vinplay.vbee.common.cp.Param
 *  com.vinplay.vbee.common.hazelcast.HazelcastUtils
 *  com.vinplay.vbee.common.messages.BaseMessage
 *  com.vinplay.vbee.common.messages.FreezeMoneyMessage
 *  com.vinplay.vbee.common.models.cache.UserActiveModel
 *  org.apache.log4j.Logger
 */
package com.vinplay.vbee.rmq.payment.processor;

import com.hazelcast.core.IMap;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.hazelcast.HazelcastUtils;
import com.vinplay.vbee.common.messages.BaseMessage;
import com.vinplay.vbee.common.messages.FreezeMoneyMessage;
import com.vinplay.vbee.common.models.cache.UserActiveModel;
import com.vinplay.vbee.dao.impl.MoneyInGameDaoImpl;
import java.util.Date;
import org.apache.log4j.Logger;

public class FreezeMoneyInGameProcessor
implements BaseProcessor<byte[], Boolean> {
    private static final Logger logger = Logger.getLogger((String)"vbee");

    public Boolean execute(Param<byte[]> param) {
        byte[] body = (byte[])param.get();
        FreezeMoneyMessage message = (FreezeMoneyMessage)BaseMessage.fromBytes((byte[])body);
        String nickName = message.getNickname();
        try {
            block8 : {
                IMap<String, UserActiveModel> userMap = HazelcastUtils.getActiveMap(nickName);
                long processId = 0L;
                if (userMap.containsKey(nickName)) {
                    try {
                        userMap.lock(nickName);
                        UserActiveModel user = (UserActiveModel)userMap.get(nickName);
                        processId = user.getLastMessageId();
                        if (Long.parseLong(message.getId()) < processId) {
                            return false;
                        }
                        user.setLastActive(new Date().getTime());
                        user.setLastMessageId(Long.parseLong(message.getId()));
                        userMap.put(nickName, user);
                    }
                    catch (Exception e) {
                        logger.error(e);
                        break block8;
                    }finally {
                    	userMap.unlock(nickName);
					}
                }
            }
            MoneyInGameDaoImpl dao = new MoneyInGameDaoImpl();
            dao.freezeMoneyInGame(message);
            return true;
        }
        catch (Exception e2) {
            logger.error(e2);
            return false;
        }
    }
}

