package com.vinplay.api.backend.processors.game3rd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.vinplay.common.game3rd.CmdGameRecords;
import com.vinplay.common.game3rd.ThirdPartyResponse;
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

public class CmdGameReportProcessor implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger(WmGameReportProcessor.class);

    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = param.get();

        String sourcename = request.getParameter("sn");
        String referenceno = request.getParameter("rn");
        String dangerstatus= request.getParameter("ds");

        String betip= request.getParameter("bip");
        String loginname = request.getParameter("ln");
        String fromTime = request.getParameter("ft");
        String endTime = request.getParameter("et");


        int page = 1, maxItem = 10, flagtime = 0;
        try {
            flagtime = Integer.parseInt(request.getParameter("fgt"));
        } catch (NumberFormatException e) {
            // TODO: handle exception
        }
        try {
            page = Integer.parseInt(request.getParameter("pg"));
        } catch (NumberFormatException e) {
            // TODO: handle exception
        }
        try {
            maxItem = Integer.parseInt(request.getParameter("mi"));
        } catch (NumberFormatException e) {
            // TODO: handle exception
        }

        logger.info("Request CMD report loginname= " + loginname);
        ThirdPartyGameReport service = new ThirdPartyGameReportImpl();
        try {
            // TODO: Check role
            ThirdPartyResponse<List<CmdGameRecords>> res = service.filterCMD(sourcename, referenceno, null, null, null, null,
                    null, null, null, null, null, null, null, null, null,null,
                    dangerstatus, null, betip, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null,null, null,
                    null, null, null, null,null, null, null, null,
                    null, null,null, null, null, null, null, loginname,null, null, null,
                    flagtime, fromTime, endTime, page, maxItem);
            if (res != null) {
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String json = ow.writeValueAsString(res);
                return new BaseResponse<String>().success(json);
            } else {
                return BaseResponse.error(Constant.ERROR_SYSTEM, "null");
            }

        } catch (Exception e) {
            logger.error(e);
            return BaseResponse.error(Constant.ERROR_SYSTEM, e.getMessage());
        }

    }
}