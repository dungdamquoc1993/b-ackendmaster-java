package com.vinplay.item;

import java.io.Serializable;
import java.util.Objects;

public class EventSportCurrentSession implements Serializable {
    private static final long serialVersionUID = 5187712558359574602L;
    
    private final int sportid;
    private final String sportname;
    private final int sporttype;
    private final Integer currentround;
    private final String urlweb;
    private final String urlwap;
    
    public EventSportCurrentSession(int sportid, String sportname, int sporttype, Integer currentround, String urlweb, String urlwap) {
        this.sportid = sportid;
        this.sportname = sportname;
        this.sporttype = sporttype;
        this.currentround = currentround;
        this.urlweb = urlweb;
        this.urlwap = urlwap;
    }
    
    public int getSportid() {
        return sportid;
    }
    
    public String getSportname() {
        return sportname;
    }
    
    public int getSporttype() {
        return sporttype;
    }
    
    public Integer getCurrentround() {
        return currentround;
    }
    
    public String getUrlweb() {
        return urlweb;
    }
    
    public String getUrlwap() {
        return urlwap;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventSportCurrentSession)) return false;
        EventSportCurrentSession key = (EventSportCurrentSession) o;
        return sportid == key.sportid && sportname.equals(key.sportname) && sporttype==key.sporttype;
    }
    
    @Override
    public int hashCode() {
        int result = sportid;
        result = 31 * result + (sportname == null ? 0 : sportname.hashCode());
        return result;
    }
}
