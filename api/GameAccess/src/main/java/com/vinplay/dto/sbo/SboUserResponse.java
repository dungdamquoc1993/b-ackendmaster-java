/**
 * Archie
 */
package com.vinplay.dto.sbo;

/**
 * @author Archie
 *
 */

public class SboUserResponse {
	String msg;
	String action_result;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getAction_result() {
		return action_result;
	}

	public void setAction_result(String action_result) {
		this.action_result = action_result;
	}

	public SboUserResponse(String msg, String action_result) {
		super();
		this.msg = msg;
		this.action_result = action_result;
	}

	public SboUserResponse() {
		super();
	}

}
