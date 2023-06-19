package com.vinplay.safebox.dao;

public interface SafeBoxDaoService {

    public boolean depositSafeBox(String userName,double amount);

    public double getSafeBox(String userName);

    public boolean withDraw(String userName,double amount);
}
