package com.vinplay.api.backend.processors.agent;

import com.vinplay.api.backend.response.BaseResponse;
import com.vinplay.dal.dao.AgentDAO;
import com.vinplay.dal.dao.impl.AgentDAOImpl;
import com.vinplay.dal.entities.agent.CheckDataAgentModel;
import com.vinplay.dal.entities.agent.VinPlayAgentModel;
import com.vinplay.payment.utils.Constant;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class InsertAgencyProcessor implements BaseProcessor<HttpServletRequest, String> {
    @Override
    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = param.get();
//        String serPath = request.getServletPath();
//        if(serPath == null || serPath.trim().isEmpty() || serPath != "/api_agent"){
//            return com.vinplay.vbee.common.response.BaseResponse.error(Constant.ERROR_PARAM, "Not allow access this api");
//        }

        String nick_name = request.getParameter("nn");
        String agency_code = request.getParameter("ac");
        BaseResponse res = new BaseResponse(false, "1001");
        try {
            AgentDAO dao = new AgentDAOImpl();
            CheckDataAgentModel checkDataAgentModel = dao.getUserID(nick_name);
            if(checkDataAgentModel.id>0){
                if(dao.isAgentExit(checkDataAgentModel.id, agency_code)){
                    res.setErrorCode("2: đại lý đã có code hoặc code đã tồn tại"); // dai ly da co code hoac code da ton tai
                }else{
                    if(checkDataAgentModel.dai_ly<1){
                        //set no la dai ly
                        dao.updateUserIsDaily(nick_name);
                    }
                    // insert code
                    dao.insertRefCodeAgent(checkDataAgentModel.id,agency_code);
                    res.setErrorCode("0");
                    res.setSuccess(true);
                }
            }else{
                res.setErrorCode("1: không tồn tại nickname này"); // khong ton tai nick name nay
            }


        }
        catch (Exception e) {
            res.setErrorCode(e.getMessage());
            // khong ton tai nick name nay
            // log
        }
        return res.toJson();
    }
}
