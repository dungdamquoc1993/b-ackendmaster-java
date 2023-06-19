package com.vinplay.api.backend.processors.game3rd;

import java.lang.reflect.Type;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.dal.dao.LogFishDao;
import com.vinplay.dal.dao.impl.LogFishDaoImpl;
import com.vinplay.shotfish.entites.ShotfishConfig;
import com.vinplay.usercore.utils.CacheConfigName;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.response.BaseResponse;
import com.vinplay.vbee.common.statics.Consts;

public class FishLogSearchProcessor implements BaseProcessor<HttpServletRequest, String> {
    @Override
    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = param.get();
        String nickname = request.getParameter("nn");
        String fromTime = request.getParameter("ft");
        String endTime = request.getParameter("et");

        int page;
        try {
            page = Integer.parseInt(request.getParameter("pg"));
        } catch (NumberFormatException e) {
            page = 1;
        }

        LogFishDao dao = new LogFishDaoImpl();
        try {
        	if(nickname != null && !nickname.trim().isEmpty()) {
        		ShotfishConfig config = getConfig();
        		nickname = config == null ? nickname : config.prefix + nickname;
        	}
        	
        	Map<String, Object> data = dao.search(nickname, fromTime, endTime, page);
        	int totalRecord = (int) data.get("totalRecord");
            return BaseResponse.success(data, totalRecord);
        }
        catch (Exception e) {
            return BaseResponse.error("-1", e.getMessage());
        }
    }
    
    private ShotfishConfig getConfig() {
		try {
			HazelcastInstance instance = HazelcastClientFactory.getInstance();
			IMap<String, String> configCache = instance.getMap(Consts.CACHE_CONFIG);
			String value = configCache.get(CacheConfigName.SHOTFISHCONFIGCACHE).toString();
			Type type = new TypeToken<ShotfishConfig>() {}.getType();
			ShotfishConfig shotfishConfig = new Gson().fromJson(value, type);
			return shotfishConfig;
		} catch (Exception e) {
			return null;
		}
	}
}