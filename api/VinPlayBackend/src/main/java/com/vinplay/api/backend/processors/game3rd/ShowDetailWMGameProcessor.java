package com.vinplay.api.backend.processors.game3rd;

import com.vinplay.common.game3rd.WMGameRecordItem;
import com.vinplay.payment.utils.Constant;
import com.vinplay.report.service.ThirdPartyGameReport;
import com.vinplay.report.service.impl.ThirdPartyGameReportImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ShowDetailWMGameProcessor implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger(AgGameReportProcessor.class);

    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = param.get();
        Long bet_id = null;
        try {
            bet_id = Long.parseLong(request.getParameter("bid"));
        } catch (NumberFormatException e) {
            // TODO: handle exception
        }
        logger.info("Request Detail AG report bet_id: " + bet_id);
        ThirdPartyGameReport service = new ThirdPartyGameReportImpl();
        try {
            // TODO: Check role
            List<WMGameRecordItem> res = service.showDetailWM(bet_id);
            if (res != null) {
                return BaseResponse.success("", "", res);
            } else {
                return BaseResponse.error(Constant.ERROR_SYSTEM, "null");
            }

        } catch (Exception e) {
            logger.error(e);
            return BaseResponse.error(Constant.ERROR_SYSTEM, e.getMessage());
        }

    }
}
