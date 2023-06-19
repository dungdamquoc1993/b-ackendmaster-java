package com.vinplay.dto.ibc2;


import java.io.Serializable;

public class UpdateMemberReqDto extends BaseReqDto implements Serializable {
	private static final long serialVersionUID = 9204419872430310305L;
	private String vendor_member_id;
	private String firstname;
	private String lastname;
	private Double maxtransfer;
	private Double mintransfer;
	private String oddstype;
	private String custominfo1;
	private String custominfo2;
	private String custominfo3;
	private String custominfo4;
	private String custominfo5;
	
	public String getVendor_member_id() {
		return vendor_member_id;
	}
	public void setVendor_member_id(String vendor_member_id) {
		this.vendor_member_id = vendor_member_id;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public Double getMaxtransfer() {
		return maxtransfer;
	}
	public void setMaxtransfer(Double maxtransfer) {
		this.maxtransfer = maxtransfer;
	}
	public Double getMintransfer() {
		return mintransfer;
	}
	public void setMintransfer(Double mintransfer) {
		this.mintransfer = mintransfer;
	}
	public String getOddstype() {
		return oddstype;
	}
	public void setOddstype(String oddstype) {
		this.oddstype = oddstype;
	}
	public String getCustominfo1() {
		return custominfo1;
	}
	public void setCustominfo1(String custominfo1) {
		this.custominfo1 = custominfo1;
	}
	public String getCustominfo2() {
		return custominfo2;
	}
	public void setCustominfo2(String custominfo2) {
		this.custominfo2 = custominfo2;
	}
	public String getCustominfo3() {
		return custominfo3;
	}
	public void setCustominfo3(String custominfo3) {
		this.custominfo3 = custominfo3;
	}
	public String getCustominfo4() {
		return custominfo4;
	}
	public void setCustominfo4(String custominfo4) {
		this.custominfo4 = custominfo4;
	}
	public String getCustominfo5() {
		return custominfo5;
	}
	public void setCustominfo5(String custominfo5) {
		this.custominfo5 = custominfo5;
	}
	
}
