package com.vinplay.usercore.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AgentBank {
	private long id;
	private String agent_code;
	private String bank_acount;
	private String bank_code;
	private String bank_name;
	private String bank_number;
	private String bank_branch;

	public AgentBank() {
		super();
	}
	
	public AgentBank(ResultSet rs) throws SQLException {
		super();
		this.id=rs.getLong("id");
		this.agent_code = rs.getString("agent_code").trim();
		this.bank_acount = rs.getString("bank_acount");
		this.bank_code = rs.getString("bank_code");
		this.bank_name = rs.getString("bank_name");
		this.bank_number = rs.getString("bank_number");
		this.bank_branch = rs.getString("bank_branch").trim();
	}
	
	public AgentBank(long id, String agent_code, String bank_acount, String bank_code, String bank_name, String bank_number,
			String bank_branch) {
		super();
		this.id = id;
		this.agent_code = agent_code;
		this.bank_acount = bank_acount;
		this.bank_code = bank_code;
		this.bank_name = bank_name;
		this.bank_number = bank_number;
		this.bank_branch = bank_branch;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAgent_code() {
		return agent_code;
	}

	public void setAgent_code(String agent_code) {
		this.agent_code = agent_code;
	}

	public String getBank_acount() {
		return bank_acount;
	}

	public void setBank_acount(String bank_acount) {
		this.bank_acount = bank_acount;
	}

	public String getBank_code() {
		return bank_code;
	}

	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}
	
	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getBank_number() {
		return bank_number;
	}

	public void setBank_number(String bank_number) {
		this.bank_number = bank_number;
	}

	public String getBank_branch() {
		return bank_branch;
	}

	public void setBank_branch(String bank_branch) {
		this.bank_branch = bank_branch;
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
