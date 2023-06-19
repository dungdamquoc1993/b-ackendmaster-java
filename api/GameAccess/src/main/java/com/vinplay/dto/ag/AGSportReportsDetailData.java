package com.vinplay.dto.ag;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "row")
@XmlAccessorType(XmlAccessType.FIELD)
public class AGSportReportsDetailData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3420185092655126036L;

	@XmlAttribute
	private String billno;
	
	@XmlAttribute
	private String productid;
	
	@XmlAttribute
	private String username;
	
	@XmlAttribute
	private String billtime; //bet time
	
	@XmlAttribute
	private String reckontime; //payout time
	
	@XmlAttribute
	private String currency;
	
	@XmlAttribute
	private String gametype;
	
	@XmlAttribute
	private String betIP;
	
	@XmlAttribute
	private String account; //bet amount
	
	@XmlAttribute
	private String cus_account; //payout amount
	
	@XmlAttribute
	private String valid_account; //valid bet amount
	
	@XmlAttribute
	private String flag;
	
	@XmlAttribute
	private String platformtype;
	
	@XmlAttribute
	private String odds;
	
	@XmlAttribute
	private String competition;
	
	@XmlAttribute
	private String market;
	
	@XmlAttribute
	private String selection;
	
	@XmlAttribute
	private String simplified_result;
	
	@XmlAttribute
	private String sport;
	
	@XmlAttribute
	private String category;
	
	@XmlAttribute
	private String extbillno;
	
	@XmlAttribute
	private String thirdbillno;
	
	@XmlAttribute
	private String bettype;
	
	@XmlAttribute
	private String system;
	
	@XmlAttribute
	private String live;
	
	@XmlAttribute
	private String current_score;

	public String getBillno() {
		return billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public String getProductid() {
		return productid;
	}

	public void setProductid(String productid) {
		this.productid = productid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBilltime() {
		return billtime;
	}

	public void setBilltime(String billtime) {
		this.billtime = billtime;
	}

	public String getReckontime() {
		return reckontime;
	}

	public void setReckontime(String reckontime) {
		this.reckontime = reckontime;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getGametype() {
		return gametype;
	}

	public void setGametype(String gametype) {
		this.gametype = gametype;
	}

	public String getBetIP() {
		return betIP;
	}

	public void setBetIP(String betIP) {
		this.betIP = betIP;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getCus_account() {
		return cus_account;
	}

	public void setCus_account(String cus_account) {
		this.cus_account = cus_account;
	}

	public String getValid_account() {
		return valid_account;
	}

	public void setValid_account(String valid_account) {
		this.valid_account = valid_account;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getPlatformtype() {
		return platformtype;
	}

	public void setPlatformtype(String platformtype) {
		this.platformtype = platformtype;
	}

	public String getOdds() {
		return odds;
	}

	public void setOdds(String odds) {
		this.odds = odds;
	}

	public String getCompetition() {
		return competition;
	}

	public void setCompetition(String competition) {
		this.competition = competition;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public String getSelection() {
		return selection;
	}

	public void setSelection(String selection) {
		this.selection = selection;
	}

	public String getSimplified_result() {
		return simplified_result;
	}

	public void setSimplified_result(String simplified_result) {
		this.simplified_result = simplified_result;
	}

	public String getSport() {
		return sport;
	}

	public void setSport(String sport) {
		this.sport = sport;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getExtbillno() {
		return extbillno;
	}

	public void setExtbillno(String extbillno) {
		this.extbillno = extbillno;
	}

	public String getThirdbillno() {
		return thirdbillno;
	}

	public void setThirdbillno(String thirdbillno) {
		this.thirdbillno = thirdbillno;
	}

	public String getBettype() {
		return bettype;
	}

	public void setBettype(String bettype) {
		this.bettype = bettype;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getLive() {
		return live;
	}

	public void setLive(String live) {
		this.live = live;
	}

	public String getCurrent_score() {
		return current_score;
	}

	public void setCurrent_score(String current_score) {
		this.current_score = current_score;
	}
}
