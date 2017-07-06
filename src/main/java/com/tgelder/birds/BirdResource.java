package com.tgelder.birds;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.persistence.Entity;

import org.springframework.hateoas.ResourceSupport;

public class BirdResource extends ResourceSupport {

	private Bird bird;
	
	public BirdResource(Bird bird) {
		
		this.bird = bird;
		this.add(linkTo(methodOn(BirdRestController.class).getBird(bird.getId())).withRel("self"));
		
		
	}
	
	BirdResource() {
		
	}
	
	public Bird getBird() {
		return bird;
	}
	
}
