package com.foodie.exception;


public class RicettaDuplicataException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public RicettaDuplicataException() {
		super("Ricetta già esistente per questo chef");
	}
	

}
