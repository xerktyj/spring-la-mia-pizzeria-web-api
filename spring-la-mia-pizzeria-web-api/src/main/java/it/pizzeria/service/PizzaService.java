package it.pizzeria.service;

import java.util.Optional;
import java.util.List;
import it.pizzeria.model.Pizza;

public interface PizzaService {
	
	public Optional<Pizza> findById(Integer id);
	
	public List<Pizza> findByName(String name);
	
	public List<Pizza> findAll();
	
	public Pizza save(Pizza pizza);
	
	public Pizza update(Integer id, Pizza pizza);
	
	public void delete(Integer id);

}
