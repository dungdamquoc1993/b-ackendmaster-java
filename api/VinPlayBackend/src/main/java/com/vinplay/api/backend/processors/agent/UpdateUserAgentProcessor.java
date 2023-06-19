package com.vinplay.api.backend.processors.agent;

import com.vinplay.dal.dao.AgentDAO;
import com.vinplay.dal.dao.impl.AgentDAOImpl;
import com.vinplay.payment.utils.Constant;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

import javax.servlet.http.HttpServletRequest;

public class UpdateUserAgentProcessor implements BaseProcessor<HttpServletRequest, String> {
    @Override
    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = param.get();
//        String serPath = request.getServletPath();
//        if(serPath == null || serPath.trim().isEmpty() || serPath != "/api_agent"){
//            return BaseResponse.error(Constant.ERROR_PARAM, "Not allow access this api");
//        }

        String username = request.getParameter("un");
        String nickname = request.getParameter("nn");
        String password = request.getParameter("ps");
        String nameagent = request.getParameter("na");
        String address = request.getParameter("adr");
        String phone = request.getParameter("ph");
        String email = request.getParameter("em");
        String facebook = request.getParameter("fa");
        String status = request.getParameter("sts");
        String namebank = request.getParameter("nb");
        String nameaccount = request.getParameter("nac");
        String numberaccount = request.getParameter("nbac");
        String code = request.getParameter("cd");
        Integer show = null, active = null, id =null, level = null;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
        }
        try {
            show = Integer.parseInt(request.getParameter("sh"));
        } catch (NumberFormatException e) {
        }
        try {
            active = Integer.parseInt(request.getParameter("ac"));
        } catch (NumberFormatException e) {
        }
        try {
            level = Integer.parseInt(request.getParameter("lv"));
        } catch (NumberFormatException e) {
        }

        AgentDAO dao = new AgentDAOImpl();
        try {
            Boolean check = dao.UpdateUserAgent(id, username, nickname, password, nameagent, address, phone, email,
                    facebook, null, status, null, namebank, nameaccount, numberaccount,
                    show, active, null, null, null, null, null,
                    null, null, null, level, code);
            if(check){
                return BaseResponse.success("", "Update thành công", null);
            }else {
                return BaseResponse.error("-1", "Update không thành công !");
            }
        }
        catch (Exception e) {
            return BaseResponse.error("-1", e.getMessage());
        }
    }
}