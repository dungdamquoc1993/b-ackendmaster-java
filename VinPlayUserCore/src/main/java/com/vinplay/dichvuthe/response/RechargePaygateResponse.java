package com.vinplay.dichvuthe.response;

public class RechargePaygateResponse<T>{
	// T stands for "Type"
    private T t;

    public void set(T t) { this.t = t; }
    public T get() { return t; }
}