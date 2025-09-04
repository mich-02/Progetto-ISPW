package com.foodie.exception;

public class UtenteEsistenteException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public UtenteEsistenteException() {
		super("Username già utilizzato");
	}
}
