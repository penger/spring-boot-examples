package com.neo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import com.neo.domain.GroupBean;
import com.neo.domain.QuorumDetail;
import com.neo.domain.ZK4Show;
import com.neo.domain.ZKClusterDetail;
import com.neo.domain.ZKInfo;
import com.neo.util.ZKUtil;


@Component
public class ZKService {
	
	
	//单一集群运行状况的方法
	/**
	 * 1.获取zookeeper的连接列表
	 * 2.zookeeper列表中的每一个zookeeper
	 * 通过mntr 获取如下值：
 		zk_version	3.4.5-cdh5.5.0--1, built on 11/09/2015 20:27 GMT
		zk_avg_latency	0
		zk_max_latency	0
		zk_min_latency	0
		zk_packets_received	5
		zk_packets_sent	4
		zk_num_alive_connections	1
		zk_outstanding_requests	0
		zk_server_state	follower
		zk_znode_count	225
		zk_watch_count	0
		zk_ephemerals_count	0
		zk_approximate_data_size	13805
		zk_open_file_descriptor_count	26
		zk_max_file_descriptor_count	4096
	 * 
	 * 
	 * 
	 */
	public ZKClusterDetail getZkClusterDetail(ZKInfo info) {
		ZKClusterDetail detail = new ZKClusterDetail();
		
		String iplist = info.getIplist();
		
		return detail;
	}
	
	//获取所有的zookeeper
	public ConcurrentHashMap<String, ZKClusterDetail> getAll() throws InterruptedException {
		//同一个集群返回为同一个clusterBean
		ConcurrentHashMap<String, ZKClusterDetail> resultMap = new ConcurrentHashMap<String,ZKClusterDetail>();
		List<ZKInfo> zkList = ZKUtil.getZKList("all");
		ExecutorService pool = Executors.newCachedThreadPool();
		
		int threadCount=0;
		//需要运行的线程总数
		for(ZKInfo info: zkList) {
			String name = info.getName();
			String iplist = info.getIplist();
			int length = iplist.split(",").length;
			threadCount = threadCount+ length;
		}
		
		System.out.println("thread count is : "+ threadCount);
		CountDownLatch countDownLatch =new CountDownLatch(threadCount);
		
		for(ZKInfo info: zkList) {
			String name = info.getName();
			String iplist = info.getIplist();
			int port = info.getPort();
			for(int i=0;i<iplist.split(",").length;i++) {
				String ip= iplist.split(",")[i];
				pool.execute(new Runnable() {
					@Override
					public void run() {
						QuorumDetail quorumDetail = ZKUtil.getQuorumDetail(ip, port, new QuorumDetail());
						System.out.println(quorumDetail);
						//
						boolean containsKey = resultMap.containsKey(name);
						if(containsKey) {
							ZKClusterDetail zkClusterDetail = resultMap.get(name);
							zkClusterDetail.getNodes().add(quorumDetail);
						}else {
							ZKClusterDetail detail = new ZKClusterDetail();
							detail.getNodes().add(quorumDetail);
							ZK4Show zk4Show = new ZK4Show();
							zk4Show.setSystem(name);
							detail.setZk4Show(zk4Show );
							resultMap.put(name, detail);
						}
						countDownLatch.countDown();
						System.out.println("当前运行的线程总数为："+countDownLatch.getCount());
					}
				});
			}
		}
		
		countDownLatch.await();
		pool.shutdown();
		
		
		return resultMap;

	}
	
	
	
	public static void main(String[] args) throws InterruptedException {
		new ZKService().getAll();
	}
	
	

	public List<ZK4Show> getZK4Show(String command,String yw) {
		
		List<ZK4Show> resultList  = new ArrayList<>();
		long start_time = System.currentTimeMillis();
		System.out.println("start at : " + start_time);
		
		List<ZKInfo> zkList = ZKUtil.getZKList(yw);
		ExecutorService threadPool = Executors.newFixedThreadPool(40);
		for(int i=0;i<zkList.size();i++) {
			ZKInfo zkInfo = zkList.get(i);
			String iplist = zkInfo.getIplist();
			for(int j=0;j<iplist.split(",").length;j++) {
				String ip = iplist.split(",")[j];
				int port = zkInfo.getPort();
				threadPool.execute(new Runnable() {
					@Override
					public void run() {
						System.out.println(ip+"          "+port);
						LinkedList<String> zkStatus = ZKUtil.getZKStatus(ip, port, command);
						//标记
						ZK4Show zk4Show = new ZK4Show();
						zk4Show.setIp(ip);
						zk4Show.setInfo(zkStatus);
						zk4Show.setSystem(zkInfo.getName());
//						zk4Show.getKey("Mode:");
						resultList.add(zk4Show);
						System.out.println("size is : "+resultList.size());
					}
				});
			}
		}
		threadPool.shutdown();
		while(!threadPool.isTerminated()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Collections.sort(resultList);
		System.out.println("-------------------------");
		for (ZK4Show zk4Show : resultList) {
			System.out.println(zk4Show);
		}
		long end_time = System.currentTimeMillis();
		long used = end_time-start_time;
		System.out.println("start at: "+ start_time + " end at : "+ end_time + " used : "+used );
		return resultList;
	}

	public Map<String, GroupBean> getGroups() {
		Map<String, GroupBean> groups = ZKUtil.getZKGroup();
		return groups;
		
	}
	
	
	

}
