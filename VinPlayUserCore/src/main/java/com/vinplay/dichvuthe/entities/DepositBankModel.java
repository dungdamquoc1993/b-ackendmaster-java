package com.vinplay.dichvuthe.entities;

public class DepositBankModel {
    public String Id;
    public String Nickname;
    public String CreatedAt;
    public String UpdatedAt;
    public String Amount;
    public int Status;
    public String Description;
    public String UserApprove;

    public DepositBankModel(String id, String nickname, int status,String amount) {
        Id = id;
        Nickname = nickname;
        Amount = amount;
        Status = status;
    }

    public DepositBankModel(String id, String nickname, String createdAt, String updatedAt, String amount, int status, String description, String userApprove) {
        Id = id;
        Nickname = nickname;
        CreatedAt = createdAt;
        UpdatedAt = updatedAt;
        Amount = amount;
        Status = status;
        Description = description;
        UserApprove = userApprove;
    }
}
