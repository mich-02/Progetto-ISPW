package com.foodie.model.dao;

import com.foodie.exception.DaoException;
import com.foodie.exception.NessunaRicettaDaApprovareException;
import com.foodie.model.Ricetta;

public interface RicetteDaApprovareDao {
	
	public void caricaRicetteDaApprovare() throws DaoException, NessunaRicettaDaApprovareException;
	
	public void salvaRicettaDaApprovare(Ricetta ricetta) throws DaoException;
	
	public void rifiutaRicetta(Ricetta ricetta) throws DaoException;
	
}
