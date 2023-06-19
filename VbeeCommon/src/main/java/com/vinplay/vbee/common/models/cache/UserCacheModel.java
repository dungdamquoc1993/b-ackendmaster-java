package com.vinplay.vbee.common.models.cache;

import com.vinplay.vbee.common.models.UserModel;
import java.io.Serializable;
import java.util.Date;

public class UserCacheModel extends UserModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private String accessToken;
	private Date lastActive;
	private long lastMessageId;
	private int online;
	private int cashout;
	private Date cashoutTime;
	private String ip;
	private String otpApp;
	private String campaign;
	private String medium;
	private String source;
	private int iapInDay;
	private Date iapTime;
	private int codeGameFail;
	private Date codeGameFailTime;
	private Date addVPTime;
	private Date subVPTime;
	private Date addVPVinTime;
	private int vpEvent;
	private int vpEventReal;
	private int vpAdd;
	private int vpSub;
	private int numAdd;
	private int numSub;
	private int place;
	private int placeMax;
	private Date lastActiveVin;
	private Date lastActiveXu;
	private String Platfrom;
	private long exchangeMoneyInDay;
	private Date exchangeMoneyTime;
	private boolean isVerifyMobile;
	private int usertype;
	private int rechargeFail;
	private Date rechargeFailTime;
	
	public boolean isVerifyMobile() {
		return isVerifyMobile;
	}

	public void setVerifyMobile(boolean isVerifyMobile) {
		this.isVerifyMobile = isVerifyMobile;
	}

	public UserCacheModel() {
	}

	public UserCacheModel(int id, String username, String nickname, String password, String email, String facebookId,
			String googleId, String mobile, String birthday, boolean gender, String address, long vin, long xu,
			long vinTotal, long xuTotal, long safe, long rechargeMoney, int vippoint, int daily, int status,
			String avatar, String identification, int vippointSave, Date createTime, int moneyVP, Date securityTime,
			long loginOtp, boolean bot, boolean isVerifyMobile, int cashout, Date cashoutTime, int vpEvent, int vpEventReal, int vpAdd,
			int vpSub, int numAdd, int numSub, int place, int placeMax,int usertype,String referralCode) {
		super(id, username, nickname, password, email, facebookId, googleId, mobile, birthday, gender, address, vin, xu,
				vinTotal, xuTotal, safe, rechargeMoney, vippoint, daily, status, avatar, identification, vippointSave,
				createTime, moneyVP, securityTime, loginOtp, bot, isVerifyMobile, usertype,referralCode);
		this.cashout = cashout;
		this.cashoutTime = cashoutTime;
		this.vpEvent = vpEvent;
		this.vpEventReal = vpEventReal;
		this.vpAdd = vpAdd;
		this.vpSub = vpSub;
		this.numAdd = numAdd;
		this.numSub = numSub;
		this.place = place;
		this.placeMax = placeMax;
		this.isVerifyMobile = isVerifyMobile;
		this.usertype = usertype;
		this.setReferralCode(referralCode);
	}

	public int getUsertype() {
		return usertype;
	}

	public void setUsertype(int usertype) {
		this.usertype = usertype;
	}

	public void setPlatfrom(String value) {
		this.Platfrom = value;
	}

	public String getPlatfrom() {
		return this.Platfrom;
	}

	public long getExchangeMoneyInDay() {
		return this.exchangeMoneyInDay;
	}

	public void setExchangeMoneyInDay(long exchangeMoneyInDay) {
		this.exchangeMoneyInDay = exchangeMoneyInDay;
	}

	public Date getExchangeMoneyTime() {
		return this.exchangeMoneyTime;
	}

	public void setExchangeMoneyTime(Date exchangeMoneyTime) {
		this.exchangeMoneyTime = exchangeMoneyTime;
	}


	public int getPlaceMax() {
		return this.placeMax;
	}

	public void setPlaceMax(int placeMax) {
		this.placeMax = placeMax;
	}

	public String getOtpApp() {
		return this.otpApp;
	}

	public void setOtpApp(String otpApp) {
		this.otpApp = otpApp;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAccessToken() {
		return this.accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Date getLastActive() {
		return this.lastActive;
	}

	public void setLastActive(Date lastActive) {
		this.lastActive = lastActive;
	}

	public long getLastMessageId() {
		return this.lastMessageId;
	}

	public void setLastMessageId(long lastMessageId) {
		this.lastMessageId = lastMessageId;
	}

	public int getOnline() {
		return this.online;
	}

	public void setOnline(int online) {
		this.online = online;
	}

	public int getCashout() {
		return this.cashout;
	}

	public void setCashout(int cashout) {
		this.cashout = cashout;
	}

	public Date getCashoutTime() {
		return this.cashoutTime;
	}

	public void setCashoutTime(Date cashoutTime) {
		this.cashoutTime = cashoutTime;
	}

	public String getCampaign() {
		return this.campaign;
	}

	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}

	public String getMedium() {
		return this.medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getIapInDay() {
		return this.iapInDay;
	}

	public void setIapInDay(int iapInDay) {
		this.iapInDay = iapInDay;
	}

	public Date getIapTime() {
		return this.iapTime;
	}

	public void setIapTime(Date iapTime) {
		this.iapTime = iapTime;
	}

	public Date getAddVPTime() {
		return this.addVPTime;
	}

	public void setAddVPTime(Date addVPTime) {
		this.addVPTime = addVPTime;
	}

	public Date getSubVPTime() {
		return this.subVPTime;
	}

	public void setSubVPTime(Date subVPTime) {
		this.subVPTime = subVPTime;
	}

	public Date getAddVPVinTime() {
		return this.addVPVinTime;
	}

	public void setAddVPVinTime(Date addVPVinTime) {
		this.addVPVinTime = addVPVinTime;
	}

	public int getVpEvent() {
		return this.vpEvent;
	}

	public void setVpEvent(int vpEvent) {
		this.vpEvent = vpEvent;
	}

	public int getVpEventReal() {
		return this.vpEventReal;
	}

	public void setVpEventReal(int vpEventReal) {
		this.vpEventReal = vpEventReal;
	}

	public int getVpAdd() {
		return this.vpAdd;
	}

	public void setVpAdd(int vpAdd) {
		this.vpAdd = vpAdd;
	}

	public int getVpSub() {
		return this.vpSub;
	}

	public void setVpSub(int vpSub) {
		this.vpSub = vpSub;
	}

	public int getNumAdd() {
		return this.numAdd;
	}

	public void setNumAdd(int numAdd) {
		this.numAdd = numAdd;
	}

	public int getNumSub() {
		return this.numSub;
	}

	public void setNumSub(int numSub) {
		this.numSub = numSub;
	}

	public int getPlace() {
		return this.place;
	}

	public void setPlace(int place) {
		this.place = place;
	}

	public Date getLastActiveVin() {
		return this.lastActiveVin;
	}

	public void setLastActiveVin(Date lastActiveVin) {
		this.lastActiveVin = lastActiveVin;
	}

	public Date getLastActiveXu() {
		return this.lastActiveXu;
	}

	public void setLastActiveXu(Date lastActiveXu) {
		this.lastActiveXu = lastActiveXu;
	}

	public int getCodeGameFail() {
		return this.codeGameFail;
	}

	public void setCodeGameFail(int codeGameFail) {
		this.codeGameFail = codeGameFail;
	}

	public Date getCodeGameFailTime() {
		return this.codeGameFailTime;
	}

	public void setCodeGameFailTime(Date codeGameFailTime) {
		this.codeGameFailTime = codeGameFailTime;
	}

	public Date getRechargeFailTime() {
		return this.rechargeFailTime;
	}

	public void setRechargeFailTime(Date rechargeFailTime) {
		this.rechargeFailTime = rechargeFailTime;
	}

	public int getRechargeFail() {
		return this.rechargeFail;
	}

	public void setRechargeFail(int rechargeFail) {
		this.rechargeFail = rechargeFail;
	}

}
