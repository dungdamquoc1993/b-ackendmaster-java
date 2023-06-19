/**
 * Archie
 */
package com.vinplay.dto.sbo;

/**
 * @author Archie
 *
 */
public class SboError {
	private Integer id;
	private String msg;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "SboError [id=" + id + ", msg=" + msg + "]";
	}

	public SboError(Integer id, String msg) {
		super();
		this.id = id;
		this.msg = msg;
	}

	public SboError() {
		super();
	}

}
