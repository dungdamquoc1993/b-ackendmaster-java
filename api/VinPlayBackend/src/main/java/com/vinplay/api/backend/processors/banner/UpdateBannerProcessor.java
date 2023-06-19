package com.vinplay.api.backend.processors.banner;

import com.google.gson.Gson;
import com.vinplay.dal.dao.BannerDAO;
import com.vinplay.dal.dao.impl.BannerDAOImpl;
import com.vinplay.dal.entities.banner.BannerModel;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

import javax.servlet.http.HttpServletRequest;

public class UpdateBannerProcessor implements BaseProcessor<HttpServletRequest, String> {
    @Override
    public String execute(Param<HttpServletRequest> param) {
        Gson gson = new Gson();
        HttpServletRequest request = param.get();
        String title = request.getParameter("tt");
        String image_path = request.getParameter("ip");
        String url = request.getParameter("url");

        Integer id = null, action = null;
        int status = 0;
        try {
            status = Integer.parseInt(request.getParameter("sts"));
        } catch (NumberFormatException e){
            status = 0;
        }
        try {
            action = Integer.parseInt(request.getParameter("ac"));
        } catch (NumberFormatException e){

        }
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e){

        }

        BannerModel bannerModel = new BannerModel(title, status, image_path, action, url);
        try {
            BannerDAO dao = new BannerDAOImpl();
            Boolean check = false;
            if(id == null){
                check = dao.updateBannerByTitle(title, status, image_path, action, url);
            }else{
                check = dao.updateBannerById(id, title, status, image_path, action, url);
            }

            if(check){
                return BaseResponse.success("", "Update thành công", bannerModel);
            } else{
                return BaseResponse.error("-1", "Update không thành công !");
            }
        }
        catch (Exception e) {
            return BaseResponse.error("-1", e.getMessage());
        }
    }
}