package com.foodie.model;

public abstract class Utente { 
	
	protected String username;
	
	protected Utente(String username) {
		this.username = username;
	}

	public String getUsername() { 
		return username;
	}
	
	public abstract String getViewIniziale(int interfacciaSelezionata);  //ottieni la view inizale da caricare
	
}