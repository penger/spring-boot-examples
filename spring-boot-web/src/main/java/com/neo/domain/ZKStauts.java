package com.neo.domain;

public enum ZKStauts {
	
	OK("1"),ERROR("0");
	
	private String desc;
	
	private ZKStauts(String desc) {
		this.desc=desc;
	}
	
	public String getValue() {
		return desc;
	}
}
