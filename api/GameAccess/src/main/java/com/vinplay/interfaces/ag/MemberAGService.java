package com.vinplay.interfaces.ag;

import java.sql.SQLException;
import java.util.Objects;

import com.vinplay.dao.ag.AgDao;
import com.vinplay.dao.impl.ag.AgDaoImpl;
import com.vinplay.dto.ag.BaseAGRequest;
import com.vinplay.dto.ag.BaseAGResponseDto;
import com.vinplay.interfaces.ag.utils.HttpUtils;
import com.vinplay.item.AGUserItem;
import com.vinplay.usercore.service.MoneyInGameService;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.MoneyInGameServiceImpl;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.usercore.utils.GameThirdPartyInit;
import com.vinplay.utils.DESEncrypt;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.response.MoneyResponse;
import com.vinplay.logic.CommonMethod;
import com.vinplay.logic.InitData;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class MemberAGService extends BaseAGService {

    private static final Logger LOGGER = Logger.getLogger(MemberAGService.class);
   
	private static final String METHOD_GET_BALANCE = "gb";
	private static final String METHOD_TRANSFER = "tc";
	private static final String METHOD_TRANSFER_CONFIRM = "tcc";
	private static final String TYPE_TRANSFER_DEPOSIT = "IN";
	private static final String TYPE_TRANSFER_WITHDRAW = "OUT";
	private AgDao agDao = new AgDaoImpl();
	
    /**
     * Create user
     * 
     * @param req
     * @return {@link BaseAGResponseDto}
     */
    public BaseAGResponseDto createUser(BaseAGRequest req) {
        return getData(req);
    }

    /**
     * Forward game
     * 
     * @param loginName
     * @return url game
     */
    public String forwardGame(String loginName, String mh5) {
        AGUserItem agUserItem = agDao.mappingUser(loginName);
        if (Objects.nonNull(agUserItem)) {
            String sId = GameThirdPartyInit.AG_CAGENT + CommonMethod.GetCurDate("yyMMddHHmmss")
                    + InitData.getids();
            BaseAGRequest req = new BaseAGRequest();
            req.setCagent(GameThirdPartyInit.AG_CAGENT);
            req.setCur(GameThirdPartyInit.AG_CURRENCY);
            req.setActype(GameThirdPartyInit.AG_AC_TYPE);
            req.setLoginname(agUserItem.getAgid());
            req.setPassword(agUserItem.getPassword());
            req.setSid(sId);
            req.setGameType(GameThirdPartyInit.AG_GAME_TYPE);
            req.setLang(GameThirdPartyInit.AG_LANG_CODE);
            if(mh5.equals("y")) {
            	req.setMh5("y");
            }
            String params = buildParams(req);
            LOGGER.info("[MemberAGService] AG Forward Game Params: " + params);
            DESEncrypt des = new DESEncrypt(GameThirdPartyInit.AG_ENCRYPT);
            try {
                String targetParams = des.encrypt(params);
                String key = HttpUtils.md5(targetParams + GameThirdPartyInit.AG_MD5);
                String url =
                        GameThirdPartyInit.AG_API_FORWARD_GAME + "params=" + targetParams + "&key=" + key;
                LOGGER.info("[MemberAGService] AG Forward Game link: " + url);
                return url;
            } catch (Exception e) {
                LOGGER.error("[MemberAGService] AG Forward Game error: " , e);
            }
        }
        return null;
    }

    public BaseAGResponseDto checkBalance(String loginName) {
		BaseAGRequest req = new BaseAGRequest();
		req.setMethod(METHOD_GET_BALANCE);
		req.setCagent(GameThirdPartyInit.AG_CAGENT);
		req.setCur(GameThirdPartyInit.AG_CURRENCY);
		req.setActype(GameThirdPartyInit.AG_AC_TYPE);
		AGUserItem agUserItem = agDao.mappingUser(loginName);
		if (agUserItem == null) {
			return null;
		}
		req.setLoginname(agUserItem.getAgid());
		req.setPassword(agUserItem.getPassword());
		BaseAGResponseDto responseDto = getData(req);
		if(responseDto != null && StringUtils.isEmpty(responseDto.getMsg())) {
			double amount = Double.parseDouble(responseDto.getInfo());
			responseDto.setInfo(""+amount);
			responseDto.setMsg(agUserItem.getAgid());
		}
		return responseDto;
	}
    
    public BaseAGResponseDto depositTransfer(String nickName, Double amount, String ip) throws SQLException {
		AGUserItem agUserItem = agDao.mappingUser(nickName);
		String sId = GameThirdPartyInit.AG_CAGENT + CommonMethod.GetCurDate("yyMMddHHmmss") + InitData.getids();
		if (agUserItem == null) {
			return null;
		}
		BaseAGRequest req = new BaseAGRequest();
		req.setMethod(METHOD_TRANSFER);
		req.setCagent(GameThirdPartyInit.AG_CAGENT);
		req.setLoginname(agUserItem.getAgid());
		req.setPassword(agUserItem.getPassword());
		req.setCur(GameThirdPartyInit.AG_CURRENCY);
		req.setActype(GameThirdPartyInit.AG_AC_TYPE);
		req.setType(TYPE_TRANSFER_DEPOSIT);
		Double amountReq = amount * GameThirdPartyInit.AG_RATE;
		req.setCredit(amount+"");
		req.setBillno(sId);
		LOGGER.info("AG Deposit transfer login name :" + nickName + "billNo:" + sId + " amount:" + amount );
		BaseAGResponseDto response = getData(req);
		if(response == null) {
			LOGGER.error("AG Deposit transfer login name :"+nickName + " AG reponse " + response);
			return response;
		}
		BaseAGResponseDto responseConfirm = null;
		if(StringUtils.isEmpty(response.getMsg())) {
			boolean saveDB = updateBalance(nickName, amountReq, 1, ip, sId,0);
			if(saveDB) {
				responseConfirm = confirmTransfer(nickName, amount, sId, TYPE_TRANSFER_DEPOSIT, true);
			} else {
				responseConfirm = confirmTransfer(nickName, amount, sId, TYPE_TRANSFER_DEPOSIT, false);
				return null;
			}
		} else {
			LOGGER.error("AG Deposit transfer login name :"+nickName + " AG reponse message:" + response.getMsg());
			responseConfirm = confirmTransfer(nickName, amount, sId, TYPE_TRANSFER_DEPOSIT, false);
			return null;
		}
		return responseConfirm;
	}
    
    private boolean updateBalance(String nickName, Double amount, int direction, String ip, String wid,
			int ispendding) throws SQLException {
		MoneyInGameService moneyService = new MoneyInGameServiceImpl();
		MoneyResponse moneyResponse = null;
		if (direction == 1) {
			//check current balance
			UserService userService = new UserServiceImpl();
			UserModel u = userService.getUserByNickName(nickName);
			if (u.getVin() < amount.longValue()) {
				return false;
			}
			//deposit
			moneyResponse = moneyService.updateMoneyGame3rdUser(nickName, amount.longValue() * (-1),
					"vin", "ag", "AG_DEPOSIT", "NẠP TIỀN AG", 0, false);
		} else {
			//withdraw
			moneyResponse = moneyService.updateMoneyGame3rdUser(nickName, amount.longValue(),
					"vin", "ag", "AG_WITHDRAW", "RÚT TIỀN AG", 0, false);
		}
		if(moneyResponse!=null && "0".equals(moneyResponse.getErrorCode())) {
			return true;
		}
		return false;

	}
	
	public BaseAGResponseDto withdrawTransfer(String loginName, double amount, String ip) throws SQLException {
		BaseAGResponseDto responseConfirm = new BaseAGResponseDto();
		//check balance
		AGUserItem agUserItem = agDao.mappingUser(loginName);
		if (agUserItem == null) {
			return null;
		}
		BaseAGRequest req1 = new BaseAGRequest();
		req1.setMethod(METHOD_GET_BALANCE);
		req1.setCagent(GameThirdPartyInit.AG_CAGENT);
		req1.setCur(GameThirdPartyInit.AG_CURRENCY);
		req1.setActype(GameThirdPartyInit.AG_AC_TYPE);
		req1.setLoginname(agUserItem.getAgid());
		req1.setPassword(agUserItem.getPassword());
		BaseAGResponseDto responseDto = getData(req1);
		if(responseDto != null && StringUtils.isEmpty(responseDto.getMsg())) {
			double amountUser = Double.parseDouble(responseDto.getInfo());
			if(amountUser * 1000< amount) {
				responseConfirm.setMsg("99");
				return responseConfirm;
			}
		}else {
			responseConfirm.setMsg("100");
			return responseConfirm;
		}
		
		String sId = GameThirdPartyInit.AG_CAGENT + CommonMethod.GetCurDate("yyMMddHHmmss") + InitData.getids();
		
		BaseAGRequest req = new BaseAGRequest();
		req.setMethod(METHOD_TRANSFER);
		req.setCagent(GameThirdPartyInit.AG_CAGENT);
		req.setLoginname(agUserItem.getAgid());
		req.setPassword(agUserItem.getPassword());
		req.setCur(GameThirdPartyInit.AG_CURRENCY);
		req.setActype(GameThirdPartyInit.AG_AC_TYPE);
		req.setType(TYPE_TRANSFER_WITHDRAW);
		double amountReq = amount * GameThirdPartyInit.AG_RATE;
		req.setCredit(amount+"");
		req.setBillno(sId);
		LOGGER.info("AG Withdraw transfer login name :" + loginName + "billNo:" + sId + " amount:" + amount);
		BaseAGResponseDto response = getData(req);
		if(response == null) {
			LOGGER.error("AG Withdraw transfer login name :"+loginName + " AG reponse " + response);
			return response;
		}
		
		if(StringUtils.isEmpty(response.getMsg())) {
			boolean saveDB = updateBalance(loginName, amountReq, 0, ip, sId,0);
			if(saveDB) {
				responseConfirm = confirmTransfer(loginName, amount, sId, TYPE_TRANSFER_WITHDRAW, true);
			} else {
				responseConfirm = confirmTransfer(loginName, amount, sId, TYPE_TRANSFER_WITHDRAW, false);
				return null;
			}
		} else {
			LOGGER.error("AG Withdraw transfer login name :"+loginName + " AG reponse message: " + response.getMsg());
			responseConfirm = confirmTransfer(loginName, amount, sId, TYPE_TRANSFER_WITHDRAW, false);
			return null;
		}
		return responseConfirm;
	}
	
	public BaseAGResponseDto confirmTransfer(String loginName,Double amount, String billNo, String type, boolean isSuccess) {
		AGUserItem agUserItem = agDao.mappingUser(loginName);
		if (agUserItem == null) {
			return null;
		}
		BaseAGRequest req = new BaseAGRequest();
		req.setMethod(METHOD_TRANSFER_CONFIRM);
		req.setCagent(GameThirdPartyInit.AG_CAGENT);
		req.setLoginname(agUserItem.getAgid());
		req.setPassword(agUserItem.getPassword());
		req.setCur(GameThirdPartyInit.AG_CURRENCY);
		req.setActype(GameThirdPartyInit.AG_AC_TYPE);
		req.setType(type);
		req.setCredit(amount+"");
		req.setBillno(billNo);
		// Value=1 if invoke ‘PrepareTransferCredit’ API success ;Value=0 if invoke ‘PrepareTransferCredit’ has some error or error code. 
		if(isSuccess) {
			req.setFlag("1");
		}else {
			req.setFlag("0");
		}
		LOGGER.info("AG confirm transfer login name :" + loginName + "-billNo:" + billNo + "- type: " + type + "-Is success: " + isSuccess + " amount:" + amount);
		return getData(req);
	}

}
