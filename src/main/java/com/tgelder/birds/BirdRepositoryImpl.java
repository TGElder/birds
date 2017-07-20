package com.tgelder.birds;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class BirdRepositoryImpl implements BirdRepositoryCustom {

	@Autowired
	BirdRepository repository;
	
	private final List<Bird> sequence = new ArrayList<>();
	
	@Override
	public void sequence(){
		
		sequence.clear();
		
		sequence.addAll(repository.findAll());
		
		List<Bird> all = new ArrayList<>(sequence);
		
		for (Bird bird : sequence) {
			bird.setSequence(-1);
		}
		
		sequence.removeIf(b->b.getFirstSeen()==null);
		
		sequence.sort((Bird a, Bird b)->a.getFirstSeen().getTimestamp().compareTo(b.getFirstSeen().getTimestamp()));
		
		for (int s=0; s<sequence.size(); s++) {
			sequence.get(s).setSequence(s+1);
		}
		
		for (Bird bird : all) {
			repository.save(bird);
		}
		
		

	}

	

}

