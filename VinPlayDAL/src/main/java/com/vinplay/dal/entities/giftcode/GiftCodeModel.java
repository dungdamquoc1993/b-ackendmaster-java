package com.vinplay.dal.entities.giftcode;


import java.util.Date;

public class GiftCodeModel {
    private Integer id;
    private String giftcode;
    private Integer type;
    private Long money;
    private Integer time_used;
    private Integer max_use;
    private Date from;
    private Date expired;
    private Date created_at;
    private String created_by;
    private Integer event;
    private String user_name;

    public GiftCodeModel() {
    }

    public GiftCodeModel(Integer id, String giftcode, Integer type, Long money, Integer time_used, Integer max_use, Date from, Date expired, Date created_at, String created_by, Integer event, String user_name) {
        this.id = id;
        this.giftcode = giftcode;
        this.type = type;
        this.money = money;
        this.time_used = time_used;
        this.max_use = max_use;
        this.from = from;
        this.expired = expired;
        this.created_at = created_at;
        this.created_by = created_by;
        this.event = event;
        this.user_name = user_name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGiftcode() {
        return giftcode;
    }

    public void setGiftcode(String giftcode) {
        this.giftcode = giftcode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Integer getTime_used() {
        return time_used;
    }

    public void setTime_used(Integer time_used) {
        this.time_used = time_used;
    }

    public Integer getMax_use() {
        return max_use;
    }

    public void setMax_use(Integer max_user) {
        this.max_use = max_user;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public Integer getEvent() {
        return event;
    }

    public void setEvent(Integer event) {
        this.event = event;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
