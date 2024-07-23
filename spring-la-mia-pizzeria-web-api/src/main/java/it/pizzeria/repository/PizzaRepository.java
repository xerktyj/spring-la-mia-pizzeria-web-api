package it.pizzeria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.pizzeria.model.Pizza;


public interface PizzaRepository extends JpaRepository<Pizza, Integer> {
	
	public List<Pizza> findByName(String name);
	
	public Pizza findById(int id);	
	

}
