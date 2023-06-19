package com.vinplay.api.backend.processors;

import com.vinplay.dal.entities.agent.DetailUserModel;
import com.vinplay.usercore.service.impl.UserForAdminServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class DetailUserProcessor implements BaseProcessor<HttpServletRequest, String> {
    @Override
    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = param.get();

        String user_name = request.getParameter("un");
        String nick_name = request.getParameter("nn");
        UserForAdminServiceImpl service = new UserForAdminServiceImpl();
        try {
            DetailUserModel user = service.searchDetailUser(user_name, nick_name);
            Map<String, Object> map = new HashMap<>();
            map.put("user", user);

            return new BaseResponse().success(map);
        }
        catch (Exception e) {
            return BaseResponse.error("-1", e.getMessage());
        }
    }
}
