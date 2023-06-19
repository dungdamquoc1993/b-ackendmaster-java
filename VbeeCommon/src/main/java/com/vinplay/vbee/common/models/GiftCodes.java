package com.vinplay.vbee.common.models;


public class GiftCodes {

  private long id;
  private String giftcode;
  private long type;
  private long money;
  private long timeUsed;
  private long maxUse;
  private java.sql.Timestamp from;
  private java.sql.Timestamp exprired;
  private java.sql.Timestamp createdAt;
  private String createdBy;
  private long event;
  private String userName;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getGiftcode() {
    return giftcode;
  }

  public void setGiftcode(String giftcode) {
    this.giftcode = giftcode;
  }


  public long getType() {
    return type;
  }

  public void setType(long type) {
    this.type = type;
  }


  public long getMoney() {
    return money;
  }

  public void setMoney(long money) {
    this.money = money;
  }


  public long getTimeUsed() {
    return timeUsed;
  }

  public void setTimeUsed(long timeUsed) {
    this.timeUsed = timeUsed;
  }


  public long getMaxUse() {
    return maxUse;
  }

  public void setMaxUse(long maxUse) {
    this.maxUse = maxUse;
  }


  public java.sql.Timestamp getFrom() {
    return from;
  }

  public void setFrom(java.sql.Timestamp from) {
    this.from = from;
  }


  public java.sql.Timestamp getExprired() {
    return exprired;
  }

  public void setExprired(java.sql.Timestamp exprired) {
    this.exprired = exprired;
  }


  public java.sql.Timestamp getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(java.sql.Timestamp createdAt) {
    this.createdAt = createdAt;
  }


  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }


  public long getEvent() {
    return event;
  }

  public void setEvent(long event) {
    this.event = event;
  }


  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

}
