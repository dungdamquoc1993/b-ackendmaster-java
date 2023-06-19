package com.vinplay.dto.ibc;

import java.io.Serializable;

public class CreateMemberReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = -2726915884293621205L;
	private String OpCode;
	private String PlayerName;
	private String SecurityToken;
	private Integer OddsType;	
	private Double MaxTransfer;
	private Double MinTransfer;
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
	public Integer getOddsType() {
		return OddsType;
	}
	public void setOddsType(Integer oddsType) {
		OddsType = oddsType;
	}
	public Double getMaxTransfer() {
		return MaxTransfer;
	}
	public void setMaxTransfer(Double maxTransfer) {
		MaxTransfer = maxTransfer;
	}
	public Double getMinTransfer() {
		return MinTransfer;
	}
	public void setMinTransfer(Double minTransfer) {
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
