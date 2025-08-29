package com.foodie.model;

public class Chef implements Utente{  //CHEF IMPLEMENTA UTENTE
	
	private String username;
	
	public Chef(String username) {
		this.username=username;
	}
	
	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
	public String getViewIniziale(int interfaccia) {
		if(interfaccia == 1) {
		return "/com/foodie/boundary/GestisciRicetteView.fxml";  //VIEW DI INIZIO
		} else {
			return "/com/foodie/boundary2/GestisciRicetteView2.fxml";
		}
	}
	
}