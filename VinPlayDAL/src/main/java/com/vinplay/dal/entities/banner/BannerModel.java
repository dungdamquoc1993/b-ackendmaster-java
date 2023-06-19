package com.vinplay.dal.entities.banner;

public class BannerModel {
    private Integer id;
    private String title;
    private Integer status;
    private String image_path;
    private Integer action;
    private String url;

    public BannerModel() {
    }

    public BannerModel(Integer id, String title, Integer status, String image_path) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.image_path = image_path;
    }

    public BannerModel(Integer id, String title, Integer status, String image_path, Integer action, String url) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.image_path = image_path;
        this.action = action;
        this.url = url;
    }

    public BannerModel(String title, Integer status, String image_path, Integer action, String url) {
        this.title = title;
        this.status = status;
        this.image_path = image_path;
        this.action = action;
        this.url = url;
    }

    public BannerModel(String title, Integer status, String image_path) {
        this.title = title;
        this.status = status;
        this.image_path = image_path;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
