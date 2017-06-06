package com.tgelder.birds;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Photo {

	@Id
	@GeneratedValue
	private Long id;
	@Column
	private String path;
	@Column
	private String location;
	@Column(name = "TIMESTAMP_FIELD")
	private Date timestamp;
	
	public Photo(String path, String location, Date timestamp) {
		this.path = path;
		this.location = location;
		this.timestamp = timestamp;
	}
	
	Photo() { // required by JPA
		
	}
	
	public Long getId() {
		return id;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getLocation() {
		return location;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public String toString() {
		return id + ": " + path;
	}
	
}
