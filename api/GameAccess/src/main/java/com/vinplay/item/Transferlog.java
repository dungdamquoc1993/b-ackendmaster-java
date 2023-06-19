package com.vinplay.item;

public class Transferlog implements java.io.Serializable {

	private static final long serialVersionUID = 3961669612543697386L;
	private String id;
	private String type;
	private String username;
	private Double amount;
	private String createtime;
	private Double balance;
	private Double wallet;
	private String wallettype;
	private String dealnumber;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public Double getAmount() {
		return amount;
	}
	
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public String getCreatetime() {
		return createtime;
	}
	
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	
	public Double getBalance() {
		return balance;
	}
	
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
	public Double getWallet() {
		return wallet;
	}
	
	public void setWallet(Double wallet) {
		this.wallet = wallet;
	}
	
	public String getWallettype() {
		return wallettype;
	}
	
	public void setWallettype(String wallettype) {
		this.wallettype = wallettype;
	}
	
	public String getDealnumber(){
		return dealnumber;
	}
	
	public void setDealnumber(String dealnumber) {
		this.dealnumber = dealnumber;
	}
	
}
