package it.pizzeria.service;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import it.pizzeria.model.Pizza;
import it.pizzeria.model.SpecialOffer;
import it.pizzeria.repository.PizzaRepository;
import it.pizzeria.repository.SpecialOfferRepository;


@Service
public class PizzaServiceImpl implements PizzaService {
	
	@Autowired
	private PizzaRepository pizzaRepo;
	
	@Autowired
	private SpecialOfferRepository offerRepo;
	
	public Optional<Pizza> findById(Integer id){
		return pizzaRepo.findById(id);		
	}
	
	@Override
	public List<Pizza> findByName(String name) {
		List<Pizza> pizze = pizzaRepo.findByName(name);
		return pizze;
	}
	
	
	@Override
	public List<Pizza> findAll(){
		return pizzaRepo.findAll();
	}
	
	@Override
	public Pizza save(Pizza pizza) {
		return pizzaRepo.save(pizza);
	}
	
	@Override
	public Pizza update(Integer id, Pizza pizza) {
		Optional<Pizza> pizzaId = pizzaRepo.findById(id);
		if(pizzaId.isEmpty())
			throw new IllegalArgumentException("pizza non trovata");
		Pizza pizzaUpdate = pizzaRepo.findById(id).get();
		pizzaUpdate.setName(pizza.getName());
		pizzaUpdate.setDescription(pizza.getDescription());
		pizzaUpdate.setPhoto(pizza.getPhoto());
		pizzaUpdate.setPrice(pizza.getPrice());
		return pizzaRepo.save(pizzaUpdate);
	}
	
	
	public void delete(Integer id) {
		Optional<Pizza> pizzaId = pizzaRepo.findById(id);
		Pizza pizza = pizzaRepo.findById(id).get();
		if (pizzaId.isEmpty())
			throw new IllegalArgumentException("pizza non trovata");
		List<SpecialOffer> pizzaOffers = new ArrayList<SpecialOffer>();
		pizzaOffers = pizza.getSpecialOffers();
		for (SpecialOffer offers : pizzaOffers) {
			offerRepo.deleteById(offers.getId());
		}
		pizzaRepo.deleteById(id);
	}


}
