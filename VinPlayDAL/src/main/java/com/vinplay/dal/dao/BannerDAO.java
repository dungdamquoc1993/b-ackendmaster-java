package com.vinplay.dal.dao;

import com.vinplay.dal.entities.banner.BannerModel;

import java.util.List;

public interface BannerDAO {

    public long countlistBanner(String title, Integer status, String image_path, Integer action, String url);

    public List<BannerModel> listBanner(String title, Integer status, String image_path, Integer action, String url, int page, int maxItem);

    public BannerModel BannerDetail(Integer id);

    public Boolean addNewBanner(String title, Integer status, String image_path, Integer action, String url);

    public Boolean updateBannerById(Integer id, String title, Integer status, String image_path, Integer action, String url);

    public Boolean updateBannerByTitle(String title, Integer status, String image_path, Integer action, String url);

    public Boolean deleteBanner(Integer id);

    public Boolean deleteBanner(String title);
}
