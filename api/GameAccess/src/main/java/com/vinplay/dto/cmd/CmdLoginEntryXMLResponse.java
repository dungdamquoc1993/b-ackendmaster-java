package com.vinplay.dto.cmd;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "authenticate")
@XmlAccessorType(XmlAccessType.FIELD)
public class CmdLoginEntryXMLResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7945341921178403216L;

	@XmlElement(name = "member_id")
	private String member_id;

	@XmlElement(name = "status_code")
	private int status_code;

	@XmlElement(name = "message")
	private String message;

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public int getStatus_code() {
		return status_code;
	}

	public void setStatus_code(int status_code) {
		this.status_code = status_code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
