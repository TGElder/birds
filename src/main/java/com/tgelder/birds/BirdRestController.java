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
@RequestMapping("/birds")
public class BirdRestController {

	private BirdRepository birdRepository;

	@Autowired
	BirdRestController(BirdRepository birdRepository) {
		this.birdRepository = birdRepository;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	BirdResource getBird(@PathVariable Long id) {
		
		return new BirdResource(this.birdRepository.findOne(id));
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/",
			produces = MediaType.APPLICATION_JSON_VALUE)
	Resources<BirdResource> getBirds() {
				
		return new Resources<>(this.birdRepository.findAll()
				.stream()
				.map(BirdResource::new)
				.collect(Collectors.toList()));
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/")
	ResponseEntity<?> add(@RequestBody Bird bird) {

		Bird result = birdRepository.save(bird);
		birdRepository.sequence();
		
		BirdResource resource = new BirdResource(result);
		
		return ResponseEntity.created(URI.create(resource.getLink("self").getHref())).build();

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
		birdRepository.sequence();

		return ResponseEntity.ok().build();

	}
	
}
