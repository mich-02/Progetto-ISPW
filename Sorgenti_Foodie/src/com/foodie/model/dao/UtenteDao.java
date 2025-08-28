package com.foodie.model.dao;

import java.sql.SQLException;

public interface UtenteDao {
	public int validazioneLogin(String username, String password) throws SQLException;
	public int controllaUsername(String username) throws SQLException;
	public void registraUtente(String nome, String cognome, String username, int ruolo, String password) throws SQLException;
}
