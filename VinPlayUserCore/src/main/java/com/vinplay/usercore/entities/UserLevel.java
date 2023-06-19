package com.vinplay.usercore.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserLevel {
	private long id;
	private String nick_name;
	private String code;
	private String ancestor;
	private String created_at;
	private String parent_user;

	public UserLevel() {
		super();
	}
	
	public UserLevel(long id, String nick_name, String code, String ancestor, String created_at, String parent_user) {
		super();
		this.id = id;
		this.nick_name = nick_name;
		this.ancestor = ancestor;
		this.created_at = created_at;
		this.parent_user = parent_user;
	}
	
	public UserLevel(ResultSet rs) throws SQLException {
		super();
		this.id=rs.getLong("id");
		this.nick_name = rs.getString("nick_name").trim();
		this.code = rs.getString("code").trim();
		this.ancestor = rs.getString("ancestor");
		this.created_at = rs.getString("created_at");
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getAncestor() {
		return ancestor;
	}

	public void setAncestor(String ancestor) {
		this.ancestor = ancestor;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
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
