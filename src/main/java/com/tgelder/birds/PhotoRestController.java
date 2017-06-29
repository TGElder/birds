package com.tgelder.birds;

import java.net.URI;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/photos")
public class PhotoRestController {

	private PhotoRepository photoRepository;

	@Autowired
	PhotoRestController(PhotoRepository photoRepository) {
		this.photoRepository = photoRepository;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	PhotoResource getPhoto(@PathVariable Long id) {
		
		return new PhotoResource(this.photoRepository.findOne(id));
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/",
			produces = MediaType.APPLICATION_JSON_VALUE)
	Resources<PhotoResource> getPhotos() {
		
		return new Resources<>(this.photoRepository.findAll()
				.stream()
				.map(PhotoResource::new)
				.collect(Collectors.toList()));
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/")
	ResponseEntity<?> add(@RequestBody Photo photo) {

		Photo result = photoRepository.save(photo);

		PhotoResource resource = new PhotoResource(result);
		
		return ResponseEntity.created(URI.create(resource.getLink("self").getHref())).build();
		
	}
	
}
