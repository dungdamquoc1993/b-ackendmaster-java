package com.vinplay.item;

import java.io.Serializable;

public class SpinEventDetailItem implements Serializable {
    private static final long serialVersionUID = -8763782213407766643L;
    private int id;
    private int eventid;
    private String prize;
    private double probability;
    private Integer quantity;
    private String createtime;
    private String createby;
    private String lastupdateby;
    private String lasttimeupdate;
    private int status;
    private int numberwin;
    private int numberremaining;
    private int numberorder;
    private int type;
    private double corresponding;
    private String urlweb;
    private String urlwebprize="";
    private String urlwebswipe;
    private String urlwap;
    private String urlwapprize="";
    private String urlwapswipe;

    public SpinEventDetailItem() {
    }


    public int getNumberorder() {
        return this.numberorder;
    }

    public void setNumberorder(int numberorder) {
        this.numberorder = numberorder;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventid() {
        return this.eventid;
    }

    public void setEventid(int eventid) {
        this.eventid = eventid;
    }

    public String getPrize() {
        return this.prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public double getProbability() {
        return this.probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public String getCreatetime() {
        return this.createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCreateby() {
        return this.createby;
    }

    public void setCreateby(String createby) {
        this.createby = createby;
    }

    public String getLastupdateby() {
        return this.lastupdateby;
    }

    public void setLastupdateby(String lastupdateby) {
        this.lastupdateby = lastupdateby;
    }

    public String getLasttimeupdate() {
        return this.lasttimeupdate;
    }

    public void setLasttimeupdate(String lasttimeupdate) {
        this.lasttimeupdate = lasttimeupdate;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }
  

    public double getCorresponding() {
		return corresponding;
	}


	public void setCorresponding(double corresponding) {
		this.corresponding = corresponding;
	}


	public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public int getNumberwin() {
        return numberwin;
    }

    public void setNumberwin(int numberwin) {
        this.numberwin = numberwin;
    }

    public int getNumberremaining() {
        return numberremaining;
    }

    public void setNumberremaining(int numberremaining) {
        this.numberremaining = numberremaining;
    }


	public String getUrlweb() {
		return urlweb;
	}

	public void setUrlweb(String urlweb) {
		this.urlweb = urlweb;
	}

    public String getUrlwap() {
        return urlwap;
    }

    public void setUrlwap(String urlwap) {
        this.urlwap = urlwap;
    }

    public String getUrlwebswipe() {
        return urlwebswipe;
    }

    public void setUrlwebswipe(String urlwebswipe) {
        this.urlwebswipe = urlwebswipe;
    }

    public String getUrlwapswipe() {
        return urlwapswipe;
    }

    public void setUrlwapswipe(String urlwapswipe) {
        this.urlwapswipe = urlwapswipe;
    }

    public String getUrlwebprize() {
        return urlwebprize;
    }

    public void setUrlwebprize(String urlwebprize) {
        this.urlwebprize = urlwebprize;
    }

    public String getUrlwapprize() {
        return urlwapprize;
    }

    public void setUrlwapprize(String urlwapprize) {
        this.urlwapprize = urlwapprize;
    }
}

