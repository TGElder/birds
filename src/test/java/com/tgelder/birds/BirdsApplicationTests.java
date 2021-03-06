package com.tgelder.birds;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;


import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.exparity.hamcrest.date.DateMatchers.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BirdsApplication.class)
@WebAppConfiguration
@ActiveProfiles(profiles = "test")
public class BirdsApplicationTests {

	@Autowired
	private BirdRepository birdRepository;
	
	@Autowired
	private PhotoRepository photoRepository;

	private List<Bird> testBirds = new ArrayList<> ();
	private List<Photo> testPhotos = new ArrayList<> ();
	
    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private ResourceLoader resourceLoader;
    
	@Before
	public void setup() throws Exception {
		
        mockMvc = webAppContextSetup(webApplicationContext).build();
		
		birdRepository.deleteAllInBatch();
		photoRepository.deleteAllInBatch();
		
		
		testPhotos.add(photoRepository.save(new Photo("barnes_wetland_centre.jpg","Barnes Wetland Centre", new SimpleDateFormat("yyyyMMdd").parse("20161215"))));
		testPhotos.add(photoRepository.save(new Photo("elmley.jpg","Elmley", new SimpleDateFormat("yyyyMMdd").parse("20161122"))));
		testPhotos.add(photoRepository.save(new Photo("farne_islands.jpg","Farne Islands", new SimpleDateFormat("yyyyMMdd").parse("20170602"))));
		
		Bird blackbird = new Bird("Blackbird");
		Set<Photo> blackBirdPhotos = new HashSet<> ();
		blackBirdPhotos.add(testPhotos.get(0));
		blackBirdPhotos.add(testPhotos.get(1));
		blackbird.setPhotos(blackBirdPhotos);
		blackbird.setFavourite(testPhotos.get(0));
		
		testBirds.add(birdRepository.save(blackbird));

		
	
		
	}
	
	@Test
	public void getBirds() throws Exception {
				
		mockMvc.perform(get("/birds/"))
			   .andExpect(status().isOk())
			   .andExpect(content().contentType(contentType))
			   .andExpect(jsonPath("$.content", hasSize(1)))
			   .andExpect(jsonPath("$.content[*].bird.name", containsInAnyOrder("Blackbird")));

	}
	
	@Test
	public void getBird() throws Exception {
				
		mockMvc.perform(get("/birds/"+testBirds.get(0).getId().intValue()))
			   .andExpect(status().isOk())
			   .andExpect(content().contentType(contentType))
			   .andExpect(jsonPath("$.bird.id", is(testBirds.get(0).getId().intValue())))
			   .andExpect(jsonPath("$.bird.name", is("Blackbird")))
			   .andExpect(jsonPath("$.bird.photos", hasSize(2)))
			   .andExpect(jsonPath("$.bird.photos[*].location", containsInAnyOrder("Elmley","Barnes Wetland Centre")))
			   .andExpect(jsonPath("$.bird.favourite.location", is("Barnes Wetland Centre")))
		   	   .andExpect(jsonPath("$.bird.firstSeen.timestamp", is(new SimpleDateFormat("yyyyMMdd").parse("20161122").getTime())));


	}
	
	@Test
	public void postBird() throws Exception {
				
		Resource resource = resourceLoader.getResource("classpath:bullfinch_post.json");
		String json = new BufferedReader(new InputStreamReader(resource.getInputStream()))
			.lines()
			.collect(Collectors.joining("\n"));
		
		
		MvcResult result = mockMvc.perform(post("/birds/")
							.contentType(contentType)
							.content(json))
							.andExpect(status().isCreated())
							.andReturn();
		
		mockMvc.perform(get(result.getResponse().getRedirectedUrl()))
				.andExpect(jsonPath("$.bird.name", is("Bullfinch")));
		

	}
	
	@Test
	public void putBirdPhotos() throws Exception {
				
		String json = "{\"photos\":[{\"id\":"
				+testPhotos.get(0).getId().intValue()
				+"},{\"id\":"
				+testPhotos.get(2).getId().intValue()
				+"}]}";
				
		mockMvc.perform(put("/birds/"+testBirds.get(0).getId().intValue())
			.contentType(contentType)
			.content(json))
			.andExpect(status().isOk());
							
		
		mockMvc.perform(get("/birds/"+testBirds.get(0).getId().intValue()))
		   .andExpect(status().isOk())
		   .andExpect(content().contentType(contentType))
		   .andExpect(jsonPath("$.bird.photos", hasSize(2)))
		   .andExpect(jsonPath("$.bird.photos[*].location", containsInAnyOrder("Barnes Wetland Centre","Farne Islands")))
		   .andExpect(jsonPath("$.bird.favourite.location", is("Barnes Wetland Centre")));
	}
	
	@Test
	public void putBirdFavourite() throws Exception {
				
		String json = "{\"favourite\":{\"id\":"
				+testPhotos.get(1).getId().intValue()
				+"}}";
		
		System.out.println(json);
				
		mockMvc.perform(put("/birds/"+testBirds.get(0).getId().intValue())
			.contentType(contentType)
			.content(json))
			.andExpect(status().isOk());
							
		
		mockMvc.perform(get("/birds/"+testBirds.get(0).getId().intValue()))
		   .andExpect(status().isOk())
		   .andExpect(content().contentType(contentType))
		   .andExpect(jsonPath("$.bird.photos", hasSize(2)))
		   .andExpect(jsonPath("$.bird.photos[*].location", containsInAnyOrder("Barnes Wetland Centre","Elmley")))
		   .andExpect(jsonPath("$.bird.favourite.location", is("Elmley")));
	}
	
	@Test
	public void getPhotos() throws Exception {
				
		mockMvc.perform(get("/photos/"))
			   .andExpect(status().isOk())
			   .andExpect(content().contentType(contentType))
			   .andExpect(jsonPath("$.content", hasSize(3)))
			   .andExpect(jsonPath("$.content[*].photo.location", containsInAnyOrder("Elmley","Barnes Wetland Centre","Farne Islands")));

	}
	
	@Test
	public void getPhoto() throws Exception {
				
		mockMvc.perform(get("/photos/"+testPhotos.get(0).getId().intValue()))
			   .andExpect(status().isOk())
			   .andExpect(content().contentType(contentType))
			   .andExpect(jsonPath("$.photo.id", is(testPhotos.get(0).getId().intValue())))
			   .andExpect(jsonPath("$.photo.path", is("barnes_wetland_centre.jpg")))
			   .andExpect(jsonPath("$.photo.location", is("Barnes Wetland Centre")))
			   .andExpect(jsonPath("$.photo.timestamp", is(new SimpleDateFormat("yyyyMMdd").parse("20161215").getTime())));

	}
	
	@Test
	public void postPhoto() throws Exception {
				
		Resource resource = resourceLoader.getResource("classpath:dungeness_post.json");
		String json = new BufferedReader(new InputStreamReader(resource.getInputStream()))
			.lines()
			.collect(Collectors.joining("\n"));
		
		
		MvcResult result = mockMvc.perform(post("/photos/")
							.contentType(contentType)
							.content(json))
							.andExpect(status().isCreated())
							.andReturn();
		
		mockMvc.perform(get(result.getResponse().getRedirectedUrl()))
		   .andExpect(jsonPath("$.photo.path", is("dungeness.jpg")))
		   .andExpect(jsonPath("$.photo.location", is("Dungeness")))
		   .andExpect(jsonPath("$.photo.timestamp", is(new SimpleDateFormat("yyyyMMdd").parse("20170515").getTime())));
		

	}

	
	@Test
	public void testSequencer() throws Exception {
		
		birdRepository.deleteAllInBatch();

		
		String url1 = mockMvc.perform(post("/birds/")
				.contentType(contentType)
				.content("{\"name\":\"bird1\"}"))
				.andExpect(status().isCreated())
				.andReturn()
				.getResponse()
				.getRedirectedUrl();
		
		String url2 = mockMvc.perform(post("/birds/")
				.contentType(contentType)
				.content("{\"name\":\"bird2\"}"))
				.andExpect(status().isCreated())
				.andReturn()
				.getResponse()
				.getRedirectedUrl();
		
		String url3 = mockMvc.perform(post("/birds/")
				.contentType(contentType)
				.content("{\"name\":\"bird3\"}"))
				.andExpect(status().isCreated())
				.andReturn()
				.getResponse()
				.getRedirectedUrl();

		mockMvc.perform(put(url1)
				.contentType(contentType)
				.content("{\"photos\":[{\"id\":"
						+testPhotos.get(0).getId().intValue()
						+"}]}"))
				.andExpect(status().isOk());
		
		mockMvc.perform(put(url2)
				.contentType(contentType)
				.content("{\"photos\":[{\"id\":"
						+testPhotos.get(1).getId().intValue()
						+"}]}"))
				.andExpect(status().isOk());
		
		mockMvc.perform(put(url3)
				.contentType(contentType)
				.content("{\"photos\":[{\"id\":"
						+testPhotos.get(2).getId().intValue()
						+"}]}"))
				.andExpect(status().isOk());
		
		mockMvc.perform(get(url1))
		   .andExpect(status().isOk())
		   .andExpect(jsonPath("$.bird.sequence", is(2)));
		
		mockMvc.perform(get(url2))
		   .andExpect(status().isOk())
		   .andExpect(jsonPath("$.bird.sequence", is(1)));
		
		mockMvc.perform(get(url3))
		   .andExpect(status().isOk())
		   .andExpect(jsonPath("$.bird.sequence", is(3)));
	}


}
