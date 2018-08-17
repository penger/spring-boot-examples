package com.neo.domain;

public class QuorumDetail {
	private int maxLatency;
	private int aliveConns;
	private QuorumStatus status;
	private int znodes;
	private int watcher;
	private int pending ;
	
	
	
	@Override
	public String toString() {
		return "QuorumDetail [maxLatency=" + maxLatency + ", aliveConns=" + aliveConns + ", status=" + status.getStatus()
				+ ", znodes=" + znodes + ", watcher=" + watcher + ", pending=" + pending + "]";
	}

	public QuorumDetail() {
		super();
		this.status=QuorumStatus.LOST;
		// TODO Auto-generated constructor stub
	}

	public QuorumDetail(int maxLatency, int aliveConns, QuorumStatus status, int znodes, int watcher, int pending) {
		super();
		this.maxLatency = maxLatency;
		this.aliveConns = aliveConns;
		this.status = status;
		this.znodes = znodes;
		this.watcher = watcher;
		this.pending = pending;
	}
	
	public int getMaxLatency() {
		return maxLatency;
	}
	public void setMaxLatency(int maxLatency) {
		this.maxLatency = maxLatency;
	}
	public int getAliveConns() {
		return aliveConns;
	}
	public void setAliveConns(int aliveConns) {
		this.aliveConns = aliveConns;
	}
	public QuorumStatus getStatus() {
		return status;
	}
	public void setStatus(QuorumStatus status) {
		this.status = status;
	}
	public int getZnodes() {
		return znodes;
	}
	public void setZnodes(int znodes) {
		this.znodes = znodes;
	}
	public int getWatcher() {
		return watcher;
	}
	public void setWatcher(int watcher) {
		this.watcher = watcher;
	}
	public int getPending() {
		return pending;
	}
	public void setPending(int pending) {
		this.pending = pending;
	}
	
	

}
