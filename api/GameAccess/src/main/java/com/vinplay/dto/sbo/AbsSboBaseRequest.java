/**
 * Archie
 */
package com.vinplay.dto.sbo;

/**
 * @author Archie
 *
 */
public class AbsSboBaseRequest {
	private String secret_key;
	private String operator_token;

	public String getSecret_key() {
		return secret_key;
	}

	public void setSecret_key(String secret_key) {
		this.secret_key = secret_key;
	}

	public String getOperator_token() {
		return operator_token;
	}

	public void setOperator_token(String operator_token) {
		this.operator_token = operator_token;
	}

	public AbsSboBaseRequest(String secret_key, String operator_token) {
		super();
		this.secret_key = secret_key;
		this.operator_token = operator_token;
	}

	public AbsSboBaseRequest() {
		super();
	}

}
