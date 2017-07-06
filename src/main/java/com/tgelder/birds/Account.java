package com.tgelder.birds;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Account {

	@Id String userName;
	String password;
	
	Account() { // Required by JPA
		
	}
	
	public Account(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	String getUserName() {
		return userName;
	}
	
	String getPassword() {
		return password;
	}
	
	public String toString() {
		return userName;
	}
	
}
