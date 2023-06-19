/*
 * Decompiled with CFR 0.144.
 *
 * Could not load the following classes:
 *  com.vinplay.vbee.common.models.slot.NoHuModel
 *  com.vinplay.vbee.common.response.BaseResponseModel
 */
package com.vinplay.api.processors.slot.response;

import com.vinplay.vbee.common.models.minigame.pokego.TopPokeGo;
import com.vinplay.vbee.common.models.slot.NoHuModel;
import com.vinplay.vbee.common.response.BaseResponseModel;
import java.util.ArrayList;
import java.util.List;

public class LSNoHuResponse extends BaseResponseModel {
	private int totalPages;
	private List<TopPokeGo> results = new ArrayList<TopPokeGo>();

	public LSNoHuResponse(boolean success, String errorCode) {
		super(success, errorCode);
	}

	public int getTotalPages() {
		return this.totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public List<TopPokeGo> getResults() {
		return this.results;
	}

	public void setResults(List<TopPokeGo> results) {
		this.results = results;
	}
}
