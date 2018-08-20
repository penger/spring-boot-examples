package com.neo.domain;

public enum QuorumStatus {
	FOLLOWER("follower"),LEADER("Leader"),LOST("LOST");
	private String status;
	private QuorumStatus(String status) {
		this.status=status;
	}
	public String getStatus() {
		return status;
	}
}
