package com.tgelder.birds;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceSupport;

public class PhotoResource extends ResourceSupport {

	private final Photo photo;
	
	public PhotoResource(Photo photo) {
		
		this.photo = photo;
		this.add(linkTo(methodOn(PhotoRestController.class).getPhoto(photo.getId())).withRel("self"));
		
		
	}
	
	public Photo getPhoto() {
		return photo;
	}
	
}
