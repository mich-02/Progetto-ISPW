package com.foodie.model.dao;

import java.util.List;
import com.foodie.exception.DaoException;
import com.foodie.exception.RicettaDuplicataException;
import com.foodie.model.Ricetta;

public interface RicettaDao {

	public List<Ricetta> trovaRicettePerDispensa(int difficolta, String username) throws DaoException;
	public List<Ricetta> trovaRicettePerAutore(String autore) throws DaoException;
	
	public void aggiungiRicetta(Ricetta ricetta) throws DaoException, RicettaDuplicataException;
	
	public void eliminaRicetta(String nome, String autore) throws DaoException;
	
	public Ricetta ottieniDatiRicetta(String nome, String autore) throws DaoException;
}
