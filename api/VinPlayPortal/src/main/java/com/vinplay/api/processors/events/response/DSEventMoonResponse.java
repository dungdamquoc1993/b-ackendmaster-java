package com.vinplay.api.processors.events.response;

import com.vinplay.vbee.common.response.BaseResponseModel;
import com.vinplay.vbee.common.response.LogMoneyUserResponse;
import com.vinplay.vbee.common.response.MoonEventResponse;

import java.util.ArrayList;
import java.util.List;

public class DSEventMoonResponse  extends BaseResponseModel {
    private List<MoonEventResponse> lstMoonEvents = new ArrayList<MoonEventResponse>();

    public DSEventMoonResponse(boolean success, String errorCode) {
        super(success, errorCode);
    }

    public void setLstMoonEvents(List<MoonEventResponse> lstMoonEvents) {
        this.lstMoonEvents = lstMoonEvents;
    }

    public List<MoonEventResponse> getLstMoonEvents() {
        return lstMoonEvents;
    }
}
