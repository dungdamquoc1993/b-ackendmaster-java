package com.vinplay.api.backend.processors.agent;

import com.vinplay.dal.dao.AgentDAO;
import com.vinplay.dal.dao.impl.AgentDAOImpl;
import com.vinplay.dal.entities.agent.UserAgentModel;
import com.vinplay.dichvuthe.response.RechargePaywellResponse;
import com.vinplay.payment.entities.PaymentConfig;
import com.vinplay.payment.service.PaymentConfigService;
import com.vinplay.payment.service.RechargeManualBankService;
import com.vinplay.payment.service.impl.PaymentConfigServiceImpl;
import com.vinplay.payment.service.impl.RechargeManualBankServiceImpl;
import com.vinplay.payment.utils.Constant;
import com.vinplay.payment.utils.PaymentConstant;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.models.UserModel;
import com.vinplay.vbee.common.response.BaseResponse;
import org.apache.commons.lang.StringUtils;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

public class TopupUser4AgentAdminProccessor implements BaseProcessor<HttpServletRequest, String> {
    @Override
    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = param.get();
        String serPath = request.getServletPath();
        if(serPath == null || serPath.trim().isEmpty() || serPath != "/api_agent")
        	return BaseResponse.error(Constant.ERROR_PARAM, "Not allow access this api");
        
        String agentCode = request.getParameter("code");
		if (StringUtils.isBlank(agentCode)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "Agent code can not empty");
		}
		
        long amount = 0;
		try {
			amount = Long.parseLong(request.getParameter("am"));
		} catch (Exception e) {
			return BaseResponse.error("99", e.getMessage());
		}
		
		//TODO: Check minimum, maximum amount
		String providerName = PaymentConstant.PROVIDER.MANUAL_BANK;
		PaymentConfigService payConfig = new PaymentConfigServiceImpl();
		PaymentConfig config = payConfig.getConfigByKey(providerName);
		long minAmount = config == null ? 1000 : config.getConfig().getMinMoney();
		if (amount < minAmount) {
			return BaseResponse.error(Constant.MIN_MONEY, "Money is greater than " + minAmount);
		}

		if (amount > 300000000) {
			return BaseResponse.error(Constant.MAX_MONEY, "Money must be less than than 300M ");
		}
		
		String nickName = request.getParameter("nn");
		if (StringUtils.isBlank(nickName)) {
			return BaseResponse.error(Constant.ERROR_PARAM, "Nickname can not empty");
		}
        
		//TODO: Check user
		UserService userService = new UserServiceImpl();
		UserModel user = new UserModel();
		try {
			user = userService.getUserByNickName(nickName);
		} catch (SQLException e2) {
			user = null;
			e2.printStackTrace();
		}
		
		if(user == null)
			return BaseResponse.error(Constant.ERROR_PARAM, "Can not found User by Nickname: " + nickName);
			
		String userId = user.getId() + "";
		String username = user.getUsername();
		if (user.isBanLogin() || user.isBanTransferMoney() || user.isBot()) {
			return BaseResponse.error(Constant.ERROR_USERTYPE, "You can not allow access this feature");
		}
		
		if(user.getDaily() == 1) {
			AgentDAO agentDao = new AgentDAOImpl();
	        UserAgentModel agentModel;
			try {
				agentModel = agentDao.DetailUserAgentByNickName(nickName);
			} catch (SQLException e1) {
				e1.printStackTrace();
				agentModel = null;
			}
			
	        if(agentModel == null)
	        	return BaseResponse.error(Constant.ERROR_PARAM, "Agent account is not exist");
	        
	        if(agentModel.getActive() == 0)
	        	return BaseResponse.error(Constant.ERROR_PARAM, "Agent is inactive");
		}
		
        try {
        	RechargeManualBankService service = new RechargeManualBankServiceImpl();
    		RechargePaywellResponse rs = null;
    		rs = service.topupByCash(userId, username, nickName, agentCode, amount);

    		if (rs == null)
    			return BaseResponse.error(Constant.ERROR_CREATE_TRANSACTION, "Topup for user: " + nickName + " failure");

    		if (PaymentConstant.SUCCESS == rs.getCode()) {
    			return BaseResponse.success(String.valueOf(rs.getCode()), "success", "success");
    		} else {
    			return BaseResponse.error(String.valueOf(rs.getCode()), rs.getData());
    		}
        }
        catch (Exception e) {
            return BaseResponse.error("-1", e.getMessage());
        }
    }
}
