package com.foodie.model;


public class Standard extends Utente{
	
	public Standard(String username) {
		super(username);
	}

	@Override
	public String getViewIniziale(int interfaccia) { //view iniziale
		if(interfaccia == 1) {
		return "/com/foodie/boundary/DispensaUtenteView.fxml";
		} else {
			return "/com/foodie/boundary2/AggiungiAlimentoView2.fxml";
		}
	}
	
}