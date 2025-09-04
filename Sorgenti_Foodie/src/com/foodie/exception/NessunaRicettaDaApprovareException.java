package com.foodie.exception;

public class NessunaRicettaDaApprovareException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public NessunaRicettaDaApprovareException() {
		super("Nessuna ricetta da approvare trovata");
	}

}
