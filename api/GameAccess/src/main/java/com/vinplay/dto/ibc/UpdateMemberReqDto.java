package com.vinplay.dto.ibc;

import java.io.Serializable;

public class UpdateMemberReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = -3368241376802635970L;
	private String OpCode;
	private String PlayerName;
	private String SecurityToken;
	private String MaxTransfer;
	private String MinTransfer;
	private String FirstName;
	private String LastName;
	public String getOpCode() {
		return OpCode;
	}
	public void setOpCode(String opCode) {
		OpCode = opCode;
	}
	public String getPlayerName() {
		return PlayerName;
	}
	public void setPlayerName(String playerName) {
		PlayerName = playerName;
	}
	public String getSecurityToken() {
		return SecurityToken;
	}
	public void setSecurityToken(String securityToken) {
		SecurityToken = securityToken;
	}
	public String getMaxTransfer() {
		return MaxTransfer;
	}
	public void setMaxTransfer(String maxTransfer) {
		MaxTransfer = maxTransfer;
	}
	public String getMinTransfer() {
		return MinTransfer;
	}
	public void setMinTransfer(String minTransfer) {
		MinTransfer = minTransfer;
	}
	public String getFirstName() {
		return FirstName;
	}
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	
	
}
