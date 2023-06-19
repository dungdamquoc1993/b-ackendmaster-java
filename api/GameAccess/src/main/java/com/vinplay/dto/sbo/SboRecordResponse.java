/**
 * 
 */
package com.vinplay.dto.sbo;

import java.io.Serializable;
import java.util.List;

/**
 * @author bright
 *
 */
public class SboRecordResponse extends AbsSboBaseResponse<String, SboError> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8719020632202128392L;
	/**
	 * 
	 */
	private List<SboRecordDetail> Result;

	public List<SboRecordDetail> getResult() {
		return Result;
	}

	public void setResult(List<SboRecordDetail> result) {
		Result = result;
	}

	@Override
	public String toString() {
		return "SboResultResponse [Result=" + Result + "]";
	}

}
