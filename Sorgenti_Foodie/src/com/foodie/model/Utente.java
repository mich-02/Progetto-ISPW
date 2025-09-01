package com.foodie.model;

public abstract class Utente { 
	
	protected String username;
	
	protected Utente(String username) {
		this.username = username;
	}

	public String getUsername() { 
		return username;
	}
	
	public abstract String getViewIniziale(int interfacciaSelezionata);  //OTTIENI LA VIEW INIZIALE DA CARICARE
	
}