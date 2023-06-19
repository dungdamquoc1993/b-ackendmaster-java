package com.vinplay.item;

import java.util.ArrayList;
import java.util.List;

public class ModelItem implements Cloneable,java.io.Serializable{
	private static final long serialVersionUID = 6621595656579632446L;
	private int id;
	private int model;
	private String lotteryid;
	private int methodid;
	private String methodname;
	private int levelnum;
	private Double level1;
	private Double level2;
	private Double level3;
	private Double level4;
	private Double level5;
	private Double level6;
	private String checkflag;
	private String openflag;
	private String lname1;
	private String lname2;
	private String lname3;
	private String lname4;
	private String lname5;
	private String lname6;
	private Double maxvalue1;
	private Double maxvalue2;
	private Double maxvalue3;
	private Double maxvalue4;
	private Double maxvalue5;
	private Double maxvalue6;
	private Integer payprizenum;

	private int active=1;//1 is on, 0 is off
	
	private List<String> namelist = new ArrayList<String>();
	private List<Double> prizelist = new ArrayList<Double>();
	private List<Double> prizefdlist = new ArrayList<Double>();
	
	private Double maxbet;
	private Double minbet;
	public Object clone(){
		ModelItem o = null;
        try{
            o = (ModelItem)super.clone();
        }catch(CloneNotSupportedException e){
            //logger.error("ex", ex);
        }
        return o;
    }
	
	public String getMethodname() {
		return methodname;
	}
	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getModel() {
		return model;
	}
	public void setModel(int model) {
		this.model = model;
	}
	public int getMethodid() {
		return methodid;
	}
	public void setMethodid(int methodid) {
		this.methodid = methodid;
	}
	public String getLotteryid() {
		return lotteryid;
	}
	public void setLotteryid(String lotteryid) {
		this.lotteryid = lotteryid;
	}
	public int getLevelnum() {
		return levelnum;
	}
	public void setLevelnum(int levelnum) {
		this.levelnum = levelnum;
	}
	public String getCheckflag() {
		return checkflag;
	}
	public void setCheckflag(String checkflag) {
		this.checkflag = checkflag;
	}
	public String getOpenflag() {
		return openflag;
	}
	public void setOpenflag(String openflag) {
		this.openflag = openflag;
	}
	public String getLname1() {
		return lname1;
	}
	public void setLname1(String lname1) {
		this.lname1 = lname1;
	}
	public String getLname2() {
		return lname2;
	}
	public void setLname2(String lname2) {
		this.lname2 = lname2;
	}
	public String getLname3() {
		return lname3;
	}
	public void setLname3(String lname3) {
		this.lname3 = lname3;
	}
	public String getLname4() {
		return lname4;
	}
	public void setLname4(String lname4) {
		this.lname4 = lname4;
	}
	public String getLname5() {
		return lname5;
	}
	public void setLname5(String lname5) {
		this.lname5 = lname5;
	}
	public String getLname6() {
		return lname6;
	}
	public void setLname6(String lname6) {
		this.lname6 = lname6;
	}
	public List<String> getNamelist() {
		if(namelist.size()==0 && this.getMethodname()!=null){
			for(int i=0;i<this.getLevelnum();i++){
				if(i==0){
					namelist.add(this.getLname1());	
				}else if(i==1){
					namelist.add(this.getLname2());	
				}else if(i==2){
					namelist.add(this.getLname3());	
				}else if(i==3){
					namelist.add(this.getLname4());	
				}else if(i==4){
					namelist.add(this.getLname5());	
				}else if(i==5){
					namelist.add(this.getLname6());	
				}
			}
		}
		return namelist;
	}
	public void setNamelist(List<String> namelist) {
		this.namelist = namelist;
	}
	public void putUserPoint(double ud){
		prizelist.clear();
		prizefdlist.clear();
		for(int i=0;i<this.getLevelnum();i++){
			if(i==0){
				prizefdlist.add(this.getLevel1());
				//MathBrian.scale(2, MathBrian.add(this.getLevel1(), MathBrian.mul(this.getMaxvalue1(), ud)));
				prizelist.add(MathBrian.scale(2, MathBrian.add(this.getLevel1(), MathBrian.mul(this.getMaxvalue1(), ud))));
			}else if(i==1){
				prizefdlist.add(this.getLevel2());
				prizelist.add(MathBrian.scale(2, MathBrian.add(this.getLevel2(), MathBrian.mul(this.getMaxvalue2(), ud))));
			}else if(i==2){
				prizefdlist.add(this.getLevel3());
				prizelist.add(MathBrian.scale(2, MathBrian.add(this.getLevel3(), MathBrian.mul(this.getMaxvalue3(), ud))));
			}else if(i==3){
				prizefdlist.add(this.getLevel4());
				prizelist.add(MathBrian.scale(2, MathBrian.add(this.getLevel4(), MathBrian.mul(this.getMaxvalue4(), ud))));
			}else if(i==4){
				prizefdlist.add(this.getLevel5());
				prizelist.add(MathBrian.scale(2, MathBrian.add(this.getLevel5(), MathBrian.mul(this.getMaxvalue5(), ud))));
			}else if(i==5){
				prizefdlist.add(this.getLevel6());
				prizelist.add(MathBrian.scale(2, MathBrian.add(this.getLevel6(), MathBrian.mul(this.getMaxvalue6(), ud))));
			}
		}
	}
	public List<Double> getPrizelist() {
		return prizelist;
	}
	public void setPrizelist(List<Double> prizelist) {
		this.prizelist = prizelist;
	}
	public Double getLevel1() {
		return level1;
	}
	public void setLevel1(Double level1) {
		this.level1 = level1;
	}
	public Double getLevel2() {
		return level2;
	}
	public void setLevel2(Double level2) {
		this.level2 = level2;
	}
	public Double getLevel3() {
		return level3;
	}
	public void setLevel3(Double level3) {
		this.level3 = level3;
	}
	public Double getLevel4() {
		return level4;
	}
	public void setLevel4(Double level4) {
		this.level4 = level4;
	}
	public Double getLevel5() {
		return level5;
	}
	public void setLevel5(Double level5) {
		this.level5 = level5;
	}
	public Double getLevel6() {
		return level6;
	}
	public void setLevel6(Double level6) {
		this.level6 = level6;
	}
	public Double getMaxvalue1() {
		return maxvalue1;
	}
	public void setMaxvalue1(Double maxvalue1) {
		this.maxvalue1 = maxvalue1;
	}
	public Double getMaxvalue2() {
		return maxvalue2;
	}
	public void setMaxvalue2(Double maxvalue2) {
		this.maxvalue2 = maxvalue2;
	}
	public Double getMaxvalue3() {
		return maxvalue3;
	}
	public void setMaxvalue3(Double maxvalue3) {
		this.maxvalue3 = maxvalue3;
	}
	public Double getMaxvalue4() {
		return maxvalue4;
	}
	public void setMaxvalue4(Double maxvalue4) {
		this.maxvalue4 = maxvalue4;
	}
	public Double getMaxvalue5() {
		return maxvalue5;
	}
	public void setMaxvalue5(Double maxvalue5) {
		this.maxvalue5 = maxvalue5;
	}
	public Double getMaxvalue6() {
		return maxvalue6;
	}
	public void setMaxvalue6(Double maxvalue6) {
		this.maxvalue6 = maxvalue6;
	}

	public List<Double> getPrizefdlist() {
		return prizefdlist;
	}
	public void setPrizefdlist(List<Double> prizefdlist) {
		this.prizefdlist = prizefdlist;
	}
	public Double getMaxbet() {
		return maxbet;
	}
	public void setMaxbet(Double maxbet) {
		this.maxbet = maxbet;
	}
	public Double getMinbet() {
		return minbet;
	}
	public void setMinbet(Double minbet) {
		this.minbet = minbet;
	}
	public Integer getPayprizenum() {
		return payprizenum;
	}
	public void setPayprizenum(Integer payprizenum) {
		this.payprizenum = payprizenum;
	}
	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
}
