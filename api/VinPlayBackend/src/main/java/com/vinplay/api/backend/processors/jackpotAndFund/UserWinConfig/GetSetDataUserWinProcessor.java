package com.vinplay.api.backend.processors.jackpotAndFund.UserWinConfig;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.vinplay.api.backend.response.BaseResponse;
import com.vinplay.dal.config.CacheConfig;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.utils.TelegramUtil;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.enums.Games;
import com.vinplay.vbee.common.hazelcast.HazelcastClientFactory;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.models.cache.UserCacheModel;
import com.vinplay.vbee.common.statics.Consts;

public class GetSetDataUserWinProcessor implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger("backend");
    
    public String execute(Param<HttpServletRequest> param) {
        BaseResponse response = new BaseResponse(false, "1001");
        HttpServletRequest request = param.get();
        String action = request.getParameter("action");
        String nickname = request.getParameter("nick_name");
		
		logger.info("GetSetDataUserWinProcessor action=" + action + " , nickname=" + nickname);
        try {
            int validateAction = Integer.parseInt(action);
            HazelcastInstance client = HazelcastClientFactory.getInstance();
			// notification
			if ("pro".equals(GameThirdPartyInit.enviroment) && validateAction == UserWinAction.SET_USER_WIN) {
				TelegramUtil.warningCheat("Setting user ti le thang cao , nickName = " + nickname);
			}
            //check exist
            IMap<String, UserModel> userMapNormal = client.getMap("users");
            if (validateAction != UserWinAction.GET_USER_WIN) {
            	
                if (!userMapNormal.containsKey(nickname)) {
                	return "Not Exist !";
                }
            }
            //check user type
            try {
            	IMap<String, Object> map = client.getMap(CacheConfig.CACHE_USER_WIN);
            	Map<String, Boolean> mapNew = new HashMap<>();
				if (map != null && !map.isEmpty()) {
					// convert to new
					map.forEach((k,v)->{
						if(v instanceof String) {
							mapNew.put(k, true);
							map.delete(k);
						}else if (v instanceof Boolean) {
							mapNew.put(k, (Boolean) v);
						}
					});
					
				}
				IMap<String, Boolean> mapfinal = client.getMap(CacheConfig.CACHE_USER_WIN);
				mapNew.forEach((k,v)->{
					mapfinal.put(k, v);
				});
			} catch (Exception e) {
				System.out.println(e);
			}
            IMap<String, Boolean> map = client.getMap(CacheConfig.CACHE_USER_WIN);
            if (validateAction == UserWinAction.GET_USER_WIN) {
            	Map<String, Boolean> res = new HashMap<>();
            	if(map!=null) {
            		map.forEach((k,v)->{
                		res.put(k, v);
                	});
            	}
                response.setData(res);
            }
            if (validateAction == UserWinAction.SET_USER_WIN) {
				// get param
				Integer usertype = Integer.parseInt(request.getParameter("ut"));
				// check usertype
				if (usertype != Consts.USER_TYPE.BOT && usertype != Consts.USER_TYPE.REAL
						&& usertype != Consts.USER_TYPE.TEST_SPORT && usertype != Consts.USER_TYPE.TEST_XD) {
					return response.toJson();
				}

				if (nickname != null) {
					map.set(nickname, true);
					// update to usertype
					try {
						userMapNormal.lock(nickname);
						UserCacheModel u = (UserCacheModel) userMapNormal.get(nickname);
						if(u.getDaily()==1) {
							response.setSuccess(true);
				            response.setErrorCode("0");
				            response.setMessage("This is an agent account");
				            return response.toJson();
						}
						u.setBanCashOut(false);
						u.setBanTransferMoney(false);
						u.setBanLogin(false);
						
						u.setUsertype(usertype);
						userMapNormal.put(nickname, u);

						UserService userService = new UserServiceImpl();
						userService.updateUserType(nickname, usertype);
					} finally {
						userMapNormal.unlock(nickname);
					}
                }
            }

			if (validateAction == UserWinAction.DELETE_USER_WIN) {
				if (nickname != null && map.containsKey(nickname)) {
					boolean value = map.get(nickname);
					map.set(nickname, !value);
					
					// update to usertype = 0
					try {
						userMapNormal.lock(nickname);
						UserCacheModel u = (UserCacheModel) userMapNormal.get(nickname);
						long currenVin = u.getVin();
						u.setBanCashOut(true);
						u.setBanTransferMoney(true);
						u.setBanLogin(true);
//						u.setVin(0);
//						u.setVinTotal(0);
						userMapNormal.put(nickname, u);

						UserService userService = new UserServiceImpl();
						userService.updateMoneyFromAdmin(nickname, Math.abs(currenVin) * (-1), "vin",
								Games.HOAN_TRA.getName(), Games.HOAN_TRA.getId() + "", "refund test");
					} finally {
						userMapNormal.unlock(nickname);
					}

				}
			}
            response.setSuccess(true);
            response.setErrorCode("0");
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug(e);
        }

        return response.toJson();
    }
}
