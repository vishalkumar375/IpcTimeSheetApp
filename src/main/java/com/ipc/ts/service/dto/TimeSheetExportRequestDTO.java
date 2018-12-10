package com.ipc.ts.service.dto;

import java.time.Instant;

public class TimeSheetExportRequestDTO {
	
	String org;
	Instant startDate;
	Instant endDate;
	UserDTO user;

	
	public String getOrg() {
		return org;
	}
	public void setOrg(String org) {
		this.org = org;
	}
	public Instant getStartDate() {
		return startDate;
	}
	public void setStartDate(Instant startDate) {
		this.startDate = startDate;
	}
	public Instant getEndDate() {
		return endDate;
	}
	public void setEndDate(Instant endDate) {
		this.endDate = endDate;
	}
	public UserDTO getUser() {
		return user;
	}
	public void setUser(UserDTO user) {
		this.user = user;
	}
	
	
}
