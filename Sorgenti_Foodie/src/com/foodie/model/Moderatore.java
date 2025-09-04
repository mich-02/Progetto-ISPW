package com.foodie.model;

public class Moderatore extends Utente { 

    public Moderatore(String username){
    	super(username);
    }

	@Override
	public String getViewIniziale(int i) {  //view iniziale
		if(i==1) {
			return "/com/foodie/boundary/ModeratoreView.fxml";
		}
		else {
			return "/com/foodie/boundary2/ModeratoreView2.fxml";
		}
	}
}