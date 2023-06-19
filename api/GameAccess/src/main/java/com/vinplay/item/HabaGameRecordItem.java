package com.vinplay.item;

public class HabaGameRecordItem implements java.io.Serializable{
	private static final long serialVersionUID = -2266994443950463998L;
	/**
	 * 
	 */
	private String playerid;
	private String brandid;
	private String username;
	private String brandgameid;
	private String gamekeyname;
	
	private Integer gametypeid;
	private String dtstarted;
	private String dtcompleted;
	private String friendlygameinstanceid;
	private String gameinstanceid;
	
	private Double stake;
	private Double payout;
	private Double jackpotwin;
	private Double jackpotcontribution;
	private String currencycode;
	
	private Integer channeltypeid;
	private Double balanceafter;
	public String getPlayerid() {
		return playerid;
	}
	public void setPlayerid(String playerid) {
		this.playerid = playerid;
	}
	public String getBrandid() {
		return brandid;
	}
	public void setBrandid(String brandid) {
		this.brandid = brandid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getBrandgameid() {
		return brandgameid;
	}
	public void setBrandgameid(String brandgameid) {
		this.brandgameid = brandgameid;
	}
	public String getGamekeyname() {
		return gamekeyname;
	}
	public void setGamekeyname(String gamekeyname) {
		this.gamekeyname = gamekeyname;
	}
	public Integer getGametypeid() {
		return gametypeid;
	}
	public void setGametypeid(Integer gametypeid) {
		this.gametypeid = gametypeid;
	}
	public String getDtstarted() {
		return dtstarted;
	}
	public void setDtstarted(String dtstarted) {
		this.dtstarted = dtstarted;
	}
	public String getDtcompleted() {
		return dtcompleted;
	}
	public void setDtcompleted(String dtcompleted) {
		this.dtcompleted = dtcompleted;
	}
	public String getFriendlygameinstanceid() {
		return friendlygameinstanceid;
	}
	public void setFriendlygameinstanceid(String friendlygameinstanceid) {
		this.friendlygameinstanceid = friendlygameinstanceid;
	}
	public String getGameinstanceid() {
		return gameinstanceid;
	}
	public void setGameinstanceid(String gameinstanceid) {
		this.gameinstanceid = gameinstanceid;
	}
	public Double getStake() {
		return stake;
	}
	public void setStake(Double stake) {
		this.stake = stake;
	}
	public Double getPayout() {
		return payout;
	}
	public void setPayout(Double payout) {
		this.payout = payout;
	}
	public Double getJackpotwin() {
		return jackpotwin;
	}
	public void setJackpotwin(Double jackpotwin) {
		this.jackpotwin = jackpotwin;
	}
	public Double getJackpotcontribution() {
		return jackpotcontribution;
	}
	public void setJackpotcontribution(Double jackpotcontribution) {
		this.jackpotcontribution = jackpotcontribution;
	}
	public String getCurrencycode() {
		return currencycode;
	}
	public void setCurrencycode(String currencycode) {
		this.currencycode = currencycode;
	}
	public Integer getChanneltypeid() {
		return channeltypeid;
	}
	public void setChanneltypeid(Integer channeltypeid) {
		this.channeltypeid = channeltypeid;
	}
	public Double getBalanceafter() {
		return balanceafter;
	}
	public void setBalanceafter(Double balanceafter) {
		this.balanceafter = balanceafter;
	}
}
