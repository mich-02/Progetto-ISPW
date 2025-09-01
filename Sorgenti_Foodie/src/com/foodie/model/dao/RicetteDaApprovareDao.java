package com.foodie.model.dao;

import java.sql.SQLException;
import java.util.List;

import com.foodie.model.Ricetta;
import com.foodie.model.RicettaDuplicataException;

public interface RicetteDaApprovareDao {
	
	public void caricaRicetteDaApprovare() throws SQLException;
	
	public void salvaRicettaDaApprovare(Ricetta ricetta) throws SQLException;
	
}
