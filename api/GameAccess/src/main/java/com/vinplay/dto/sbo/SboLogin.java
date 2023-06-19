/**
 * Archie
 */
package com.vinplay.dto.sbo;

import java.io.Serializable;

/**
 * @author Archie
 *
 */
public class SboLogin extends AbsSboBaseResponse<String, SboError> implements Serializable{
	
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public SboLogin() {
	}

	@Override
	public String toString() {
		return "SboLogin [url=" + url + "]";
	}
	
}
