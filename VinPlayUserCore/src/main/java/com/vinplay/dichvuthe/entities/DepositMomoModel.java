package com.vinplay.dichvuthe.entities;

public class DepositMomoModel {
    public String Id;
    public String Nickname;
    public String CreatedAt;
    public String UpdatedAt;
    public long Amount;
    public int Status;
    public String ReceivedPhoneNumber;
    public String ReceivedName;
    public String SendFromNumber;
    public String Description;
    public String UserApprove;

    public DepositMomoModel(String id, String nickname, String createdAt, String updatedAt, long amount, int status, String receivedPhoneNumber, String receivedName, String sendFromNumber, String description) {
        Id = id;
        Nickname = nickname;
        CreatedAt = createdAt;
        UpdatedAt = updatedAt;
        Amount = amount;
        Status = status;
        ReceivedPhoneNumber = receivedPhoneNumber;
        ReceivedName = receivedName;
        SendFromNumber = sendFromNumber;
        Description = description;
    }

    public DepositMomoModel(String id, String nickname, String createdAt, String updatedAt, long amount, int status, String receivedPhoneNumber, String receivedName, String sendFromNumber, String description, String userApprove) {
        Id = id;
        Nickname = nickname;
        CreatedAt = createdAt;
        UpdatedAt = updatedAt;
        Amount = amount;
        Status = status;
        ReceivedPhoneNumber = receivedPhoneNumber;
        ReceivedName = receivedName;
        SendFromNumber = sendFromNumber;
        Description = description;
        UserApprove = userApprove;
    }

    public DepositMomoModel(String nickname, long amount, String receivedPhoneNumber, String receivedName, String sendFromNumber) {
        Nickname = nickname;
        Amount = amount;
        ReceivedPhoneNumber = receivedPhoneNumber;
        ReceivedName = receivedName;
        SendFromNumber = sendFromNumber;
    }

    public DepositMomoModel(String id, String nickname, String status, String sendFromNumber) {
        Id = id;
        Nickname = nickname;
        if(!status.isEmpty()){
            Status = Integer.parseInt(status);
        }

        SendFromNumber = sendFromNumber;
    }
}
