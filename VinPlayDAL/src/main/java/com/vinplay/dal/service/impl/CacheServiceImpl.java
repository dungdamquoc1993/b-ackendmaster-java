package com.vinplay.dal.service.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.dal.service.CacheService;
import com.vinplay.vbee.common.exceptions.KeyNotFoundException;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CacheServiceImpl implements CacheService {
    @Override
    public void setValue(String key, String value) {
        HazelcastInstance instance = HazelcastClientFactory.getInstance();
        IMap map = instance.getMap("cacheConfig");
        map.put((Object)key, (Object)value);
    }

    @Override
    public void setValue(String key, int value) {
        HazelcastInstance instance = HazelcastClientFactory.getInstance();
        IMap map = instance.getMap("cacheConfig");
        map.put((Object)key, (Object)String.valueOf(value));
    }

    @Override
    public String getValueStr(String key) throws KeyNotFoundException {
        HazelcastInstance instance = HazelcastClientFactory.getInstance();
        IMap map = instance.getMap("cacheConfig");
        if (map.containsKey((Object)key)) {
            return (String)map.get((Object)key);
        }
        throw new KeyNotFoundException();
    }

    @Override
    public int getValueInt(String key) throws KeyNotFoundException, NumberFormatException {
        HazelcastInstance instance = HazelcastClientFactory.getInstance();
        IMap map = instance.getMap("cacheConfig");
        if (map.containsKey((Object)key)) {
            return Integer.parseInt((String)map.get((Object)key));
        }
        throw new KeyNotFoundException();
    }

    @Override
    public boolean removeKey(String key) throws KeyNotFoundException {
        HazelcastInstance instance = HazelcastClientFactory.getInstance();
        IMap map = instance.getMap("cacheConfig");
        if (map.containsKey((Object)key)) {
            map.remove((Object)key);
            return true;
        }
        throw new KeyNotFoundException();
    }

    @Override
    public void setObject(String key, Object obj) {
        HazelcastInstance instance = HazelcastClientFactory.getInstance();
        IMap map = instance.getMap("cacheGameBai");
        map.put((Object)key, obj);
    }

    @Override
    public Object getObject(String key) throws KeyNotFoundException {
        HazelcastInstance instance = HazelcastClientFactory.getInstance();
        IMap map = instance.getMap("cacheGameBai");
        if (map.containsKey((Object)key)) {
            return map.get((Object)key);
        }
        throw new KeyNotFoundException();
    }

    @Override
    public Object removeObject(String key) throws KeyNotFoundException {
        HazelcastInstance instance = HazelcastClientFactory.getInstance();
        IMap map = instance.getMap("cacheGameBai");
        if (map.containsKey((Object)key)) {
            return map.remove((Object)key);
        }
        throw new KeyNotFoundException();
    }

    @Override
    public int getValueIntWithDefault(String key) {
        try{
            HazelcastInstance instance = HazelcastClientFactory.getInstance();
            IMap map = instance.getMap("cacheConfig");
            if (map.containsKey(key)) {
                return Integer.parseInt((String)map.get(key));
            }else{
                return -1;
            }
        }catch (Exception e){
            return -1;
        }
    }

    @Override
    public Map<String, Object> getBulk(Set<String> keys) {
        HazelcastInstance instance = HazelcastClientFactory.getInstance();
        IMap map = instance.getMap("cacheGameBai");
        return map.getAll(keys);
    }

    @Override
    public void setObject(String key, int expireTime, Object obj) {
        HazelcastInstance instance = HazelcastClientFactory.getInstance();
        IMap map = instance.getMap("cacheGameBai");
        map.put((Object)key, obj, (long)expireTime, TimeUnit.SECONDS);
    }

	@Override
	public void setValueJp(String key, Long value) {
		HazelcastInstance instance = HazelcastClientFactory.getInstance();
		IMap<String,Long> map = instance.getMap("cacheJackpot");
		map.put(key, value);
	}

	@Override
	public Long getValueJP(String key) throws KeyNotFoundException {
		HazelcastInstance instance = HazelcastClientFactory.getInstance();
        IMap<String,Long> map = instance.getMap("cacheJackpot");
        if (map.containsKey(key)) {
            return map.get(key);
        }
        throw new KeyNotFoundException();
	}
}

