package it.pizzeria.controller;

import java.time.LocalDate;
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


import it.pizzeria.model.Pizza;
import it.pizzeria.model.SpecialOffer;
import it.pizzeria.repository.PizzaRepository;
import it.pizzeria.repository.SpecialOfferRepository;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/pizza")
public class SpecialOfferController {
	
	@Autowired
	private SpecialOfferRepository specialOfferRepo;
	
	@Autowired
	private PizzaRepository pizzaRepo;
	
	@PostMapping("/addoffer")
	public String storeOfferPizza(@Valid @ModelAttribute("offerta") SpecialOffer formOffer, BindingResult bindingResult,
			Model model) {
		model.addAttribute("addMode", true);

		if (formOffer.getEndDate() != null && formOffer.getStartDate() != null) {
			if (formOffer.getEndDate().isBefore(formOffer.getStartDate())) {
				bindingResult.addError(
						new ObjectError("offerta", "la data di fine promozione è prima della data di inizio"));
			}
		}

		if (bindingResult.hasErrors()) {
			return "/offerte/add-edit_offer";
		}

		specialOfferRepo.save(formOffer);

		return "/offerte/offer";
	}
	
	@GetMapping("/specialoffers/{id}")
	public String indexSpecialOffers(@PathVariable("id") Integer id, Model model){
		Pizza pizza = pizzaRepo.findById(id).get();
		List<SpecialOffer> listOffers = new ArrayList<SpecialOffer>();
		listOffers = specialOfferRepo.findByPizza(pizza);

		if (listOffers.size() == 0) {
			model.addAttribute("listEmpty", true);
		} else {
			model.addAttribute("offersList", listOffers);
		}

		return "/offerte/special_offers";

	}
	
	@GetMapping("/offer/{id}")
	public String dataOffer(@PathVariable("id") Integer id ,Model model) {
		model.addAttribute("offerta", specialOfferRepo.findById(id).get());
		return "/offerte/offer";
	}
	
	@GetMapping("/offer/{id}/edit")
	public String editOffer(@PathVariable("id") Integer id ,Model model) {
		model.addAttribute("addMode", false);
		model.addAttribute("offerta", specialOfferRepo.findById(id).get());
		return "/offerte/add-edit_offer";
	}
	
	@PostMapping("/offer/{id}/edit")
	public String storeEditOffer(@Valid @ModelAttribute("offerta") SpecialOffer formOffer, BindingResult bindingResult,
			Model model) {		
		model.addAttribute("addMode", false);		
		if (formOffer.getEndDate() != null && formOffer.getStartDate() != null) {
			if (formOffer.getEndDate().isBefore(formOffer.getStartDate())) {
				bindingResult.addError(
						new ObjectError("offerta", "la data di fine promozione è prima della data di inizio"));
			}
		}		
		if (bindingResult.hasErrors()) {
			return "/offerte/add-edit_offer";
		}
		specialOfferRepo.save(formOffer);
		return "/offerte/offer";
	}
	
	@PostMapping("/offer/{id}/delete")
	public String deleteOffer(@PathVariable int id) {		
		specialOfferRepo.deleteById(id);
		return "/offerte/offer";
	}

}
