package com.vinplay.item;

public class CurScheduleItem implements java.io.Serializable{
	private static final long serialVersionUID = -8493659699585305760L;
	private int lotteryid;
	private String issue;
	private String starttime;
	private String endtime;
	private int second;
	private String opentime;
	private String sysopentime;
	private int left;
	private int sale;
	private String historyissue;
	private String winnumber;
	private int status=0;			//前一条记录的状录,非当前记录
	private String hissysopentime;	//系统抓开奖号的时
	private int collectnum=0;		//系统抓取次数
	
	private String hisopentime;
	private String hisendtime;
	
	private String w1;//个位
	private String w2;
	private String w3;
	private String w4;
	private String w5;//万位
	
	public CurScheduleItem(){
	}
	public CurScheduleItem(int n){
		lotteryid = n;
	}
	public int getLotteryid() {
		return lotteryid;
	}
	public void setLotteryid(int lotteryid) {
		this.lotteryid = lotteryid;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public int getSecond() {
		return second;
	}
	public void setSecond(int second) {
		this.second = second;
	}
	public String getOpentime() {
		return opentime;
	}
	public void setOpentime(String opentime) {
		this.opentime = opentime;
	}
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public int getSale() {
		return sale;
	}
	public void setSale(int sale) {
		this.sale = sale;
	}
	public String getHistoryissue() {
		return historyissue;
	}
	public void setHistoryissue(String historyissue) {
		this.historyissue = historyissue;
	}
	public String getWinnumber() {
		return winnumber;
	}
	public void setWinnumber(String winnumber) {
		this.winnumber = winnumber;
	}
	public String getSysopentime() {
		return sysopentime;
	}
	public void setSysopentime(String sysopentime) {
		this.sysopentime = sysopentime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getHissysopentime() {
		return hissysopentime;
	}
	public void setHissysopentime(String hissysopentime) {
		this.hissysopentime = hissysopentime;
	}
	public int getCollectnum() {
		return collectnum;
	}
	public void setCollectnum(int collectnum) {
		this.collectnum = collectnum;
	}
	public String getHisopentime() {
		return hisopentime;
	}
	public void setHisopentime(String hisopentime) {
		this.hisopentime = hisopentime;
	}
	public String getHisendtime() {
		return hisendtime;
	}
	public void setHisendtime(String hisendtime) {
		this.hisendtime = hisendtime;
	}
	public String getW1() {
		if(winnumber!=null && winnumber.length()>0){
			if(winnumber.length()==10){
				w1 = winnumber.substring(winnumber.length()-2, winnumber.length());
			}else{
				w1 = winnumber.substring(winnumber.length()-1, winnumber.length());
			}
		}else{
			w1="x";
		}
		return w1;
	}
	public void setW1(String w1) {
		this.w1 = w1;
	}
	public String getW2() {
		if(winnumber!=null && winnumber.length()>1){
			if(winnumber.length()==10){
				w2 = winnumber.substring(winnumber.length()-4, winnumber.length()-2);
			}else{
				w2 = winnumber.substring(winnumber.length()-2, winnumber.length()-1);
			}
		}else{
			w2="x";
		}
		return w2;
	}
	public void setW2(String w2) {
		this.w2 = w2;
	}
	public String getW3() {
		if(winnumber!=null && winnumber.length()>2){
			if(winnumber.length()==10){
				w3 = winnumber.substring(winnumber.length()-6, winnumber.length()-4);
			}else{
				w3 = winnumber.substring(winnumber.length()-3, winnumber.length()-2);
			}
		}else{
			w3="x";
		}
		return w3;
	}
	public void setW3(String w3) {
		this.w3 = w3;
	}
	public String getW4() {
		if(winnumber!=null && winnumber.length()>3){
			if(winnumber.length()==10){
				w4 = winnumber.substring(winnumber.length()-8, winnumber.length()-6);
			}else{
				w4 = winnumber.substring(winnumber.length()-4, winnumber.length()-3);
			}
			
		}else{
			w4="x";
		}
		return w4;
	}
	public void setW4(String w4) {
		this.w4 = w4;
	}
	public String getW5() {
		if(winnumber!=null && winnumber.length()>4){
			if(winnumber.length()==10){
				w5 = winnumber.substring(winnumber.length()-10, winnumber.length()-8);
			}else{
				w5 = winnumber.substring(winnumber.length()-5, winnumber.length()-4);
			}
		}else{
			w5="x";
		}
		return w5;
	}
	public void setW5(String w5) {
		this.w5 = w5;
	}
}
