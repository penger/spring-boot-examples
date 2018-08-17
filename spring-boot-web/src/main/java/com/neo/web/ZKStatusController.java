package com.neo.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.neo.domain.ZK4Show;
import com.neo.domain.ZKInfo;
import com.neo.service.ZKService;
import com.neo.util.ZKUtil;

@Controller
public class ZKStatusController {
	
	
	
	public static void main(String[] args) {
		List<String> get4Words = ZKUtil.get4Words();
    	StringBuffer sb = new StringBuffer();
    	for (String word : get4Words) {
    		sb.append(word+ " ,");
		}
    	String command_useful = sb.toString();
    	command_useful = command_useful.substring(0,command_useful.length()-1);
    	System.out.println(command_useful);
	}
	
	@Autowired
	private ZKService zkService;
	
	
	 @RequestMapping("/u")
	    public void getZKstat(HttpServletResponse response) {
	    	response.setHeader("Content-type", "text/html;charset=UTF-8");
	    	response.setCharacterEncoding("UTF-8");
	    	List<String> get4Words = ZKUtil.get4Words();
	    	StringBuffer sb = new StringBuffer();
	    	for (String word : get4Words) {
	    		sb.append(word+ " ,");
			}
	    	String command_useful = sb.toString();
	    	command_useful = command_useful.substring(0,command_useful.length()-1);
	    	try {
		        PrintWriter writer;
		        writer=response.getWriter();
		        String usage="stat用法：http://localhost:8080/stat?yw=all<br/>"
		        		+ "command用法：http://localhost:8080/getzkinfo?yw=all&command=conf<br/>"
		        		+ "常用四字命令："+command_useful+"<br/><br/><br/><br/>";
		        
		        writer.print(usage);
		        
		        
		        writer.print("<table border='1'>");
		        List<ZKInfo> zkList = ZKUtil.getZKList("all");
		        for (ZKInfo zkInfo : zkList) {
		        	String content="";
		        	content="<td>"+zkInfo.getName()+"</td><td>"+zkInfo.getIplist().replaceAll(",", "&nbsp;&nbsp;&nbsp;")+"</td><td>"+zkInfo.getPort()+"</td>";
					writer.println("<tr>"+content+"<tr/>");
				}
		        writer.print("</table>");
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	    
	
	//过滤业务相关的zk
    @RequestMapping("/stat")
    public void getZKstat(@RequestParam(value="yw",required=false,defaultValue="测试") String yw,HttpServletResponse response) {
    	response.setHeader("Content-type", "text/html;charset=UTF-8");
    	response.setCharacterEncoding("UTF-8");
    	List<ZK4Show> zk4ShowList = zkService.getZK4Show("stat",yw);
    	for (ZK4Show zk4Show : zk4ShowList) {
			zk4Show.converntKey("Mode");
		}
        PrintWriter writer;
		try {
			writer = response.getWriter();
			String temp="";
			for (ZK4Show zk4Show : zk4ShowList) {
				String new_temp=zk4Show.getSystem();
				if(!temp.equals(new_temp)) {
					if(!temp.equals("")) {
						writer.print("<br/>");
					}
					writer.print(new_temp+": &nbsp;&nbsp;&nbsp;");
					temp=new_temp;
				}
				writer.print(zk4Show.getIp()+"-");
				if(zk4Show.getKey().equalsIgnoreCase("follower")) {
					writer.print("<font color='blue'>"+zk4Show.getKey()+"</font>     ");
				}else if(zk4Show.getKey().equalsIgnoreCase("leader")) {
					writer.print("<font color='green'>"+zk4Show.getKey()+"</font>     ");
				}else {
					writer.print("<font color='red'>"+zk4Show.getKey()+"</font>     ");
				}
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @RequestMapping("/getzkinfo")
    public void getZKInfo(@RequestParam(value="command",required=false,defaultValue="stat") String command ,
    		@RequestParam(value="yw",required=false,defaultValue="测试") String yw,HttpServletResponse response) {
    	response.setHeader("Content-type", "text/html;charset=UTF-8");
    	response.setCharacterEncoding("UTF-8");
    	List<String> get4Words = ZKUtil.get4Words();
    	List<ZK4Show> zk4ShowList = zkService.getZK4Show(command,yw);
    	PrintWriter writer;
    	if(!get4Words.contains(command)) {
    		try {
				writer=response.getWriter();
				writer.print("unknown command :"+ command);
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	try {
			writer = response.getWriter();
			String tmp="";
			for (ZK4Show zk4Show : zk4ShowList) {
				
				if(!tmp.equals(zk4Show.getSystem())) {
					writer.print("<font color='grey'>业务名称："+zk4Show.getSystem()+"</font><br/>");
					tmp=zk4Show.getSystem();
				}
				
				writer.println(zk4Show.getIp()+"<br/>");
				writer.println();
				List<String> info = zk4Show.getInfo();
				for (String line : info) {
					if(line.contains("watch")) {
						writer.print(line+"<br/>");
					}
				}
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    
    
}