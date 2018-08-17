package com.neo.web;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.neo.domain.ZKClusterDetail;
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

    
    @RequestMapping("/chart")
 	public String chart() {
    	try {
			ConcurrentHashMap<String, ZKClusterDetail> all = zkService.getAll();
			Set<Entry<String, ZKClusterDetail>> entrySet = all.entrySet();
			Iterator<Entry<String, ZKClusterDetail>> iterator = entrySet.iterator();
			while(iterator.hasNext()) {
				Entry<String, ZKClusterDetail> entry = iterator.next();
				String clustername = entry.getKey();
				ZKClusterDetail detail = entry.getValue();
				detail.full();
				System.out.println(clustername+ " detail is : "+ detail);
				
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

 		return "chart";
 	}
}