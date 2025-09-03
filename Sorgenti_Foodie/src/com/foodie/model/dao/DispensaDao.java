package com.foodie.model.dao;

import java.util.List;
import com.foodie.exception.DaoException;
import com.foodie.model.Alimento;

public interface DispensaDao {
	
	public void salvaDispensa(String username) throws DaoException;
	
	public List<Alimento> caricaDispensa(String username) throws DaoException;
}
