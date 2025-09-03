package com.foodie.exception;


public class RicettaDuplicataException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public RicettaDuplicataException() {
		super("Coppia autore-ricetta già presente nel sistema");
	}
	

}
