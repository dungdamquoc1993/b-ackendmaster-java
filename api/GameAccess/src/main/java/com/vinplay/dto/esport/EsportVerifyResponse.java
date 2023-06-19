/**
 * Archie
 */
package com.vinplay.dto.esport;

import java.io.Serializable;

/**
 * @author Archie
 *
 */
public class EsportVerifyResponse implements Serializable {

	String loginName;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Override
	public String toString() {
		return "EsportVerifyResponse [loginName=" + loginName + "]";
	}
	
}
