package com.vinplay.api.processors;

import com.vinplay.api.processors.response.LogMoneyResponse;
import com.vinplay.dal.service.LogMoneyUserService;
import com.vinplay.dal.service.impl.LogMoneyUserServiceImpl;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.LogMoneyUserResponse;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class GetLogMoneyUserProcessor
implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger((String)"api");

    public String execute(Param<HttpServletRequest> param) {
        LogMoneyResponse response = new LogMoneyResponse(false, "1001");
        HttpServletRequest request = (HttpServletRequest)param.get();
        String accessToken = request.getParameter("at");
        UserService userSer = new UserServiceImpl();
        String nickName = request.getParameter("nn");
        if (userSer.isActiveToken(nickName, accessToken)) {
            int moneyType = Integer.parseInt(request.getParameter("mt"));
            int page = Integer.parseInt(request.getParameter("p"));
            if (page < 0 || moneyType <= 0 || moneyType > 5 || nickName == null || nickName.isEmpty()) {
                return response.toJson();
            }
            LogMoneyUserService service = new LogMoneyUserServiceImpl();
            try {
                List<LogMoneyUserResponse> trans = service.getHistoryTransactionLogMoney(nickName, moneyType, page);
                int totalPages = 5;
                response.setTotalPages(totalPages);
                response.setTransactions(trans);
                response.setSuccess(true);
                response.setErrorCode("0");
            }
            catch (Exception e) {
                e.printStackTrace();
                logger.debug(e);
            }
        }
        return response.toJson();
    }
}

