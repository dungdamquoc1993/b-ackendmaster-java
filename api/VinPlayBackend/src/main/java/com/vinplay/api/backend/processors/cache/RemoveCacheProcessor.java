package com.vinplay.api.backend.processors.cache;

import com.hazelcast.core.IMap;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class RemoveCacheProcessor implements BaseProcessor<HttpServletRequest, String> {
	private static final Logger logger = Logger.getLogger((String) "api");

	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = (HttpServletRequest) param.get();
		String key = request.getParameter("key");
		try {
			IMap userMap = HazelcastClientFactory.getInstance().getMap(key);
			if (userMap != null && !userMap.isEmpty()) {
				userMap.remove(key);
			}
		} catch (Exception e) {
			logger.debug((Object) e);
		}
		return "";
	}
}
