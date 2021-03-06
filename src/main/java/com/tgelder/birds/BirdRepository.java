package com.tgelder.birds;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BirdRepository extends JpaRepository<Bird,Long>, BirdRepositoryCustom {
	
	Bird findByName(String name);
}
