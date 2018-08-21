package com.neo.web;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.neo.domain.GroupBean;
import com.neo.domain.ZKClusterDetail;
import com.neo.domain.ZKStauts;
import com.neo.service.ZKService;


@Controller
public class IndexController {
	
	@Resource
	private ZKService zkService;
	
    @RequestMapping("/index")
	public String hello() {
//		model.addAttribute("greeting", "Hello!");
//
//		Date date = new Date();
//		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);        
//		String formattedDate = dateFormat.format(date);
//		model.addAttribute("currentTime", formattedDate);

		return "index";
	}

    
    @RequestMapping(value = "/chart")
 	public String chart(Model model) {
    	try {
    		//分组，用于获取分组的图表
    		Map<String, GroupBean> groups = zkService.getGroups();
			ConcurrentHashMap<String, ZKClusterDetail> all = zkService.getAll();
			LinkedList<ZKClusterDetail> linkedList = new LinkedList<>();
			Set<Entry<String, ZKClusterDetail>> entrySet = all.entrySet();
			Iterator<Entry<String, ZKClusterDetail>> iterator = entrySet.iterator();
			int good =0;
			int bad = 0;
			while(iterator.hasNext()) {
				Entry<String, ZKClusterDetail> entry = iterator.next();
				String clustername = entry.getKey();
				ZKClusterDetail detail = entry.getValue();
				detail.full();
				linkedList.add(detail);
				//将leaderip加入到统计
				if(!groups.isEmpty()) {
					Collection<GroupBean> groupBeans = groups.values();
					for (GroupBean groupBean : groupBeans) {
						Map<String, Integer> leaderMap = groupBean.getLeaderMap();
						if(leaderMap.containsKey(detail.getLeaderip())) {
							Integer leadercount = leaderMap.get(detail.getLeaderip());
							leadercount++;
							leaderMap.put(detail.getLeaderip(), leadercount);
						}
					}
				}
				
				if(detail.getStatus()==ZKStauts.OK) {
					good++;
				}else {
					bad++;
				}
				System.out.println(clustername+ " detail is : "+detail);
			}
			//组的详细值
			model.addAttribute("groups", groups.values());
			//组的数量
			model.addAttribute("groupcount",groups.size());
			Collections.sort(linkedList);
			model.addAttribute("all", linkedList);
			model.addAttribute("good",good);
			model.addAttribute("bad",bad);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

 		return "charts";
 	}
}