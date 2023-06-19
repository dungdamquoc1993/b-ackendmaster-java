package com.vinplay.vbee.common.response;

import java.io.Serializable;

public class MoonEventResponse implements Serializable {
    public int idEvent;
    public String nameEvent;

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public String getNameEvent() {
        return nameEvent;
    }

    public void setNameEvent(String nameEvent) {
        this.nameEvent = nameEvent;
    }
}
