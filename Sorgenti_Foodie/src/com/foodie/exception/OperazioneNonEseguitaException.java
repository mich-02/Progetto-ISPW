package com.foodie.exception;

public class OperazioneNonEseguitaException extends Exception {
	
	private static final long serialVersionUID = -1058399577241961958L;
	public OperazioneNonEseguitaException(String messaggio) {
		super(messaggio);
	}
	
}
