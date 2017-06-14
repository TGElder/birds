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
@RequestMapping("/birds")
public class BirdRestController {

	private BirdRepository birdRepository;

	@Autowired
	BirdRestController(BirdRepository birdRepository) {
		this.birdRepository = birdRepository;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	Bird getBird(@PathVariable Long id) {
		
		return this.birdRepository.findOne(id);
	}
	
//	@RequestMapping(method = RequestMethod.GET, value = "/{birdName}")
//	Bird getBird(@PathVariable String birdName) {
//		
//		return this.birdRepository.findByName(birdName);
//	}
//	
	@RequestMapping(method = RequestMethod.GET, value = "/")
	List<Bird> getBirds() {
		
		return this.birdRepository.findAll();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/")
	ResponseEntity<?> add(@RequestBody Bird bird) {

		Bird result = birdRepository.save(bird);
		
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest().path("/{id}")
			.buildAndExpand(result.getId()).toUri();

		return ResponseEntity.created(location).build();

	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	ResponseEntity<?> put(@PathVariable Long id, @RequestBody Bird put) {

		Bird bird = birdRepository.findOne(id);
		
		if (bird == null) {
			
			ResponseEntity.notFound().build();
		}
		
		if (put.getFavourite() != null){
			bird.setFavourite(put.getFavourite());
		}
		
		if (!put.getPhotos().isEmpty()){
			bird.setPhotos(put.getPhotos());
		}
		
		birdRepository.save(bird);

		return ResponseEntity.ok().build();

	}
	
}
