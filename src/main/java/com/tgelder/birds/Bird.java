package com.tgelder.birds;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Bird {
		
	@Id @GeneratedValue	private Long id;	
	private String name;
	@ManyToOne private Photo favourite;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name="BIRD_PHOTO",
		    joinColumns=@JoinColumn(name="BIRD_ID", referencedColumnName="ID"),
		    inverseJoinColumns=@JoinColumn(name="PHOTO_ID", referencedColumnName="ID"))
	private Set<Photo> photos = new HashSet<>();
	
	private int sequence;
	
	public Bird(String name) {
		this.name = name;
	}
	
	Bird() { // required by JPA
		
	}
	
	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public Set<Photo> getPhotos() {
		return photos;
	}
	
	public void setPhotos(Set<Photo> photos) {
		this.photos = photos;
	}
	
	public Photo getFavourite() {
		return favourite;
	}
	
	public void setFavourite(Photo photo) {
		favourite  = photo;
	}
	
	public int getPhotoCount(){
		return photos.size();
	}
	
	public String toString() {
		
		return id+": "+name;
	}

	public Photo getFirstSeen() {
		
		if (photos.isEmpty()) {
			return null;
		}
		
		List<Photo> photosList = new ArrayList<>(photos);
		photosList.sort((a, b)->a.getTimestamp().compareTo(b.getTimestamp()));
		return photosList.get(0);
	}


	public Photo getLastSeen() {
		
		if (photos.isEmpty()) {
			return null;
		}
		
		List<Photo> photosList = new ArrayList<>(photos);
		photosList.sort((a, b)->b.getTimestamp().compareTo(a.getTimestamp()));
		return photosList.get(0);
	}

	
	public void setSequence(int sequence) {
		this.sequence = sequence;

	}
	
	public int getSequence() {
		return sequence;
	}

	
	
	
	
}
