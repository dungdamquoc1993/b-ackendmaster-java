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

public class RestoreMoneyInGameProcessor
implements BaseProcessor<byte[], Boolean> {
    private static final Logger logger = Logger.getLogger("vbee");

    public Boolean execute(Param<byte[]> param) {
        byte[] body = param.get();
        FreezeMoneyMessage message = (FreezeMoneyMessage)BaseMessage.fromBytes(body);
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
                        break block8;
                    }finally {
                    	userMap.unlock(message.getNickname());
					}
                }
            }
            MoneyInGameDaoImpl dao = new MoneyInGameDaoImpl();
            dao.restoreMoneyInGame(message);
            return true;
        }
        catch (Exception e2) {
            logger.error(e2);
            return false;
        }
    }
}

