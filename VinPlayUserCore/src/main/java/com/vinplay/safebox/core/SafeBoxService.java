package com.vinplay.safebox.core;

import com.vinplay.safebox.response.SafeBoxResponse;

public interface SafeBoxService {
    public SafeBoxResponse depositSafeBox(String userName, double amount);

    public SafeBoxResponse getSafeBox(String userName);

    public SafeBoxResponse withDraw(String userName,double amount, String otp);
}
