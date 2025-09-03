package com.foodie.exception;

public class OperazioneFallitaException extends Exception {
	
	private static final long serialVersionUID = -1058399577241961958L;
	public OperazioneFallitaException(String messaggio) {
		super(messaggio);
	}
	
}
