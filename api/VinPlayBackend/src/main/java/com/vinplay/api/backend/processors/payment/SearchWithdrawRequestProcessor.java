package com.vinplay.api.backend.processors.payment;

import com.mongodb.BasicDBObject;
import com.vinplay.payment.dao.WithDrawPaygateDao;
import com.vinplay.payment.dao.impl.WithDrawPaygateDaoImpl;
import com.vinplay.payment.utils.Constant;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchWithdrawRequestProcessor implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger(SearchWithdrawRequestProcessor.class);

    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = param.get();

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


        HashMap<String, Object>  condition = getSearchCondition(request);
        WithDrawPaygateDao withdrawDAO = new WithDrawPaygateDaoImpl();
        try {
            List<Object> data = withdrawDAO.find(condition, page, maxItem);
            long total = withdrawDAO.count(condition);
            Long[] statistic = withdrawDAO.statistic(condition);

            String str = BaseResponse.success(data,total, statistic);
            return str;
        } catch (Exception e) {
            logger.error(e);
            return BaseResponse.error(Constant.ERROR_SYSTEM, e.getMessage());
        }
    }

    private HashMap<String, Object> getSearchCondition(HttpServletRequest request) {
        HashMap<String, Object> conditions = new HashMap<String, Object>();

        String nickName = request.getParameter("nn");
        String providerName = request.getParameter("pn");
        String orderId = request.getParameter("oi");
        String transactionId = request.getParameter("ti");
        String fromTime = request.getParameter("ft");
        String endTime = request.getParameter("et");
        String bankCode = request.getParameter("bc");
        String bankAccountName = request.getParameter("ba");
        String bankAccountNumber = request.getParameter("bn");
        String status = request.getParameter("st");
        String  fromAmount = request.getParameter("fa");
        String toAmount = request.getParameter("ta");

        try {
            if (status != null && !status.trim().isEmpty()) {
                conditions.put("Status",  Integer.parseInt(status.trim()));
            }
        } catch (NumberFormatException e) {
            //			 nothing
        }

        try {
            if (fromAmount != null && toAmount != null && !fromAmount.trim().isEmpty() && !toAmount.trim().isEmpty()) {
                BasicDBObject obj = new BasicDBObject();
                obj.put("$gte", Long.parseLong(fromAmount.trim()));
                obj.put("$lte", Long.parseLong(toAmount.trim()));
                conditions.put("Amount", obj);
            } else if (fromAmount != null && !fromAmount.trim().isEmpty()) {
                BasicDBObject obj = new BasicDBObject();
                obj.put("$gte", Long.parseLong(fromAmount.trim()));
                conditions.put("Amount", obj);
            } else if (toAmount != null && !toAmount.trim().isEmpty()) {
                BasicDBObject obj = new BasicDBObject();
                obj.put("$lte", Long.parseLong(toAmount.trim()));
                conditions.put("Amount", obj);
            }
        } catch (NumberFormatException e) {
            //			 nothing
        }

        if (nickName != null && !nickName.trim().isEmpty()) {
            conditions.put("Nickname", nickName.trim());
        }

        if (providerName != null && !providerName.trim().isEmpty()) {
            conditions.put("ProviderName", providerName.trim());
        }

        if (orderId != null && !orderId.trim().isEmpty()) {
            conditions.put("CartId", orderId.trim());
        }

        if (transactionId != null && !transactionId.trim().isEmpty()) {
            conditions.put("ReferenceId", transactionId.trim());
        }

        if (bankCode != null && !bankCode.trim().isEmpty()) {
            conditions.put("BankCode", bankCode.trim());
        }

        if (bankAccountNumber != null && !bankAccountNumber.trim().isEmpty()) {
            conditions.put("BankAccountNumber", bankAccountNumber.trim());
        }

        if (bankAccountName != null && !bankAccountName.trim().isEmpty()) {
            conditions.put("BankAccountName", bankAccountName.trim());
        }

        if (fromTime != null && !fromTime.isEmpty() && endTime != null && !endTime.isEmpty()) {
            BasicDBObject obj = new BasicDBObject();
            obj.put("$gte", fromTime);
            obj.put("$lte", endTime);
            conditions.put("ModifiedAt", obj);
        }

        return conditions;
    }
}
