package it.pizzeria.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "ingredienti")
public class Ingredient {
	
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@NotBlank(message="inserire un nome")
	@Size(min=1, max=100, message = "il nome è troppo lungo stare in 100 caratteri")
	@Column(name="nome", nullable=false)
	private String name;
	
	@NotNull(message="inserire un numero di 8 cifre")
	@Column(name="codice", nullable=false )
	private Long serialNumber;
	
	@NotBlank(message="inserire un fornitore")
	@Size(min=1, max=100, message = "il nome è troppo lungo stare in 100 caratteri")
	@Column(name="fornitore", nullable=false)	
	private String supplier;
	
	@ManyToMany(mappedBy="ingredients")
	@JsonBackReference
	private List<Pizza> pizze;

	public int getId() {
		return id;
	}
   
	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}



	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(Long serialNumber) {
		this.serialNumber = serialNumber;
	}

	public List<Pizza> getPizze() {
		return pizze;
	}

	public void setPizze(List<Pizza> pizze) {
		this.pizze = pizze;
	}

	@Override
	public String toString() {
		return "nome: " + name + ", codice: " + serialNumber  + " fornitore: " + supplier;
	}
		

}
