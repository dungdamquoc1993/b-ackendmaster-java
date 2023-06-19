package com.vinplay.vbee.common.models;


import java.sql.Timestamp;

public class HoanTraModel {
    public int id;
    public String nick_name;
    public String time;

    public int vip_point = 0;

    public long total_money_sport = 0;
    public long hoan_tra_sport = 0;

    public long total_money_casino = 0;
    public long hoan_tra_casino = 0;


    public long total_money_game = 0;
    public long hoan_tra_game = 0;

    public int vip_index = 0;

    public Boolean send_success = null;

    public Timestamp created_at = null;
    public Timestamp updated_at = null;
    public String message = null;

    public HoanTraModel(){};
    
	public boolean isHoanTra() {
		return this.total_money_casino + this.total_money_sport + this.total_money_game > 0;
	}

    public HoanTraModel(LogReportModel logReportModel, int vip_point){
        this.nick_name = logReportModel.nick_name;
        this.time = logReportModel.time;
        this.total_money_casino = logReportModel.getMoneyLiveCasino();
        this.total_money_sport = logReportModel.getMoneySport();
        this.total_money_game = logReportModel.getMoneyMyGame();
        this.vip_point = vip_point;
        this.vip_index = HoanTraVipConfig.getIndexExpVip(this.vip_point);

        this.getMoneyHoanTra();
    }

    public void getMoneyHoanTra(){
        this.hoan_tra_sport = HoanTraVipConfig.getHoanTraSport(this.vip_point, this.total_money_sport);
        this.hoan_tra_casino =  HoanTraVipConfig.getHoanTraCasino(this.vip_point, this.total_money_casino);
        this.hoan_tra_game =  HoanTraVipConfig.getHoanTraGame(this.vip_point, this.total_money_game);
    }

    @Override
    public String toString() {
        return "HoanTraModel{" +
                "id=" + id +
                ", nick_name='" + nick_name + '\'' +
                ", time='" + time + '\'' +
                ", vip_point=" + vip_point +
                ", total_money_sport=" + total_money_sport +
                ", hoan_tra_sport=" + hoan_tra_sport +
                ", total_money_casino=" + total_money_casino +
                ", hoan_tra_casino=" + hoan_tra_casino +
                ", total_money_game=" + total_money_game +
                ", hoan_tra_game=" + hoan_tra_game +
                ", vip_index=" + vip_index +
                '}';
    }
}
