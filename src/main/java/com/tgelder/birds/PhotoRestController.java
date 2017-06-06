package com.tgelder.birds;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/photos")
public class PhotoRestController {

	private PhotoRepository photoRepository;

	@Autowired
	PhotoRestController(PhotoRepository photoRepository) {
		this.photoRepository = photoRepository;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	Photo getPhoto(@PathVariable Long id) {
		
		return this.photoRepository.findOne(id);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/")
	List<Photo> getPhotos() {
		
		return this.photoRepository.findAll();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/")
	ResponseEntity<?> add(@RequestBody Photo photo) {

		Photo result = photoRepository.save(photo);
		
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest().path("/{id}")
			.buildAndExpand(result.getId()).toUri();

		return ResponseEntity.created(location).build();

	}
	
}
