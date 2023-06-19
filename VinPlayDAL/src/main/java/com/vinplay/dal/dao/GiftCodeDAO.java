package com.vinplay.dal.dao;

import com.vinplay.dal.entities.giftcode.GiftCodeModel;

import java.sql.SQLException;
import java.util.List;

public interface GiftCodeDAO {
    public List<GiftCodeModel> showListGiftCode(String giftcode,String user_name,String created_by,Integer event,String startTime,String endTime,int page,int maxItem) throws SQLException;

    public Long countGiftCode(String giftcode,String user_name,String created_by,Integer event,String startTime,String endTime) throws SQLException;

    public Long countValueGiftCode(String giftcode,String user_name,String created_by,Integer event,String startTime,String endTime) throws SQLException;
}
