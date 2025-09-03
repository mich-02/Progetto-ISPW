package com.foodie.model.dao;

import com.foodie.exception.DaoException;
import com.foodie.model.Ricetta;

public interface RicetteDaApprovareDao {
	
	public void caricaRicetteDaApprovare() throws DaoException;
	
	public void salvaRicettaDaApprovare(Ricetta ricetta) throws DaoException;
	
	public void rifiutaRicetta(Ricetta ricetta) throws DaoException;
	
}
