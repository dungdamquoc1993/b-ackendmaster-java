package com.vinplay.api.backend.processors.user;

import com.vinplay.usercore.service.UserForAdminService;
import com.vinplay.usercore.service.impl.UserForAdminServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

import javax.servlet.http.HttpServletRequest;

public class ChangePasswordUserProcessor implements BaseProcessor<HttpServletRequest, String> {
    @Override
    public String execute(Param<HttpServletRequest> param) {
        HttpServletRequest request = param.get();

        String nick_name = request.getParameter("nn");
        String old_password = request.getParameter("op");
        String new_password = request.getParameter("np");
        String is_bot = request.getParameter("b");
        String dai_ly = request.getParameter("dl");
        Boolean is_reset = Boolean.valueOf(request.getParameter("r"));

        if(nick_name == null || nick_name.trim().isEmpty()) {
            return BaseResponse.error("-1", "Thiếu nickname");
        }

        if(dai_ly != null && dai_ly.equals("100")) {
            return BaseResponse.error("-1", "Tài khoản không thể đổi password");
        }

        UserForAdminService service = new UserForAdminServiceImpl();
        try {
            String status = service.changePasswordUser(nick_name, old_password, new_password, is_bot, dai_ly, is_reset);
            switch (status) {
                case "success":
                    return BaseResponse.success(null, 1);
                case "not_found":
                    return BaseResponse.error("-1", "Không tìm thấy user");
                case "not_same":
                    return BaseResponse.error("-1", "Mật khẩu cũ nhập sai");
                default:
                    return BaseResponse.error("-1", status);
            }
        }
        catch (Exception e) {
            return BaseResponse.error("-1", e.getMessage());
        }
    }
}