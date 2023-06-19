package com.vinplay.payment.utils;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.dichvuthe.service.AlertService;
import com.vinplay.dichvuthe.service.impl.AlertServiceImpl;
import com.vinplay.payment.entities.MerchantInfo;
import com.vinplay.payment.service.PaymentService;
import com.vinplay.payment.service.impl.PaymentServiceImpl;
import com.vinplay.usercore.dao.GameConfigDao;
import com.vinplay.usercore.dao.impl.GameConfigDaoImpl;
import com.vinplay.usercore.logger.MoneyLogger;
import com.vinplay.usercore.utils.GameCommon;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.utils.DateTimeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.log4j.Logger;

public class PayUtils {
	private static final Logger LOGGER = Logger.getLogger(PayUtils.class);
	
    private static Map<String, MerchantInfo> merchantMap = new HashMap<String, MerchantInfo>();
    private static PaymentService pmService = new PaymentServiceImpl();
    private static AlertService alertSer = new AlertServiceImpl();
    private static List<String> receives = Arrays.asList("0347975421", "0965672296");
    public static final String TO_VIN = "0";
    public static final String FROM_VIN = "1";
    public static final int SUCCESS = 0;
    public static final int ERROR_SYSTEM = 1;
    public static final int MERCHANT_ID_INVALID = 2;
    public static final int CHECKSUM_INVALID = 3;
    public static final int PARAMS_INVALID = 4;
    public static final int NICKNAME_INVALID = 5;
    public static final int TOKEN_INVALID = 6;
    public static final int TOKEN_TIME_OUT = 7;
    public static final int NOT_ENOUGH_MONEY = 8;
    public static final int DUPLICATE_TRANS_ID = 9;
    public static final int LIMITED = 10;
    public static final int BLOCKED = 11;
    public static final int AMOUNT_INVALID = 12;

    //status payment
    public static final String STATUS_TRANS_PENDING = "pending";
    public static final String STATUS_TRANS_RECEIVED = "received";
    public static final String STATUS_TRANS_REJECT = "reject";
    public static final String STATUS_TRANS_ERROR = "error";
    public static final String STATUS_TRANS_SUCCESS = "success";  
    public static final String STATUS_TRANS_COMPLETED = "completed";

    public static void init() throws Exception {
        GameConfigDao dao = new GameConfigDaoImpl();
        JSONObject mcObj = new JSONObject(dao.getGameCommon("merchant"));
        JSONArray jArray = mcObj.getJSONArray("mc_info");
        if (jArray != null) {
            for (int i = 0; i < jArray.length(); ++i) {
                JSONObject jObj = jArray.getJSONObject(i);
                Iterator keys = jObj.keys();
                while (keys.hasNext()) {
                    String key = (String)keys.next();
                    JSONArray a = jObj.getJSONArray(key);
                    long moneyInDay = pmService.getTotalMoney(key, null, DateTimeUtils.getStartTimeToDay(), DateTimeUtils.getEndTimeToDay(), TO_VIN);
                    merchantMap.put(key, new MerchantInfo(key, a.getString(0), a.getString(1), a.getDouble(2), a.getDouble(3), a.getLong(4), a.getLong(5), a.getLong(6), a.getLong(7), moneyInDay, Calendar.getInstance().get(6), 0));
                }
            }
        }
    }

    public static boolean checkMerchantId(String merchantId) {
        return merchantMap.containsKey(merchantId);
    }

    public static boolean checkMerchantKey(String merchantId, String merchantKey) {
        return PayUtils.checkMerchantId(merchantId) && merchantMap.get(merchantId).getMerchantKey().equals(merchantKey);
    }

    public static MerchantInfo getMerchant(String merchantId) {
        if (PayUtils.checkMerchantId(merchantId)) {
            return merchantMap.get(merchantId);
        }
        return null;
    }

    public static String getMerchantKey(String merchantId) {
        if (PayUtils.checkMerchantId(merchantId)) {
            return merchantMap.get(merchantId).getMerchantKey();
        }
        return null;
    }

    public static boolean checkMoneyLimit(String nickname, MerchantInfo merchantInfo, long money, String type) throws Exception {
        if (type.equals(FROM_VIN)) {
            return false;
        }
        long moneySystemLimit = GameCommon.getValueLong(merchantInfo.getMerchantId() + "CASHOUT_LIMIT_SYSTEM");
        boolean isToday = Calendar.getInstance().get(6) == merchantInfo.getUpdateDay();
        long moneySystemInDay = isToday ? merchantInfo.getMoneyInDay() : 0L;
        try {
            if (isToday) {
                if (moneySystemInDay > Math.round((double)moneySystemLimit * 0.8) && merchantInfo.getNumAlertInDay() < 3) {
                    String content = "Canh bao merchant: " + merchantInfo.getMerchantId() + " sap vuot qua han muc doi Vin trong ngay. Use: " + moneySystemInDay + ". Limit: " + moneySystemLimit;
                    alertSer.alert2List(receives, content, false);
                    merchantInfo.setNumAlertInDay(merchantInfo.getNumAlertInDay() + 1);
                    merchantMap.put(merchantInfo.getMerchantId(), merchantInfo);
                }
            } else {
                merchantInfo.setNumAlertInDay(0);
                merchantMap.put(merchantInfo.getMerchantId(), merchantInfo);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (money + moneySystemInDay <= moneySystemLimit) {
            long moneyUserLimit = GameCommon.getValueLong(merchantInfo.getMerchantId() + "CASHOUT_LIMIT_USER");
            long moneyUserInDay = pmService.getTotalMoney(merchantInfo.getMerchantId(), nickname, DateTimeUtils.getStartTimeToDay(), DateTimeUtils.getEndTimeToDay(), type);
            if (money + moneyUserInDay <= moneyUserLimit) {
                return false;
            }
        }
        return true;
    }

    public static boolean updateMoneyMerchant(MerchantInfo merchantInfo, long money, String type) {
        if (type.equals(TO_VIN)) {
            int today = Calendar.getInstance().get(6);
            if (today == merchantInfo.getUpdateDay()) {
                merchantInfo.setMoneyInDay(merchantInfo.getMoneyInDay() + money);
            } else {
                merchantInfo.setMoneyInDay(money);
                merchantInfo.setUpdateDay(today);
            }
            merchantMap.put(merchantInfo.getMerchantId(), merchantInfo);
        }
        return true;
    }
	
    public static String requestAPIs(String url, String body) throws IOException {
    	HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		StringEntity params = new StringEntity(body);
		post.addHeader("content-type", "application/json");
		post.setEntity(params);
		StringBuffer result = new StringBuffer();
		String line = "";
		try {
			HttpResponse response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			return "{\"status\":2,\"message\":\"" + e.getMessage() + "\"}";
		}
		
		//JSONObject jsonObj = new JSONObject(result.toString());
		//{"errCode":"001","mess":"Not valid input: \"serial\" length must be at least 5 characters long","data":null}
		return result.toString();
	}
    
    public static String requestAPIs(String url, List<NameValuePair> param) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		HttpEntity postParams = new UrlEncodedFormEntity(param);
		httpPost.setEntity(postParams);
		CloseableHttpResponse httpResponse = null;
		BufferedReader reader = null;
		String inputLine;
		StringBuffer response = new StringBuffer();
		try {
			httpResponse = httpClient.execute(httpPost);
			System.out.println("POST Response Status:: " + httpResponse.getStatusLine().getStatusCode());
			reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			while ((inputLine = reader.readLine()) != null) {
				response.append(inputLine);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (reader != null)
				reader.close();
			if (httpClient != null)
				httpClient.close();
		}
		return response.toString();
	}

	public static String requestAPI(String url, List<NameValuePair> param) throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		post.setEntity(new UrlEncodedFormEntity(param));
		StringBuffer result = new StringBuffer();
		String line = "";
		try {
			HttpResponse response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			return "{\"status\":2,\"message\":\"" + e.getMessage() + "\"}";
		}
		
		//JSONObject jsonObj = new JSONObject(result.toString());
		return result.toString();
	}
	
	public static String requestGetAPI(String url) throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		StringBuffer result = new StringBuffer();
		String line = "";
		try {
			HttpResponse response = client.execute(get);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			return "{\"code\":2,\"message\":\"" + e.getMessage() + "\"}";
		}
		LOGGER.info("bank_list_oneclick" + result);
		return result.toString();
	}
	
	public static String getPayType(int paytype,String providerName) {
		switch (providerName) {
		case PaymentConstant.PROVIDER.PAYWELL:
			if (paytype==PaymentConstant.PayType.ONLINE.getKey()) {
				return "IB_ONLINE";
			} else {
				return "PW_OFFLINE";
			}
		case PaymentConstant.PROVIDER.CLICK_PAY:
			if (paytype==PaymentConstant.PayType.ONLINE.getKey()) {
				return "IB_ONLINE";
			} else {
				return "PW_OFFLINE";
			}
		case PaymentConstant.PROVIDER.PRINCE_PAY:
			if (paytype == PaymentConstant.PayType.ONLINE.getKey()) {
				return "907";
			}
			if (paytype == PaymentConstant.PayType.OFFLINE.getKey()) {
				return "908";
			}
			if (paytype == PaymentConstant.PayType.MOMO_DEP.getKey()) {
				return "923";
			}
			if (paytype == PaymentConstant.PayType.WITHDRAW.getKey()) {
				return "712";
			}
		case PaymentConstant.PROVIDER.MANUAL_BANK:
			if (paytype == PaymentConstant.PayType.OFFLINE.getKey()) {
				return "bank_recharge";
			}
			if (paytype == PaymentConstant.PayType.MOMO_DEP.getKey()) {
				return "momo_recharge";
			}
			if (paytype == PaymentConstant.PayType.ZALO_DEP.getKey()) {
				return "zalo_recharge";
			}
		default:
			return "";
		}
	}

	public static String getCurDate(String type) {
		if (type == null) {
			type = "yyyy-MM-dd HH:mm:ss";
		}
		try {
			String strdate = "";
			Date currentTime = new Date();
			SimpleDateFormat format0 = new SimpleDateFormat(type);
			strdate = format0.format(currentTime);
			return strdate;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static int ids = 100000;

	public synchronized static int getids() {
		ids++;
		if (ids > 999999) {
			ids = 100000;
		}
		return ids;
	}
	
	public static boolean validateRequest(String orderID) {
		Map<String, Long> mapCache = new ConcurrentHashMap<String, Long>();
	    if (mapCache.isEmpty()) {
	      long t1 = new java.util.Date().getTime();
	      mapCache.put(orderID, t1);
	    } else {
	      if (mapCache.containsKey(orderID)) {

	        long t1 = mapCache.get(orderID);
	        long t2 =  new java.util.Date().getTime();
	        if ((t2 - t1) > 1000 * 2) {
	          mapCache.put(orderID, t2);
	          return true;
	        } else {
	          return false;
	        }

	      } else {
	        long t1 =  new java.util.Date().getTime();
	        mapCache.put(orderID, t1);
	      }
	    }
	    
	    return true;
	  }
}

