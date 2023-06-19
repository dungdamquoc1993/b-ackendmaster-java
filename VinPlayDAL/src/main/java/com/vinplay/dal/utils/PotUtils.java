package com.vinplay.dal.utils;

import com.hazelcast.core.IMap;
import com.vinplay.dal.dao.impl.PotDaoImpl;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.models.PotModel;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PotUtils {
	public static void init() throws SQLException, IOException {
		IMap<String,PotModel> potMap = HazelcastClientFactory.getInstance().getMap("huGameBai");
		PotDaoImpl dao = new PotDaoImpl();
		List<PotModel> listModel = dao.getAll();
		for (PotModel model : listModel) {
			if (potMap.containsKey(model.getPotName()))
				continue;
			potMap.put(model.getPotName(), model);
		}
	}
}
