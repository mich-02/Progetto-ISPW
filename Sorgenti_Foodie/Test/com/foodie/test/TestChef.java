package com.foodie.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.foodie.model.Chef;

public class TestChef { //BIRU MICHELE FRANCESCO 1

	private static final String USERNAME = "test_chef";

	@Test
    public void testGetUsernameUguale() {
        Chef chef = new Chef(USERNAME);
		
        assertEquals(USERNAME, chef.getUsername()); // verifica che il metodo restituisca lo username corretto
    }

    @Test
    public void testGetViewInizialeInterfaccia1() {
        Chef chef = new Chef(USERNAME);
        assertEquals("/com/foodie/boundary/GestisciRicetteView.fxml", chef.getViewIniziale(1)); //verifica che restituisca la view corretta
    }

    @Test
    public void testGetViewInizialeInterfaccia2() {
    	Chef chef = new Chef(USERNAME);
        assertEquals("/com/foodie/boundary2/GestisciRicetteView2.fxml", chef.getViewIniziale(2)); //verifica che restituisca la view corretta
	}

}
