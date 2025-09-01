package com.foodie.model;

public class Chef extends Utente{  
	
	public Chef(String username) {
		super(username);
	}
	
	@Override
	public String getViewIniziale(int interfaccia) {
		if(interfaccia == 1) {
		return "/com/foodie/boundary/GestisciRicetteView.fxml";  //view iniziale
		} else {
			return "/com/foodie/boundary2/GestisciRicetteView2.fxml";
		}
	}
	
}