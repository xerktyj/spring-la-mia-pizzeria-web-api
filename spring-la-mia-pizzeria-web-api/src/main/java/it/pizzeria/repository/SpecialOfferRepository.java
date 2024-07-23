package it.pizzeria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.pizzeria.model.Pizza;
import it.pizzeria.model.SpecialOffer;

public interface SpecialOfferRepository extends JpaRepository<SpecialOffer, Integer> {
	
	public List<SpecialOffer>  findByPizza(Pizza pizza);
	
	public SpecialOffer findById(int id);

}
