package com.creditas.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.creditas.challenge.model.Cav;

public interface CavRepository extends JpaRepository<Cav, Integer>{
	public Cav findByName(String name);
}
