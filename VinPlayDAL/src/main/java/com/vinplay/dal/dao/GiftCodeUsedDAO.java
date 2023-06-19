package com.vinplay.dal.dao;

import com.vinplay.dal.entities.giftcode.GiftCodeUsedModel;

import java.sql.SQLException;
import java.util.List;

public interface GiftCodeUsedDAO {

    public List<GiftCodeUsedModel> showListGiftCodeUsed(String gid, String nickname, Integer type, Integer event, int flagtime, String startTime, String endTime,int page,int maxItem) throws SQLException;

    public long countGiftCodeUsed(String gid, String nickname, Integer type, Integer event, int flagtime, String startTime, String endTime) throws SQLException;

    public long countValueGiftCodeUsed(String gid, String nickname, Integer type, Integer event, int flagtime, String startTime, String endTime) throws SQLException;
}
