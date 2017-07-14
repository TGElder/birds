package com.tgelder.birds;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SummaryController {

	@Autowired
	BirdRepository birdRepository;
	
	@RequestMapping(value = "/", method  = RequestMethod.GET)
	public ModelAndView getSummary() {
		
		ModelAndView model = new ModelAndView("summary");
		
		List<Bird> birds = birdRepository.findAll();
		Map<Bird,String> thumbUrls = new HashMap<> ();
		Map<Bird,String> photoUrls = new HashMap<> ();
		
		birds.sort((a,b)->a.getSequence()-b.getSequence());
		
		for (Bird bird : birds) {
			if (bird.getFavourite()!=null){
				thumbUrls.put(bird, new PhotoResource(bird.getFavourite()).getLink("thumb").getHref());
				photoUrls.put(bird, new PhotoResource(bird.getFavourite()).getLink("photo").getHref());
			}
		}
		
		model.addObject("birds",birds);
		model.addObject("thumbUrls",thumbUrls);
		model.addObject("photoUrls",photoUrls);
		

		return model;
	}
	
}
