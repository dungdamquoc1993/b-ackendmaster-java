package com.vinplay.safebox.response;

public class SafeBoxResponse {

    public int status;
    public String message;
    public double amount;

    public SafeBoxResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
