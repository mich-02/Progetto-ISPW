package com.foodie.exception;

public class NessunAlimentoTrovatoException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public NessunAlimentoTrovatoException(String nome) {
		super("Nessun alimento restituito dall'API Nutrixionix che inizi per: " + nome);
	}

}
