package it.pizzeria.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.pizzeria.model.Ingredient;
import it.pizzeria.model.Pizza;
import it.pizzeria.repository.IngredientRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/pizza")
public class IngredientController {
	
	@Autowired
	private IngredientRepository ingredientRepo;
	
	@GetMapping("/ingredient/{id}")
	public String idPizza(@PathVariable int id, Model m) {
		m.addAttribute("ingrediente", ingredientRepo.findById(id));
		return "/ingredienti/ingredient";
	}
	
	@GetMapping("/ingredients")
	public String index(Model model) {
		List<Ingredient> listIngredients = new ArrayList<Ingredient>();
		listIngredients = ingredientRepo.findAll();
		model.addAttribute("ingredientsList", listIngredients);
		return "/ingredienti/ingredients";
	}
	
	@GetMapping("/addingredient")
	public String addIngredient(Model model) {
		model.addAttribute("addMode", true);
		model.addAttribute("ingrediente", new Ingredient());
		return "/ingredienti/add-edit_ingredient";
	}
	
	@PostMapping("/addingredient")
	public String storeIngredient(@Valid @ModelAttribute("ingrediente") Ingredient formIngredient, BindingResult bindingResult, Model model) {
		model.addAttribute("addMode", true);
		List<Ingredient> list = ingredientRepo.findBySerialNumber(formIngredient.getSerialNumber());
		
		if (list.size() > 0) {
			bindingResult.addError(new ObjectError("ingrediente", "ingrediente già prensente in lista"));
		} else if(formIngredient.getSerialNumber() != null) {
			String code = Long.toString(formIngredient.getSerialNumber());
			if (code.length() != 8) {
				bindingResult.addError(new ObjectError("ingrediente", "inserire un codice di 8 cifre hai inserito: " + code.length()));
			}
		}			
		
		if (bindingResult.hasErrors()) {
			return "/ingredienti/add-edit_ingredient";
		}
		
		ingredientRepo.save(formIngredient);
		return "/ingredienti/ingredient";
	}
	
	@GetMapping("/ingredient/{id}/edit")
	public String editIngredient(@PathVariable int id, Model m) {
		m.addAttribute("addMode",false);
		m.addAttribute("ingrediente", ingredientRepo.findById(id));
		return "/ingredienti/add-edit_ingredient";
	}
	
	@PostMapping("/ingredient/{id}/edit")
	public String storeEditIngredient(@Valid @ModelAttribute("ingrediente") Ingredient formIngredient,
			BindingResult bindingResult, Model model) {
		model.addAttribute("addMode", false);
		
		if (formIngredient.getSerialNumber() != null) {
			String code = Long.toString(formIngredient.getSerialNumber());
			if (code.length() != 8) {
				bindingResult.addError(
						new ObjectError("ingrediente", "inserire un codice di 8 cifre hai inserito: " + code.length()));
			}
		}

		if (bindingResult.hasErrors()) {
			return "/ingredienti/add-edit_ingredient";
		}
		ingredientRepo.save(formIngredient);
		return "/ingredienti/ingredient";
	}
	
	@PostMapping("/ingredient/{id}/delete")
	public String deleteIngredient(@PathVariable int id, Model model) {	
		model.addAttribute("deleteMode", true);
		List<Pizza> pizze = ingredientRepo.findById(id).getPizze();
		if(pizze.size()>0) {
			model.addAttribute("deleteError", true);
		}else {
			model.addAttribute("deleteCorrect", true);
			ingredientRepo.deleteById(id);
		}		
		return "/ingredienti/ingredient";
	}
	
	@GetMapping("/ingredient/{id}/listpizze")
	public String ingredientListPizze(@PathVariable int id, Model model) {
		List<Pizza> listPizze = ingredientRepo.findById(id).getPizze();
		model.addAttribute("pizzeList", listPizze);
		return  "/pizzeria/pizze";
	}


}
