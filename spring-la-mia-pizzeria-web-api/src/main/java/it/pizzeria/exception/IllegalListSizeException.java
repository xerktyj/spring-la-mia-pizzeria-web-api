package it.pizzeria.exception;

import it.pizzeria.response.Payload;

public class IllegalListSizeException extends RuntimeException{

	public IllegalListSizeException() {
		super("la lista è vuota");
	}
	
	public IllegalListSizeException(String message) {
		super(message);
	}

}
