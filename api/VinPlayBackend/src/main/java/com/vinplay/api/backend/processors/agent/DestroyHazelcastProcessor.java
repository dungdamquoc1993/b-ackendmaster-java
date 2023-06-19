/*
 * Decompiled with CFR 0.144.
 * 
 * Could not load the following classes:
 *  com.vinplay.usercore.service.impl.BlockUserServiceImpl
 *  com.vinplay.vbee.common.cp.BaseProcessor
 *  com.vinplay.vbee.common.cp.Param
 *  com.vinplay.vbee.common.response.ResultBlockUserResponse
 *  javax.servlet.http.HttpServletRequest
 */
package com.vinplay.api.backend.processors.agent;

import com.hazelcast.core.HazelcastInstance;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.response.BaseResponse;
import javax.servlet.http.HttpServletRequest;

public class DestroyHazelcastProcessor implements BaseProcessor<HttpServletRequest, String>{
	public String execute(Param<HttpServletRequest> param) {
        try {
        	HazelcastInstance hazelcast = HazelcastClientFactory.getInstance();
            hazelcast.getDistributedObjects()
            .forEach(distributedObject -> distributedObject.destroy());
            return new BaseResponse<Object>().success(null);
        }
        catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.error("1001", e.getMessage());
        }
        
    }
}

