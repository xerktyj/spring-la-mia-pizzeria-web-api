package it.pizzeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import it.pizzeria.model.Ingredient;


public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
	
	public Ingredient findById(int id);
	
	public List<Ingredient> findByName(String ingredient);
	
	public List<Ingredient> findBySerialNumber(Long serialNumber);
	
    
}
