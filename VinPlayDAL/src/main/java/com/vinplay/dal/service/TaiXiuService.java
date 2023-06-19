package com.vinplay.dal.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeoutException;
import com.vinplay.vbee.common.models.cache.ThanhDuTXModel;

import com.vinplay.dal.entities.report.ReportMoneySystemModel;
import com.vinplay.dal.entities.taixiu.ResultTaiXiu;
import com.vinplay.dal.entities.taixiu.TransactionTaiXiu;
import com.vinplay.dal.entities.taixiu.TransactionTaiXiuDetail;
import com.vinplay.vbee.common.models.minigame.TopWin;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
public interface TaiXiuService {
    public boolean saveTransactionTaiXiu(long var1, int var3, String var4, int var5, long var6, short var8, long var9, long var11, long jp) throws IOException, TimeoutException, InterruptedException;

    public boolean saveTransactionTaiXiu(List<TransactionTaiXiu> var1) throws IOException, TimeoutException, InterruptedException;

    public boolean saveTransactionTaiXiuDetails(List<TransactionTaiXiuDetail> var1) throws IOException, TimeoutException, InterruptedException;

    public boolean saveTransactionTaiXiuDetail(TransactionTaiXiuDetail var1) throws IOException, TimeoutException, InterruptedException;

    public boolean updateTransactionTaiXiuDetail(TransactionTaiXiuDetail var1) throws IOException, TimeoutException, InterruptedException;

    public boolean saveResultTaiXiu(long var1, int var3, int var4, int var5, int var6, long var7, long var9, int var11, int var12, long var13, long var15, long var17, long var19, int var21, long jp) throws Exception;
	public boolean saveResultTaiXiu(long var1, int var3, int var4, int var5, int var6, long var7, long var9, int var11, int var12, long var13, long var15, long var17, long var19, int var21) throws Exception;
	public boolean saveResultTaiXiu(long var1, int var3, int var4, int var5, int var6, long var7, long var9, int var11, int var12, long var13, long var15, long var17, long var19, int var21, String var22, String var23) throws Exception;

    public boolean saveResultTaiXiu(ResultTaiXiu var1) throws Exception;
	    public void calculateThanhDu(long var1, List<TransactionTaiXiu> var3, int var4) throws IOException, TimeoutException, InterruptedException;

    public List<ThanhDuTXModel> getTopThanhDuDaily(String var1, int var2) throws SQLException;

    public List<ThanhDuTXModel> getTopThanhDuMonthly(String var1, int var2) throws SQLException, ParseException;


    public String getLichSuPhien(int var1, int var2) throws SQLException;

    public List<ResultTaiXiu> getListLichSuPhien(int var1, int var2) throws SQLException;

    public ResultTaiXiu getKetQuaPhien(long var1, int var3) throws SQLException;

    public List<TopWin> getTopWin(int var1) throws SQLException;

    public List<TransactionTaiXiu> getLichSuGiaoDich(String var1, int var2, int var3) throws SQLException;

    public int countLichSuGiaoDich(String var1, int var2) throws SQLException;

    public List<TransactionTaiXiuDetail> getChiTietPhienTX(long var1, int var3) throws SQLException;

    public boolean updatePot(long var1, String var3);

    public boolean updateFund(long var1, String var3);

    public void updateAllTop();

    public ReportMoneySystemModel getReportTXToday();

    public ReportMoneySystemModel getReportTX(int var1);

    public void setKetQuaTaiXiu(short[] ketQuaTaiXiu);

    public short[] getKetQuaTaiXiu();

    public short[] suaKetQuaTaiXiu();
}

