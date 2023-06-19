package com.vinplay.api.processors;

import com.vinplay.payment.utils.Constant;
import com.vinplay.usercore.service.AgentBankService;
import com.vinplay.usercore.service.impl.AgentBankServiceImpl;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class SearchAgentBankProcessor implements BaseProcessor<HttpServletRequest, String> {
	private static final Logger logger = Logger.getLogger("api");

	public String execute(Param<HttpServletRequest> param) {
		HttpServletRequest request = param.get();
		String nickname = request.getParameter("nn");
		String accessToken = request.getParameter("at");
		String agentCode = request.getParameter("code");
		if (nickname == null || nickname.trim().isEmpty())
			return BaseResponse.error(Constant.ERROR_PARAM, "Nickname không được để trắng");

		if (StringUtils.isBlank(accessToken))
			return BaseResponse.error(Constant.ERROR_PARAM, "Mã phiên làm việc không được để trắng");

		if (StringUtils.isBlank(agentCode))
			return BaseResponse.error(Constant.ERROR_PARAM, "Mã đại lý không được để trắng");

		UserService userService = new UserServiceImpl();
		boolean isToken = userService.isActiveToken(nickname, accessToken);
		if (!isToken) {
			return BaseResponse.error(Constant.ERROR_SESSION,
					"Phiên làm việc của bạn đã hết hạn , vui lòng tải lại trang !");
		}
		
		int page, maxItem;
        try {
            page = Integer.parseInt(request.getParameter("pg"));
        } catch (NumberFormatException e) {
            page = 1;
        }

        try {
            maxItem = Integer.parseInt(request.getParameter("mi"));
        } catch (NumberFormatException e) {
            maxItem = 10;
        }

        AgentBankService service = new AgentBankServiceImpl();
        try {
        	Map<String, Object> rs = service.search(null, agentCode, page, maxItem);
            return BaseResponse.success(rs.get("agentBanks"), Long.parseLong(rs.get("total").toString()));
        }
        catch (Exception e) {
        	logger.trace(e);
            return BaseResponse.error("-1", "Lỗi hệ thống. Vui lòng liên hệ bộ phận CSKH để được giúp đỡ.");
        }
	}
}