package com.vinplay.vbee.common.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vinplay.vbee.common.utils.VinPlayUtils;

public class UserAdminModel {
	private int id;
	private String username;
	private String password;
	private String nameagent;
	private String fullname;
	private String email;
	private String address;
	private String birthday;
	private int cmnd;
	private String phone;
	private String status;
	private Integer parentid;
	private Integer active;
	private Integer isthuong;
	private Integer issuper;
	private String facebook;
	private String key;
	private Double balance;
	private Date create_time;

	public UserAdminModel() {
		super();
	}

	public UserAdminModel(ResultSet rs) throws SQLException {
		this.id = rs.getInt("id");
		this.username = rs.getString("username");
		this.password = rs.getString("password");
		this.email = rs.getString("email");
		this.phone = rs.getString("phone");
		this.birthday = rs.getDate("birthday") != null ? VinPlayUtils.parseDateToString(rs.getDate("birthday")) : null;
		this.address = rs.getString("address");
		this.status = rs.getString("status");
		this.active = rs.getInt("active");
		String sCreateTime = rs.getString("create_time");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.create_time = sCreateTime != null && !"".equals(sCreateTime) ? format.parse(sCreateTime) : null;
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public UserAdminModel(int id, String username, String password, String nameagent, String fullname, String email,
			String address, String birthday, int cmnd, String phone, String status, Integer parentid, Integer active,
			Integer isthuong, Integer issuper, String facebook, String key, Double balance, Date create_time) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.nameagent = nameagent;
		this.fullname = fullname;
		this.email = email;
		this.address = address;
		this.birthday = birthday;
		this.cmnd = cmnd;
		this.phone = phone;
		this.status = status;
		this.parentid = parentid;
		this.active = active;
		this.isthuong = isthuong;
		this.issuper = issuper;
		this.facebook = facebook;
		this.key = key;
		this.balance = balance;
		this.create_time = create_time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNameagent() {
		return nameagent;
	}

	public void setNameagent(String nameagent) {
		this.nameagent = nameagent;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public int getCmnd() {
		return cmnd;
	}

	public void setCmnd(int cmnd) {
		this.cmnd = cmnd;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getParentid() {
		return parentid;
	}

	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public Integer getIsthuong() {
		return isthuong;
	}

	public void setIsthuong(Integer isthuong) {
		this.isthuong = isthuong;
	}

	public Integer getIssuper() {
		return issuper;
	}

	public void setIssuper(Integer issuper) {
		this.issuper = issuper;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

}
