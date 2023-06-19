/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.hazelcast.core.HazelcastInstance
 *  com.hazelcast.core.IMap
 *  com.vinplay.vbee.common.cp.BaseProcessor
 *  com.vinplay.vbee.common.cp.Param
 *  com.vinplay.vbee.common.hazelcast.HazelcastClientFactory
 *  com.vinplay.vbee.common.messages.BaseMessage
 *  com.vinplay.vbee.common.messages.LogMoneyUserMessage
 *  com.vinplay.vbee.common.models.commission.AgentCommissionModel
 *  com.vinplay.vbee.common.utils.DateTimeUtils
 *  org.apache.log4j.Logger
 */
package com.vinplay.vbee.rmq.processor.commission;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.messages.BaseMessage;
import com.vinplay.vbee.common.messages.LogMoneyUserMessage;
import com.vinplay.vbee.common.models.commission.AgentCommissionModel;
import com.vinplay.vbee.common.utils.DateTimeUtils;
import com.vinplay.vbee.dao.impl.ExceptionDaoImpl;
import com.vinplay.vbee.dao.impl.UserDaoImpl;
import java.util.Date;
import org.apache.log4j.Logger;

public class CommissionAgentProcessor
implements BaseProcessor<byte[], Boolean> {
    private static final Logger logger = Logger.getLogger((String)"vbee");

    public Boolean execute(Param<byte[]> param) {
        block8 : {
            LogMoneyUserMessage message = (LogMoneyUserMessage)BaseMessage.fromBytes((byte[])((byte[])param.get()));
            try {
                if (message.isBot() || message.getFee() <= 0L || !message.getMoneyType().equals("vin") || !message.isVp()) break block8;
                HazelcastInstance client = HazelcastClientFactory.getInstance();
                IMap agentCommMap = client.getMap("cacheAgentCommission");
                AgentCommissionModel commission = new AgentCommissionModel();
                UserDaoImpl dao = new UserDaoImpl();
                long fee = 0L;
                String timeEnd = DateTimeUtils.getCurrentTime((String)"yyyy-MM-dd HH:mm:ss");
                String nickName =message.getNickname();
                if (agentCommMap.containsKey(nickName)) {
                    try {
                        if (Long.parseLong(message.getId()) < commission.getLastMessageId()) {
                            logger.error(("message kh\u00f4ng x\u1eed l\u00fd: " + message.getId()));
                            ExceptionDaoImpl ex = new ExceptionDaoImpl();
                            ex.insertLogExceptionDB(message.getMoneyType(), message.toJson(), "Message kh\u00f4ng x\u1eed l\u00fd, current process ID: " + commission.getLastMessageId());
                            return false;
                        }
                        agentCommMap.lock(nickName);
                        commission = (AgentCommissionModel)agentCommMap.get(nickName);
                        fee = commission.getFee() + message.getFee();
                        commission.setFee(fee);
                        commission.setLastActive(new Date());
                        commission.setLastMessageId(Long.parseLong(message.getId()));
                        agentCommMap.put(nickName, commission);
                    }
                    catch (Exception e) {
                        logger.error(e);
                        e.printStackTrace();
                        return false;
                    }finally {
                    	if(agentCommMap.isLocked(nickName)) {
                    		agentCommMap.unlock(nickName);
                    	}
                    	
					}
                    break block8;
                }
                fee = dao.getFeeUser(nickName, timeEnd);
                commission.setFee(fee);
                commission.setNickName(nickName);
                commission.setUserId(message.getUserId());
                commission.setLastActive(new Date());
                commission.setLastMessageId(Long.parseLong(message.getId()));
                agentCommMap.put(nickName, commission);
            }
            catch (Exception e2) {
                e2.printStackTrace();
                logger.error(e2);
            }
        }
        return false;
    }
}

