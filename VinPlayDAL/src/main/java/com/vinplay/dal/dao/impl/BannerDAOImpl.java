package com.vinplay.dal.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.vinplay.dal.dao.BannerDAO;
import com.vinplay.dal.entities.banner.BannerModel;
import com.vinplay.vbee.common.pools.ConnectionPool;

public class BannerDAOImpl implements BannerDAO {
	
    @Override
    public long countlistBanner(String title, Integer status, String image_path, Integer action, String url) {
        long count = 0;
        try(Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname")) {
            int index = 1;
            Boolean b_title = (title==null || title.trim().isEmpty());
            Boolean b_image_path = (image_path==null || image_path.trim().isEmpty());
            Boolean b_status = (status==null);
            Boolean b_url = (url==null || url.trim().isEmpty());
            Boolean b_action = (action==null);
            String sql = "Select count(*) as cnt from vinplay.banner where 1=1 "
                    +(b_title ? "" : (" and title = ?"))
                    +(b_status ? "" : (" and status = ?"))
                    +(b_image_path ? "" : (" and image_path = ?"))
                    +(b_action ? "" : (" and action = ?"))
                    +(b_url ? "" : (" and url = ?"));
            PreparedStatement stmt = conn.prepareStatement(sql);
            if(!b_title){
                stmt.setString(index++, title);
            }
            if(!b_status){
                stmt.setInt(index++, status);
            }
            if(!b_image_path){
                stmt.setString(index++, image_path);
            }
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt("cnt");
            }
            if(!b_action){
                stmt.setInt(index++, action);
            }
            if(!b_url){
                stmt.setString(index++, url);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public List<BannerModel> listBanner(String title, Integer status, String image_path, Integer action, String url, int page, int maxItem) {
        List<BannerModel> banners = new ArrayList<>();
        Boolean b_title = (title==null || title.trim().isEmpty());
        Boolean b_image_path = (image_path==null || image_path.trim().isEmpty());
        Boolean b_status = (status==null);
        Boolean b_url = (url==null || url.trim().isEmpty());
        Boolean b_action = (action==null);
        String sql = "Select * from vinplay.banner where 1=1 "
                +(b_title ? "" : (" and title = ?"))
                +(b_status ? "" : (" and status = ?"))
                +(b_image_path ? "" : (" and image_path = ?"))
                +(b_action ? "" : (" and action = ?"))
                +(b_url ? "" : (" and url = ?"))
                +" order by id desc limit ?,?";
        try(Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
        		PreparedStatement stmt = conn.prepareStatement(sql);) {
            page = (page - 1) < 0 ? 0 : (page - 1);
            int index = 1;
            
            if(!b_title){
                stmt.setString(index++, title);
            }
            if(!b_status){
                stmt.setInt(index++, status);
            }
            if(!b_image_path){
                stmt.setString(index++, image_path);
            }
            if(!b_action){
                stmt.setInt(index++, action);
            }
            if(!b_url){
                stmt.setString(index++, url);
            }
            stmt.setInt(index++, page * maxItem);
            stmt.setInt(index, maxItem);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                BannerModel bannerModel = new BannerModel();
                bannerModel.setId(rs.getInt("id"));
                bannerModel.setTitle(rs.getString("title"));
                bannerModel.setStatus(rs.getInt("status"));
                bannerModel.setImage_path(rs.getString("image_path"));
                bannerModel.setAction(rs.getInt("action"));
                bannerModel.setUrl(rs.getString("url"));
                banners.add(bannerModel);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return banners;
    }

    @Override
    public BannerModel BannerDetail(Integer id) {
        BannerModel bannerModel = new BannerModel();
        try(Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");) {
            String sql = "Select * from vinplay.banner where id = ? ";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                bannerModel.setId(rs.getInt("id"));
                bannerModel.setTitle(rs.getString("title"));
                bannerModel.setStatus(rs.getInt("status"));
                bannerModel.setImage_path(rs.getString("image_path"));
                bannerModel.setAction(rs.getInt("action"));
                bannerModel.setUrl(rs.getString("url"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bannerModel;
    }

    @Override
    public Boolean addNewBanner(String title, Integer status, String image_path, Integer action, String url) {
        String sql = "INSERT INTO vinplay.banner (title, status, image_path, action, url) VALUES(?,?,?,?,?)";
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");
        		 PreparedStatement stm = conn.prepareStatement(sql);){
            if(title == null) title = "";
            if(image_path == null) image_path = "";
            if(action == null) action = -1;
            if(url == null) url = "";
            stm.setString(1, title);
            stm.setInt(2, status);
            stm.setString(3, image_path);
            stm.setInt(4, action);
            stm.setString(5, url);
            stm.executeUpdate();
            stm.close();
        }
        catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean updateBannerById(Integer id, String title, Integer status, String image_path, Integer action, String url) {
        String sql = "UPDATE banner SET " +
                ((title == null || title.trim().isEmpty()) ? "" : "title = ?") +
                (status != null ? ", " : " ") +
                ((status == null ) ? "" : "status = ?") +
                ((image_path != null && !image_path.trim().isEmpty()) ? ", " : " ") +
                ((image_path == null || image_path.trim().isEmpty()) ? "" : "image_path = ?") +
                (action != null ? ", " : " ") +
                ((action == null ) ? "" : "action = ?") +
                ((url != null && !url.trim().isEmpty()) ? ", " : " ") +
                ((url == null || url.trim().isEmpty()) ? "" : "url = ?") +
                "WHERE id = ?";
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname")) {
            PreparedStatement stm = conn.prepareStatement(sql);
            int index = 1;
            if(title != null && !title.trim().isEmpty()) {
                stm.setString(index++, title);
            }
            if(status != null) {
                stm.setInt(index++, status);
            }
            if(image_path != null && !image_path.trim().isEmpty()) {
                stm.setString(index++, image_path);
            }
            if(action != null) {
                stm.setInt(index++, action);
            }
            if(url != null && !url.trim().isEmpty()) {
                stm.setString(index++, url);
            }
            stm.setInt(index++, id);
            stm.executeUpdate();
        }catch (SQLException e){
            return false;
        }
        return true;
    }

    @Override
    public Boolean updateBannerByTitle(String title, Integer status, String image_path, Integer action, String url) {
        String sql = "UPDATE banner SET " +
                ((status == null ) ? "" : "status = ?") +
                ((image_path != null && !image_path.trim().isEmpty()) ? ", " : " ") +
                ((image_path == null || image_path.trim().isEmpty()) ? "" : "image_path = ?") +
                (action != null ? ", " : " ") +
                ((action == null ) ? "" : "action = ?") +
                ((url != null && !url.trim().isEmpty()) ? ", " : " ") +
                ((url == null || url.trim().isEmpty()) ? "" : "url = ?") +
                "WHERE title = ?";
        try (Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname")) {
            PreparedStatement stm = conn.prepareStatement(sql);
            int index = 1;
            if(status != null) {
                stm.setInt(index++, status);
            }
            if(image_path != null && !image_path.trim().isEmpty()) {
                stm.setString(index++, image_path);
            }
            if(action != null) {
                stm.setInt(index++, action);
            }
            if(url != null && !url.trim().isEmpty()) {
                stm.setString(index++, url);
            }
            stm.setString(index++, title);
            stm.executeUpdate();
        }catch (SQLException e){
            return false;
        }
        return true;
    }

    @Override
    public Boolean deleteBanner(Integer id) {
        String sql = "DELETE FROM vinplay.banner where id = ?";
        try(Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");) {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, id);
            stm.executeUpdate();
            stm.close();
            if (conn != null) {
                conn.close();
            }
        }
        catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean deleteBanner(String title) {
        String sql = "DELETE FROM vinplay.banner where title = ?";
        try(Connection conn = ConnectionPool.getInstance().getConnection("mysqlpoolname");) {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, title);
            stm.executeUpdate();
            stm.close();
            if (conn != null) {
                conn.close();
            }
        }
        catch (SQLException e) {
            return false;
        }
        return true;
    }
}
