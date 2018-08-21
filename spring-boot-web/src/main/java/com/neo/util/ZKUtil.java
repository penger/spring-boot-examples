package com.neo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.neo.domain.GroupBean;
import com.neo.domain.QuorumDetail;
import com.neo.domain.QuorumStatus;
import com.neo.domain.ZKInfo;

public class ZKUtil {

	public static final String FILE_NAME="zklistinfo";
	public static final String GROUP_FILE_NAME="zkgroup";
	
	public static void main(String[] args) {
		getZKGroup();
	}
	
	
	public static QuorumDetail getQuorumDetail(String ip,int port,QuorumDetail detail) {
		detail.setIp(ip);
		LinkedList<String> zkStatus = getZKStatus(ip, port, "mntr");
		if(zkStatus.isEmpty()) {
			return detail;
		}
		Map<String,String> map = new HashMap<String,String>();
		for(String str :zkStatus) {
			str= str.replace("	", " ");
			System.out.println("值为： "+ str);
			map.put(str.split(" ")[0], str.split(" ")[1]);
		}
		detail.setAliveConns(Integer.parseInt(map.get("zk_num_alive_connections")));
		detail.setMaxLatency(Integer.parseInt(map.get("zk_max_latency")));
		detail.setPending(Integer.parseInt(map.get("zk_outstanding_requests")));
		String statu = map.get("zk_server_state");
		if(QuorumStatus.FOLLOWER.getStatus().equals(statu)) {
			detail.setStatus(QuorumStatus.FOLLOWER);			
		}else {
			detail.setStatus(QuorumStatus.LEADER);
		}
		detail.setWatcher(Integer.parseInt(map.get("zk_watch_count")));
		detail.setZnodes(Integer.parseInt(map.get("zk_znode_count")));
		return detail;
	}
	
	
	
	public static LinkedList<String> getZKStatus(String ip,int port,String command){
		LinkedList<String> answerList = new LinkedList<>();
		BufferedReader bufferedReader = null;
		PrintWriter printWriter = null ;
		Socket socket = null;
		try {
			socket = new Socket(ip,port);
			socket.setSoTimeout(1000*5);
			printWriter = new PrintWriter(socket.getOutputStream());
			printWriter.write(command);
			printWriter.flush();
			socket.shutdownOutput();
			
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String info = null;
			while((info=bufferedReader.readLine())!=null) {
				answerList.add(info);
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(bufferedReader!=null) {
					bufferedReader.close();
				}
				if(printWriter!=null) {
					printWriter.close();
				}
				if(socket!=null) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return answerList;
	}
	
	public static LinkedList<String> array2linkedlist(String[] arrays) {
		LinkedList<String> linkedList = new LinkedList<String>();
		for(int i=0;i<arrays.length;i++) {
			linkedList.add(arrays[i]);
		}
		return linkedList;
	}
	
	public static LinkedList<String> str2linkedlist(String str) {
		String[] arrays = str.split(",");
		LinkedList<String> linkedList = new LinkedList<String>();
		for(int i=0;i<arrays.length;i++) {
			linkedList.add(arrays[i]);
		}
		return linkedList;
	}
	
	public static List<String> get4Words(){
		List<String> list = new ArrayList<>();
		list.add("ruok");
		list.add("wchs");
		list.add("stat");
		list.add("conf");
		list.add("mntr");
		return list;
	}
	
	public static Map<String, GroupBean> getZKGroup(){
		Map<String,GroupBean> map = new HashMap<>();
		String line=null;
		try {
			InputStream inputStream = ZKUtil.class.getClassLoader().getResourceAsStream(GROUP_FILE_NAME);
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			while((line=reader.readLine())!=null) {
				GroupBean group = new GroupBean();
				String[] gstr = line.split("-");
				String gname = gstr[0];
				String gvalue = gstr[1];
				String chartId= gstr[2];
				List<String> list = new LinkedList<>();
				String[] split = gvalue.split(",");
				for (String s : split) {
					list.add(s);
				}
				group.setGroupName(gname);
				group.setIplist(list);
				group.setChartID(chartId);
				map.put(gname, group);
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	
	//根据IP列表，如果ip列表相同放置到同一个map中
	public static List<ZKInfo> getZKList(String yw) {
		
		List <ZKInfo>zkList = new ArrayList<ZKInfo>();
		String line =null;
		try {
			InputStream inputStream = ZKUtil.class.getClassLoader().getResourceAsStream(FILE_NAME);
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			while ((line= reader.readLine())!=null) {
				if(line.startsWith("#") || StringUtils.isEmpty(line)) {
					continue;
				}
				String[] split = line.split("-");
				ZKInfo zkInfo = new ZKInfo(split[0], split[1], Integer.parseInt(split[2]));
				if(!StringUtils.isEmpty(yw) && !yw.equals("all")) {
					if(split[0].contains(yw)) {
						zkList.add(zkInfo);
					}else {
						continue;
					}
				}else {
					zkList.add(zkInfo);
				}
			}
			reader.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return zkList;
	}

}
