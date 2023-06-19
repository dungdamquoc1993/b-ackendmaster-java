package com.vinplay.vbee.common.models;


public class GiftCodeUseds {

  private long giftcodeId;
  private String username;
  private java.sql.Timestamp createdAt;
  private java.sql.Timestamp updatedAt;
  private String idNumber;


  public long getGiftcodeId() {
    return giftcodeId;
  }

  public void setGiftcodeId(long giftcodeId) {
    this.giftcodeId = giftcodeId;
  }


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }


  public java.sql.Timestamp getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(java.sql.Timestamp createdAt) {
    this.createdAt = createdAt;
  }


  public java.sql.Timestamp getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(java.sql.Timestamp updatedAt) {
    this.updatedAt = updatedAt;
  }


  public String getIdNumber() {
    return idNumber;
  }

  public void setIdNumber(String idNumber) {
    this.idNumber = idNumber;
  }

}
