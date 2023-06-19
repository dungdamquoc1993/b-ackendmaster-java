package com.vinplay.api.backend.processors.banner;

import com.google.gson.Gson;
import com.vinplay.dal.dao.BannerDAO;
import com.vinplay.dal.dao.impl.BannerDAOImpl;
import com.vinplay.dal.entities.banner.BannerModel;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.BaseResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class ShowListBannerProcessor implements BaseProcessor<HttpServletRequest, String> {
    @Override
    public String execute(Param<HttpServletRequest> param) {
        Gson gson = new Gson();
        HttpServletRequest request = param.get();
        String title = request.getParameter("tt");
        String image_path = request.getParameter("ip");
        String url = request.getParameter("url");

        Integer status = null, action = null, page = 1, maxItem = 10;
        try {
            status = Integer.parseInt(request.getParameter("sts"));
        } catch (NumberFormatException e){

        }
        try {
            action = Integer.parseInt(request.getParameter("ac"));
        } catch (NumberFormatException e){

        }
        try {
            page = Integer.parseInt(request.getParameter("pg"));
        } catch (NumberFormatException e){
            page = 1;
        }
        try {
            maxItem = Integer.parseInt(request.getParameter("mi"));
        } catch (NumberFormatException e){
            maxItem = 10;
        }

        try {
            BannerDAO dao = new BannerDAOImpl();
            long total = dao.countlistBanner(title,status,image_path, action, url);
            List<BannerModel> banners = dao.listBanner(title,status,image_path, action, url,page,maxItem);
            return BaseResponse.success(banners, total);
        }
        catch (Exception e) {
            return BaseResponse.error("-1", e.getMessage());
        }
    }
}