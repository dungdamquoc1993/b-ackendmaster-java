package com.vinplay.item;

import java.util.List;

public class Event implements java.io.Serializable{
	private static final long serialVersionUID = 231361253670845852L;
	private String name;
	private Integer type;
	private Integer id;
	private String startdate;
	private String enddate;
	private String startopen;
	private String endopen;
	private Integer multiple;
	private List<EventRank> eventrank;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getStartopen() {
		return startopen;
	}
	public void setStartopen(String startopen) {
		this.startopen = startopen;
	}
	public String getEndopen() {
		return endopen;
	}
	public void setEndopen(String endopen) {
		this.endopen = endopen;
	}
	public Integer getMultiple() {
		return multiple;
	}
	public void setMultiple(Integer multiple) {
		this.multiple = multiple;
	}
	public List<EventRank> getEventrank() {
		return eventrank;
	}
	public void setEventrank(List<EventRank> eventrank) {
		this.eventrank = eventrank;
	}


}
