package com.vinplay.payment.service.impl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.payment.entities.BankConfig;
import com.vinplay.payment.entities.BankOneClick;
import com.vinplay.payment.entities.PaymentConfig;
import com.vinplay.payment.entities.Response;
import com.vinplay.payment.service.PaymentConfigService;
import com.vinplay.payment.service.RechargeOneClickPayService;
import com.vinplay.usercore.dao.impl.GameConfigDaoImpl;
import com.vinplay.usercore.utils.CacheConfigName;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.response.ResultGameConfigResponse;
import com.vinplay.vbee.common.statics.Consts;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.log4j.Logger;

public class PaymentConfigServiceImpl implements PaymentConfigService {
	private static final Logger logger = Logger.getLogger("PaymentConfigService");
	public static final String NAME = "payment_config";
	public static final String VERSION = "1.0.0";
	public static final String FLATFORM = "all";
	
//	/**	
//	 * Add configuration payment
//	 * @param paymentConfigs
//	 * @return Response
//	 */
//	@Override
//	public Response add(List<PaymentConfig> paymentConfigs) {
//		Response res = new Response(1, "");
//		try {
//			GameConfigDaoImpl gameConfigDao = new GameConfigDaoImpl();
//			List<ResultGameConfigResponse> configs = gameConfigDao.getGameConfigAdmin(NAME, FLATFORM);
//			if (configs.size() > 0 && configs != null) {
//				res.setData("Config: " + NAME + " exist");
//				return res;
//			}
//			HazelcastInstance instance = HazelcastClientFactory.getInstance();
//			IMap<String, String> paymentConfigCache = instance.getMap(MAPCACHE);
//			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//			String json = ow.writeValueAsString(paymentConfigs);
//			Boolean result = gameConfigDao.createGameConfig(NAME, json, VERSION, FLATFORM);
//			if (result) {
//				if (!paymentConfigCache.containsKey(CacheConfigName.PAYMENTCONFIGCACHE)) {
//					paymentConfigCache.put(CacheConfigName.PAYMENTCONFIGCACHE, json);
//					res.setData("Update config " + NAME + " payment success");
//					return res;
//				}
//				
//				paymentConfigCache.lock(CacheConfigName.PAYMENTCONFIGCACHE);
//				paymentConfigCache.put(CacheConfigName.PAYMENTCONFIGCACHE, json);
//				paymentConfigCache.unlock(CacheConfigName.PAYMENTCONFIGCACHE);
//				res.setData("Add config failed");
//				return res;
//			}
//
//			res.setCode(0);
//			res.setData("Add payment config success");
//			return res;
//		} catch (Exception e) {
//			logger.debug(e);
//			res.setData("Add payment config failed");
//			return res;
//		}
//	}
//	
//	/**
//	 * Add provider config
//	 * @param paymentConfig
//	 * @return Response
//	 */
//	@Override
//	public Response addProvider(PaymentConfig paymentConfig) {
//		Response res = new Response(1, "");
//		try {			
//			GameConfigDaoImpl gameConfigDao = new GameConfigDaoImpl();
//			List<PaymentConfig> paymentConfigs = new ArrayList<PaymentConfig>();
//			List<ResultGameConfigResponse> configs = gameConfigDao.getGameConfigAdmin(NAME, FLATFORM);
//			if (configs.size() == 0 || configs == null) {
//				paymentConfigs.add(paymentConfig);
//				return add(paymentConfigs);
//			}
//			
//			paymentConfigs.add(paymentConfig);
//			return update(paymentConfigs);
//		} catch (Exception e) {
//			logger.debug(e);
//			res.setData("Add provider config: " + paymentConfig.getName() + " failed");
//			return res;
//		}
//	}
	
	/**
	 * Update configuration
	 * @param name
	 * @param value
	 * @param version
	 * @param platForm
	 * @return Response
	 */
//	@Override
//	public Response update(List<PaymentConfig> paymentConfigs) {
//		Response res = new Response(1, "");
//		try {
//			GameConfigDaoImpl gameConfigDao = new GameConfigDaoImpl();
//			List<ResultGameConfigResponse> configs = gameConfigDao.getGameConfigAdmin(NAME, FLATFORM);
//			if (configs.size() == 0 || configs == null) {
//				res.setData("Cannot find config: " + NAME);
//				return res;
//			}
//			
//			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//			String json = ow.writeValueAsString(paymentConfigs);
//			//TODO: Write data to database
//			if (!gameConfigDao.updateGameConfig(String.valueOf(configs.get(0).id), json, VERSION, FLATFORM)) {
//				res.setData("Update "+ NAME + " config failed");
//				return res;
//			}
//			
//			//TODO: Write data to hazelcast
//			HazelcastInstance instance = HazelcastClientFactory.getInstance();
//			IMap<String, String> paymentConfigCache = instance.getMap(MAPCACHE);
//			if (!paymentConfigCache.containsKey(CacheConfigName.PAYMENTCONFIGCACHE)) {
//				paymentConfigCache.put(CacheConfigName.PAYMENTCONFIGCACHE, json);
//				res.setData("Update config payment success");
//				return res;
//			}
//
//			paymentConfigCache.lock(CacheConfigName.PAYMENTCONFIGCACHE);
//			paymentConfigCache.put(CacheConfigName.PAYMENTCONFIGCACHE, json);
//			paymentConfigCache.unlock(CacheConfigName.PAYMENTCONFIGCACHE);
//			res.setData("Update config payment success");
//			return res;
//		} catch (Exception e) {
//			logger.debug(e);
//			res.setData("Update config payment failed");
//			return res;
//		}
//	}
//	
//	/**
//	 * Update configuration
//	 * @param name
//	 * @param value
//	 * @param version
//	 * @param platForm
//	 * @return Response
//	 */
//	@Override
//	public Response updateProvider(PaymentConfig paymentConfig) {
//		Response res = new Response(1, "");
//		try {
//			GameConfigDaoImpl gameConfigDao = new GameConfigDaoImpl();
//			List<ResultGameConfigResponse> configs = gameConfigDao.getGameConfigAdmin(NAME, FLATFORM);
//			if (configs.size() == 0 || configs == null) {
//				res.setData("Config: " + NAME + " not found");
//				return res;
//			}
//			
//			ResultGameConfigResponse gameConfig = configs.get(0);
//			String json = gameConfig.value;
//			Type listType = new TypeToken<List<PaymentConfig>>() {}.getType();
//			List<PaymentConfig> paymentConfigs = new Gson().fromJson(json, listType);
//		    int index = IntStream.range(0, paymentConfigs.size())
//		    			.filter(i -> paymentConfigs.get(i).getName().equals(paymentConfig.getName()))
//		    			.findFirst()
//		    			.orElse(-1);
//		    
//		    if(index != -1) {
//		    	paymentConfigs.set(index, paymentConfig);
//		    }
//		    
//		    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//			json = ow.writeValueAsString(paymentConfigs);
//			if (!gameConfigDao.updateGameConfig(String.valueOf(gameConfig.id), json, VERSION, FLATFORM)) {
//				res.setData("Update config provider " + paymentConfig.getName() + " failed");
//				return res;
//			}
//
//			//TODO: Write data to hazelcast
//			HazelcastInstance instance = HazelcastClientFactory.getInstance();
//			IMap<String, String> paymentConfigCache = instance.getMap(MAPCACHE);
//			if (!paymentConfigCache.containsKey(CacheConfigName.PAYMENTCONFIGCACHE)) {
//				paymentConfigCache.put(CacheConfigName.PAYMENTCONFIGCACHE, json);
//				res.setData("Update config provider " + paymentConfig.getName() + " success");
//				return res;
//			}
//
//			paymentConfigCache.lock(CacheConfigName.PAYMENTCONFIGCACHE);
//			paymentConfigCache.put(CacheConfigName.PAYMENTCONFIGCACHE, json);
//			paymentConfigCache.unlock(CacheConfigName.PAYMENTCONFIGCACHE);
//			res.setData("Update config provider " + paymentConfig.getName() + " success");
//			return res;
//		} catch (Exception e) {
//			logger.debug(e);
//			res.setData("Update config provider " + paymentConfig.getName() + " failed");
//			return res;
//		}
//	}
//	
//	/**
//	 * Delete config provider
//	 * @param key
//	 * @return Response
//	 */
//	@Override
//	public Response deleteProvider(String key) {
//		Response res = new Response(1, "");
//		try {
//			GameConfigDaoImpl gameConfigDao = new GameConfigDaoImpl();
//			List<ResultGameConfigResponse> configs = gameConfigDao.getGameConfigAdmin(NAME, FLATFORM);
//			if (configs.size() == 0 || configs == null) {
//				res.setData("Config: " + NAME + " not found");
//				return res;
//			}
//			
//			ResultGameConfigResponse gameConfig = configs.get(0);
//			String json = gameConfig.value;
//			Type listType = new TypeToken<List<PaymentConfig>>() {}.getType();
//			List<PaymentConfig> paymentConfigs = new Gson().fromJson(json, listType);
//		    int index = IntStream.range(0, paymentConfigs.size())
//		    			.filter(i -> paymentConfigs.get(i).getName().equals(key))
//		    			.findFirst()
//		    			.orElse(-1);
//		    
//		    if(index != -1) {
//		    	res.setData("Config provider: " + key + " not found");
//		    }
//		    
//		    paymentConfigs.remove(index);
//		    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//			json = ow.writeValueAsString(paymentConfigs);
//			if (!gameConfigDao.updateGameConfig(String.valueOf(gameConfig.id), json, VERSION, FLATFORM)) {
//				res.setData("Delete config provider " + key + " failed");
//				return res;
//			}
//
//			//TODO: Write data to hazelcast
//			HazelcastInstance instance = HazelcastClientFactory.getInstance();
//			IMap<String, String> paymentConfigCache = instance.getMap(MAPCACHE);
//			if (!paymentConfigCache.containsKey(CacheConfigName.PAYMENTCONFIGCACHE)) {
//				paymentConfigCache.put(CacheConfigName.PAYMENTCONFIGCACHE, json);
//				res.setData("Delete config provider " + key + " success");
//				return res;
//			}
//
//			paymentConfigCache.lock(CacheConfigName.PAYMENTCONFIGCACHE);
//			paymentConfigCache.put(CacheConfigName.PAYMENTCONFIGCACHE, json);
//			paymentConfigCache.unlock(CacheConfigName.PAYMENTCONFIGCACHE);
//			res.setData("Delete config provider " + key + " success");
//			return res;
//		} catch (Exception e) {
//			logger.debug(e);
//			res.setData("Delete config provider " + key + " failed");
//			return res;
//		}
//	}
	
	/**
	 * Get all config payment
	 * @return List<PaymentConfig>
	 */
	public static String initPayment() throws IOException {
		StringBuilder fullStr = new StringBuilder();
		try (BufferedReader br = Files.newBufferedReader(Paths.get(Consts.PAYMENT_CONFIG_FILE))) {
			String line;
			while ((line = br.readLine()) != null) {
				fullStr.append(line);
			}
		}
		return fullStr.toString();
	}
	@Override
	public List<PaymentConfig> getConfig() {
		//convert string json to List
		try {
			HazelcastInstance instance = HazelcastClientFactory.getInstance();
			IMap<String, String> configCache = instance.getMap(Consts.CACHE_CONFIG);
			String configPayment = configCache.get(CacheConfigName.PAYMENTCONFIGCACHE);
			if(configPayment==null ||"".equals(configPayment)) {
				configPayment = initPayment();
			}
			Type listType = new TypeToken<List<PaymentConfig>>() {}.getType();
			List<PaymentConfig> paymentConfigs = new Gson().fromJson(configPayment, listType);
		    return paymentConfigs;
		} catch (Exception e) {
			logger.debug(e);
			return null;
		}
	}
	
	/**
	 * Get config provider by key
	 * @param key
	 * @return PaymentConfig
	 */
	@Override
	public PaymentConfig getConfigByKey(String key) {
		try {
			HazelcastInstance instance = HazelcastClientFactory.getInstance();
			IMap<String, String> paymentConfigCache = instance.getMap(Consts.CACHE_CONFIG);
			String value = paymentConfigCache.get(CacheConfigName.PAYMENTCONFIGCACHE).toString();
			Type listType = new TypeToken<List<PaymentConfig>>() {}.getType();
			List<PaymentConfig> paymentConfigs = new Gson().fromJson(value, listType);
			return paymentConfigs.stream().filter(item -> item.getName().equals(key)).findFirst().orElse(null);
		} catch (Exception e) {
			logger.debug(e);
			return null;
		}
	}

	/**
	 * Get a bit information of configuration (minMoney & payType)
	 * @param name
	 * @param key
	 * @return Response
	 */
	@Override
	public Response getConfig(String key) {
		Response res = new Response(1, "");
		try {
			List<PaymentConfig> paymentConfigs = getConfig();
			if(paymentConfigs == null || paymentConfigs.size() == 0) {
				res.setData("Config: " + NAME + " not found");
				return res;
			}
			
			PaymentConfig paymentConfig = paymentConfigs.stream().filter(item -> item.getName().equals(key)).findFirst().orElse(null);
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String payTypes = ow.writeValueAsString(paymentConfig.getConfig().getPayType());
			String data = "{\"minMoney\":" + paymentConfig.getConfig().getMinMoney() + "},";
			data = data + "{\"payType\":" + payTypes + "}";
			res.setData(data.toString());
			res.setCode(0);
			return res;
		} catch (Exception e) {
			logger.debug(e);
			res.setData("Get config provider " + key + " failed");
			return res;
		}
	}
	
	/**
	 * Get list bank by name of configuration and key(provider name. Ex: paywell, royalpay,..)
	 * @param name
	 * @param key
	 * @return Response
	 */
	@Override
	public Response getBanks(String key) {
		Response res = new Response(1, "");
		try {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			List<PaymentConfig> paymentConfigs = getConfig();
			if(paymentConfigs == null || paymentConfigs.size() == 0) {
				res.setData("Config: " + NAME + " not found");
				return res;
			}
			
			PaymentConfig paymentConfig = paymentConfigs.stream().filter(item -> item.getName().equals(key)).findFirst().orElse(null);
			List<BankConfig> bankConfigs = paymentConfig.getConfig().getBanks();
			if(key.equals("clickpay")) {
				RechargeOneClickPayService rechargeOneClickPayService = new RechargeOneClickPayServiceImpl();
				List<BankOneClick> banks = rechargeOneClickPayService.getListBankSupport();
				if(banks != null) {
					for (int i = 0; i < banks.size(); i++) {
						BankOneClick bankOneClick = banks.get(i);
						int index = IntStream.range(0, bankConfigs.size())
				    			.filter(j -> bankConfigs.get(j).getName().equalsIgnoreCase(bankOneClick.bank_name))
				    			.findFirst()
				    			.orElse(-1);
						if(index != -1) {
							bankConfigs.get(index).setStatus(1);
							bankConfigs.get(index).setIsWithdraw(1);
					    }
					}
				}
			}
			
			String json = ow.writeValueAsString(bankConfigs);
			res.setData(json.toString());
			res.setCode(0);
			return res;
		} catch (Exception e) {
			logger.debug(e);
			res.setData("Get banks config provider " + key + " failed");
			return res;
		}
	}
	
	/**
	 * Get list bank of provider support with draw by bank name
	 * @param bankName
	 * @param isWithdraw : 0-Not support;1-Support
	 * @return
	 */
	@Override
	public Response getBankWithdraw(String bankName, Integer isWithdraw) {
		Response res = new Response(1, "");
		try {
			Map<String,List<BankConfig>> result = new HashMap<String,List<BankConfig>>();
			List<PaymentConfig> paymentConfigs = getConfig();
			for (int i = 0; i < paymentConfigs.size(); i++) {
				List<BankConfig> bankConfigs = paymentConfigs.get(i).getConfig().getBanks()
												.stream().filter(x->x.getIsWithdraw() == isWithdraw && x.getName().equals(bankName))
												.collect(Collectors.toList());
				if(bankConfigs.size() > 0) {
					result.put(paymentConfigs.get(i).getName(), bankConfigs);
				}
			}
			
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(result);
			res.setData(json.toString());
			res.setCode(0);
			return res;
		} catch (Exception e) {
			logger.debug(e);
			res.setData(null);
			return res;
		}
	}
}
