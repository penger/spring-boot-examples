package com.neo.domain;

import java.util.List;

public class ZK4Show implements Comparable<ZK4Show> {
	private String system;
	private String ip;
	private List<String> info;
	private String key;
	
	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public List<String> getInfo() {
		return info;
	}

	public void setInfo(List<String> info) {
		this.info = info;
	}

	public void converntKey(String key) {
		String value = "error";
		for(int i=0;i<info.size();i++) {
			String item = info.get(i);
			if(item.contains(key)) {
				value = item.replace(key, "").replace(":","").trim();
				break;
			}
		}
		this.key=value;
	}
	
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		String line= "";
		if(info!=null) {
			for (String str : info) {
				line+=str+"<br/>";
			}
		}
		return "[<br/> system=" + system + ", <br/> ip=" + ip + ", <br/> info=" + line + ", <br/> key=" + key + "]<br/>";
	}

	@Override
	public int compareTo(ZK4Show o) {
		//先根据集群名称排序，再通过ip排序
		if(this.system.compareTo(o.system)!=0) {
			return this.system.compareTo(o.system);
		}else {
			return this.ip.compareTo(o.ip);
		}
	}

	

}
