package com.vinplay.api.processors;

import com.vinplay.usercore.service.impl.MailBoxServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponseModel;
import javax.servlet.http.HttpServletRequest;

public class DeleteMailBoxProcessor
implements BaseProcessor<HttpServletRequest, String> {
    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = (HttpServletRequest)param.get();
        BaseResponseModel response = new BaseResponseModel(false, "1001");
        String mailId = request.getParameter("mid");
        if (!mailId.isEmpty()) {
            MailBoxServiceImpl service = new MailBoxServiceImpl();
            int del = service.deleteMailBox(mailId);
            if (del == 0) {
                response.setErrorCode("0");
                response.setSuccess(true);
            } else {
                response.setErrorCode("10001");
            }
            return response.toJson();
        }
        return "MISSING PARAMETTER";
    }
}

