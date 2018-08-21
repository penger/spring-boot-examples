package com.neo.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 每个zk集群一个指标，用于展示在
 * @author gongp
 *
 */
public class ZKClusterDetail implements Comparable<ZKClusterDetail> {
	
	private int max_latency;
	private int connections;
	private int pending;
	private int watchers;
	private int znodecount;
	private String leaderip;
	
	private List<QuorumDetail> nodes;
	
	//zk状况，值为OK 或者ERROR
	private ZKStauts status;
	
	private ZK4Show zk4Show;
	
	public void full() {
		
		status=ZKStauts.ERROR;
		
		for(QuorumDetail q: nodes) {
			if(q.getMaxLatency()>max_latency) {
				max_latency=q.getMaxLatency();
			}
			if(q.getAliveConns()>connections) {
				connections=q.getAliveConns();
			}
			if (q.getPending()>pending) {
				pending=q.getPending();
			}
			if(q.getWatcher()>watchers) {
				watchers=q.getWatcher();
			}
			if(q.getZnodes()>znodecount) {
				znodecount=q.getZnodes();
			}
			if(q.getStatus().equals(QuorumStatus.LEADER)) {
				status = ZKStauts.OK;
				leaderip=q.getIp();
			}
		}
	}
	
	
	public List<QuorumDetail> getNodes() {
		return nodes;
	}

	public void setNodes(List<QuorumDetail> nodes) {
		this.nodes = nodes;
	}

	public ZKClusterDetail() {
		super();
		nodes=new ArrayList<>();
	}

	public ZKClusterDetail(int max_latency, int connections, int pending, int watchers, int queuesize, ZKStauts status,ZK4Show zk4Show) {
		super();
		this.max_latency = max_latency;
		this.connections = connections;
		this.pending = pending;
		this.watchers = watchers;
		this.znodecount = queuesize;
		this.status = status;
		this.setZk4Show(zk4Show);
	}
	
	public int getMax_latency() {
		return max_latency;
	}
	public void setMax_latency(int max_latency) {
		this.max_latency = max_latency;
	}
	public int getConnections() {
		return connections;
	}
	public void setConnections(int connections) {
		this.connections = connections;
	}
	public int getPending() {
		return pending;
	}
	public void setPending(int pending) {
		this.pending = pending;
	}
	public int getWatchers() {
		return watchers;
	}
	public void setWatchers(int watchers) {
		this.watchers = watchers;
	}
	
	public int getZnodecount() {
		return znodecount;
	}
	public void setZnodecount(int znodecount) {
		this.znodecount = znodecount;
	}
	public ZKStauts getStatus() {
		return status;
	}
	public void setStatus(ZKStauts status) {
		this.status = status;
	}
	public ZK4Show getZk4Show() {
		return zk4Show;
	}
	
	public void setZk4Show(ZK4Show zk4Show) {
		this.zk4Show = zk4Show;
	}
	
	public String getLeaderip() {
		return leaderip;
	}

	public void setLeaderip(String leaderip) {
		this.leaderip = leaderip;
	}


	@Override
	public String toString() {
		return "ZKClusterDetail [max_latency=" + max_latency + ", connections=" + connections + ", pending=" + pending
				+ ", watchers=" + watchers + ", znodecount=" + znodecount + ", status=" + status + "]";
	}


	@Override
	public int compareTo(ZKClusterDetail o) {
		return this.getZk4Show().getSystem().compareTo(o.getZk4Show().getSystem());
	}

	
	
	
}
