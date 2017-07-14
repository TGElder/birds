package com.tgelder.birds;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

public class PhotoResource extends ResourceSupport {

	private Photo photo;
	
	public PhotoResource(Photo photo) {
		
		this.photo = photo;
		this.add(linkTo(methodOn(PhotoRestController.class).getPhoto(photo.getId())).withRel("self"));
		this.add(new Link("/files/"+photo.getPath()+"_thumb.jpg").withRel("thumb"));
		this.add(new Link("/files/"+photo.getPath()+".jpg").withRel("photo"));
		
	}
	
	PhotoResource() {
		
	}
	
	public Photo getPhoto() {
		return photo;
	}
	
}
