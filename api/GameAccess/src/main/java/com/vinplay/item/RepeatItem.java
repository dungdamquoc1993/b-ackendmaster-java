package com.vinplay.item;

public class RepeatItem implements java.io.Serializable{
	private static final long serialVersionUID = 7663608648696895795L;
	private String s;
	private int num=0;
	public RepeatItem(String str){
		s = str;
		num = 1;
	}
	public void numAdd(){
		num++;
	}
	public String getS() {
		return s;
	}
	public void setS(String s) {
		this.s = s;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
}
