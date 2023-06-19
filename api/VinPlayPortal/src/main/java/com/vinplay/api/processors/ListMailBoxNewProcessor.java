package com.vinplay.api.processors;

import com.vinplay.usercore.service.MailBoxService;
import com.vinplay.usercore.service.UserService;
import com.vinplay.usercore.service.impl.MailBoxServiceImpl;
import com.vinplay.usercore.service.impl.UserServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.ListMailBoxResponse;
import com.vinplay.vbee.common.response.MailBoxResponse;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class ListMailBoxNewProcessor implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger((String)"api");

    public String execute(Param<HttpServletRequest> param) {
        ListMailBoxResponse response = new ListMailBoxResponse(false, "1001");
        HttpServletRequest request = param.get();
        String accessToken = request.getParameter("at");
        UserService userSer = new UserServiceImpl();
        String nickName = request.getParameter("nn");
        if (userSer.isActiveToken(nickName, accessToken)) {
            int page = Integer.parseInt(request.getParameter("p"));
            int total = 0;
            int totalPages = 0;
            if (page < 0) {
                return response.toJson();
            }
            MailBoxService service = new MailBoxServiceImpl();
            try {
                List<MailBoxResponse> trans = service.listMailBox(nickName, page);
                int mailnotread = service.countMailBoxInActive(nickName);
                if (trans.size() > 0) {
                    total = service.countMailBox(nickName);
                    totalPages = total % 5 == 0 ? total / 5 : total / 5 + 1;
                    response.setMailNotRead(mailnotread);
                    response.setTotalPages(totalPages);
                    response.setTransactions(trans);
                    response.setSuccess(true);
                    response.setErrorCode("0");
                } else {
                    response.setErrorCode("10001");
                }
            }
            catch (Exception e) {
                logger.error(e);
            }
        }
        return response.toJson();
    }
}

