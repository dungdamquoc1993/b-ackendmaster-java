/**
 * Archie
 */
package com.vinplay.dto.sbo;

/**
 * @author Archie
 *
 */
public class AbsSboBaseResponse<T, K> {
	private T serverId;
	private K error;

	public AbsSboBaseResponse(T serverId, K error) {
		super();
		this.serverId = serverId;
		this.error = error;
	}

	public T getServerId() {
		return serverId;
	}

	public void setServerId(T serverId) {
		this.serverId = serverId;
	}

	public K getError() {
		return error;
	}

	public void setError(K error) {
		this.error = error;
	}

	public AbsSboBaseResponse() {
		super();
	}

	@Override
	public String toString() {
		return "AbsSboBaseResponse [serverId=" + serverId + ", error=" + error + "]";
	}

}
