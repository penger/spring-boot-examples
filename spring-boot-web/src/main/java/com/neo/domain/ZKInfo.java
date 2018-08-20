package com.neo.domain;

public class ZKInfo {
	
	private String name;
	private String iplist;
	private int port;
	
	
	
	public ZKInfo(String name, String iplist, int port) {
		super();
		this.name = name;
		this.iplist = iplist;
		this.port = port;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIplist() {
		return iplist;
	}
	public void setIplist(String iplist) {
		this.iplist = iplist;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	@Override
	public String toString() {	
		return "ZKInfo [name=" + name + ", iplist=" + iplist + ", port=" + port + "]";
	}
	
	

}
