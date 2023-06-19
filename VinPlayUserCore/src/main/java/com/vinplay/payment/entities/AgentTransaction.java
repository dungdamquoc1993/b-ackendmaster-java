package com.vinplay.payment.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AgentTransaction {
	public String Id;
	public String CreatedAt;
	public String ModifiedAt;
	public Boolean IsDeleted;
	public String AgentId;
	public String Username;
	public String Nickname;
	public String AgentCode;
	public String RequestTime;
	public long Point;
	public long Money;
	public long Fee;
	public long Bonus;
	public int Status;
	public String FromBankNumber;
	public String ToBankNumber;
	public String Content;
	public String Description;
	public String UserApprove;

	public AgentTransaction() {
	}

	public AgentTransaction(String id, String createdAt, String modifiedAt, Boolean isDeleted, String agentId,
			String username, String nickname, String agentCode, String requestTime, long point, long money, long fee, long bonus,
			int status, String fromBankNumber, String toBankNumber, String content,String description, String userApprove) {
		super();
		Id = id;
		CreatedAt = createdAt;
		ModifiedAt = modifiedAt;
		IsDeleted = isDeleted;
		AgentId = agentId;
		Username = username;
		Nickname = nickname;
		AgentCode = agentCode;
		RequestTime = requestTime;
		Point = point;
		Money = money;
		Fee = fee;
		Bonus = bonus;
		Status = status;
		FromBankNumber = fromBankNumber;
		ToBankNumber = toBankNumber;
		Content = content;
		Description = description;
		UserApprove = userApprove;
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
