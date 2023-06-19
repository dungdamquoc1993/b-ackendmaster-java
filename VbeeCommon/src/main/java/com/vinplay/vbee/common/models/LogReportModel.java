package com.vinplay.vbee.common.models;

import java.io.Serializable;

public class LogReportModel implements Serializable {
    private static final long serialVersionUID = 112358;

    public long id = -1;
    public String time;
    public String nick_name;

    public long wm= 0;
    public long wm_win= 0;

    public long ibc= 0;
    public long ibc_win= 0;

    public long ag= 0;
    public long ag_win= 0;
    
    public long cmd= 0;
    public long cmd_win= 0;

    public long tlmn= 0;
    public long tlmn_win= 0;

    public long bacay= 0;
    public long bacay_win= 0;

    public long xocdia= 0;
    public long xocdia_win= 0;

    public long minipoker= 0;
    public long minipoker_win= 0;

    public long slot_pokemon= 0;
    public long slot_pokemon_win= 0;

    public long slot_galaxy= 0;
    public long slot_galaxy_win= 0;

    public long baucua= 0;
    public long baucua_win= 0;

    public long taixiu= 0;
    public long taixiu_win= 0;

    public long caothap= 0;
    public long caothap_win= 0;

    public long slot_bitcoin= 0;
    public long slot_bitcoin_win= 0;

    public long slot_taydu= 0;
    public long slot_taydu_win= 0;

    public long slot_angrybird= 0;
    public long slot_angrybird_win= 0;

    public long slot_thantai= 0;
    public long slot_thantai_win= 0;

    public long slot_thethao= 0;
    public long slot_thethao_win= 0;
    
    public long slot_chiemtinh= 0;
    public long slot_chiemtinh_win= 0;
    
    public long taixiu_st= 0;
    public long taixiu_st_win= 0;
    
    public long fish= 0;
    public long fish_win= 0;
    
    public long slot_thanbai= 0;
    public long slot_thanbai_win= 0;
    
    public long ebet= 0;
    public long ebet_win= 0;
    
    public long sbo= 0;
    public long sbo_win= 0;

    public long slot_bikini= 0;
    public long slot_bikini_win= 0;
    
    public long deposit= 0;//tổng nạp
    public long withdraw= 0;//tổng rút
    
    public long totalBonus= 0;// tổng bonus

	public long totalRefund= 0;// tổng hoàn trả
	
	public String code;//ma dai ly
	public long attendance;//diem danh
	
    public long getMoneyLiveCasino(){
        return this.wm + this.ag +this.ebet;
    }

	public long getMoneySport() {
		return this.ibc + this.cmd + this.sbo;
	}

    public long getMoneyMyGame(){
        return this.bacay 
        		+ this.xocdia 
        		+ this.minipoker 
        		+ this.slot_pokemon 
        		+ this.baucua 
        		+ this.taixiu 
        		+ this.caothap 
        		+ this.slot_bitcoin 
                + this.slot_taydu 
                + this.slot_angrybird 
                + this.slot_thantai 
                + this.slot_thethao
                + this.slot_chiemtinh
                + Math.abs(this.tlmn) 
                + Math.abs(this.taixiu_st)
                + Math.abs(this.fish)
                + Math.abs(this.slot_thanbai)
                + Math.abs(this.slot_bikini)
                + this.slot_galaxy;
    }

    public long getMoneyWinCasino(){
        return this.wm_win + this.ag_win;
    }

    public long getMoneyWinSport(){
        return this.ibc_win+this.cmd_win;
    }

    public long getMoneyWinMyGame(){
        return this.bacay_win 
        		+ this.xocdia_win 
        		+ this.minipoker_win 
        		+ this.slot_pokemon_win 
        		+ this.baucua_win 
        		+ this.taixiu_win 
                + this.caothap_win 
                + this.slot_bitcoin_win 
                + this.slot_taydu_win 
                + this.slot_angrybird_win 
                + this.slot_thantai_win 
                + this.slot_thethao_win  
                + this.tlmn_win
                + this.slot_chiemtinh_win
                + this.taixiu_st_win
                + this.fish_win
                + this.slot_thanbai_win
                + this.slot_bikini_win
                + this.slot_galaxy_win;
    }
}
