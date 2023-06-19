package com.vinplay.api.backend.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinplay.vbee.common.response.BaseResponseModel;

public class TongCuocResponse{

    private Long moneyWinCasino;
    private Long moneyWinSport;
    private Long moneyWinMyGame;
    private Long moneyLiveCasino;
    private Long moneySport;
    private Long moneyMyGame;
    private Long tongCuoc;
    private Long tongThang;


    public Long getMoneyWinCasino() {
        return moneyWinCasino;
    }

    public void setMoneyWinCasino(Long moneyWinCasino) {
        this.moneyWinCasino = moneyWinCasino;
    }

    public Long getMoneyWinSport() {
        return moneyWinSport;
    }

    public void setMoneyWinSport(Long moneyWinSport) {
        this.moneyWinSport = moneyWinSport;
    }

    public Long getMoneyWinMyGame() {
        return moneyWinMyGame;
    }

    public void setMoneyWinMyGame(Long moneyWinMyGame) {
        this.moneyWinMyGame = moneyWinMyGame;
    }

    public Long getMoneyLiveCasino() {
        return moneyLiveCasino;
    }

    public void setMoneyLiveCasino(Long moneyLiveCasino) {
        this.moneyLiveCasino = moneyLiveCasino;
    }

    public Long getMoneySport() {
        return moneySport;
    }

    public void setMoneySport(Long moneySport) {
        this.moneySport = moneySport;
    }

    public Long getMoneyMyGame() {
        return moneyMyGame;
    }

    public void setMoneyMyGame(Long moneyMyGame) {
        this.moneyMyGame = moneyMyGame;
    }

    public Long getTongCuoc() {
        return tongCuoc;
    }

    public void setTongCuoc(Long tongCuoc) {
        this.tongCuoc = tongCuoc;
    }

    public Long getTongThang() {
        return tongThang;
    }

    public void setTongThang(Long tongThang) {
        this.tongThang = tongThang;
    }
}
