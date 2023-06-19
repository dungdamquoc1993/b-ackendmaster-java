package com.vinplay.dal.entities.fish;

public class FishGameRecord {
	private String gid;

	private Integer groups;

	private String mid;

	private String endtime;

	private Integer sync;

	private Integer roomid;

	private Integer bp;

	private Double betcoin;

	private String muid;
	
	private Integer uid;
	
	private Double grade;

	private Double wlcoin;

	private Double choushui;

	private Double coinquit;

	private Double coinenter;

	private Integer id;

	private String aid;

	private Double codeamount;

	private String gameno;

	private Double coin;


	public FishGameRecord() {
		super();
	}
	
	public FishGameRecord(String gid, Integer groups, String mid, String endtime, Integer sync, Integer roomid,
			Integer bp, Double betcoin, String muid, Integer uid, Double grade, Double wlcoin, Double choushui,
			Double coinquit, Double coinenter, Integer id, String aid, Double codeamount, String gameno, Double coin) {
		super();
		this.gid = gid;
		this.groups = groups;
		this.mid = mid;
		this.endtime = endtime;
		this.sync = sync;
		this.roomid = roomid;
		this.bp = bp;
		this.betcoin = betcoin;
		this.muid = muid;
		this.uid = uid;
		this.grade = grade;
		this.wlcoin = wlcoin;
		this.choushui = choushui;
		this.coinquit = coinquit;
		this.coinenter = coinenter;
		this.id = id;
		this.aid = aid;
		this.codeamount = codeamount;
		this.gameno = gameno;
		this.coin = coin;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public Integer getGroups() {
		return groups;
	}

	public void setGroups(Integer groups) {
		this.groups = groups;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public Integer getSync() {
		return sync;
	}

	public void setSync(Integer sync) {
		this.sync = sync;
	}

	public Integer getRoomid() {
		return roomid;
	}

	public void setRoomid(Integer roomid) {
		this.roomid = roomid;
	}

	public Integer getBp() {
		return bp;
	}

	public void setBp(Integer bp) {
		this.bp = bp;
	}

	public Double getBetcoin() {
		return betcoin;
	}

	public void setBetcoin(Double betcoin) {
		this.betcoin = betcoin;
	}

	public String getMuid() {
		return muid;
	}

	public void setMuid(String muid) {
		this.muid = muid;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Double getGrade() {
		return grade;
	}

	public void setGrade(Double grade) {
		this.grade = grade;
	}

	public Double getWlcoin() {
		return wlcoin;
	}

	public void setWlcoin(Double wlcoin) {
		this.wlcoin = wlcoin;
	}

	public Double getChoushui() {
		return choushui;
	}

	public void setChoushui(Double choushui) {
		this.choushui = choushui;
	}

	public Double getCoinquit() {
		return coinquit;
	}

	public void setCoinquit(Double coinquit) {
		this.coinquit = coinquit;
	}

	public Double getCoinenter() {
		return coinenter;
	}

	public void setCoinenter(Double coinenter) {
		this.coinenter = coinenter;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public Double getCodeamount() {
		return codeamount;
	}

	public void setCodeamount(Double codeamount) {
		this.codeamount = codeamount;
	}

	public String getGameno() {
		return gameno;
	}

	public void setGameno(String gameno) {
		this.gameno = gameno;
	}

	public Double getCoin() {
		return coin;
	}

	public void setCoin(Double coin) {
		this.coin = coin;
	}
}
