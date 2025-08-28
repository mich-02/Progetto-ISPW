package com.foodie.model.dao;

import java.sql.SQLException;
import java.util.List;
import com.foodie.model.Alimento;
import com.foodie.model.Dispensa;

public interface DispensaDao {
	
	public void salvaDispensa(String username) throws SQLException;
	
	public List<Alimento> caricaDispensa(String username) throws SQLException;
}
