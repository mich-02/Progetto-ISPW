package com.foodie.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import com.foodie.model.Ricetta;
import com.foodie.model.RicetteDaApprovare;

public class TestRicetteDaApprovare {   //BIRU MICHELE FRANCESCO
	
	private static final String NOME1 = "Pasta alla Carbonara";
	private static final String NOME2 = "Pasta alla Gricia";
	private static final String AUTORE = "Barbieri";
	
	private RicetteDaApprovare ricetteDaApprovare;
	
	@Before
	public void setup() {
		ricetteDaApprovare = new RicetteDaApprovare();
		Ricetta ricettaDiProva = new Ricetta();
		ricettaDiProva.setNome(NOME1);
		ricettaDiProva.setAutore(AUTORE);
		ricetteDaApprovare.aggiungiRicettaDaVerificare(ricettaDiProva);	
	}
	
	@Test
	public void testAggiungiRicettaDaVerificareNonPresente() {
		Ricetta ricettaDiProva = new Ricetta();
		ricettaDiProva.setNome(NOME2);
		ricettaDiProva.setAutore(AUTORE);
		ricetteDaApprovare.aggiungiRicettaDaVerificare(ricettaDiProva); 
		assertTrue(ricetteDaApprovare.getRicetteDaVerificare().contains(ricettaDiProva));  //controllo se la ricetta è stata aggiunta
		ricetteDaApprovare.ricettaVerificata(ricettaDiProva);  //così riporto allo stato iniziale per fare gli altri test
	}
	
	@Test
	public void testAggiungiRicettaDaVerificarePresente() {
		int oldSize = ricetteDaApprovare.getRicetteDaVerificare().size();
		Ricetta ricettaDiProva = new Ricetta();
		ricettaDiProva.setNome(NOME1);
		ricettaDiProva.setAutore(AUTORE);
		ricetteDaApprovare.aggiungiRicettaDaVerificare(ricettaDiProva);
		assertEquals(ricetteDaApprovare.getRicetteDaVerificare().size(), oldSize);  //CONTROLLO SE LA LISTA E' RIMASTA INVARIATA
	}
	
	
	@Test
	public void testRicettaVerificataNonPresente() {
		int oldSize = ricetteDaApprovare.getRicetteDaVerificare().size();
		Ricetta ricettaDiProva = new Ricetta();
		ricettaDiProva.setNome(NOME2);
		ricettaDiProva.setAutore(AUTORE);
		ricetteDaApprovare.ricettaVerificata(ricettaDiProva);
		assertEquals(ricetteDaApprovare.getRicetteDaVerificare().size(), oldSize);   //CONTROLLO CHE LA DIMENSIONE E' RIMASTA INVARIATA
	}
	
	
	@Test
	public void testRicettaVerificataPresente() {
		Ricetta ricettaDiProva = new Ricetta();
		ricettaDiProva.setNome(NOME1);
		ricettaDiProva.setAutore(AUTORE);
		ricetteDaApprovare.ricettaVerificata(ricettaDiProva);
		assertFalse(ricetteDaApprovare.getRicetteDaVerificare().contains(ricettaDiProva));
		ricetteDaApprovare.aggiungiRicettaDaVerificare(ricettaDiProva);  //RIPRISTINO LO STATO INIZIALE DELLA RICETTA
	}
	
	@Test
	public void testOttieniRicetta() {
		Ricetta ricettaDiProva = new Ricetta();
		ricettaDiProva.setNome(NOME1);
		ricettaDiProva.setAutore(AUTORE);
		assertEquals(ricetteDaApprovare.ottieniRicetta(NOME1, AUTORE),ricettaDiProva);  //CONTROLLA SE OTTENGO LA RICETTA ASPETTATA
	}
}