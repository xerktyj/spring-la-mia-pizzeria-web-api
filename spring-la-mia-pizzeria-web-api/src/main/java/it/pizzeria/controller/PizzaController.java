package it.pizzeria.controller;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import it.pizzeria.model.Pizza;
import it.pizzeria.model.SpecialOffer;
import it.pizzeria.repository.IngredientRepository;
import it.pizzeria.repository.PizzaRepository;
import it.pizzeria.repository.SpecialOfferRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/home")
public class PizzaController {

	@Autowired
	private PizzaRepository pizzaRepo;
	
	@Autowired
	private SpecialOfferRepository specialOfferRepo;
	
	@Autowired
	private IngredientRepository ingredientRepo;
	
	@GetMapping
	public String home() {
		return "/pizzeria/home";
	}

	@GetMapping("/pizze")
	public String index(@RequestParam(name = "name", required = false) String name, Model model){
		List<Pizza> listPizze = new ArrayList<Pizza>();
		
			if (searchPizza(name)) {
				listPizze = pizzaRepo.findByName(name);
			}else {
				listPizze = pizzaRepo.findAll();
			}
			model.addAttribute("pizzeList", listPizze);
			return  "/pizzeria/pizze";		
	}

	@GetMapping("/pizze/{id}")
	public String idPizza(@PathVariable int id, Model m) {	
		List<SpecialOffer> offers = specialOfferRepo.findByPizza(pizzaRepo.findById(id));
		SpecialOffer offerActive = null;
		for(SpecialOffer offer : offers) {
			if(offer.getStartDate().isBefore(LocalDate.now()) && offer.getEndDate().isAfter(LocalDate.now()) ) {
				offerActive = offer;  
			}
		}
		m.addAttribute("pizza", pizzaRepo.findById(id));
		m.addAttribute("offer", offerActive);
		return "/pizzeria/data_pizza";
	}
	
	@GetMapping("/pizze/addpizza")
	public String addPizza(Model model) {
		model.addAttribute("addMode", true);
		model.addAttribute("pizza", new Pizza());
		model.addAttribute("ingredientsList", ingredientRepo.findAll());
		return "/pizzeria/add-edit_pizza";
	}
	
	@PostMapping("/pizze/addpizza")
	public String storePizza(@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult, Model model) {
		model.addAttribute("addMode", true);
		List<Pizza> list = pizzaRepo.findByName(formPizza.getName());		
		if(list.size()>0) {
			bindingResult.addError(new ObjectError("pizza","pizza già prensente in lista"));
		}
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("ingredientsList", ingredientRepo.findAll());
			return "/pizzeria/add-edit_pizza";
		}
		
		pizzaRepo.save(formPizza);
		return "/pizzeria/data_pizza";
	}
	
	@GetMapping("/pizze/editpizza/{id}")
	public String editPizza(@PathVariable int id, Model m) {
		m.addAttribute("addMode",false);
		m.addAttribute("pizza", pizzaRepo.findById(id));
		m.addAttribute("ingredientsList", ingredientRepo.findAll());
		return "/pizzeria/add-edit_pizza";
	}
	
	@PostMapping("/pizze/editpizza/{id}")
	public String storeEditPizza(@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult, Model model) {
		model.addAttribute("addMode", false);		
		if (bindingResult.hasErrors()) {
			model.addAttribute("ingredientsList", ingredientRepo.findAll());
			return "/pizzeria/add-edit_pizza";
		}
		pizzaRepo.save(formPizza);
		return "/pizzeria/data_pizza";
	}
	
	@PostMapping("/pizze/delete/{id}")
	public String deletePizza(@PathVariable int id, Model model) {
		Pizza pizza = pizzaRepo.findById(id);
		if(specialOfferRepo.findByPizza(pizza).size()>0) {
			model.addAttribute("deleteError", true );
		}else {
			pizzaRepo.deleteById(id);
			model.addAttribute("deleteCorrect", true);
		}		
		return "/pizzeria/data_pizza";
	}

	@GetMapping("/pizze/addoffer/{id}")
	private String addOffer(@PathVariable("id") Integer id,Model model) {
		model.addAttribute("addMode", true);
		Pizza pizza = pizzaRepo.findById(id).get();
		SpecialOffer offerta = new SpecialOffer();
		offerta.setPizza(pizza);
		model.addAttribute("offerta" , offerta);
		return "offerte/add-edit_offer";
	}
	
	private boolean searchPizza(String name) {
		boolean message = false;
		if (name != null) {
			message = true;
		}
		return message;
	}
}
