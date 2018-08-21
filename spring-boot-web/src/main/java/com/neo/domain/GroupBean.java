package com.neo.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupBean {
	private String groupName;
	private List<String > iplist;
	private List<Integer > leaderlist;
	private String chartID;
	
	public String getChartID() {
		return chartID;
	}

	public void setChartID(String chartID) {
		this.chartID = chartID;
	}
	private Map<String, Integer> leaderMap;
	
	public GroupBean() {
		leaderMap = new HashMap<String,Integer>();
		leaderlist = new ArrayList<Integer>();
	}
	
	public Map<String, Integer> getLeaderMap() {
		return leaderMap;
	}


	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public List<String> getIplist() {
		return iplist;
	}
	public void setIplist(List<String> iplist) {
		this.iplist = iplist;
		for (String ip : iplist) {
			leaderMap.put(ip, 0);
		}
	}
	//获取count
	public List<Integer> getLeaderlist() {
		
		for (String ip : iplist) {
			Integer count = leaderMap.get(ip);
			leaderlist.add(count);
		}
		return leaderlist;
	}
	public void setLeaderlist(List<Integer> leaderlist) {
		this.leaderlist = leaderlist;
	}

	
}
