package com.foodie.model.dao;

import java.sql.SQLException;
import java.util.List;
import com.foodie.model.Dispensa;
import com.foodie.model.Ricetta;
import com.foodie.model.RicettaDuplicataException;

public interface RicettaDao {
	
	//public List<Ricetta> trovaRicette(Dispensa dispensa, int difficolta, String autore) throws SQLException;
	public List<Ricetta> trovaRicettePerDispensa(int difficolta, String username) throws SQLException;
	public List<Ricetta> trovaRicettePerAutore(String autore) throws SQLException;
	
	public void aggiungiRicetta(Ricetta ricetta) throws SQLException,RicettaDuplicataException;
	
	public void eliminaRicetta(String nome, String autore) throws SQLException;
	
	public Ricetta ottieniDatiRicetta(String nome, String autore) throws SQLException;
}
