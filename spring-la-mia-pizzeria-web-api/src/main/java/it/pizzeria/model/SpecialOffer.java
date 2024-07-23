package it.pizzeria.model;


import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name="Offerte speciali")
public class SpecialOffer {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)	
	private int id;
	
	@NotNull
	@FutureOrPresent
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name="inizio data", nullable=false)
	private LocalDate startDate;
	
	@NotNull
	@FutureOrPresent
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name="fine data", nullable=false)
	private LocalDate endDate;
	
	@NotNull
	@NotBlank(message="mettere un titolo")
	@Size(min=1,max=100)
	@Column(name="titolo", nullable=false)
	private String title;
	
	
	@ManyToOne
	@JoinColumn(name="pizza_id",nullable=false)
	@JsonBackReference
	private Pizza pizza;

	public Integer getId() {
		return id;
	}

	public Pizza getPizza() {
		return pizza;
	}

	public void setPizza(Pizza pizza) {
		this.pizza = pizza;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {			
		this.startDate = startDate;				
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate=endDate;			
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void clean() {
		this.startDate = null;
		this.endDate = null;
		this.title = null;
	}
	
	public String toString() {
		return "SpecialOffer: " + "Inizio: "+ startDate + " Fine: "+ endDate + ", title=" + title;
	}
			
}
