package it.pizzeria.restcontroller.api;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.pizzeria.exception.IllegalListSizeException;
import it.pizzeria.model.Pizza;
import it.pizzeria.response.Payload;
import it.pizzeria.service.PizzaService;
import jakarta.validation.Valid;


@RestController
@CrossOrigin
@RequestMapping("/api/pizze")
public class PizzaRestControllerApi {
	
	@Autowired(required=true)
	private PizzaService pizzaService;
	
	@GetMapping
	public ResponseEntity index(@RequestParam(name = "name", required = false) String name) {
		List<Pizza> listPizze = new ArrayList<Pizza>();
		if (searchPizza(name)) {
			listPizze = pizzaService.findByName(name);
		} else {
			listPizze = pizzaService.findAll();
		}
		try {
			if (listPizze.size() == 0)
				throw new IllegalListSizeException();
			return new ResponseEntity<List<Pizza>>(listPizze, HttpStatus.OK);
		} catch (IllegalListSizeException e) {
			return new ResponseEntity<Payload<Pizza>>(new Payload<Pizza>(null,e.getMessage(),null), HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			return new ResponseEntity<Payload<Pizza>>(new Payload<Pizza>(null,e.getMessage(),null), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Payload<Pizza>> dataPizza(@PathVariable Integer id) {	
		Optional<Pizza> pizza = pizzaService.findById(id);
		if(pizza.isPresent()) {
			return ResponseEntity.ok(new Payload<Pizza>(pizza.get(), null, HttpStatus.OK));
		}else {
			return ResponseEntity.ofNullable(new Payload<Pizza>(null,"id non trovato " + id,HttpStatus.NOT_FOUND));
		}		 
	}
	
	@PostMapping
	public ResponseEntity<Payload<Pizza>> storePizza(@Valid @RequestBody Pizza pizza) {
		List<Pizza> list = pizzaService.findByName(pizza.getName());
		try {
			if (list.size() > 0)
				throw new IllegalListSizeException("pizza già presente in lista");
			Pizza pizzaSaved = pizzaService.save(pizza);
			return ResponseEntity.ok(new Payload<Pizza>(pizzaSaved, null, HttpStatus.OK));
		} catch (Exception e) {
			return new ResponseEntity(new Payload<Pizza>(pizza, e.getMessage(), null), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Payload<Pizza>> updatePizza(@PathVariable Integer id,@Valid @RequestBody Pizza pizza) {
		try {
			Pizza updatePizza = pizzaService.update(id, pizza);
			return ResponseEntity.ok(new Payload<Pizza>(updatePizza,null,HttpStatus.OK));
		}catch(IllegalArgumentException e) {
			return ResponseEntity.ofNullable(new Payload<Pizza>(null,"id non trovato " + id,HttpStatus.NOT_FOUND));
		}catch(Exception e) {
			return new ResponseEntity(new Payload<Pizza>(pizza,e.getMessage(), null), HttpStatus.BAD_REQUEST);
		}			
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Payload<Pizza>> deletePizza(@PathVariable int id) {
		try {
			pizzaService.delete(id);
			return ResponseEntity.ok(new Payload<Pizza>(null,"cancellazione effettuata correttamente",HttpStatus.OK));
		}catch(IllegalArgumentException e) {
			return new ResponseEntity(new Payload<Pizza>(null,e.getMessage(), null), HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			return new ResponseEntity(new Payload<Pizza>(null,e.getMessage(), null), HttpStatus.BAD_REQUEST);
		}		
	}
	
	private boolean searchPizza(String name) {
		boolean message = false;
		if (name != null) {
			message = true;
		}
		return message;
	}

}
