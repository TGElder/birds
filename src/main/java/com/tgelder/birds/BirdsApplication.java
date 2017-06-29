package com.tgelder.birds;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.tgelder.birds.storage.StorageProperties;
import com.tgelder.birds.storage.StorageService;

@SpringBootApplication
@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class BirdsApplication {

	private static final Logger log = LoggerFactory.getLogger(BirdsApplication.class);
	
    @Autowired
    JdbcTemplate jdbcTemplate;
	
	public static void main(String[] args) {
		SpringApplication.run(BirdsApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner demo(BirdRepository birdRepository, PhotoRepository photoRepository) {
		
		return (args) -> {
			
			Bird blackBird = new Bird("Blackbird");
			Bird snipe = new Bird("Snipe");
			Bird oystercatcher = new Bird("Oystercatcher");
			
			Photo photo1 = new Photo("photo1.jpg","Thorpe Hall", new SimpleDateFormat("yyyyMMdd").parse("20160602"));
			Photo photo2 = new Photo("photo2.jpg","Gyllngvase Beach", new SimpleDateFormat("yyyyMMdd").parse("20161001"));
			Photo photo3 = new Photo("photo3.jpg","Barnes Wetland Centre", new SimpleDateFormat("yyyyMMdd").parse("20161215"));
			
			photo1 = photoRepository.save(photo1);
			photo2 = photoRepository.save(photo2);
			photo3 = photoRepository.save(photo3);
		
			blackBird.getPhotos().add(photo1);
			blackBird.getPhotos().add(photo3);
			snipe.getPhotos().add(photo3);
			oystercatcher.getPhotos().add(photo2);
			
			blackBird.setFavourite(photo1);
		
			birdRepository.save(blackBird);
			birdRepository.save(snipe);
			birdRepository.save(oystercatcher);
			
			
			
//	        for (Object result : jdbcTemplate.queryForList("SELECT * FROM BIRD")) {
//	            log.info(result.toString());
//	        }
//	        
//	        for (Object result : jdbcTemplate.queryForList("SELECT * FROM PHOTO")) {
//	            log.info(result.toString());
//	        }
//	        
//	        for (Object result : jdbcTemplate.queryForList("SELECT * FROM BIRD_PHOTO")) {
//	            log.info(result.toString());
//	        }
		};
	}
	

	@Bean
	CommandLineRunner initStorage(StorageService storageService) {
		return (args) -> {
            //storageService.deleteAll();
            storageService.init();
		};
	}
}
