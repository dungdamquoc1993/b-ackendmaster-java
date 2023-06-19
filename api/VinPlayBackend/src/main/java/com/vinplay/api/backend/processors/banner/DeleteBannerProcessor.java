package com.vinplay.api.backend.processors.banner;

import com.google.gson.Gson;
import com.vinplay.dal.dao.BannerDAO;
import com.vinplay.dal.dao.impl.BannerDAOImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

import javax.servlet.http.HttpServletRequest;

public class DeleteBannerProcessor implements BaseProcessor<HttpServletRequest, String> {
    @Override
    public String execute(Param<HttpServletRequest> param) {
        Gson gson = new Gson();
        HttpServletRequest request = param.get();
        String title = request.getParameter("tt");

        Integer id = null;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e){

        }

        try {
            BannerDAO dao = new BannerDAOImpl();
            Boolean check = false;
            if(id == null){
                check = dao.deleteBanner(title);
            }else{
                check = dao.deleteBanner(id);
            }

            if(check){
                return BaseResponse.success("", "Delete thành công", null);
            } else{
                return BaseResponse.error("-1", "Delete không thành công !");
            }
        }
        catch (Exception e) {
            return BaseResponse.error("-1", e.getMessage());
        }
    }
}