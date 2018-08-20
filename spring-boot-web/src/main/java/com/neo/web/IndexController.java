package com.neo.web;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
			ConcurrentHashMap<String, ZKClusterDetail> all = zkService.getAll();
			Set<Entry<String, ZKClusterDetail>> entrySet = all.entrySet();
			Iterator<Entry<String, ZKClusterDetail>> iterator = entrySet.iterator();
			int good =0;
			int bad = 0;
			while(iterator.hasNext()) {
				Entry<String, ZKClusterDetail> entry = iterator.next();
				String clustername = entry.getKey();
				ZKClusterDetail detail = entry.getValue();
				detail.full();
				if(detail.getStatus()==ZKStauts.OK) {
					good++;
				}else {
					bad++;
				}
				System.out.println(clustername+ " detail is : "+detail);
			}
			model.addAttribute("all", all);
			model.addAttribute("good",good);
			model.addAttribute("bad",bad);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

 		return "charts";
 	}
}