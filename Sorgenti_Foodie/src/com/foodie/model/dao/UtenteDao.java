package com.foodie.model.dao;

import com.foodie.exception.DaoException;

public interface UtenteDao {
	public int validazioneLogin(String username, String password) throws DaoException;
	public int controllaUsername(String username) throws DaoException;
	public void registraUtente(String nome, String cognome, String username, int ruolo, String password) throws DaoException;
}
