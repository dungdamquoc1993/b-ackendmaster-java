package com.vinplay.usercore.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserWages {
	private long id;
	private String nick_name;
	private long bonus;
	private String created_at;
	private String modify_at;
	private int status;
	private String parent_user;

	public UserWages() {
		super();
	}
	
	public UserWages(long id, String nick_name, long bonus, String created_at, String modify_at, int status, String parent_user) {
		super();
		this.id = id;
		this.nick_name = nick_name;
		this.bonus = bonus;
		this.created_at = created_at;
		this.modify_at = modify_at;
		this.status = status;
		this.parent_user = parent_user;
	}
	
	public UserWages(ResultSet rs) throws SQLException {
		super();
		this.id=rs.getLong("id");
		this.nick_name = rs.getString("nick_name").trim();
		this.bonus = rs.getLong("bonus");
		this.created_at = rs.getString("created_at");
		this.modify_at = rs.getString("modify_at");
		this.status = rs.getInt("status");
		this.parent_user = rs.getString("parent_user");
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public long getBonus() {
		return bonus;
	}

	public void setBonus(long bonus) {
		this.bonus = bonus;
	}
	
	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	
	public String getModify_at() {
		return modify_at;
	}

	public void setModify_at(String modify_at) {
		this.modify_at = modify_at;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getParent_user() {
		return parent_user;
	}

	public void setParent_user(String parent_user) {
		this.parent_user = parent_user;
	}

	public String toJson() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString((Object) this);
		} catch (JsonProcessingException e) {
			return "";
		}
	}
}
