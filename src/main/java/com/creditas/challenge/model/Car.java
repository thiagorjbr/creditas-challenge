package com.creditas.challenge.model;

import javax.persistence.Entity;

@Entity
public class Car extends BaseEntity<Car>{
	
	private String brand;
	
	private String model;

	public String getBrand() {
		return brand;
	}

	public String getModel() {
		return model;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public void setModel(String model) {
		this.model = model;
	}
	
	@Override
	public void merge(Car another) {
		this.setBrand(another.getBrand() == null ? this.getBrand() : another.getBrand());
		this.setModel(another.getModel() == null ? this.getModel() : another.getModel());		
	}
}
