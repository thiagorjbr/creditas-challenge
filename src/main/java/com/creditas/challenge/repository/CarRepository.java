package com.creditas.challenge.repository;

import com.creditas.challenge.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Integer> {

}
