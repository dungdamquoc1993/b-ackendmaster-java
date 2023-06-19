package com.vinplay.api.backend.processors.agent;

import com.vinplay.api.backend.models.AgentResponse;
import com.vinplay.dal.dao.AgentDAO;
import com.vinplay.dal.dao.impl.AgentDAOImpl;
import com.vinplay.dal.entities.agent.UserAgentLoginModel;
import com.vinplay.payment.utils.Constant;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;
import com.vinplay.vbee.common.response.BaseResponseModel;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


//public class LoginAgentProcessor extends HttpServlet {
//
//    private static final long serialVersionUID = 1L;
//
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        doPost(request, response);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
////        super.doPost(request, response);
//        response.setContentType("application/json");
////        response.setContentType("text/html");
//        response.setCharacterEncoding("UTF-8");
//        response.setStatus(200);
//
//        String username = request.getParameter("un");
//        String password = request.getParameter("ps");
//        if(username==null || username.trim().isEmpty()){
//            response.getWriter().println(AgentResponse.error("-1", "Nhập thiếu username"));
//            return;
//        }
//        if(password==null || password.trim().isEmpty()){
//            response.getWriter().println(AgentResponse.error("-1", "Nhập thiếu password"));
//            return;
//        }
//
//        try {
//            AgentDAO dao = new AgentDAOImpl();
//            Map<String, Object> map = dao.loginSystemAgent(username, password);
//            UserAgentLoginModel userAgentModel = (UserAgentLoginModel) map.get("user");
//            String mess = (String) map.get("mess");
//            if(userAgentModel.getId()!=null) {
//                response.getWriter().println(AgentResponse.success("00", mess, userAgentModel));
//                return;
//            }else {
//                response.getWriter().println(AgentResponse.error("-1", mess));
//                return;
//            }
//        }
//        catch (Exception e) {
//            response.getWriter().println(AgentResponse.error("-1", e.getMessage()));
//            return;
//        }
//    }
//}

public class LoginAgentProcessor implements BaseProcessor<HttpServletRequest, String> {
    private static final Logger logger = Logger.getLogger((String)"api_agent");

    @Override
    public String execute(Param<HttpServletRequest> param) {
        BaseResponseModel res = new BaseResponseModel(false, "1001");
        HttpServletRequest request = param.get();
        String serPath = request.getServletPath();
        if(serPath == null || serPath.trim().isEmpty() || serPath != "/api_agent"){
            return BaseResponse.error(Constant.ERROR_PARAM, "Not allow access this api");
        }

        String username = request.getParameter("un");
        String password = request.getParameter("ps");
        if(username==null || username.trim().isEmpty()){
            return BaseResponse.error(Constant.ERROR_PARAM, "username(un) can not empty");
        }
        if(password==null || password.trim().isEmpty()){
            return BaseResponse.error(Constant.ERROR_PARAM, "password(ps) can not empty");
        }

        try {
            AgentDAO dao = new AgentDAOImpl();
            Map<String, Object> map = dao.loginSystemAgent(username, password);
            UserAgentLoginModel userAgentModel = (UserAgentLoginModel) map.get("user");
            String mess = (String) map.get("mess");
            if(userAgentModel.getId()!=null) {
                res.setData(userAgentModel);
                res.setErrorCode("0");
                res.setSuccess(true);
                res.setMessage("Add agency success");
            }else {
                res.setMessage("Add agency not success");
            }
        }
        catch (Exception e) {
            logger.debug((Object)e);
            res.setMessage("Add agency error");
        }

        return res.toJson();
    }
}

