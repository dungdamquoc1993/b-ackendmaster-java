package com.vinplay.dto.ag;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "result")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetAGSportReportsResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6729493946562081212L;

	@XmlElement
	private String info;
	
	@XmlElement
	private AGGamesReportsAdditionalInfo addition;
	
	@XmlElement
	private List<AGSportReportsDetailData> row;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public AGGamesReportsAdditionalInfo getAddition() {
		return addition;
	}

	public void setAddition(AGGamesReportsAdditionalInfo addition) {
		this.addition = addition;
	}

	public List<AGSportReportsDetailData> getRow() {
		return row;
	}

	public void setRow(List<AGSportReportsDetailData> row) {
		this.row = row;
	}
}
