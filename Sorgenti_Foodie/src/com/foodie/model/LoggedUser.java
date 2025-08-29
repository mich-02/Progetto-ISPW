package com.foodie.model;

public class LoggedUser {
	
	private static LoggedUser instanza = null;
	private Utente utente;
	
	private LoggedUser() {
	}

	public static LoggedUser ottieniIstanza() {
		if (instanza == null) {
			instanza = new LoggedUser();
		}
		return instanza;
	}
	
	public void setUtente(Utente utente) {
		this.utente = utente;
	}
	
	public String getUsername() {
		return utente.getUsername();
	}

}
